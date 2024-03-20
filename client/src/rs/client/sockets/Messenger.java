package rs.client.sockets;

import java.io.File;
import java.io.FileOutputStream;
import rs.sockets.ClientListener;
import rs.sockets.Messaging;
import rs.sockets.NioClient;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import rs.resources.GameMap;
import rs.sockets.Download;
import rs.util.Util;

public class Messenger implements ClientListener{
	public static interface Callback{}

	public static interface CallbackPing32 extends Callback{
		public void callback();
	}

	public static class LoginData{
		public byte Response;
		public int Id;
		public float x, y;
		public int Map;

	}
	
	private NioClient _oClientThread;
	private ByteBuffer _oPendingData;
	private ByteBuffer _oPendingBytes;
	private String _sCachePath;
	private final Map<Short, Callback> _oMapCallbacks = new HashMap();
	private final LinkedList<MessengerListener> _oMessengerListeners = new LinkedList<MessengerListener>();
	private final Map<Integer, Integer> _queueMapVersion = new HashMap();
	private final LoginData _queueLogInData = new LoginData();
	private final Map<String, Download> _oDownloads = new HashMap<String, Download>();
	private final ArrayList<String> _oDownloadsReady = new ArrayList<String>();

	public Messenger(){
//		_oMessengerListeners = new ArrayList<MessengerListener>();
	}

	public Messenger(NioClient oClientThread){
//		this();
		_oClientThread = oClientThread;
	}

	public void dataArrived(ByteBuffer oByteBuffer){
		ByteBuffer oWorkByteBuffer = oByteBuffer;
		while(true){
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
			short iMessage = oWorkByteBuffer.getShort();

			messageArrived(iMessage, oWorkByteBuffer);
			
			if(_oPendingData != null){
				oWorkByteBuffer = oByteBuffer;
				_oPendingData = null;
			}
		}
	}

	public void acceptedConnection(){
		for(final MessengerListener o : _oMessengerListeners){
			new Thread(){
				@Override
				public void run(){
					o.acceptedConnection();
				}
			}.start();

		}
	}

	public void closedConnection(){
		for(MessengerListener o : _oMessengerListeners){
			o.closedConnection();
		}
	}

	public void refusedConnection(){
		for(MessengerListener o : _oMessengerListeners){
			o.refusedConnection();
		}
	}

