 /*
-Djava.security.policy=applet.policy
 -Djava.security.policy=applet.policy
 -Djava.library.path=E://lwjgl-2.2.0/native/windows
 -Dorg.lwjgl.util.Debug=true
*/

package rs.client;

import java.awt.Canvas;
import rs.client.gears.Timer;
import rs.client.gears.AngelCodeFont;
import rs.client.gears.Texture;
import rs.client.gears.Camera;
import rs.client.gears.Color;
import rs.client.gears.Sprite;
import rs.client.gears.Actor;
import rs.client.gears.GLTileset;
import rs.client.gears.InputListener;
import rs.resources.*;
import rs.sockets.NioClient;
import rs.client.sockets.Messenger;
import rs.client.sockets.MessengerListener;
import rs.sockets.Messaging;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.Rectangle;
import rs.RavenShrineConstants;
import rs.client.gears.PlayerSpriteEvent;
import rs.client.gears.PublicChat;
import rs.client.gears.Util;
import rs.client.gears.gui.Button;
import rs.client.gears.gui.Label;
import rs.client.gears.gui.MouseListener;
import rs.client.gears.gui.TextAlign;
import rs.client.gears.gui.Textbox;
import rs.client.gears.gui.Window;

public class Main implements Runnable, MessengerListener, RavenShrineConstants{
	// <editor-fold defaultstate="collapsed" desc="Declarations">

	public static final long SECOND_IN_NANOS = (long) Math.pow(10, 9);

	private static final String WINDOW_TITLE = "Raven Shrine";
	private static final float DESIRED_FPS = 60;
	private static final float DESIRED_FRAME_LENGHT_NANO = (float)((Math.pow(1000, 3)) / DESIRED_FPS);
	private static final float DESIRED_FRAME_LENGHT_MILLI = DESIRED_FRAME_LENGHT_NANO / 1000000;
	private static final boolean LIMIT_FPS = false;
	//private static final String DEFAULT_SERVER_IP = "ravenshrine.servegame.com";
	private static final String DEFAULT_SERVER_IP = "localhost";
	private static final int DEFAULT_SERVER_PORT = 50100;
	private static final String FILE_CONFIG = "config.txt";

	private static final int
			SCENE_CONNECTING = 1,
			SCENE_FAILED_TO_CONNECT = 2,
			SCENE_LOG_IN = 3,
			SCENE_MAP = 4,
			SCENE_CHECKING_FOR_UPDATES = 5,
			SCENE_LOGIN_IN = 6,
			SCENE_INVALID_USERNAME_OR_PASSWORD = 7;

	private static final String sTextDebugKeyRef =
				  "F1 - Toggle debug key reference display"
				+ "\nF2 - Toggle debug info display"
				+ "\n\nF5 - Toggle Limit FPS"
				+ "\nF6 - Toggle Walk Through"
				+ "\nF7 - Toggle Custom Cursors"
				+ "\nF8 - Super Speed"
				+ "\n\nF9 - Test pixels per second"
				+ "\nF10 - Print FPS";

	private boolean _bGameRunning;
	private boolean _bFullscreen;
	private boolean _bMustUpdateDisplay;
	private boolean _bLimitFps = LIMIT_FPS;
	private long _iFrames;
	private Timer _oTimer = new Timer();
	private float _iFrameTimeStart;
	private float _iFrameTimeLength;
	private float _iFrameTimeSleep;
	private float _iFrame15TimeStart = System.nanoTime();
	private float _iFrame15TimeLength;
	private float _iFrame15FrameStart;
	private float _iFps15;
	private float _iFpsSecond;
	private float _iFpsSecondStart;
	private float _iFpsSecondFrameStart;
//	private int _iIgnoreInputRemainingFrames;
	private int _iScene;
	private int _iWindowFocus = 0;
	private String _sCachePath;
	private boolean _bCustomCursors;
	private int _iScreenWidthWindowed = 640, _iScreenWidthFull = 1280,
				_iScreenHeightWindowed = 480, _iScreenHeightFull = 1024;

	private String _sDebug = "";
	
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="More Declarations">
	private NioClient _oClientThread;
	private Messenger _oMessenger;
	private Canvas _oCanvas;
	private Camera _oCamera = new Camera();

	private GameMap[][] _oMaps = new GameMap[3][3];
	private GameMap _oMap;

	private Texture _oLoginBackground;
	private Texture _oLoginLogo;
	private Texture _oLoginBox;
	private List<Tileset> _oTilesets;
	private Tileset _oTileset;
	private GLTileset _oTilesetGL;
	private Actor _oPlayerActor;
	private Sprite _oSpriteCursor;

	private float _oLoginBackgroundAlpha = 0;
	private Timer _oLoginBackgroundTimer = new Timer();
	
	private Sequencer _oSequencer;
	private Soundbank _oSoundbank;
	private Synthesizer _oSynthesizer;

	private final String _sSoundbank = "soundbank-deluxe.gm";
	private final String _sTitleSong = "music/boarfox.mid";
	private final String _sMapSong = "music/sea shanty 2.mid";

	private Window _oDebugRefWindow;
	private Button btnAttack;
	private Button btnDefend;
	private Window wndDebugResolution;
	private Button btnDebugResolution640;
	private Button btnDebugResolution800;
	private Button btnDebugResolution1024;
	private Button btnDebugResolution1280;
	private Button btnPing;
	private Window wndClose;
	private Button btnCloseYes;
	private Button btnCloseNo;
	private Label lblClose;

	private AngelCodeFont _oFontTahoma16Bold;
	private AngelCodeFont _oFontTahoma12;

	private final Map<String, String> _sConfig = new HashMap();

	/**
	 * Map of Players and their Id's
	 */
	private final Map<Integer, Player> _oPlayers = new HashMap();
	/**
	 * List of all actors, both Player and NonPlayer
	 */
	private final List<Actor> _oActors = new LinkedList<Actor>();
	/**
	 * Map of PlayerActors and their Id's.
	 * The PlayerActor is the visible representation of the player.
	 */
	private final Map<Integer, Actor> _oPlayerActors = new HashMap();
	/**
	 * Map of NonPlayerActors and their Id's.
	 * NonPlayerActors are the commonly know NPC's
	 */
	private final Map<Integer, Actor> _oNonPlayerActors = new HashMap();

	private final List<InputListener> _oKeyListeners = new ArrayList<InputListener>();
	private final List<GLTileset> _oTilesetsGlReadyToLoad = new LinkedList();
	private final List<Texture> _oTexturesToUnload = new LinkedList<Texture>();
	private final List<Window> _oWindows = new LinkedList<Window>();
	private final List<PublicChat> _oPublicChats = new LinkedList<PublicChat>();
	private final List<PlayerSpriteEvent> _oPlayerSpriteEventQueue = new ArrayList<PlayerSpriteEvent>();

	private String _sLogInMessage = "";

	private boolean _bMustInitilizePredownloadedTextures;

	private int _iUserId;
	private String _sUsername = "";
	private String _sPassword = "";

	private String _sChatInput = "";
	private String _sChat = "";
	private String _sChatPart = "";

	private Textbox txtUsername;
	private Textbox txtPassword;
	private int _iLoginBoxX;
	private int _iLoginBoxY;

	private boolean _bShowDebugKeyRef;
	private boolean _bShowDebugInfo = true;

	private long _iTestWalkSpeedTime;
	private float _iTestWalkSpeedX;

	private boolean _bNoSound = true;
	private boolean _bNoObscureNeighbours;
	private boolean _bDrawSpriteBoundings;
	// </editor-fold>

	public static void main(String argv[]){
		Thread oThread = new Thread(new Main());
		oThread.setName("Main");
		oThread.start();
	}

	public Main(java.awt.Canvas oCanvas){
		this();
		_oCanvas = oCanvas;
	}

	public Main(){
		_oCamera.setScreenSize(_iScreenWidthWindowed, _iScreenHeightWindowed);
		_sCachePath = Util.getCachePath();
		System.out.println("Cache Path: " + _sCachePath);
		readConfig();
	}

	public void run(){
		initializeGears();
		initializeGame();
		initializeNetworking();

		_oTimer.start();

		while(_bGameRunning){
			handleFrameStart();
			handleTextureInitializations();
			handleInput();
			handleLogic();
			handleGraphics();
			handleFrameEnd();
		}
		
		cleanup();
	}

	public void stop(){
		_bGameRunning = false;
	}

	private void cleanup(){
		Texture.unloadAll();
		Display.destroy();
		terminateNetworking();
		if(_oSequencer != null && _oSequencer.isOpen()){
			_oSequencer.stop();
			_oSequencer.close();
		}
//		System.exit(0);
	}

