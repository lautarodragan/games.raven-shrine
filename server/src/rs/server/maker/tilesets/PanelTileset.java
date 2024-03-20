package rs.server.maker.tilesets;

import java.awt.AlphaComposite;
import java.io.IOException;
import rs.resources.Paths;
import rs.resources.Tileset;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import rs.RavenShrineConstants;

public class PanelTileset extends JPanel implements  MouseMotionListener, MouseListener{	

	public enum Mode{
		View, SelectTiles, EditPassability, EditPriority, EditPassabilityWays
	};

	public static interface PanelTilesetListener extends EventListener{
		public void selectionChanged(PanelTilesetEvent o);
	}

	public static class PanelTilesetEvent extends EventObject{
		private int[][] _iSelection;

		public PanelTilesetEvent(Object source, int[][] iSelection) {
			super(source);
			_iSelection = iSelection;
		}

		public int[][] selection(){
			return _iSelection;
		}


	}

	private Vector<PanelTilesetListener> _oPanelTilesetListeners = new Vector<PanelTilesetListener>();
	private Tileset _oTileset;
	private BufferedImage _oImageTileset = null;
	private BufferedImage[] _oImageAutotiles = new BufferedImage[8];
	private boolean _bDrawCheckeredBackground = true;
	private boolean _bDrawGrid = false;
	private Mode _eMode = Mode.View;
	private int _iCursorX;
	private int _iCursorY;
	private int _iSelectionX;
	private int _iSelectionY;
	private byte _iSelectedPencil = 0;
	private int _iSelectedDirection = 0;
	private BufferedImage _oImageCursorBlock = null;
	private BufferedImage _oImageCursorPass = null;
	private BufferedImage[] _oImagePriority = new BufferedImage[6];
	private BufferedImage[][] _oImagePassabilityWays = new BufferedImage[4][2];
	private boolean _bBlank;

	////////////////////////////////
	//		CONSTRUCTORS
	////////////////////////////////

