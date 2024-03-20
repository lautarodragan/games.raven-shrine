/*
 * Handles all server high-level networking: parses messages, etc
 */

package rs.server.sockets;

import rs.sockets.Download;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.resources.Player;
import rs.server.database.Database;
import rs.server.database.Database.AlreadyLoggedInException;
import rs.server.database.Database.InvalidUsernameOrPasswordException;
import rs.sockets.Messaging;
import rs.sockets.NioServer;
import rs.sockets.ServerListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import rs.resources.GameMap;
import rs.resources.GameMapReference;
import rs.resources.Paths;
import rs.resources.Resourcebase;
import rs.resources.Tileset;


/**
 *
 * @author Lautaro
 */
public class ServerMessenger implements ServerListener{
	private Database _oDatabase;
	private NioServer _oServerThread;

	private ByteBuffer _oPendingData;
	private ByteBuffer _oPendingBytes;

	private final Map<SocketChannel, Player> _oPlayers = new HashMap();
	private final Map<SocketChannel, List<Download>> _oDownloads = new HashMap<SocketChannel, List<Download>>();

	private List<GameMapReference> _oGameMapReferences;
	private List<GameMap> _oGameMaps = new ArrayList<GameMap>();
	private List<Tileset> _oTilesets;
	private Map<Integer, GameMap> _oMapGameMaps = new HashMap<Integer, GameMap>();
	private Map<Integer, Tileset> _oMapTilesets = new HashMap<Integer, Tileset>();
	
	private List<ServerMessengerListener> _oServerMessengerListeners = new LinkedList<ServerMessengerListener>();