	public void messageArrived(short iMessage, ByteBuffer oWorkByteBuffer){
		if(iMessage == Messaging.MSG_PUBLICCHAT){
			int iPlayerId = oWorkByteBuffer.getInt();
			int iLength = oWorkByteBuffer.getInt();
			int iOldLimit = oWorkByteBuffer.limit();

			oWorkByteBuffer.limit(oWorkByteBuffer.position() + iLength);
			String sMessage = Messaging.stringDecode(oWorkByteBuffer);

			oWorkByteBuffer.limit(iOldLimit);

			for(MessengerListener o : _oMessengerListeners){
				o.publicChat(iPlayerId, sMessage);
			}
		}else if(iMessage == Messaging.MSG_PLAYER_LOGGED_OUT){
			int iId = oWorkByteBuffer.getInt();

			for(MessengerListener o : _oMessengerListeners){
				o.playerLoggedOut(iId);
			}

		}else if(iMessage == Messaging.MSG_LOGIN){
			final byte iResponse = oWorkByteBuffer.get();
			final int iId = oWorkByteBuffer.getInt();
			final float iX = oWorkByteBuffer.getFloat();
			final float iY = oWorkByteBuffer.getFloat();
			final int iMap = oWorkByteBuffer.getInt();

			synchronized(_queueLogInData){
				LoginData o = _queueLogInData;
				o.Response = iResponse;
				o.Id = iId;
				o.Map = iMap;
				o.x = iX;
				o.y = iY;
				o.notifyAll();
			}

		}else if(iMessage == Messaging.MSG_PLAYER_LOGGED_IN){
			final int iId = oWorkByteBuffer.getInt();
			final String sUsername = Messaging.getString(oWorkByteBuffer);
			final float x = oWorkByteBuffer.getFloat();
			final float y = oWorkByteBuffer.getFloat();
			final int iMap = oWorkByteBuffer.getInt();

			for(MessengerListener o : _oMessengerListeners){
				o.playerLoggedIn(iId, sUsername, x, y, iMap);
			}
		}else if(iMessage == Messaging.MSG_SENDFILE){
			String sFile = Messaging.getString(oWorkByteBuffer);
			long iLength = oWorkByteBuffer.getLong();
			byte[] iData = Messaging.getBytes(oWorkByteBuffer);

			Download oDownload = new Download();
			oDownload.Length = iLength;
			oDownload.Name = sFile;
			oDownload.Progress = iData.length;

			try{
				new File(_sCachePath + sFile).getParentFile().mkdirs();
				FileOutputStream oDownloadStream = new FileOutputStream(_sCachePath + sFile);
				oDownloadStream.write(iData);
				if(iData.length >= iLength){
					synchronized(_oDownloadsReady){
						_oDownloadsReady.add(sFile);
						_oDownloadsReady.notifyAll();
					}
				}else{
					_oDownloads.put(oDownload.Name, oDownload);
				}
				oDownloadStream.close();
			}catch(IOException ex){
				ex.printStackTrace();
			}

		}else if(iMessage == Messaging.MSG_SENDFILE_APPEND){
			String sFile = Messaging.getString(oWorkByteBuffer);
			byte[] iData = Messaging.getBytes(oWorkByteBuffer);

			Download oDownload;
			oDownload = _oDownloads.get(sFile);

			try{
				FileOutputStream oDownloadStream = new FileOutputStream(_sCachePath + sFile, true);
				oDownloadStream.write(iData);
				oDownloadStream.close();
				oDownload.Progress += iData.length;

				System.out.println(oDownload.Name + " " + Util.getSizeString(oDownload.Progress) + " / " + Util.getSizeString(oDownload.Length));

				if(oDownload.Progress >= oDownload.Length){
					for(MessengerListener o : _oMessengerListeners){
						_oDownloads.remove(sFile);
						synchronized(_oDownloadsReady){
							_oDownloadsReady.add(sFile);
							_oDownloadsReady.notifyAll();
						}
					}
				}

			}catch(Exception ex){
				ex.printStackTrace();
			}

		}else if(iMessage == Messaging.MSG_VERSION_MAP){
			final int iMap = oWorkByteBuffer.getInt();
			final int iVersion = oWorkByteBuffer.getInt();

			_queueMapVersion.put(iMap, iVersion);
			synchronized(_queueMapVersion){
				_queueMapVersion.notifyAll();
			}

		}else if(iMessage == Messaging.MSG_PLAYER_MOVE){
			int iId = oWorkByteBuffer.getInt();
			byte iDirection = oWorkByteBuffer.get();
			byte iFlag = oWorkByteBuffer.get();

			for(MessengerListener o : _oMessengerListeners){
				o.playerMove(iId, iDirection, iFlag);
			}
		}else if(iMessage == Messaging.MSG_UPDATE_POSITION){
			int iId = oWorkByteBuffer.getInt();
			float x = oWorkByteBuffer.getFloat();
			float y = oWorkByteBuffer.getFloat();

			for(MessengerListener o : _oMessengerListeners){
				o.updatePlayerPosition(iId, x, y);
			}
		}else if(iMessage == Messaging.MSG_PLAYER_SPRITE_FILENAME){
			final int iId = oWorkByteBuffer.getInt();
			final String sSpriteFilename = Messaging.getString(oWorkByteBuffer);

			for(MessengerListener o : _oMessengerListeners){
				o.playerSpriteFilename(iId, sSpriteFilename);
			}

		}else if(iMessage == Messaging.MSG_PING_32){
			byte[] o = Messaging.getBytes(oWorkByteBuffer);
			if(o[0] == 0){ // 0 means it isn't a response, so we respond to it
				_oClientThread.send(Messaging.ping32(true));
			}else{
				final CallbackPing32 oCallback;
				oCallback = (CallbackPing32)_oMapCallbacks.get(iMessage);
				if(oCallback != null){
					_oMapCallbacks.remove(iMessage);
					oCallback.callback();
				}
			}

		}else{
			System.out.println("Unknown message: " + iMessage);
		}
	}

	public void addMessengerListener(MessengerListener o){
		_oMessengerListeners.add(o);
	}

	public void removeMessengerListener(MessengerListener o){
		_oMessengerListeners.remove(o);
	}

	public void setClientThread(NioClient o){
		_oClientThread = o;
	}

	public NioClient getClientThread(){
		return _oClientThread;
	}

