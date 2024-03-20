package rs.sockets;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface ServerListener {
	public void dataArrived(SocketChannel oSocketChannel, ByteBuffer oByteBuffer);
	public void acceptedConnection(SocketChannel oSocketChannel);
	public void closedConnection(SocketChannel oSocketChannel);
}