	public PanelTileset(){
		addMouseMotionListener(this);
		addMouseListener(this);
		try{
			_oImageCursorBlock = ImageIO.read(getClass().getResourceAsStream("/rs/server/maker/icons/Cursor_Cross_Opaque.png"));
			_oImageCursorPass = ImageIO.read(getClass().getResourceAsStream("/rs/server/maker/icons/Cursor_Circle_Opaque.png"));
			for(int i = 0; i < 6; i++)
				_oImagePriority[i] = ImageIO.read(getClass().getResourceAsStream("/rs/server/maker/icons/Cursor_Star" + i + "_Opaque.png"));
			for(int i = 0; i < 4; i++)
				for(int j = 0; j < 2; j++)
					_oImagePassabilityWays[i][j] = ImageIO.read(getClass().getResourceAsStream("/rs/server/maker/icons/Passability4-" + (i) + "-" + (j) + ".png"));

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	////////////////////////////////
	//		EVENT FIRING
	////////////////////////////////

	public synchronized void addPanelTilesetListener(PanelTilesetListener o){
		_oPanelTilesetListeners.add(o);
	}

	public synchronized void removePanelTilesetListener(PanelTilesetListener o){
		_oPanelTilesetListeners.remove(o);
	}

	public synchronized void fireSelectionChanged(int[][] iSelection){
		PanelTilesetEvent ev = new PanelTilesetEvent(this, iSelection);

		for(PanelTilesetListener o : _oPanelTilesetListeners){
			o.selectionChanged(ev);
		}
	}


	////////////////////////////////
	//		PROPERTIES
	////////////////////////////////

	public void setDrawCheckeredBackground(boolean b){
		_bDrawCheckeredBackground = b;
		repaint();
	}

	public boolean getDrawCheckeredBackground(){
		return _bDrawCheckeredBackground;
	}

	public void setDrawGrid(boolean bValue){
		_bDrawGrid = bValue;
		repaint();
	}

	public boolean getDrawGrid(){
		return _bDrawGrid;
	}

	public void setTileset(Tileset o){
		_oTileset = o;

//		String sFile = _oTileset.getFilename();
//		File oFile = new File(sFile);
//		File oFile = new File(_oTileset.getFilename());
//		if(oFile.exists() && oFile.isFile()){
//			try{
//				_oImageTileset = ImageIO.read(oFile);
//			}catch(IOException ex){
//				ex.printStackTrace();
//			}
//
//		}

		if(o != null)
		updateAutotileImages();
	}

	public Tileset getTileset(){
		return _oTileset;
	}

	public void setMode(Mode eMode){
		_eMode = eMode;
		updateUI();
	}

	public Mode getMode(){
		return _eMode;
	}

	/**
	 * Set this PanelTileset's Blank flag to true or false.
	 * @param b if true, the autotiles, tileset, and editing icons won't be drawn.
	 */
	public void setBlank(boolean b){
		_bBlank = b;
		repaint();
	}


	////////////////////////////////
	//		PAINTING
	////////////////////////////////

	@Override
	public void paint(Graphics g){
		Graphics2D oGraphics = (Graphics2D)g;

		if(_bDrawCheckeredBackground){
			drawCheckeredBackground(oGraphics);
			
		}else{
			g.setColor(new Color(208, 208, 208));
			g.fillRect(0, 0, getWidth(), getHeight());

			if(_oImageTileset != null && !_bBlank){
				g.setColor(Color.white);
				oGraphics.setColor(new Color(239, 243, 255));
				g.fillRect(0, 0, _oImageTileset.getWidth(), _oImageTileset.getHeight() + 32);
			}

		}

		if(!_bBlank){
			for(int i = 0; i < _oImageAutotiles.length; i++){
				if(_oImageAutotiles[i] == null){
					continue;
				}
				oGraphics.drawImage(_oImageAutotiles[i], i * 32, 0, i * 32 + 32, 32, 0, 0, 32, 32, null);
			}

			if(_oImageTileset != null){
				oGraphics.drawImage(_oImageTileset, null, 0, 32);
			}

			if(_oImageTileset != null){
				switch(_eMode){
					case EditPassability:
						drawPassability(oGraphics);
						break;
					case EditPriority:
						drawPriority(oGraphics);
						break;
					case EditPassabilityWays:
						drawPassabilityWays(oGraphics);
						break;
				}
			}

		}

		
//		for(int x = 0; x < 32; x++)
//		for(int y = 0; y < 32; y++)
//			if(x > 16 && Math.abs(y - 16) < x - 16)
//				oGraphics.drawLine(x, y, x, y);

		if(_bDrawGrid && _oImageTileset != null && !_bBlank){
			drawGrid(oGraphics);
		}

		if(_eMode == Mode.SelectTiles){
			Rectangle o = getSelectionRectangle();
			drawSelectionRectangle(oGraphics, o.x * 32, o.y * 32, o.width * 32 - 1, o.height * 32 - 1);
		}
	}

	public static void drawSelectionRectangle(Graphics2D g, int x, int y, int w, int h){
		g.setColor(new Color(0, 0, 0));
		g.drawRect(x, y, w, h);
		g.setColor(new Color(255, 255, 255));
		g.drawRect(x + 1, y + 1, w - 2, h - 2);
		g.drawRect(x + 2, y + 2, w - 4, h - 4);
		g.setColor(new Color(0, 0, 0));
		g.drawRect(x + 3, y + 3, w - 6, h - 6);
	}

	public void drawCheckeredBackground(Graphics2D g){
		Color oColors[][] = new Color[2][2]; // [inside/outside][firstcolor/secondcolor]
		oColors[0][0] = new Color(205, 205, 205);
		oColors[0][1] = new Color(151, 151, 151);
		oColors[1][0] = new Color(255, 255, 255);
		oColors[1][1] = new Color(191, 191, 191);
		int iColor;

		for(int x = 0; x < getWidth() / 16 + 1; x++){
			for(int y = 0; y < getHeight() / 16 + 1; y++){
				iColor = (_bBlank || _oImageTileset == null || (x >= _oImageTileset.getWidth() / 16) || (y >= _oImageTileset.getHeight() / 16)) ? 0 : 1;
				g.setColor(oColors[iColor][(x + y) % 2 == 0 ? 0 : 1]);
				g.fillRect(x * 16, y * 16, 16, 16);

			}
		}
	}

	public void drawGrid(Graphics2D g){
		g.setColor(new Color(0, 0, 0, 48));
		for(int x = 0; x < _oImageTileset.getWidth() / 32; x++){
			for(int y = 0; y < _oImageTileset.getHeight() / 32 + 1; y++){
				g.drawRect(x * 32, y * 32, 31, 31);
			}
		}
	}

	public void drawPassability(Graphics2D g){
		for(int x = 0; x < _oImageTileset.getWidth() / 32; x++){
			for(int y = 0; y < _oImageTileset.getHeight() / 32 + 1; y++){
				Tileset.Tile oTile = _oTileset.getTile(getTileIndex(x, y - 1));

				Composite oOldComposite = g.getComposite();
				if(x != _iCursorX || y != _iCursorY + 1)
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

				g.drawImage(oTile.isPassable() ? _oImageCursorPass : _oImageCursorBlock, null, x * 32, y * 32);

				g.setComposite(oOldComposite);
			}
		}
	}

	public void drawPriority(Graphics2D g){
		for(int x = 0; x < _oImageTileset.getWidth() / 32; x++){
			for(int y = 0; y < _oImageTileset.getHeight() / 32 + 1; y++){
				Tileset.Tile oTile = _oTileset.getTile(getTileIndex(x, y - 1));

				Composite oOldComposite = g.getComposite();
				if(x != _iCursorX || y != _iCursorY + 1)
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

				g.drawImage(_oImagePriority[oTile.Priority], null, x * 32, y * 32);

				g.setComposite(oOldComposite);
			}
		}
	}

	public void drawPassabilityWays(Graphics2D g){
		for(int x = 0; x < _oImageTileset.getWidth() / 32; x++){
			for(int y = 0; y < _oImageTileset.getHeight() / 32 + 1; y++){
				Tileset.Tile oTile = _oTileset.getTile(getTileIndex(x, y - 1));

				Composite oOldComposite = g.getComposite();

				for(int i = 0; i < 4; i++){
					if(x != _iCursorX || y != _iCursorY + 1 || _iSelectedDirection != i)
						g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					else
						g.setComposite(oOldComposite);
					g.drawImage(_oImagePassabilityWays[i][oTile.isPassable(i) ? 0 : 1], null, x * 32, y * 32);
				}

				g.setComposite(oOldComposite);
			}
		}
	}

	
	////////////////////////////////
	//		EVENT HANDLING
	////////////////////////////////

	public void mouseDragged(MouseEvent e){
		if(_oTileset == null || _oImageTileset == null)
			return;
		if(_eMode == Mode.SelectTiles){
			_iSelectionX = Math.max(Math.min(e.getX() / 32, _oImageTileset.getWidth() / 32 - 1), 0);
			_iSelectionY = Math.max(Math.min(e.getY() / 32 - 1, _oImageTileset.getHeight() / 32 - 2), -1);
		}else{
			_iCursorX = e.getX() / 32;
			_iCursorY = e.getY() / 32 - 1;

			Tileset.Tile oTile = _oTileset.getTile(getTileIndex(_iCursorX, _iCursorY));
			if(_oImageTileset != null && isCursorOverImage())
			if(_eMode == Mode.EditPassability)
				oTile.setPassable(_iSelectedPencil == 1);
			else if(_eMode == Mode.EditPriority)
				oTile.Priority = _iSelectedPencil;
			else if(_eMode == Mode.EditPassabilityWays)
				oTile.setPassable(_iSelectedDirection, _iSelectedPencil == 1);
		}
		
		repaint();
	}

	public void mouseMoved(MouseEvent e){
		if(_oTileset == null || _oImageTileset == null)
			return;
		if(_eMode != Mode.SelectTiles){
			_iCursorX = e.getX() / 32;
			_iCursorY = e.getY() / 32 - 1;
		}
		int iX, iY;
		iX = e.getX() % 32;
		iY = e.getY() % 32;

		if(iY <= 16 && Math.abs(iX - 16) <= 16 - iY){
			_iSelectedDirection = RavenShrineConstants.RS_DIR_N;
		}else if(iY > 16 && Math.abs(iX - 16) <= iY - 16){
			_iSelectedDirection = RavenShrineConstants.RS_DIR_S;
		}else if(iX <= 16 && Math.abs(iY - 16) <= 16 - iX){
			_iSelectedDirection = RavenShrineConstants.RS_DIR_W;
		}else if(iX > 16 && Math.abs(iY - 16) <= iX - 16){
			_iSelectedDirection = RavenShrineConstants.RS_DIR_E;
		}

		repaint();
	}

	public void mouseClicked(MouseEvent e){
	}

	public void mousePressed(MouseEvent e){
		requestFocusInWindow();
		if(_oTileset == null || _oImageTileset == null)
			return;
		
		_iCursorX = e.getX() / 32;
		_iCursorY = e.getY() / 32 - 1;
		
		if(isCursorOverImage()){
			_iSelectionX = _iCursorX;
			_iSelectionY = _iCursorY;
		}

		Tileset.Tile oTile = _oTileset.getTile(getTileIndex(_iCursorX, _iCursorY));

		if(_eMode == Mode.EditPassability && _oImageTileset != null && isCursorOverImage()){
			oTile.setPassable(!oTile.isPassable());
			_iSelectedPencil = (byte)(oTile.isPassable() ? 1 : 0);
		}else if(_eMode == Mode.EditPriority && _oImageTileset != null && isCursorOverImage()){
			if(e.getButton() == 1){
				if(oTile.Priority >= 5)
					oTile.Priority = 0;
				else
					oTile.Priority++;
			}else if(e.getButton() == 3){
				if(oTile.Priority <= 0)
					oTile.Priority = 5;
				else
					oTile.Priority--;
			}
			_iSelectedPencil = oTile.Priority;
		}else if(_eMode == Mode.EditPassabilityWays && _oImageTileset != null && isCursorOverImage()){
			oTile.setPassable(_iSelectedDirection, !oTile.isPassable(_iSelectedDirection));
			_iSelectedPencil = (byte)(oTile.isPassable(_iSelectedDirection) ? 1 : 0);
		}
		repaint();
	}

	public void mouseReleased(MouseEvent e){
		fireSelectionChanged(getSelection());
	}

	public void mouseEntered(MouseEvent e){
	}

	public void mouseExited(MouseEvent e){
		if(_oTileset == null || _oImageTileset == null)
			return;
		if(_eMode != Mode.SelectTiles){
			_iCursorX = -1;
			_iCursorY = -2;
			repaint();
		}
	}

	public void updateAutotileImages(){
		for(int i = 0; i < _oTileset.autotiles().length; i++){

			if(_oTileset.autotiles()[i] == null){
				_oImageAutotiles[i] = null;
				continue;
			}
			if(_oTileset.autotiles()[i].length() < 1){
				_oImageAutotiles[i] = null;
				continue;
			}
			File oFile = new File(Paths.PATH_GRAPHICS_AUTOTILES + "/" + _oTileset.autotiles()[i]);
			if(!oFile.exists()){
				_oImageAutotiles[i] = null;
				continue;
			}
			try{
				_oImageAutotiles[i] = ImageIO.read(oFile);
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
	}


	////////////////////////////////
	//		ETC
	////////////////////////////////

	/**
	 * Loads sFile and uses it as the tileset image. The "Blank" tag is set to false,
	 * and size is readjusted to fit the new tileset image.
	 * @param sFile path to the tileset image file
	 * @param bCreateAutotiles if true, this PanelTileset's Tileset's Tile array will be replaced by a
	 * new one, with the size for the new tileset image.
	 * @throws RuntimeException if this.getTileset() == null
	 */
	public void loadImage(String sFile, boolean bCreateAutotiles){
		try{
			if(_oTileset == null){
				throw new RuntimeException("Can't load an image if there is no tileset!");
			}

			_oImageTileset = ImageIO.read(new File(sFile));

			if(bCreateAutotiles){
				int iTilesetDataLength = getTileIndex(_oImageTileset.getWidth() / 32, _oImageTileset.getHeight() / 32);
				Tileset.Tile[] oTiles;
				oTiles = new Tileset.Tile[iTilesetDataLength];
				for(int i = 0; i < oTiles.length; i++){
					oTiles[i] = new Tileset.Tile();
				}
				_oTileset.setTiles(oTiles);


			}
			_bBlank = false;

			setSize(_oImageTileset.getWidth(), _oImageTileset.getHeight() + 32);
			setPreferredSize(new Dimension(_oImageTileset.getWidth(), _oImageTileset.getHeight() + 32));

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * Returns index of the tile located at (x, y) on this component.
	 * If it's an autotile, the returned value's autotile bit will be 1.
	 * The returned value doesn't contain any autotile data.
	 * 
	 * @param x location of the tile
	 * @param y location of the tile
	 * @return index of the tile at the location
	 */
	public int getTileIndex(int x, int y){
		if(_oImageTileset == null){
			throw new RuntimeException("_oImageTileset == null");
		}else{
			if(y >= 0){
				return (x + y * _oImageTileset.getWidth() / 32);
			}else if(y == -1){
				return (x | RavenShrineConstants.RS_AUTOTILE_BIT);
			}else{
				throw new RuntimeException("y < -1 (x = " + x + ", y = " + y + ")");
			}
		}
	}

	public boolean isCursorOverImage(){
		return (_iCursorX >= 0 && _iCursorX < _oImageTileset.getWidth() / 32) && (_iCursorY >= -32 && _iCursorY < _oImageTileset.getHeight() / 32);
	}

	public Rectangle getSelectionRectangle(){
		int x, y, w, h;

		if(_iSelectionX >= _iCursorX){
			x = _iCursorX;
			w = _iSelectionX - _iCursorX + 1;
		}else{
			x = _iSelectionX;
			w = _iCursorX - _iSelectionX + 1;
		}
		if(_iSelectionY >= _iCursorY){
			y = _iCursorY + 1;
			h = _iSelectionY - _iCursorY + 1;
		}else{
			y = _iSelectionY + 1;
			h = _iCursorY - _iSelectionY + 1;
		}

		return new Rectangle(x, y, w, h);
	}

	public int[][] getSelection(){
		Rectangle oRecSelection = getSelectionRectangle();
		int[][] iSelection = new int[oRecSelection.width][oRecSelection.height];

		for(int x = 0; x < oRecSelection.width; x++){
			for(int y = 0; y < oRecSelection.height; y++){
				iSelection[x][y] = getTileIndex(x + oRecSelection.x, y + oRecSelection.y - 1);
			}
		}

		return iSelection;
	}

	public void clearSelection(){
		_iCursorX = -1;
		_iCursorY = -2;
		_iSelectionX = -1;
		_iSelectionY = -2;
		repaint();
	}
}
