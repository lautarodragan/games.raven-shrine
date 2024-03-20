package rs.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
//import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NioServer extends NioClientServer /*implements Runnable*/ {
	private ServerListener _oServerThreadListener;
	private ServerSocketChannel _oServerChannel;
	private final List<SocketChannel> _oClients = new LinkedList<SocketChannel>();

	public NioServer(InetAddress hostAddress, int port) throws IOException {
		super(hostAddress, port);
		
		_oSelector = SelectorProvider.provider().openSelector();

		_oServerChannel = ServerSocketChannel.open();
		_oServerChannel.configureBlocking(false);

		InetSocketAddress oInetSocketAddress = new InetSocketAddress(_oHostAddress, _iPort);
		_oServerChannel.socket().bind(oInetSocketAddress);

		_oServerChannel.register(_oSelector, SelectionKey.OP_ACCEPT);
	}

	@Override
	public void run() {
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
								if(key != null)
									key.interestOps(change.ops);
								else
									System.out.println("null key"); // client isn't available - proabbly disconnected
						}
					}
					_oPendingChanges.clear();
				}

				_oSelector.select();
				if(_bStop)
					break;

				Iterator<SelectionKey> oSelectedKeys = _oSelector.selectedKeys().iterator();
				while (oSelectedKeys.hasNext()) {
					SelectionKey oSelectionKey = oSelectedKeys.next();
					oSelectedKeys.remove();

					if (!oSelectionKey.isValid()) {
						System.out.println("Invalid key?");
						continue;
					}
					
					if (oSelectionKey.isAcceptable()) {
						_accept(oSelectionKey);
					} else if (oSelectionKey.isReadable()) {
						_read(oSelectionKey);
					} else if (oSelectionKey.isWritable()) {
						_write(oSelectionKey);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try{
			_oServerChannel.close();
			_oSelector.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	private void _accept(SelectionKey oSelectionKey) throws IOException {
		ServerSocketChannel oServerSocketChannel = (ServerSocketChannel) oSelectionKey.channel();

		SocketChannel oSocketChannel = oServerSocketChannel.accept();
		oSocketChannel.configureBlocking(false);

		synchronized(_oClients){
			_oClients.add(oSocketChannel);
		}

		if(_oServerThreadListener != null)
			_oServerThreadListener.acceptedConnection(oSocketChannel);

		oSocketChannel.register(_oSelector, SelectionKey.OP_READ);
		
	}

	private void _read(SelectionKey oSelectionKey) throws IOException {
		SocketChannel oSocketChannel = (SocketChannel) oSelectionKey.channel();
		
		_oReadBuffer.clear();

		int iReadCount;
		try {
			iReadCount = oSocketChannel.read(_oReadBuffer);
			_oReadBuffer.flip();
		} catch (IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel.
			oSelectionKey.cancel();
			oSocketChannel.close();

			if(_oServerThreadListener != null){
				_oServerThreadListener.closedConnection(oSocketChannel);
				synchronized(_oClients){
					_oClients.remove(oSocketChannel);
				}
			}
			return;
		}

		if (iReadCount == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			oSelectionKey.channel().close();
			oSelectionKey.cancel();

			if(_oServerThreadListener != null){
				_oServerThreadListener.closedConnection(oSocketChannel);
				synchronized(_oClients){
					_oClients.remove(oSocketChannel);
				}
			}
			return;
		}
		
		_oServerThreadListener.dataArrived(oSocketChannel, _oReadBuffer);

	}

	private void _write(SelectionKey oSelectionKey) throws IOException {
		SocketChannel oSocketChannel = (SocketChannel) oSelectionKey.channel();

		synchronized (_oPendingData) {
			List<ByteBuffer> oList = _oPendingData.get(oSocketChannel);

			while (!oList.isEmpty()) {
				ByteBuffer oByteBuffer = oList.get(0);
				try{
					oSocketChannel.write(oByteBuffer);
				}catch(IOException ex){
					synchronized(_oClients){
						_oClients.remove(oSocketChannel);
					}
					_oPendingData.remove(oSocketChannel);
					oList.clear();
					oSelectionKey.cancel();
					oSocketChannel.close();
					_oServerThreadListener.closedConnection(oSocketChannel);
					return;
				}
				if (oByteBuffer.remaining() > 0) {
					break;
				}
				oList.remove(0);
				break; // TODO: what?
			}

			if (oList.isEmpty()) {
				oSelectionKey.interestOps(SelectionKey.OP_READ);
			}
		}
	}

	public void setServerListener(ServerListener o){
		_oServerThreadListener = o;
	}

	public List<SocketChannel> clients(){
		return _oClients;
	}
	
}