	public void setCachePath(String s){
		_sCachePath = s;
	}

	public String getCachePath(){
		return _sCachePath;
	}

	public void addCallback(short iMessage, Callback oCallback){
		_oMapCallbacks.put(iMessage, oCallback);
	}

	public void send(ByteBuffer oByteBuffer, short iMessage, Callback oCallback){
		addCallback(iMessage, oCallback);
		_oClientThread.send(oByteBuffer);
	}

	/**
	 * sends a versionMap message to the server, and waits for its response. Blocking method.
	 * @param iMap Id of the map which's version is wanted
	 * @return the publicVersion of the map
	 */
	public int getMapVersion(int iMap){
		_oClientThread.send(Messaging.versionMap(iMap));
		Integer oVersion;
		while(true){
			synchronized(_queueMapVersion){
				oVersion = _queueMapVersion.get(iMap);
				if(oVersion != null){
					_queueMapVersion.remove(oVersion);
					break;
				}
				try {
					_queueMapVersion.wait();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
		return oVersion;

	}

	/**
	 * sends a logIn message to the server and waits for its response. Blocking method.
	 * @param sUsername
	 * @param sPassword
	 * @return LoginData object with all the login data
	 */
	public LoginData logIn(String sUsername, String sPassword){
		_oClientThread.send(Messaging.logIn(sUsername, sPassword));
		while(true){
			synchronized(_queueLogInData){
				if(_queueLogInData.Response > 0){
					break;
				}
				try {
					_queueLogInData.wait();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
		return _queueLogInData;
	}

	/**
	 * Hangs in an infinite loop until sFile
	 * @param sFile
	 */
	public void waitForDownload(String sFile){
		while(true){
			synchronized(_oDownloadsReady){
				if(_oDownloadsReady.contains(sFile)){
					System.out.println("donloaded " + sFile);
					_oDownloadsReady.remove(sFile);
					break;
				}
				try {
					_oDownloadsReady.wait();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Check whether a file exists, and if it doesn't, downloads it.
	 * @param sFile Path to the file
	 * @return <code>true</code> if file didn't exist and is going to be downloaded, <code>false</code> otherwize
	 */
	public boolean checkAndDownload(String sFile){
		return checkAndDownload(sFile, true);
	}

	/**
	 * Check whether a file exists, and if it doesn't, downloads it.
	 * @param sFile Path to the file
	 * @param bWait if true, calling thread will wait until file finished downloading
	 * @return <code>true</code> if file didn't exist and is going to be downloaded, <code>false</code> otherwize
	 */
	public boolean checkAndDownload(String sFile, boolean bWait){
		if(!new File(getCachePath() + sFile).exists()){
			download(sFile, bWait);
			return true;
		}
		return false;
	}

	/**
	 * Calls download(sFile, true);
	 * @param sFile Path to the file to download
	 */
	public void download(String sFile){
		download(sFile, true);
	}

	/**
	 * Start downloading a file
	 * @param sFile Path to the file to download
	 * @param bWait if true, calling thread will wait until file finished downloading
	 */
	public void download(String sFile, boolean bWait){
		System.out.println("download request: " + sFile);
		_oClientThread.send(Messaging.requestFile(sFile, 0));
		if(bWait)
			waitForDownload(sFile);
	}

	/**
	 * Loads iMap. Downloads if doesn't exist locally, or if there's a new version. Blocking method.
	 * @param iMap Map Id
	 * @return the map
	 */
	public GameMap loadMap(int iMap){
		String sMap = "data/" + iMap + ".map";
		boolean bMustCheckVersion = !checkAndDownload(sMap);

		GameMap oMap = GameMap.read(_sCachePath + sMap, true);

		if(oMap == null){
			download(sMap);
			oMap = GameMap.read(_sCachePath + sMap);
		}else if(bMustCheckVersion){
			int iVersion = getMapVersion(iMap);
			if(oMap.getPublicVersion() < iVersion){
				System.err.println(sMap + ": update required " + "(local version: " + oMap.getPublicVersion() +  "; server serion: " + iVersion + ")");
				download(sMap);
				oMap = GameMap.read(_sCachePath + sMap);

			}else{
				System.err.println(sMap + ": version is up to date (" + oMap.getPublicVersion() + ")");
			}
		}else{
			System.err.println(sMap + ": downloaded");
		}

		return oMap;

	}


}
