package rs.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NioClientServer implements Runnable{
	protected InetAddress _oHostAddress;
	protected int _iPort;
	protected boolean _bStop = false;

	protected Selector _oSelector;

	protected ByteBuffer _oReadBuffer = ByteBuffer.allocate(8192);

	protected final List _oPendingChanges = new LinkedList();
	protected final Map<SocketChannel, List<ByteBuffer>> _oPendingData = new HashMap();

	public NioClientServer(InetAddress oHostAddress, int iPort) throws IOException {
		_iPort = iPort;
		_oHostAddress = oHostAddress;
		_oSelector = SelectorProvider.provider().openSelector();
	}

	public NioClientServer(InetAddress oHostAddress) throws IOException {
		this(oHostAddress, 0);
	}

	public void run(){
		
	}

	public void send(SocketChannel oSocketChannel, ByteBuffer oByteByffer){
		synchronized(_oPendingChanges){
			_oPendingChanges.add(new ChangeRequest(oSocketChannel, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

			synchronized(_oPendingData){
				List oList = _oPendingData.get(oSocketChannel);
				if (oList == null) {
					oList = new ArrayList();
					_oPendingData.put(oSocketChannel, oList);
				}
				oList.add(oByteByffer);
			}
		}

		_oSelector.wakeup();
	}

	public void stop() {
		_bStop = true;
		_oSelector.wakeup();
	}

	public boolean isStopped() {
		return _bStop;
	}
	
}
