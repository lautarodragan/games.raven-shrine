package rs.server.sockets;

import java.nio.channels.SocketChannel;

public interface ServerMessengerListener {
	public void event(String s);
	public void acceptedConnection(SocketChannel oSocketChannel);
	public void closedConnection(SocketChannel oSocketChannel);
	public void publicChat(String sUsername, String sMessage);
	public void ping();
	public void playerMoved(int iId);
	public void fileRequested(String sUser, String sFile);
	public void invalidFileRequested(String sUser, String sFile);
}
