package rs.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.*;

public class NioClient extends NioClientServer{
	private ClientListener _oClientListener;
	private SocketChannel _oSocket;

	public NioClient(InetAddress oHostAddress, int iPort) throws IOException {
		super(oHostAddress, iPort);
	}
	public NioClient(InetAddress oHostAddress) throws IOException {
		super(oHostAddress);
	}

	public void send(ByteBuffer oByteBuffer) {
		send(_oSocket, oByteBuffer);
	}

	@Override
	public void run() {
		try{
			_oSocket = _initiateConnection();
		} catch(IOException iOException){

		}
		_bStop = false;
		while (!_bStop) {
			try {
				// Process any pending changes
				synchronized (_oPendingChanges) {
					Iterator changes = _oPendingChanges.iterator();
					while (changes.hasNext()) {
						ChangeRequest change = (ChangeRequest) changes.next();
						switch (change.type) {
							case ChangeRequest.CHANGEOPS:
								SelectionKey key = change.socket.keyFor(_oSelector);
								key.interestOps(change.ops);
								break;
							case ChangeRequest.REGISTER:
								change.socket.register(_oSelector, change.ops);
								break;
						}
					}
					_oPendingChanges.clear();
				}

				// Wait for an event one of the registered channels
				_oSelector.select();

				// Iterate over the set of keys for which events are available
				Iterator selectedKeys = _oSelector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					// Check what event is available and deal with it
					if (key.isConnectable()) {
						if(!_finishConnection(key)){
							break;
						}
					} else if (key.isReadable()) {
						_read(key);
					} else if (key.isWritable()) {
						_write(key);
					}
				}
				if(_bStop)
					break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try{
			_oSocket.close();
			_oSelector.close();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	private void _read(SelectionKey oSelectionKey) throws IOException {
		SocketChannel oSocketChannel = (SocketChannel) oSelectionKey.channel();

		// Clear out our read buffer so it's ready for new data
		_oReadBuffer.clear();

		// Attempt to read off the channel
		int iNumRead;
		try {
			iNumRead = oSocketChannel.read(_oReadBuffer);
			_oReadBuffer.flip();
		} catch (IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel.
			oSelectionKey.cancel();
			oSocketChannel.close();
//			System.out.println("Close A");
			if(_oClientListener != null)
				_oClientListener.closedConnection();
			return;
		}

		if (iNumRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			oSelectionKey.channel().close();
			oSelectionKey.cancel();
			System.out.println("Close B");
			return;
		}
//		System.out.println("Client read");
		if(_oClientListener != null)
			_oClientListener.dataArrived(_oReadBuffer);
	}

	private void _write(SelectionKey oSelectionKey) throws IOException {
		SocketChannel oSocketChannel = (SocketChannel) oSelectionKey.channel();
		synchronized (_oPendingData) {
			List oList = (List) _oPendingData.get(oSocketChannel);

			if(oList == null){
				oList = new ArrayList();
			}
			// Write until there's not more data ...
			while (!oList.isEmpty()) {
				ByteBuffer oByteBuffer = (ByteBuffer) oList.get(0);
				oSocketChannel.write(oByteBuffer);
				if (oByteBuffer.remaining() > 0) {
					// ... or the socket's buffer fills up
					break;
				}
				
				oList.remove(0);
			}

			if (oList.isEmpty()) {
				// We wrote away all data, so we're no longer interested
				// in writing on this socket. Switch back to waiting for
				// data.
				oSelectionKey.interestOps(SelectionKey.OP_READ);
			}
		}
	}

	private boolean _finishConnection(SelectionKey oSelectionKey) throws IOException {
		SocketChannel oSocketChannel = (SocketChannel) oSelectionKey.channel();

		try {
			oSocketChannel.finishConnect();
		}catch(IOException e){
//			e.printStackTrace();
			if(_oClientListener != null)
				_oClientListener.refusedConnection();
			oSelectionKey.cancel();
//			stop();
			_bStop = true;
			return false;
		}

		oSelectionKey.interestOps(SelectionKey.OP_WRITE);

		if(_oClientListener != null)
			_oClientListener.acceptedConnection();

		return true;
	}

	private SocketChannel _initiateConnection() throws IOException {
		SocketChannel oSocketChannel = SocketChannel.open();
		oSocketChannel.configureBlocking(false);
//		System.out.println("Client attempting to connect to " + _oHostAddress + ":"  + _iPort);
		oSocketChannel.connect(new InetSocketAddress(_oHostAddress, _iPort));

		synchronized (_oPendingChanges) {
			_oPendingChanges.add(new ChangeRequest(oSocketChannel, ChangeRequest.REGISTER, SelectionKey.OP_CONNECT));
		}

		return oSocketChannel;
	}

	public void setClientListener(ClientListener o){
		_oClientListener = o;
	}

	public SocketChannel getSocketChannel(){
		return _oSocket;
	}
	
}