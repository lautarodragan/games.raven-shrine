package rs.client.sockets;

public interface MessengerListener {
	public void acceptedConnection();
	public void closedConnection();
	public void refusedConnection();
	
	public void playerMove(int iId, byte iDirection, byte iFlag);
	public void playerLoggedIn(int iId, String sUsername, float x, float y, int iMap);
	public void playerLoggedOut(int iId);
	public void publicChat(int iPlayer, String sMessage);
	public void updatePlayerPosition(int iId, float x, float y);
	public void playerSpriteFilename(int iId, String sSpriteFilename);
}