	public void loadSoundbank(){
		File oFileSoundbank = new File(_sCachePath + _sSoundbank);
		if(!oFileSoundbank.exists()){
			return;
		}
		try {
			_oSynthesizer = MidiSystem.getSynthesizer();
			_oSynthesizer.open();
			if(_oSequencer.isOpen()){
				_oSequencer.stop();
			}
			List<Transmitter> oTrasnmitters = _oSequencer.getTransmitters();
			for(Transmitter o : oTrasnmitters){
				o.close();
			}
			_oSoundbank = MidiSystem.getSoundbank(oFileSoundbank);
			_oSynthesizer.loadAllInstruments(_oSoundbank);
			_oSequencer.getTransmitter().setReceiver(_oSynthesizer.getReceiver());
			if(_oSequencer.isOpen())
				_oSequencer.start();

		} catch (InvalidMidiDataException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}catch (MidiUnavailableException ex){
			ex.printStackTrace();
		}
	}

	public void initializeGame(){
		_oLoginBackground = new Texture();
		_oLoginBackground.setName("Background");
		System.out.println("loginbox");
		_oLoginBox = new Texture(getClass().getResource("/rs/client/resources/login-box.png"));
		_oLoginLogo = new Texture(getClass().getResource("/rs/client/resources/logo3.png"));
		_oFontTahoma16Bold = new AngelCodeFont("/rs/client/resources/Tahoma.16.Bold.fnt", "/rs/client/resources/Tahoma.16.Bold_0.png", true);
//		_oSpriteCursor = new Sprite(getClass().getResource("/rs/client/resources/Cursor.Mario.Normal.png"));
//		_oSpriteCursor.getAnimations().add(new Animation());
//		_oSpriteCursor.getAnimation().Frames.add(0);

		if(new File(_sCachePath + "background-a1.png").exists()){
			_oLoginBackground.load(_sCachePath + "background-a1.png");
		}

		initializeWindows();

		if(!_bNoSound){
			try {
				_oSequencer = MidiSystem.getSequencer();

				File oFileMusic = new File(_sCachePath + _sTitleSong);

				if(oFileMusic.exists()){
					_oSequencer.setSequence(MidiSystem.getSequence(new File(_sCachePath + _sTitleSong)));
					_oSequencer.open();
					_oSequencer.start();
					System.err.println("Loaded existing music: " + _sTitleSong);
				}

//				File oFileSoundbank = new File(_sCachePath + _sSoundbank);
//				if(oFileSoundbank.exists()){
//					loadSoundbank();
//					System.err.println("Loaded existing soundbank: " + _sSoundbank);
//				}

			} catch (MidiUnavailableException ex) {
				ex.printStackTrace();
			}catch(InvalidMidiDataException ex){
				ex.printStackTrace();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}

		_sLogInMessage = "Connecting to server...";
		_bGameRunning = true;
		_iScene = SCENE_CONNECTING;
	}

	public void initializeWindows(){
		Color oBorderColor = new Color(90f/256f, 51f/256f, 32f/256f, 1);
		Color oBackColor = new Color(70f/256f, 37f/256f, 18f/256f, 1);
		txtUsername = new Textbox();
		txtUsername.setBackgroundColor(oBackColor);
		txtUsername.setBorderColor(oBorderColor);
		txtUsername.setFont(_oFontTahoma16Bold);
		txtUsername.focus();

		txtPassword = new Textbox();
		txtPassword.setBackgroundColor(oBackColor);
		txtPassword.setBorderColor(oBorderColor);
		txtPassword.setFont(_oFontTahoma16Bold);

		btnAttack = new Button();
		btnAttack.setBounds(new Rectangle(_oCamera.getScreenWidth() - 34-16- 8 - 34, 16, 34, 34));
		btnAttack.setBackgroundImage(new Texture(getClass().getResource("/rs/client/resources/icons.png"), 0, 3*34, 34, 34));
		btnAttack.setBackgroundColor(oBackColor);
		btnAttack.setBorderColor(oBorderColor);
		_oWindows.add(btnAttack);

		btnDefend = new Button();
		btnDefend.setBounds(new Rectangle(_oCamera.getScreenWidth() - 34-16 , 16, 34, 34));
		btnDefend.setBackgroundImage(new Texture(getClass().getResource("/rs/client/resources/icons.png"), 34*2, 7*34, 34, 34));
		btnDefend.setBackgroundColor(oBackColor);
		btnDefend.setBorderColor(oBorderColor);
		_oWindows.add(btnDefend);

		wndDebugResolution = new Window();
		_oWindows.add(wndDebugResolution);


		btnDebugResolution640 = new Button();
		btnDebugResolution640.setBounds(0, 0, 68, 28);
		btnDebugResolution640.setBackgroundColor(oBackColor);
		btnDebugResolution640.setBorderColor(oBorderColor);
		btnDebugResolution640.setText("640x480");
		btnDebugResolution640.setFont(_oFontTahoma16Bold);
		btnDebugResolution640.addMouseListener(new MouseListener(){
			public void mouseMoved(int x, int y) {}
			public void mousePressed(int iButton, boolean bState, int x, int y) {
				if(bState){
					changeResolutionLater(640, 480);
				}
			}
		});
		wndDebugResolution.addChild(btnDebugResolution640);

		btnDebugResolution800 = new Button();
		btnDebugResolution800.setBounds(0, (28 + 8) * 1, 68, 28);
		btnDebugResolution800.setBackgroundColor(oBackColor);
		btnDebugResolution800.setBorderColor(oBorderColor);
		btnDebugResolution800.setText("800x600");
		btnDebugResolution800.setFont(_oFontTahoma16Bold);
		btnDebugResolution800.addMouseListener(new MouseListener(){
			public void mouseMoved(int x, int y) {}
			public void mousePressed(int iButton, boolean bState, int x, int y) {
				if(!bState){
					changeResolutionLater(800, 600);
				}
			}
		});
		wndDebugResolution.addChild(btnDebugResolution800);

		btnDebugResolution1024 = new Button();
		btnDebugResolution1024.setBounds(0, (28 + 8) * 2, 68, 28);
		btnDebugResolution1024.setBackgroundColor(oBackColor);
		btnDebugResolution1024.setBorderColor(oBorderColor);
		btnDebugResolution1024.setText("1024x768");
		btnDebugResolution1024.setFont(_oFontTahoma16Bold);
		btnDebugResolution1024.addMouseListener(new MouseListener(){
			public void mouseMoved(int x, int y) {}
			public void mousePressed(int iButton, boolean bState, int x, int y) {
				if(!bState){
					changeResolutionLater(1024, 768);
				}
			}
		});
		wndDebugResolution.addChild(btnDebugResolution1024);

		btnDebugResolution1280 = new Button();
		btnDebugResolution1280.setBounds(0, (28 + 8) * 3, 68, 28);
		btnDebugResolution1280.setBackgroundColor(oBackColor);
		btnDebugResolution1280.setBorderColor(oBorderColor);
		btnDebugResolution1280.setText("1280x1024");
		btnDebugResolution1280.setFont(_oFontTahoma16Bold);
		btnDebugResolution1280.addMouseListener(new MouseListener(){
			public void mouseMoved(int x, int y) {}
			public void mousePressed(int iButton, boolean bState, int x, int y) {
				if(!bState){
					changeResolutionLater(1280, 1024);
				}
			}
		});
		wndDebugResolution.addChild(btnDebugResolution1280);

		btnPing = new Button();
		btnPing.setBackgroundColor(oBackColor);
		btnPing.setBorderColor(oBorderColor);
		btnPing.setText("ping");
		btnPing.addMouseListener(new MouseListener(){
			public void mouseMoved(int x, int y) {}
			public void mousePressed(int iButton, boolean bState, int x, int y) {
				if(!bState){
					final Timer oTimer = new Timer();
					_oMessenger.send(Messaging.ping32(false), Messaging.MSG_PING_32, new Messenger.CallbackPing32() {
						public void callback() {
							newChatLine("::ping " + oTimer.getTime());
						}
					});
				}
			}
		});
		_oWindows.add(btnPing);

		wndClose = new Window();
		wndClose.setBackgroundColor(new Color(0, 0, 0, .5f));
		wndClose.setBorderColor(wndClose.getBackgroundColor().darker());
		wndClose.setVisible(false);
		_oWindows.add(wndClose);

		btnCloseNo = new Button();
		btnCloseNo.setBackgroundColor(new Color(.1f, .1f, .1f, .5f));
		btnCloseNo.setBorderColor(btnCloseNo.getBackgroundColor().darker());
		btnCloseNo.setText("No");
		btnCloseNo.setFont(_oFontTahoma16Bold);
		btnCloseNo.setVisible(false);
		btnCloseNo.addMouseListener(new MouseListener(){
			public void mouseMoved(int x, int y) {}
			public void mousePressed(int iButton, boolean bState, int x, int y) {
				if(!bState){
					wndClose.setVisible(false);
					btnCloseYes.setVisible(false);
					btnCloseNo.setVisible(false);
					lblClose.setVisible(false);
				}
			}
		});
		wndClose.addChild(btnCloseNo);

		btnCloseYes = new Button();
		btnCloseYes.setBackgroundColor(new Color(.1f, .1f, .1f, .5f));
		btnCloseYes.setBorderColor(btnCloseYes.getBackgroundColor().darker());
		btnCloseYes.setText("Yes");
		btnCloseYes.setFont(_oFontTahoma16Bold);
		btnCloseYes.setVisible(false);
		btnCloseYes.addMouseListener(new MouseListener(){
			public void mouseMoved(int x, int y) {}
			public void mousePressed(int iButton, boolean bState, int x, int y) {
				if(!bState){
					stop();
				}
			}
		});
		wndClose.addChild(btnCloseYes);

		lblClose = new Label();
		lblClose.setVisible(false);
		lblClose.setFont(_oFontTahoma16Bold);
		lblClose.setText("Want to quit the game? :(");
		lblClose.setTextAlign(TextAlign.Middle);
		wndClose.addChild(lblClose);

		positionScreenRelative();
	}

	/**
	 * Prefetches some resources and initializes some basic stuff
	 */
	public void initializeSceneLogin(){
//		_sLogInMessage = "Checking for updates...";

		String sMarker = "graphics/marker.png";
		String sTilesets = "data/tilesets.db";

		_sLogInMessage = "Downloading...";
		_oMessenger.checkAndDownload(sMarker);
		_oMessenger.download(sTilesets); // TODO: tileset version check?
		_oMessenger.checkAndDownload("graphics/fonts/Tahoma.12_0.png");
		_oMessenger.checkAndDownload("graphics/fonts/Tahoma.12.fnt");
		_oMessenger.checkAndDownload("background-a1.png");
		
		if(!_bNoSound && _oMessenger.checkAndDownload(_sTitleSong)){
			try {
				System.err.println("Donloaded music");

				_oSequencer.setSequence(MidiSystem.getSequence(new File(_sCachePath + _sTitleSong)));
				_oSequencer.open();
				_oSequencer.start();

				if(_oSynthesizer != null){
					_oSynthesizer.close();
					_oSynthesizer.open();
				}
			} catch (InvalidMidiDataException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}catch (MidiUnavailableException ex){
				ex.printStackTrace();
			}
		}

		if(!_bNoSound)
		new Thread(){
			@Override
			public void run(){
				Timer oTimer = new Timer();
				oTimer.start();
				if(_oMessenger.checkAndDownload(_sSoundbank)){
//					try {
//						Thread.sleep(10000);
//					} catch (InterruptedException ex) {
//						Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//					}
					loadSoundbank();
					System.err.println("Downloaded soundbank: \"" + _sSoundbank + "\". Took: " + oTimer.getTime());
				}
			}
		}.start();
		
		_sLogInMessage = "Loading...";

		_oTilesets = Tileset.readTilesets(_oMessenger.getCachePath() + sTilesets);
//		if(_oTilesets == null){
//			System.err.println("_oTilesets == null");
//			System.exit(0);
//		}
		
		_iScene = SCENE_LOG_IN;
		_sLogInMessage = "";
		_sUsername = "lainmaster";
		_sPassword = "123456";

		_bMustInitilizePredownloadedTextures = true;
	}

	/**
	 * Initializes OpenGL, and some basic Display, Keyboard, and Mouse stuff.
	 */
	public void initializeGears(){
		try{
			if(_oCanvas != null){
				Display.setParent(_oCanvas);
			}else{
				Display.setTitle(WINDOW_TITLE);
				Util.setDisplayMode(_iScreenWidthWindowed, _iScreenHeightWindowed, _bFullscreen);
			}
			Display.create(new PixelFormat(1, 1, 8));
		}catch(Exception ex){
			ex.printStackTrace();
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DITHER);
		GL11.glDisable(GL11.GL_STENCIL_TEST);

		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDepthRange(0, 1);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, _oCamera.getScreenWidth(), _oCamera.getScreenHeight(), 0, -1, 1);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, _oCamera.getScreenWidth(), _oCamera.getScreenHeight());
	
