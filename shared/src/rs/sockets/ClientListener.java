package rs.sockets;

import java.nio.ByteBuffer;

public interface ClientListener {
	public void dataArrived(ByteBuffer oByteBuffer);
	public void acceptedConnection();
	public void closedConnection();
	public void refusedConnection();
}