	private Runnable _oDownloader = new Runnable() {
		public void run() {
			final int iBlockSize = 6 * 1024;
			final int iMaxBandwidth = 15 * 1024;
			long iUsedBandwidth;
			Iterator<SocketChannel> oIteratorClients = null;
			Iterator<Download> oIteratorDownloads = null;
			int iBytesRead;
			byte[] iData = new byte[iBlockSize];
			SocketChannel oSocketChannel = null;
			List<Download> oDownloads = null;

			while(true){
				iUsedBandwidth = 0;
				synchronized(_oServerThread.clients()){
					synchronized(_oDownloads){
//						if(oIteratorClients == null || !oIteratorClients.hasNext())
							oIteratorClients = _oServerThread.clients().listIterator();
						outter:
						while (oIteratorClients.hasNext()) {
//							if(oIteratorDownloads == null || !oIteratorDownloads.hasNext()){
								oSocketChannel = oIteratorClients.next();
								oDownloads = _oDownloads.get(oSocketChannel);
								oIteratorDownloads = oDownloads.iterator();
//							}
							while (oIteratorDownloads.hasNext()) {
								Download oDownload = oIteratorDownloads.next();

								try {
									FileInputStream oFileInputStream = new FileInputStream(rs.resources.Paths.PATH + File.separatorChar + oDownload.Name);
									if(oFileInputStream.skip(oDownload.Progress) != oDownload.Progress)
										System.err.println("Could not skip correct amount of bytes");
									
									iBytesRead = oFileInputStream.read(iData);

									if(oDownload.Progress == 0)
										_oServerThread.send(oSocketChannel, Messaging.sendFile(oDownload.Name, iData, iBytesRead, oDownload.Length));
									else
										_oServerThread.send(oSocketChannel, Messaging.sendFileAppend(oDownload.Name, iData, iBytesRead));

									oDownload.Progress += iBytesRead;

									if(oDownload.Progress >= oDownload.Length){
										oIteratorDownloads.remove();
									}

									iUsedBandwidth += iBytesRead;

									if(_bTerminateDownloader || iUsedBandwidth >= iMaxBandwidth){
										break outter;
									}
								} catch (FileNotFoundException ex) {
									ex.printStackTrace();
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}
						}
					}
				}
				if(_bTerminateDownloader)
					return;
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				
			}
		}
	};
	private Thread _oDownloaderThread;
	private boolean _bTerminateDownloader;

	public boolean _bSimulateHighPing = false;

	public ServerMessenger(){
		_oTilesets = Resourcebase.readTilesets();
		if(_oTilesets != null){
			Iterator<Tileset> oTilesetIterator = _oTilesets.iterator();
			Tileset oTileset;
			while(oTilesetIterator.hasNext()){
				oTileset = oTilesetIterator.next();
				_oMapTilesets.put(oTileset.getId(), oTileset);
			}
		}

		_oGameMapReferences = Resourcebase.readMapReferences();
		if(_oGameMapReferences != null){
			Iterator<GameMapReference> oIterator = _oGameMapReferences.iterator();
			GameMapReference o;
			GameMap oGameMap;
			while(oIterator.hasNext()){
				o = oIterator.next();
				oGameMap = Resourcebase.readMap(o.getId());
				_oGameMaps.add(oGameMap);
				_oMapGameMaps.put(o.getId(), oGameMap);
			}
		}
	}

	public ServerMessenger(NioServer oServerThread, Database oDatabase){
		this();
		_oServerThread = oServerThread;
		_oDatabase = oDatabase;
	}

	public static String socketChannelToString(SocketChannel o){
		return o.socket().getInetAddress().getHostAddress() + ":" + o.socket().getPort();
	}

	public void acceptedConnection(SocketChannel oSocketChannel) {
		synchronized(_oDownloads){
			_oDownloads.put(oSocketChannel, new LinkedList<Download>());
		}

		logEvent("<html><span style='color: gray;'>" + socketChannelToString(oSocketChannel) + "</span> connected</html>");
		
		for(ServerMessengerListener o : _oServerMessengerListeners){
			o.acceptedConnection(oSocketChannel);
		}
	}

	public void closedConnection(SocketChannel oSocketChannel) {
		Player oPlayer = _oPlayers.get(oSocketChannel);
		String sWho;

		_oPlayers.remove(oSocketChannel);
		synchronized(_oDownloads){
			_oDownloads.remove(oSocketChannel);
		}

		if(oPlayer != null){
			_oDatabase.logOut(oPlayer.Id);
			_oDatabase.setCoords(oPlayer.Id, oPlayer.MapPosition.x, oPlayer.MapPosition.y);
			_oDatabase.setIp(oPlayer.Id, socketChannelToString(oSocketChannel));

			synchronized(_oServerThread.clients()){
				Iterator<SocketChannel> oIterator = _oServerThread.clients().iterator();
				while (oIterator.hasNext()) {
					SocketChannel o = oIterator.next();
					if(o != oSocketChannel)
						_oServerThread.send(o, Messaging.playerLoggedOut(oPlayer.Id));
				}
			}

			sWho = oPlayer.Username;

		}else{
			sWho = socketChannelToString(oSocketChannel);
		}

		logEvent("<html><span style='color: gray;'>" + sWho + "</span> disconnected</html>");
		
		for(ServerMessengerListener o : _oServerMessengerListeners){
			o.closedConnection(oSocketChannel);
		}
	}

	public void dataArrived(SocketChannel oSocketChannel, ByteBuffer oByteBuffer) {
		ByteBuffer oWorkByteBuffer = oByteBuffer;

		while(true){
			// <editor-fold defaultstate="collapsed" desc="Bytes">
			if(oWorkByteBuffer.remaining() < 1){
				return;
			}
			if(oWorkByteBuffer.remaining() < 4){
				_oPendingBytes = ByteBuffer.allocate(4);
				_oPendingBytes.put(oWorkByteBuffer);
				return;
			}
			if(_oPendingData == null){
				int iByteBufferSize;
				if(_oPendingBytes == null){
					iByteBufferSize = oWorkByteBuffer.getInt();
				}else{
					while(_oPendingBytes.remaining() > 0)
						_oPendingBytes.put(oWorkByteBuffer.get());
					_oPendingBytes.flip();
					iByteBufferSize = _oPendingBytes.getInt();
					_oPendingBytes = null;
				}
				if(iByteBufferSize > oWorkByteBuffer.remaining()){
					_oPendingData = ByteBuffer.allocate(iByteBufferSize);
					_oPendingData.put(oWorkByteBuffer);
					return;
				}
			}else{
				if(_oPendingData.remaining() > oWorkByteBuffer.limit()){
					_oPendingData.put(oWorkByteBuffer);
					return;
				}
				while(_oPendingData.remaining() > 0){
					_oPendingData.put(oWorkByteBuffer.get());
				}
				_oPendingData.clear();
				oWorkByteBuffer = _oPendingData;
			}

			 // </editor-fold>

			short iMessage = oWorkByteBuffer.getShort();

			Player oPlayer = _oPlayers.get(oSocketChannel);

			if(_bSimulateHighPing){
				try {
					Thread.sleep((long) Math.floor(Math.random() * 300 + 100));
				} catch (InterruptedException ex) {
					Logger.getLogger(ServerMessenger.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			
			if(iMessage == Messaging.MSG_PUBLICCHAT){
				int iLength = oWorkByteBuffer.getInt();
				int iOldLimit = oWorkByteBuffer.limit();

				oWorkByteBuffer.limit(oWorkByteBuffer.position() + iLength);
				String sMessage = Messaging.stringDecode(oWorkByteBuffer);

				oWorkByteBuffer.limit(iOldLimit);

				if(oPlayer == null)
					logChat(socketChannelToString(oSocketChannel), sMessage);
				else
					logChat(oPlayer.Username, sMessage);

				if(oPlayer != null){
					synchronized(_oServerThread.clients()){
						Iterator<SocketChannel> oIterator = _oServerThread.clients().iterator();
						while (oIterator.hasNext()) {
							SocketChannel o = oIterator.next();
							if(o != oSocketChannel)
								_oServerThread.send(o, Messaging.publicChat(oPlayer.Id, sMessage));
						}
					}
				}

			}else if(iMessage == Messaging.MSG_LOGIN){
				String sName = Messaging.getString(oWorkByteBuffer);
				String sPassword = Messaging.getString(oWorkByteBuffer);
				byte iResponse = 1;
				Player oNewPlayer = null;
				boolean bLoggedIn = false;

				try {
					oNewPlayer = _oDatabase.logIn(sName, sPassword);
					bLoggedIn = true;
				} catch (InvalidUsernameOrPasswordException ex) {
					iResponse = 2;
					logEvent(socketChannelToString(oSocketChannel) + " failed to log in as " + sName + ":" + sPassword);
					bLoggedIn = false;
				} catch (AlreadyLoggedInException ex) {
					iResponse = 3;
					logEvent(socketChannelToString(oSocketChannel) + " already logged in as " + sName + ":" + sPassword);
					bLoggedIn = false;
				}

				if(oNewPlayer != null)
					_oServerThread.send(oSocketChannel, Messaging.loginResponse(iResponse, oNewPlayer.Id, oNewPlayer.MapPosition.x, oNewPlayer.MapPosition.y, oNewPlayer.Map));
				else
					_oServerThread.send(oSocketChannel, Messaging.loginResponse(iResponse, 0, 0, 0, 0));

				if(bLoggedIn){
					_oServerThread.send(oSocketChannel, Messaging.playerSpriteFilename(oNewPlayer.Id, oNewPlayer.SpriteFilename));

					if(oNewPlayer != null){
						oNewPlayer._oMapGameMaps = _oMapGameMaps;
						oNewPlayer.oGameMap = _oMapGameMaps.get(oNewPlayer.Map);
						oNewPlayer.oTileset = _oMapTilesets.get(oNewPlayer.oGameMap.getTileset());
						
						_oPlayers.put(oSocketChannel, oNewPlayer);
						logEvent("<html><span style='color: gray;'>" + socketChannelToString(oSocketChannel) + "</span> logged in as " + oNewPlayer.Id + ":" + sName + ":" + sPassword + "</html>");

						Player oPlayer2;

						synchronized(_oServerThread.clients()){
							Iterator<SocketChannel> oIterator = _oServerThread.clients().iterator();
							while (oIterator.hasNext()) {
								SocketChannel o = oIterator.next();
								if(o != oSocketChannel){
									oPlayer2 = _oPlayers.get(o);
									if(oPlayer2 == null)
										continue;
									_oServerThread.send(o, Messaging.playerLoggedIn(oNewPlayer.Id, oNewPlayer.Username, oNewPlayer.MapPosition.x, oNewPlayer.MapPosition.y, oNewPlayer.Map));
									_oServerThread.send(oSocketChannel, Messaging.playerLoggedIn(oPlayer2.Id, oPlayer2.Username, oPlayer2.MapPosition.x, oPlayer2.MapPosition.y, oPlayer2.Map));
									_oServerThread.send(o, Messaging.playerSpriteFilename(oNewPlayer.Id, oNewPlayer.SpriteFilename));
									_oServerThread.send(oSocketChannel, Messaging.playerSpriteFilename(oPlayer2.Id, oPlayer2.SpriteFilename));

								}
							}
						}
					}
				}

			}else if(iMessage == Messaging.MSG_LOGOUT){
				if(oPlayer == null)
					continue;
				logEvent(oPlayer.Username + " logged out");
				_oPlayers.remove(oSocketChannel);
				_oDatabase.logOut(oPlayer.Id);
				_oDatabase.setCoords(oPlayer.Id, oPlayer.MapPosition.x, oPlayer.MapPosition.y);
				_oDatabase.setIp(oPlayer.Id, socketChannelToString(oSocketChannel));

			}else if(iMessage == Messaging.MSG_REQUESTFILE){
				String sFile = Messaging.getString(oWorkByteBuffer);
				long iOffset = oWorkByteBuffer.getLong();

				File oFile = new File(rs.resources.Paths.PATH + "/" + sFile);

				if(oFile.exists()){
					fireFileRequested(oPlayer == null ? socketChannelToString(oSocketChannel) : oPlayer.Username, sFile, false);
				}else{
					fireFileRequested(oPlayer == null ? socketChannelToString(oSocketChannel) : oPlayer.Username, sFile, true);
					continue;
				}

				Download oDownload = new Download();
				oDownload.Name = sFile;
				oDownload.Length = oFile.length();

				synchronized(_oDownloads){
					_oDownloads.get(oSocketChannel).add(oDownload);
				}

				startDownloader();
				
			}else if(iMessage == Messaging.MSG_VERSION_MAP){
				int iMap = oWorkByteBuffer.getInt();
				String sMap = Paths.PATH_DATA + "/" + iMap + ".map";
				GameMap oMap = GameMap.read(sMap);
				int iVersion = oMap.getPublicVersion();
				_oServerThread.send(oSocketChannel, Messaging.versionMap(iMap, iVersion));

			}else if(iMessage == Messaging.MSG_INPUT_MOVE){
				byte iDirection = oWorkByteBuffer.get();
				byte iFlag = oWorkByteBuffer.get();
				
				oPlayer.updatePosition(iDirection, iFlag);

				synchronized(_oServerThread.clients()){
					Iterator<SocketChannel> oIterator = _oServerThread.clients().iterator();
					while (oIterator.hasNext()) {
						SocketChannel o = oIterator.next();
						_oServerThread.send(o, Messaging.mapObjectMove(oPlayer.Id, iDirection, iFlag));
						if(iFlag == 0)
							_oServerThread.send(o, Messaging.updatePlayerPosition(oPlayer.Id, oPlayer.MapPosition.x, oPlayer.MapPosition.y));

					}
				}

				for(ServerMessengerListener o : _oServerMessengerListeners){
					o.playerMoved(oPlayer.Id);
				}

			}else if(iMessage == Messaging.MSG_PING_32){
				byte[] oBytes = Messaging.getBytes(oWorkByteBuffer);
				if(oBytes[0] == 0){ // 0 means it isn't a response, so we respond to it
					_oServerThread.send(oSocketChannel, Messaging.ping32(true));
				}else{
					for(ServerMessengerListener o : _oServerMessengerListeners){
						o.ping();
					}
				}

			}else{
				logEvent("Unknown message " + iMessage);
			}
			
			if(_oPendingData != null){
				oWorkByteBuffer = oByteBuffer;
				_oPendingData = null;
			}
		}
	}

	public void addServerMessengerListener(ServerMessengerListener o){
		_oServerMessengerListeners.add(o);
	}

	public void removeMessengerListener(ServerMessengerListener o){
		_oServerMessengerListeners.remove(o);
	}

	public void setServerThread(NioServer o){
		_oServerThread = o;
	}

	public NioServer getServerThread(){
		return _oServerThread;
	}

	public void setDatabase(Database o){
		_oDatabase = o;
	}

	public Database getDatabase(){
		return _oDatabase;
	}

	private void logEvent(String s){
		for(ServerMessengerListener o : _oServerMessengerListeners){
			o.event(s);
		}

	}

	private void logChat(String sUsername, String sMessage){
		for(ServerMessengerListener o : _oServerMessengerListeners){
			o.publicChat(sUsername, sMessage);
		}
	}

	public Map<SocketChannel, Player> getPlayers(){
		return _oPlayers;
	}

	public void startDownloader(){
		_oDownloaderThread = new Thread(_oDownloader);
		_oDownloaderThread.setName("Downloader");
		_oDownloaderThread.setDaemon(true);
		_oDownloaderThread.start();
	}

	public void fireFileRequested(String sUser, String sFile, boolean bInvalid){
		for(ServerMessengerListener o : _oServerMessengerListeners){
			if(!bInvalid)
				o.fileRequested(sUser, sFile);
			else
				o.invalidFileRequested(sUser, sFile);
		}
	}
}