		Keyboard.enableRepeatEvents(true);
//		Mouse.setGrabbed(true);

//		IntBuffer o = ByteBuffer.allocateDirect(64).asIntBuffer();
//		GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE, o);
//		int iMaxTex = o.get();
		Texture.setTextureSizeMode(Texture.TEXTURE_SIZE_MODE_IGNORE);
		//Texture.setTextureSizeMode(Texture.TEXTURE_SIZE_MODE_SPLIT);
		//Texture.setTextureMaxSize(iMaxTex);
//		System.out.println("max tex " + iMaxTex);
//		Texture.setTextureMaxSize(256);

		Timer.setTimerMode(Timer.TimerMode.JavaNanoTime);
		Util.printSystemInfo();

	}

	/**
	 * Initializes all client-server stuff
	 */
	public void initializeNetworking(){
		String sHost = _sConfig.get("ServerAddress");
		int iPort = new Integer(_sConfig.get("ServerPort"));

		try{
			System.out.println(InetAddress.getByName(sHost));
			_oClientThread = new NioClient(InetAddress.getByName(sHost), iPort);
			_oMessenger = new Messenger(_oClientThread);
			_oMessenger.addMessengerListener(this);
			_oMessenger.setCachePath(_sCachePath);
			_oClientThread.setClientListener(_oMessenger);
			Thread thread = new Thread(_oClientThread);
			thread.setName("Networking Thread");
			thread.setDaemon(true);
			thread.start();
		}catch(java.net.UnknownHostException e){
			System.out.println("Unknown host " + sHost);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void handleTextureInitializations(){
		if(_bMustInitilizePredownloadedTextures){
			_bMustInitilizePredownloadedTextures = false;
			_oFontTahoma12 = new AngelCodeFont(_oMessenger.getCachePath() + "graphics/fonts/Tahoma.12.fnt", _oMessenger.getCachePath() + "graphics/fonts/Tahoma.12_0.png");

			btnPing.setFont(_oFontTahoma12);
			btnDebugResolution640.setFont(_oFontTahoma12);
			btnDebugResolution800.setFont(_oFontTahoma12);
			btnDebugResolution1024.setFont(_oFontTahoma12);
			btnDebugResolution1280.setFont(_oFontTahoma12);

			if(_oLoginBackground.getId() < 1){
				_oLoginBackground.load(_oMessenger.getCachePath() + "background-a1.png");
			}
		}

		synchronized(_oPlayerSpriteEventQueue){
			if(!_oPlayerSpriteEventQueue.isEmpty()){
				Iterator<PlayerSpriteEvent> oIterator = _oPlayerSpriteEventQueue.iterator();
				while(oIterator.hasNext()){
					PlayerSpriteEvent o = oIterator.next();
					Actor oActor = _oPlayerActors.get(o.Id);
					// TODO: make a better system for this (PlayerSpriteEventQueue)
					if(oActor == null){
						continue;
					}
					oActor.setSprite(new SpriteChar(_sCachePath + "graphics/characters/" + o.SpriteFilename));
					_oKeyListeners.add(oActor);
					System.out.println("_oPlayerSpriteEventQueue" + o.SpriteFilename);
					oIterator.remove();
				}
			}
		}

		synchronized(_oTilesetsGlReadyToLoad){
			if(!_oTilesetsGlReadyToLoad.isEmpty()){
				for(GLTileset o : _oTilesetsGlReadyToLoad){
					o.loadTextures(_oMessenger.getCachePath() + "graphics/tilesets/", _oMessenger.getCachePath() + "graphics/autotiles/");
				}
				_oTilesetsGlReadyToLoad.clear();
			}
		}

		synchronized(_oTexturesToUnload){
			Iterator<Texture> o = _oTexturesToUnload.iterator();
			while(o.hasNext()){
				o.next().unload();
			}
			_oTexturesToUnload.clear();
		}
		
	}

	public void handleInput(){
		if(Display.isCloseRequested()){
			stop();
			return;
		}

//		if(_iIgnoreInputRemainingFrames > 0){
//			_iIgnoreInputRemainingFrames--;
//			return;
//		}

		switch(_iScene){
			case SCENE_MAP:
				handleInputMap();
				break;
			case SCENE_LOG_IN:
				handleInputLogin();
				break;
			default:
				handleInputEtc();
				break;
		}
		
	}

	public void handleInputLogin(){
		while(Keyboard.next()){
			if(!Keyboard.getEventKeyState())
				continue;
			if(Character.isLetterOrDigit(Keyboard.getEventCharacter())){
				if(_iWindowFocus == 0)
					_sUsername += Keyboard.getEventCharacter();
				else if(_iWindowFocus == 1)
					_sPassword += Keyboard.getEventCharacter();
			}else if(Keyboard.getEventKey() == Keyboard.KEY_BACK){
				if(_iWindowFocus == 0 &&_sUsername.length() > 0)
					_sUsername = _sUsername.substring(0, _sUsername.length() - 1);
				else if(_iWindowFocus == 1 &&_sPassword.length() > 0)
					_sPassword = _sPassword.substring(0, _sPassword.length() - 1);
			}else if(Keyboard.getEventKey() == Keyboard.KEY_TAB){
				if(_iWindowFocus == 0){
					_iWindowFocus = 1;
					txtPassword.focus();
					txtUsername.blur();
				}else{
					_iWindowFocus = 0;
					txtPassword.blur();
					txtUsername.focus();
				}
			}else if(Keyboard.getEventKey() == Keyboard.KEY_RETURN){
				Thread thread = new Thread(){
					@Override
					public void run(){
						handleLogIn();
					}
				};
				thread.setName("Loggin Handler Thread");
				thread.start();
			}else if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				_bGameRunning = false;
				return;
			}
		}
	}

	public void handleInputEtc(){
		while(Keyboard.next()){
			if(_iScene == SCENE_INVALID_USERNAME_OR_PASSWORD && Keyboard.getEventKeyState() == true){
				_iScene = SCENE_LOG_IN;
			}else if(_iScene == SCENE_FAILED_TO_CONNECT && Keyboard.getEventKeyState() == true){
				if(Keyboard.getEventKey() == Keyboard.KEY_RETURN){
					_iScene = SCENE_CONNECTING;
					_sLogInMessage = "Connecting to server...";
					initializeNetworking();
				}else if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
					_bGameRunning = false;
					return;
				}
			}
		}
	}

	public void handleInputMap(){
		while(Mouse.next()){
			int iMouseX = Mouse.getEventX();
			int iMouseY = _oCamera.getScreenHeight() - Mouse.getEventY();
			int iMapX = (int) Math.floor((iMouseX + _oCamera.x - _oCamera.getScreenWidth() / 2) / 32);
			int iMapY = (int) Math.floor((iMouseY + _oCamera.y - _oCamera.getScreenHeight() / 2) / 32);

			synchronized(_oWindows){
				if(!_oWindows.isEmpty()){
					Iterator<Window> oIterator = _oWindows.iterator();
					while(oIterator.hasNext()){
						Window o = oIterator.next();
						if(!o.isVisible())
							continue;
						o.mouseMoved(iMouseX - o.getBounds().getX(), iMouseY - o.getBounds().getY());
						if(Mouse.getEventButton() >= 0 && o.getBounds().contains(iMouseX, iMouseY)){
							o.mousePressed(Mouse.getEventButton(), Mouse.getEventButtonState(), iMouseX - o.getBounds().getX(), iMouseY - o.getBounds().getY());
						}
					}
				}
			}
		}
		while(Keyboard.next()){
			byte iKeyState = (byte) (Keyboard.getEventKeyState() ? 1 : 0);
			if(_sChatInput.length() < 64 && (Keyboard.getEventCharacter() > 31)){
				_sChatInput += Keyboard.getEventCharacter();
			}
			switch(Keyboard.getEventKey()){
				case Keyboard.KEY_P:
					if(!Keyboard.getEventKeyState())
						break;
					if(!GL11.glIsEnabled(GL11.GL_SCISSOR_TEST))
						GL11.glEnable(GL11.GL_SCISSOR_TEST);
					else
						GL11.glDisable(GL11.GL_SCISSOR_TEST);
					GL11.glScissor(0, 0, 640, 480);
					break;
				case Keyboard.KEY_LEFT:
					if(_oPlayerActor.isMovingLeft() == Keyboard.getEventKeyState())
						break;
					_oClientThread.send(Messaging.inputMove((byte)0, iKeyState));
					break;
				case Keyboard.KEY_RIGHT:
					if(_oPlayerActor.isMovingRight() == Keyboard.getEventKeyState())
						break;
					_oClientThread.send(Messaging.inputMove((byte)1, iKeyState));
					break;
				case Keyboard.KEY_UP:
					if(_oPlayerActor.isMovingUp() == Keyboard.getEventKeyState())
						break;
					_oClientThread.send(Messaging.inputMove((byte)2, iKeyState));
					break;
				case Keyboard.KEY_DOWN:
					if(_oPlayerActor.isMovingDown() == Keyboard.getEventKeyState())
						break;
					_oClientThread.send(Messaging.inputMove((byte)3, iKeyState));
					break;
				case Keyboard.KEY_BACK:
					if(!Keyboard.getEventKeyState())
						break;
					if(_sChatInput.length() > 0)
						_sChatInput = _sChatInput.substring(0, _sChatInput.length() - 1);
					break;
				case Keyboard.KEY_RETURN:
					if(Keyboard.getEventKeyState() && Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
						toggleFullscreen();
					}else if(Keyboard.getEventKeyState() && _sChatInput.length() > 0){
						_oClientThread.send(Messaging.publicChat(_sChatInput));
						newChatLine(_sUsername + ": " + _sChatInput);
						newChatLinePublic(_sChatInput, _iUserId);
						_sChatInput = "";
						
					}
					break;
				case Keyboard.KEY_F1:
					if(!Keyboard.getEventKeyState())
						break;
					_bShowDebugKeyRef = !_bShowDebugKeyRef;
					break;
				case Keyboard.KEY_F2:
					if(!Keyboard.getEventKeyState())
						break;
					_bShowDebugInfo = !_bShowDebugInfo;
					break;
				case Keyboard.KEY_F5:
					if(!Keyboard.getEventKeyState())
						break;

					_bLimitFps = !_bLimitFps;
//					_iIgnoreInputRemainingFrames = 60;
					_oPlayerActor.setMoving(false);

					_sChat += "\nLimiptFPS " + (_bLimitFps?"enabled":"disabled");
					updateChatPart();
					break;
				case Keyboard.KEY_F6:
					if(!Keyboard.getEventKeyState())
						break;
					_oPlayerActor.setWalkThrough(!_oPlayerActor.getWalkThrough());

					_sChat += "\nWalkThrough " + (_oPlayerActor.getWalkThrough()?"enabled":"disabled");
					updateChatPart();
					break;
				case Keyboard.KEY_F7:
					if(!Keyboard.getEventKeyState())
						break;
					if(!_bCustomCursors){
						setCustomCursor();
					}else{
						setDefaultCursor();
					}

					_sChat += "\nCustom Cursor " + (_bCustomCursors?"enabled":"disabled");
					updateChatPart();
					break;
				case Keyboard.KEY_F8:
					if(!Keyboard.getEventKeyState())
						break;

					if(_oPlayerActor.getSpace().DesiredSpeed == 2.5f){
						_oPlayerActor.getSpace().DesiredSpeed = 10;
					}else{
						_oPlayerActor.getSpace().DesiredSpeed = 2.5f;
					}

					_sChat += "\nSuper Speed " + (_oPlayerActor.getSpace().DesiredSpeed == 10?"enabled":"disabled");
					updateChatPart();
					break;
				case Keyboard.KEY_F9:
					if(!Keyboard.getEventKeyState())
						break;
					_iTestWalkSpeedTime = System.nanoTime();
					_iTestWalkSpeedX = _oPlayerActor.getSpace().x;
					break;
				case Keyboard.KEY_F10:
					if(!Keyboard.getEventKeyState())
						break;
					newChatLine("" + _iFpsSecond);
					break;
				case Keyboard.KEY_ESCAPE:
//						_oClientThread.send(Messaging.logOut());
//						_oKeyListeners.clear();
//						_oMapObjects.clear();
//						_oPlayers.clear();
//						_iScene = SCENE_LOG_IN;
//					stop();
					if(!Keyboard.getEventKeyState())
						break;
					wndClose.setVisible(!wndClose.isVisible());
					btnCloseNo.setVisible(wndClose.isVisible());
					btnCloseYes.setVisible(wndClose.isVisible());
					lblClose.setVisible(wndClose.isVisible());
					break;
			}
		}
	}

	public void handleLogic(){		
		if(_iScene == SCENE_MAP){
			if(_iTestWalkSpeedTime > 0 && System.nanoTime() - _iTestWalkSpeedTime >= SECOND_IN_NANOS){
				newChatLine("Pixels per second test: " + (System.nanoTime() - _iTestWalkSpeedTime + ", " + (_oPlayerActor.getSpace().x - _iTestWalkSpeedX)));
				_iTestWalkSpeedTime = 0;
			}
			for(InputListener o : _oKeyListeners){
				if(o.isInputEnabled())
					o.handleInput();
			}
			if(_oPlayerActor != null){
				_oCamera.set(_oPlayerActor.getSpace());
				if(_oPlayerActor.getSpace().x < 0){
					System.err.println("left " + _oPlayerActor.getSpace().x + ", " + (_oMap.getWidth() * 32 - 10));
					_oPlayerActor.getSpace().x += _oMap.getWidth() * 32 - 10;
					for(int i = 0; i < 3; i++){
						_oMaps[2][i] = _oMaps[1][i];
						_oMaps[1][i] = _oMaps[0][i];
					}
					_oMap = _oMaps[1][1];
					_oPlayerActor.setMap(_oMap);
				}else if(_oPlayerActor.getSpace().x > _oMap.getWidth() * 32){
					System.err.println("right");
					_oPlayerActor.getSpace().x -= _oMap.getWidth() * 32;
					for(int i = 0; i < 3; i++){
						_oMaps[0][i] = _oMaps[1][i];
						_oMaps[1][i] = _oMaps[2][i];
					}
					_oMap = _oMaps[1][1];
					_oPlayerActor.setMap(_oMap);
				}
				if(_oPlayerActor.getSpace().y < 0){
					System.err.println("up " + _oMaps[1][1]);
					_oPlayerActor.getSpace().y += _oMap.getHeight() * 32;
					for(int i = 0; i < 3; i++){
						_oMaps[i][2] = _oMaps[i][1];
						_oMaps[i][1] = _oMaps[i][0];
					}
					_oMap = _oMaps[1][1];
					_oPlayerActor.setMap(_oMap);

				}else if(_oPlayerActor.getSpace().y > _oMap.getHeight() * 32){
					System.err.println("down");
					_oPlayerActor.getSpace().y -= _oMap.getHeight() * 32;
					for(int i = 0; i < 3; i++){
						_oMaps[i][0] = _oMaps[i][1];
						_oMaps[i][1] = _oMaps[i][2];
					}
					_oMap = _oMaps[1][1];
					_oPlayerActor.setMap(_oMap);
				}
			}
			
			// Stops the camera from showing off-map space
//			_oCamera.setBounds(_oCamera.getScreenWidth() / 2, _oCamera.getScreenHeight() / 2, _oMap.getWidth() * 32 - _oCamera.getScreenWidth() / 2,  _oMap.getHeight() * 32 - _oCamera.getScreenHeight() / 2);
			// Stops the player from walking outside of the mape
//			if(_oPlayerMapObject != null)
//				_oPlayerMapObject.getSpace().setBounds(0, 0, _oMap.getWidth() * 32, _oMap.getHeight() * 32);

//			if(_oPlayerMapObject.getPlayerDestLocation() != null){
//				_oMarker.getSpace().set(_oPlayerMapObject.getPlayerDestLocation());
//				_oMarker.updateSpriteSpace();
//				_oMarker.getSprite().setVisible(true);
//			}else{
//				_oMarker.getSprite().setVisible(false);
//			}
		}else{
			if(_oLoginBackground.getId() > 0 && _oLoginBackgroundAlpha < 1){
				if(_oLoginBackgroundTimer.getTime() > 50){
					_oLoginBackgroundAlpha += .03f;
					_oLoginBackgroundTimer.start();
				}
			}
		}
	}

	public void handleGraphics(){
		if(_bMustUpdateDisplay){
			_bMustUpdateDisplay = false;
			updateDisplayMode();
		}

		switch(_iScene){
			case SCENE_CONNECTING:
			case SCENE_FAILED_TO_CONNECT:
			case SCENE_CHECKING_FOR_UPDATES:
			case SCENE_LOG_IN:
			case SCENE_LOGIN_IN:
			case SCENE_INVALID_USERNAME_OR_PASSWORD:
				handleLogInGraphics();
				break;
			case SCENE_MAP:
				handleMapGraphics();
		}
		if(_bShowDebugInfo){
			if(_bShowDebugKeyRef)
				GL11.glColor4d(.6, .6, .6, 1);
			else
				GL11.glColor4d(1, 1, 1, 1);

			debug("Time " + _oTimer.getTime());
			debug("Frames " + _iFrames);
			debug("FpsSecond " + _iFpsSecond);
			if(!_bNoSound)
			debug("Music " + _oSequencer.getTickPosition() + " / " + _oSequencer.getTickLength());
			
			if(_iScene == SCENE_MAP){
				debug("Player " + _oMap.getId() + ", " + _oPlayerActor.getSpace().x + ", " + _oPlayerActor.getSpace().y);
				debug("Camera " + _oCamera.x + ", " + _oCamera.y);
			}

			AngelCodeFont oFont;

			if(_oFontTahoma12 == null)
				oFont = _oFontTahoma16Bold;
			else
				oFont = _oFontTahoma12;

			oFont.drawString(8, 8, _sDebug);
			_sDebug = "";
		}

		if(_bShowDebugKeyRef){
			_oDebugRefWindow.draw();
			_oFontTahoma16Bold.drawString(38, 38, sTextDebugKeyRef, Color.white);

		}

//		if(_bCustomCursors){
//			_oSpriteCursor.getSpace().set(Mouse.getX(), _oCamera.getScreenHeight() - Mouse.getY());
//			_oSpriteCursor.draw();
//		}
		
		Display.update();
	}

	public void handleMapGraphics(){
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		
		if(_oTilesetGL.getTilesetTexture() == null){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			AngelCodeFont oFont = _oFontTahoma12;
			String s = "Graphics still loading, please wait...";
			oFont.drawString(_oCamera.getScreenWidth() / 2 - oFont.getWidth(s) / 2, _oCamera.getScreenHeight() / 2 - oFont.getHeight(s) / 2, s);
			return;
		}

		int iTile;
		int iTileInMapX, iTileInMapY;

		if(_bShowDebugKeyRef)
			GL11.glColor4d(.6, .6, .6, 1);

		int iMaxX = _oCamera.getScreenWidth() / 32 + 2;
		int iMaxY = _oCamera.getScreenHeight() / 32 + 2;
		int iTileInMapXPre =  (int) Math.floor(_oCamera.x / 32 - (_oCamera.getScreenWidth() / 2) / 32);
		int iTileInMapYPre =  (int) Math.floor(_oCamera.y / 32 - (_oCamera.getScreenHeight() / 2) / 32);
		float iScreenWidthHalfMod = (_oCamera.getScreenWidth() / 2) % 32;
		float iScreenHeightHalfMod = (_oCamera.getScreenHeight() / 2) % 32;
		int iCameraLagX, iCameraLagY;

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);

		for(int iLayer = 0; iLayer < 3; iLayer++){
			for(int x = -1; x < iMaxX; x++){
				for(int y = -1; y < iMaxY; y++){
					iTileInMapX = x + iTileInMapXPre;
					iTileInMapY = y + iTileInMapYPre;
					GameMap oMap = _oMaps[1][1];
					iTile = 0;

					int iMapX = 1, iMapY = 1;

					if(iTileInMapX < 0){
						iMapX--;
						iTileInMapX += _oMap.getWidth();
					}else if(iTileInMapX > _oMap.getWidth() - 1){
						iMapX++;
						iTileInMapX -= _oMap.getWidth();
					}
					if(iTileInMapY < 0){
						iMapY--;
						iTileInMapY += _oMap.getHeight();
					}else if(iTileInMapY > _oMap.getHeight() - 1){
						iMapY++;
						iTileInMapY -= _oMap.getHeight();
					}
					oMap = _oMaps[iMapX][iMapY];

					if(!_bNoObscureNeighbours){
						if(iMapX == 1 && iMapY == 1){
							Color.white.bind();
						}else{
							Color.gray.bind();
						}
					}

					if(oMap != null && oMap.isInsideMap(iTileInMapX, iTileInMapY)){
						iTile = oMap.getMapData()[iLayer][iTileInMapX][iTileInMapY];
						if((iTile & 0xFFFF) == RS_NULL_TILE)
							continue;
					}else{
						if(iLayer == 0)
							iTile = RS_NULL_TILE;
						else
							continue;
					}

					iCameraLagX = (int) Math.floor(_oCamera.x % 32);
					iCameraLagY = (int) Math.floor(_oCamera.y % 32);
					if(iCameraLagX < 0) iCameraLagX += 32;
					if(iCameraLagY < 0) iCameraLagY += 32;

					float iDrawX = x * 32 - iCameraLagX + iScreenWidthHalfMod;
					float iDrawY = y * 32 - iCameraLagY + iScreenHeightHalfMod;

					if(iTile != RS_NULL_TILE){
						Tileset.Tile oTile = _oTileset.getTile(iTile);
						_oTilesetGL.setDrawingDepth((oTile.Priority * 5 + iLayer) / 1000000f);
					}
					_oTilesetGL.paintTile(iDrawX, iDrawY, iTile);
				}
			}
		}

		if(_oTilesetGL._iLastTexture != -1){
			GL11.glEnd();
			_oTilesetGL._iLastTexture = -1;
		}
		
		Color.white.bind();

		synchronized(_oActors){
			for(Actor o : _oActors){
				if(o.getSprite() == null)
					continue;

				if(_bDrawSpriteBoundings){
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					Util.DrawRectangle(new Rectangle((int)Math.floor(o.getSprite().getSpace().x), (int)Math.floor(o.getSprite().getSpace().y), 32, 48), false, new Color(255, 0, 0, 128));
					Util.DrawRectangle(new Rectangle((int)Math.floor(o.getSprite().getSpace().x), (int)Math.floor(o.getSprite().getSpace().y + 32), 32, 16), true, new Color(255, 0, 0, 128));
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					Color.white.bind();
				}
				
				o.updateSpriteSpace();
				o.getSprite().getAnimation().nextFrame();
				o.getSprite().getTexture().setZ(5f  / 1000000f);
				o.getSprite().draw();
			}
		}

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		synchronized(_oPublicChats){
			if(!_oPublicChats.isEmpty()){
				Iterator<PublicChat> oIterator = _oPublicChats.iterator();
				while(oIterator.hasNext()){
					PublicChat o = oIterator.next();
					if(System.nanoTime() - o.Time > SECOND_IN_NANOS * 3){
						oIterator.remove();
						continue;
					}
					Actor oActor = _oPlayerActors.get(o.PlayerId);
					if(oActor == null){
						continue;
					}
					AngelCodeFont oFont = _oFontTahoma16Bold;
					float w = oFont.getWidth(o.Text), h = oFont.getHeight(o.toString());
					float x = oActor.getSprite().getSpace().x + oActor.getSprite().getFrameWidth() / 2 - w / 2;
					float y = oActor.getSprite().getSpace().y - oActor.getSprite().getFrameHeight() / 4 - 6;
					Util.DrawRectangle(new Rectangle((int)x - 3, (int)y - 3, (int)w + 6, (int)h + 6) , true, new Color(0, 0, 0, .5f));
					oFont.drawString(x, y, o.Text, Color.white);
				}

			}
		}

		AngelCodeFont oFont = _oFontTahoma12;
		
		Color oChatboxBackgroundColor = new Color(.6f, .3f, .1f, .5f);
		Rectangle oChatboxRectangle = new Rectangle(6, _oCamera.getScreenHeight() - 8 - 150, 500, 150);
		Util.DrawRectangle(oChatboxRectangle, true, oChatboxBackgroundColor);
		Util.DrawRectangle(oChatboxRectangle, false, oChatboxBackgroundColor.darker());

		oFont.drawString(8, _oCamera.getScreenHeight() - 28, _sUsername + ": " + _sChatInput, Color.yellow);
		oFont.drawString(8, _oCamera.getScreenHeight() - 28 - oFont.getHeight(_sChatPart) - 8, _sChatPart, Color.white);

		synchronized(_oWindows){
			if(!_oWindows.isEmpty()){
				Iterator<Window> oIterator = _oWindows.iterator();
				while(oIterator.hasNext()){
					Window o = oIterator.next();
					o.draw();
				}
			}
		}

	}	

	public void handleLogInGraphics(){
		GL11.glColor4f(1, 1, 1, _oLoginBackgroundAlpha);
		_oLoginBackground.draw(0, 0, _oCamera.getScreenWidth(), _oCamera.getScreenHeight(), 0, 0, _oCamera.getScreenWidth(), _oCamera.getScreenHeight());

		Color.white.bind();
		_oLoginLogo.draw(_oCamera.getScreenWidth() / 2 - _oLoginLogo.getWidth() / 2, 50);
		_oLoginBox.draw(_iLoginBoxX, _iLoginBoxY);

		if(_iScene == SCENE_LOG_IN){
			txtUsername.setText(_sUsername);
			txtUsername.draw();
			txtPassword.setText(_sPassword);
			txtPassword.draw();

			_oFontTahoma16Bold.drawString(_iLoginBoxX + 20, _iLoginBoxY + 18, "Username: ", Color.white);
			_oFontTahoma16Bold.drawString(_iLoginBoxX + 20, _iLoginBoxY + 48, "Password: ", Color.white);

		}else{
			_oFontTahoma16Bold.drawString(_iLoginBoxX + 20, _iLoginBoxY + 24, _sLogInMessage);
		}
	}

	public void handleFrameStart(){
		_iFrameTimeStart = System.nanoTime();

		if(Timer.getSystemTime() - _iFpsSecondStart >= 1000){
			_iFpsSecond = _iFrames - _iFpsSecondFrameStart;
			_iFpsSecondStart = Timer.getSystemTime();
			_iFpsSecondFrameStart = _iFrames;
		}

		if(_iFrames >= _iFrame15FrameStart + 15){
			_iFrame15FrameStart = _iFrames;
			_iFrame15TimeLength = Timer.getSystemTime() - _iFrame15TimeStart;
			_iFrame15TimeStart = Timer.getSystemTime();
			_iFps15 = (float) ((15000 / (_iFrame15TimeLength)) ); // 15 frames * 1000 milliseconds
			updateRelativeSpeeds();
		}

	}

	public void handleFrameEnd(){
		_iFrameTimeLength = (System.nanoTime() - _iFrameTimeStart);
		_iFrameTimeSleep = (DESIRED_FRAME_LENGHT_NANO - _iFrameTimeLength) - 1000000;
        
        if(_bLimitFps && _iFrameTimeSleep > 0){
            try{
				long millis = (long)Math.floor(_iFrameTimeSleep / 1000000);
				int nanos = (int) (_iFrameTimeSleep - millis * 1000000);
				Thread.sleep(millis, nanos);
            }catch(Exception ex){}
        }
		_iFrames++;

	}

	public void handleLogIn(){
		_sLogInMessage = "Login in...";
		_iScene = SCENE_LOGIN_IN;

		Messenger.LoginData oLoginData = _oMessenger.logIn(_sUsername, _sPassword);

		if(oLoginData.Response == 1){
			_sLogInMessage = "Loading...";

			_oMap = _oMessenger.loadMap(oLoginData.Map);

			_oTileset = rs.resources.Resourcebase.getTilesetById(_oTilesets, _oMap.getTileset());
			_oTilesetGL = new GLTileset(_oTileset);

			_oMessenger.checkAndDownload("graphics/tilesets/" + _oTileset.getFilename());
			for(int i = 0; i < _oTileset.autotiles().length; i++){
				String sPath = "graphics/autotiles/" + _oTileset.autotiles()[i];
				if(_oTileset.autotiles()[i] == null)
					continue;
				_oMessenger.checkAndDownload(sPath);
			}

			_oTilesetsGlReadyToLoad.add(_oTilesetGL);

			_oMaps[1][1] = _oMap;

			new Thread(){
				@Override
				public void run(){
					if(_oMap.getMapNorth() > -1){
						GameMap oMap = _oMessenger.loadMap(_oMap.getMapNorth());
						_oMaps[1][0] = oMap;

					}
					if(_oMap.getMapSouth() > -1){
						GameMap oMap = _oMessenger.loadMap(_oMap.getMapSouth());
						_oMaps[1][2] = oMap;

					}
					if(_oMap.getMapWest() > -1){
						GameMap oMap = _oMessenger.loadMap(_oMap.getMapWest());
						_oMaps[0][1] = oMap;

					}
					if(_oMap.getMapEast() > -1){
						GameMap oMap = _oMessenger.loadMap(_oMap.getMapEast());
						_oMaps[2][1] = oMap;

					}
					int iMap;
					
					if(_oMaps[1][0] != null && _oMaps[1][0].getMapWest() > -1){
						iMap = _oMaps[1][0].getMapWest();
					}else if(_oMaps[0][1] != null && _oMaps[0][1].getMapNorth() > -1){
						iMap = _oMaps[0][1].getMapNorth();
					}else{
						iMap = -1;
					}
					if(iMap > -1){
						GameMap oMap = _oMessenger.loadMap(iMap);
						_oMaps[0][0] = oMap;
					}

					if(_oMaps[1][0] != null && _oMaps[1][0].getMapEast() > -1){
						iMap = _oMaps[1][0].getMapEast();
					}else if(_oMaps[2][1] != null && _oMaps[2][1].getMapNorth() > -1){
						iMap = _oMaps[2][1].getMapNorth();
					}else{
						iMap = -1;
					}
					if(iMap > -1){
						GameMap oMap = _oMessenger.loadMap(iMap);
						_oMaps[2][0] = oMap;
					}

					if(_oMaps[1][2] != null && _oMaps[1][2].getMapWest() > -1){
						iMap = _oMaps[1][2].getMapWest();
					}else if(_oMaps[0][1] != null && _oMaps[0][1].getMapSouth() > -1){
						iMap = _oMaps[0][1].getMapSouth();
					}else{
						iMap = -1;
					}
					if(iMap > -1){
						GameMap oMap = _oMessenger.loadMap(iMap);
						_oMaps[0][2] = oMap;
					}

					if(_oMaps[1][2] != null && _oMaps[1][2].getMapEast() > -1){
						iMap = _oMaps[1][2].getMapEast();
					}else if(_oMaps[2][1] != null && _oMaps[2][1].getMapSouth() > -1){
						iMap = _oMaps[2][1].getMapSouth();
					}else{
						iMap = -1;
					}
					if(iMap > -1){
						GameMap oMap = _oMessenger.loadMap(iMap);
						_oMaps[2][2] = oMap;
					}

				}
			}.start();

			_oPlayerActor = new Actor(_oMap, _oCamera, _oTileset);
			Player oPlayer = new Player();
			oPlayer.Id = oLoginData.Id;
			oPlayer.Username = _sUsername;
			oPlayer.MapPosition.set(oLoginData.x, oLoginData.y);
			_iUserId = oLoginData.Id;
			_oPlayers.put(oLoginData.Id, oPlayer);
			_oPlayerActor.getSpace().set(oLoginData.x, oLoginData.y);
			_oPlayerActor._oGameMaps = _oMaps;
			_oActors.add(_oPlayerActor);
			_oPlayerActors.put(oLoginData.Id, _oPlayerActor);

			newChatLine("Welcome to Raven Shrine!");
			newChatLine("Press F1 for Debug Key Reference");

			_oDebugRefWindow = new Window();
			_oDebugRefWindow.setBounds(new Rectangle(30, 30, _oCamera.getScreenWidth() - 60, _oCamera.getScreenHeight() - 60));
			_oDebugRefWindow.setBackgroundColor(new Color(0, 0, 0, .6f));
			_oDebugRefWindow.setBorderColor(_oDebugRefWindow.getBackgroundColor().darker());

			if(!_bNoSound){
				_oMessenger.checkAndDownload(_sMapSong);
				_oSequencer.stop();
				_oSequencer.close();
				try {
					_oSequencer = MidiSystem.getSequencer();
					_oSequencer.setSequence(MidiSystem.getSequence(new File(_sCachePath + _sMapSong)));
					_oSequencer.open();

					_oSequencer.start();
					_oSequencer.setLoopCount(_oSequencer.LOOP_CONTINUOUSLY);
//					loadSoundbank();

				}catch (MidiUnavailableException ex){
					ex.printStackTrace();
				}catch(InvalidMidiDataException ex){
					ex.printStackTrace();
				}catch(IOException ex){
					ex.printStackTrace();
				}
			}

			_iScene = SCENE_MAP;

			_oTexturesToUnload.add(_oLoginLogo);
			_oTexturesToUnload.add(_oLoginBackground);
			_oTexturesToUnload.add(_oLoginBox);

		}else if(oLoginData.Response == 2){
			_sLogInMessage = "Invalid username or password";
			_iScene = SCENE_INVALID_USERNAME_OR_PASSWORD;
		}else if(oLoginData.Response == 3){
			_sLogInMessage = "Already logged in";
			_iScene = SCENE_INVALID_USERNAME_OR_PASSWORD;
		}

	}

	/**
	 * Tries and reads the config file
	 */
	public void readConfig(){
		String sFile = _sCachePath + "/" + FILE_CONFIG;
		if(new File(sFile).exists()){
			try {
				System.out.println("Found config file with the following settings:");
				BufferedReader oBufferedReader = new BufferedReader(new FileReader(sFile));
				String sLine = null;
				String[] sLineParts;
				while ((sLine = oBufferedReader.readLine()) != null) {
					sLineParts = sLine.split(":", 2);

					for(int i = 0; i < sLineParts.length; i++)
						sLineParts[i] = sLineParts[i].trim();

					if(sLineParts.length > 1){
						System.out.println(sLineParts[0] + ": " + sLineParts[1]);
						_sConfig.put(sLineParts[0], sLineParts[1]);
					}else if(sLineParts.length == 1){
						System.out.println(sLineParts[0]);
						_sConfig.put(sLineParts[0], "true");
					}
				}
			} catch (Exception ex) {
			}
		}else{
			System.out.println("No config file");
		}

		System.out.println();
		System.out.println("Using the following settings:");

		String sHost;
		String sPort;

		sHost = _sConfig.get("ServerAddress");
		if(sHost == null){
			_sConfig.put("ServerAddress", DEFAULT_SERVER_IP);
		}
		System.out.println("ServerAddress: " + _sConfig.get("ServerAddress"));

		sPort = _sConfig.get("ServerPort");
		if(sPort == null){
			_sConfig.put("ServerPort", String.valueOf(DEFAULT_SERVER_PORT));
		}
		System.out.println("ServerPort: " + _sConfig.get("ServerPort"));

		String sNoSound = _sConfig.get("NoSound");
		if(sNoSound != null && sNoSound.equals("true")){
			_bNoSound = true;
			System.out.println("NoSound");
		}

		String sNoObscureNeighbours = _sConfig.get("NoObscureNeighbours");
		if(sNoObscureNeighbours != null && sNoObscureNeighbours.equals("true")){
			_bNoObscureNeighbours = true;
			System.out.println("NoObscureNeighbours");
		}

		String sDrawSpriteBoundings = _sConfig.get("DrawSpriteBoundings");
		if(sDrawSpriteBoundings != null && sDrawSpriteBoundings.equals("true")){
			_bDrawSpriteBoundings = true;
			System.out.println("DrawSpriteBoundings");
		}
	}

	/**
	 * Updates Display, GL Viewport, and GL Ortho, using <code>_oCamera.getScreenWidth()</code>, <code>_oCamera.getScreenHeight()</code>, and <code>_bFullscreen</code>
	 */
	public void updateDisplayMode(){
		positionScreenRelative();
		try {
			Util.setDisplayMode(_oCamera.getScreenWidth(), _oCamera.getScreenHeight(), _bFullscreen);

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, _oCamera.getScreenWidth(), _oCamera.getScreenHeight(), 0, -1, 1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glViewport(0, 0, _oCamera.getScreenWidth(), _oCamera.getScreenHeight());

		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * Switches from windowed to full screen and vice versa
	 */
	public void toggleFullscreen(){
		_bFullscreen = !_bFullscreen;
		if(_bFullscreen){
			_oCamera.setScreenSize(_iScreenWidthFull, _iScreenHeightFull);
		}else{
			_oCamera.setScreenSize(_iScreenWidthWindowed, _iScreenHeightWindowed);
		}
		updateDisplayMode();
	}

	/**
	 * If in fullscreen mode, attempts to change screen resolution. Otherwise, attempts to change window size.
	 * @param iWidth either the new screen width or window width
	 * @param iHeight either the new screen height or window height
	 */
	public void changeResolution(int iWidth, int iHeight){
		_oCamera.setScreenSize(iWidth, iHeight);
		updateDisplayMode();
	}

	/**
	 * Changes resolution as soon as possible, in the running thread (instead of the caller thread)
	 * @param iWidth
	 * @param iHeight
	 */
	public void changeResolutionLater(int iWidth, int iHeight){
		if(_oCamera.getScreenWidth() == iWidth && _oCamera.getScreenHeight() == iHeight){
			return;
		}
		_oCamera.setScreenSize(iWidth, iHeight);
		_iScreenWidthWindowed = iWidth;
		_iScreenHeightWindowed = iHeight;
		_bMustUpdateDisplay = true;

	}

	/**
	 * Makes the OS' cursor invisible for the game window.
	 */
	public void setInvisibleCursor(){
		if(_bCustomCursors)
			return;
		_bCustomCursors = true;
		try {
			IntBuffer oIntBuffer = IntBuffer.allocate(256);
			Mouse.setNativeCursor(new Cursor(16, 16, 0, 0, 1, oIntBuffer, null));
		} catch (LWJGLException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Makes the OS' cursor invisible for the game window.
	 */
	public void setCustomCursor(){
//		if(_bCustomCursors)
//			return;
//		_bCustomCursors = true;
//		try {
//			BufferedImage oBufferedImage = null;

//			try{
//				oBufferedImage = ImageIO.read(getClass().getResource("/rs/client/resources/Cursor.Mario.Normal.png"));
//				_oCanvas.setCursor(_oCanvas.getToolkit().createCustomCursor(oBufferedImage, new java.awt.Point(3, 3), "asd"));
//			} catch(IOException e){
//				e.printStackTrace();
//			}

//			DataBufferByte oDataBufferByte = ((DataBufferByte) oBufferedImage.getRaster().getDataBuffer());
//			byte[] iData = oDataBufferByte.getData();
//			ByteBuffer oPixelsBuffer = ByteBuffer.allocate(iData.length);
//			IntBuffer oPixelsBufferInt = oPixelsBuffer.asIntBuffer();

//			for(int i = 0; i < iData.length; i+=4){
//				oPixelsBuffer.put((byte)254);
//				oPixelsBuffer.put((byte)128);
//				oPixelsBuffer.put((byte)64);
//				oPixelsBuffer.put((byte)32);
//			}
//			for(int x = 0; x < oBufferedImage.getWidth(); x++){
//				for(int y = 0; y < oBufferedImage.getHeight(); y++){
//					oPixelsBufferInt.put(oBufferedImage.getRGB(y, oBufferedImage.getHeight()-1-x));
//				}
//			}

//			oPixelsBuffer.rewind();

//			Cursor oCursor = new Cursor(32, 32, 0, 31, 1, oPixelsBuffer.asIntBuffer(), null);
//			Cursor o = Mouse.getNativeCursor();

//			System.out.println(o.getCapabilities());
//			System.out.println(Cursor.CURSOR_8_BIT_ALPHA);
//			System.out.println(Cursor.CURSOR_ONE_BIT_TRANSPARENCY);
//			System.out.println(Cursor.getMaxCursorSize());

//			Mouse.setNativeCursor(new Cursor(32, 32, 0, 31, 1, oPixelsBuffer.asIntBuffer(), null));
//			o.destroy();
//
//			Mouse.setNativeCursor(o);
//			Mouse.updateCursor();
//		} catch (LWJGLException ex) {
//			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//		}
	}

	/**
	 * Makes the OS' cursor the OS' default one, for the game window.
	 */
	public void setDefaultCursor(){
		if(!_bCustomCursors)
			return;
		_bCustomCursors = false;
		try {
			Mouse.setNativeCursor(null);
		} catch (LWJGLException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Updates speeds that are relative to frame rate
	 */
	public void updateRelativeSpeeds(){
		if(_oActors != null)
			for(Actor o : _oActors){
				o.getSpace().Speed = o.getSpace().DesiredSpeed * (DESIRED_FPS / _iFps15);
				if(o.getSprite() != null && o.getSprite().getAnimation() != null)
					o.getSprite().getAnimation().IntervalRelativeSpeed = (DESIRED_FPS / _iFps15);
			}
	}

	/**
	 * Sets the location of windows that have positions relative to the screen.
	 */
	public void positionScreenRelative(){
		int iLeft = 120;

		_iLoginBoxX = _oCamera.getScreenWidth() / 2 - _oLoginBox.getWidth() / 2;
		_iLoginBoxY = _oCamera.getScreenHeight() / 2 - _oLoginBox.getHeight() / 2 + 100;
		txtUsername.setBounds(_iLoginBoxX + iLeft - 3, _iLoginBoxY + 16, 199, 25);
		txtPassword.setBounds(_iLoginBoxX + iLeft - 3, _iLoginBoxY + 46, 199, 25);
		btnAttack.setBounds(_oCamera.getScreenWidth() - 34-16- 8 - 34, 16, 34, 34);
		btnDefend.setBounds(_oCamera.getScreenWidth() - 34-16 , 16, 34, 34);
		wndDebugResolution.setBounds(_oCamera.getScreenWidth() - 84 , 16 + 34 + 8, 68, 200);

		btnPing.setBounds(_oCamera.getScreenWidth() - 84  , 16 + +34+8+(34 + 8) * 4, 68, 28);

		btnDebugResolution640.setVisible(!_bFullscreen);
		wndDebugResolution.setVisible(_bFullscreen || _oCanvas == null);

		wndClose.setBounds(_oCamera.getScreenWidth() / 2 - 160, _oCamera.getScreenHeight() / 2 - 60, 320, 120);
		lblClose.setBounds(0, 0, wndClose.getBounds().getWidth(), wndClose.getBounds().getHeight() / 2);
		btnCloseNo.setBounds(wndClose.getBounds().getWidth() / 2 - 32 - 64, wndClose.getBounds().getHeight() - 40, 64, 28);
		btnCloseYes.setBounds(wndClose.getBounds().getWidth() / 2 - 32 + 64, wndClose.getBounds().getHeight() - 40, 64, 28);
		
	}

	/**
	 * Terminates all networking, by calling <code>_oClientThread.stop();</code>
	 */
	public void terminateNetworking(){
		System.out.println("terminateNetworking");
		if(_oClientThread != null)
			_oClientThread.stop();
	}

	/**
	 * Sets _sChatPart to the last 8 lines of _sChat
	 */
	public void updateChatPart(){
		int iCharPosition = _sChat.length();
		for(int i = 0; i < 8; i++){
			iCharPosition = _sChat.lastIndexOf('\n', iCharPosition - 1);
			if(iCharPosition < 0){
				iCharPosition = 0;
				break;
			}
		}
		_sChatPart = _sChat.substring(iCharPosition);

	}

	/**
	 * Inserts <code>\n + s</code> at the bottom of the chat, and calls <code>updateChatPart()</code>
	 * @param s
	 */
	public void newChatLine(String s){
		_sChat += "\n" + s;
		updateChatPart();
	}

	public void debug(String s){
		_sDebug += s + "\n";
	}

	public void newChatLinePublic(String sMessage, int iPlayer){
		PublicChat o = new PublicChat();
		o.PlayerId = iPlayer;
		o.Text = sMessage;
		o.Time = System.nanoTime();
		publicChatDeleteAll(iPlayer);
		synchronized(_oPublicChats){
			_oPublicChats.add(o);
		}
	}

	public void publicChatDeleteAll(int iPlayer){
		synchronized(_oPublicChats){
			if(!_oPublicChats.isEmpty()){
				Iterator<PublicChat> oIterator = _oPublicChats.iterator();
				while(oIterator.hasNext()){
					PublicChat o = oIterator.next();
					if(o.PlayerId == iPlayer){
						oIterator.remove();
					}
				}
			}
		}
	}

	public void acceptedConnection(){
//		_bIsConnected = true;
		_iScene = SCENE_CHECKING_FOR_UPDATES;
		initializeSceneLogin();
	}

	public void closedConnection(){
		System.out.println("Server shutted down!");
		newChatLine("Server shutted down!");
		System.exit(0);
	}

	public void refusedConnection(){
		_sLogInMessage = "Could not connect to server\nPress enter to retry";
		_iScene = SCENE_FAILED_TO_CONNECT;
	}

	public void playerMove(int iId, byte iDirection, byte iFlag) {
		if(_iScene != SCENE_MAP){
			return;
		}
		boolean bFlag = iFlag == 1;
		Actor actor = _oPlayerActors.get(iId);
		if(actor == null){
			System.err.println("\t\t\tNo player with Id " + iId);
			return;
		}
		switch(iDirection){
			case 0:
				actor.setMovingLeft(bFlag);
				break;
			case 1:
				actor.setMovingRight(bFlag);
				break;
			case 2:
				actor.setMovingUp(bFlag);
				break;
			case 3:
				actor.setMovingDown(bFlag);
				break;
		}
		
	}

	public void playerLoggedIn(final int iId, final String sUsername, final float x, final float y, final int iMap) {
		System.out.printf("%s(%d) logged in\n", sUsername, iId);
		newChatLine(sUsername + " logged in.");

		// TODO: make a better system for this (player logins)

		Thread thread = new Thread(){
			@Override
			public void run(){
				while(_iScene != SCENE_MAP){}
				Player oPlayer = new Player();
				oPlayer.Id = iId;
				oPlayer.Username = sUsername;
				oPlayer.Map = iMap;
				oPlayer.MapPosition.set(x, y);
				Actor oActor = new Actor(_oMap, _oCamera, _oTileset);
				oActor.getSpace().set(x, y);
				synchronized(_oActors){
					_oActors.add(oActor);
				}
				_oPlayerActors.put(iId, oActor);
				_oPlayers.put(iId, oPlayer);
			}
		};
		thread.setName("PlayerLoggedIn Thread");
		thread.start();
		
		
	}

	public void publicChat(int iPlayer, String sMessage) {
		Player oPlayer = _oPlayers.get(iPlayer);
		if(oPlayer == null){
			System.out.println("publicChat(" + iPlayer + ", " + sMessage + ") -> oPlayer == null");
		}
		newChatLine(oPlayer.Username + ": " + sMessage);
		newChatLinePublic(sMessage, iPlayer);
	}

	public void updatePlayerPosition(int iId, float x, float y) {
		if(_iScene != SCENE_MAP)
			return;
//		System.out.println("updatePlayerPosition " + x + ", " + y);

		Actor o = _oPlayerActors.get(iId);
		
		if(o == null){
			System.err.println("updatePlayerPosition: No player with Id " + iId);
			return;
		}

		o.getSpace().set(x, y);
	}

	public void playerLoggedOut(int iId) {
		System.out.println("playerLoggedOut iId = " + iId);
		Player oPlayer = _oPlayers.get(iId);
		if(oPlayer == null)
			return;
		newChatLine(oPlayer.Username + " logged out.");
		_oPlayers.remove(iId);
		Actor oPlayerMapObject = _oPlayerActors.get(iId);
		_oPlayerActors.remove(iId);
		_oActors.remove(oPlayerMapObject);
	}

	public void playerSpriteFilename(final int iId, final String sSpriteFilename) {
		Thread thread = new Thread(){
			@Override
			public void run(){
				System.out.println("playerSpriteFilename(" + iId + ", " + sSpriteFilename + ")");
				PlayerSpriteEvent oPlayerSpriteEvent = new PlayerSpriteEvent();
				oPlayerSpriteEvent.Id = iId;
				oPlayerSpriteEvent.SpriteFilename = sSpriteFilename;
				_oMessenger.checkAndDownload("graphics/characters/" + sSpriteFilename, true);
				synchronized(_oPlayerSpriteEventQueue){
					_oPlayerSpriteEventQueue.add(oPlayerSpriteEvent);
				}
			}
		};
		thread.start();
		
	}

}

