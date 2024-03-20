package rs.server.maker.maps;

import java.util.logging.Level;
import java.util.logging.Logger;
import rs.resources.GameMap;
import rs.resources.Paths;
import rs.resources.Tileset;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import rs.RavenShrineConstants;
import rs.server.RsServerConstants;
import rs.server.maker.cursors.Cursors;

public class PanelMap extends JPanel{
	
	public enum Mode{View, Draw, Select, Objects};

	public enum DrawMode{Pencil, Rectangle, FloodFill};

	public static interface PanelMapListener extends EventListener{
		public void mapChanged(PanelMapEvent evt);
	}

	public static class PanelMapEvent extends EventObject{
		public PanelMapEvent(Object source){
			super(source);
		}
	}

	private final List<PanelMapListener> _oPanelMapListeners = new LinkedList<PanelMapListener>();
	
	private boolean _bOptionsDimOtherLayers = true;
	private boolean _bDrawGrid = true;
	private Mode _eMode = Mode.View;
	private DrawMode _eDrawMode = DrawMode.Pencil;
	/**
	 * Zoom value used to paint this component. Defaults to 1.
	 */
	private float _iZoom = 1;
	
	private GameMap _oMap;
	private Tileset _oTileset;

	private Image _oImageTileset = null;
	private BufferedImage[] _oImageAutotiles = new BufferedImage[8];

	/**
	 * The mouse position relative to this control, measured in pixels.
	 */
	private int _iMouseX, _iMouseY;
	/**
	 * The mouse position relative to this control, measured in tiles.
	 */
	private int _iCursorX, _iCursorY;
	/**
	 * Top and left position of the camera or view of this component.
	 */
	private int _iCameraX, _iCameraY;
	private int _iSelectedTileX, _iSelectedTileY;
	private boolean _bMouseInside;
	private boolean _bMouseDragging;
	private boolean _bScrolling;
	private boolean _bIgnoreOneMouseRelease;

	private int _iScrollingX, _iScrollingY;
	private boolean _bScrollingMoveX, _bScrollingMoveY;

	private int _iSelectedLayer = 0;
	private int[][] _iSelectedTiles;
	private boolean _bCanceledPainting;
	private int[][] _iTemporalLayer;
	private Rectangle _oRectangleDraw = new Rectangle();

	private final Thread _oScrollerThread = new Thread(){
		@Override
		public void run(){
			int i = 0;
			while(!_bKillScrollerThread){
				if(_bWaitScrollerThread){
					_bWaitScrollerThread = false;
					try {
						synchronized(this){
							this.wait();
						}
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				if(_bScrolling){
					boolean bRepaint = false;
					if(_bScrollingMoveX){
						_iCameraX += (_iMouseX - _iScrollingX) / 12;
						_iCameraX ++;
						bRepaint = true;
					}
					if(_bScrollingMoveY){
						_iCameraY += (_iMouseY - _iScrollingY) / 12;
//						_iCameraY ++;
						bRepaint = true;
					}
					if(_iCameraX < 0){
						_iCameraX = 0;
					}else if(_iCameraX > _oMap.getWidth() * RavenShrineConstants.RS_TILE_SIZE - getWidth()){
						_iCameraX = _oMap.getWidth() * RavenShrineConstants.RS_TILE_SIZE - getWidth();
					}
					if(_iCameraY < 0){
						_iCameraY = 0;
					}else if(_iCameraY > _oMap.getHeight() * RavenShrineConstants.RS_TILE_SIZE - getHeight()){
						_iCameraY = _oMap.getHeight() * RavenShrineConstants.RS_TILE_SIZE - getHeight();
					}
					if(bRepaint)
						repaint();
					revalidate();
				}
				
				yield();
				try {
					sleep(10);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
	};
	private boolean _bKillScrollerThread;
	private boolean _bWaitScrollerThread;

	private final Thread _oAnimationThread = new Thread(){
		@Override
		public void run(){
			_bKillAnimationThread = false;
			while(!_bKillAnimationThread){
				if(_iAnimation < 3)
					_iAnimation++;
				else
					_iAnimation = 0;
				repaint();
				try {
					sleep(200);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
	};
	private boolean _bKillAnimationThread;
	private int _iAnimation = 0;

	private final List<Point> _oObjects = new LinkedList<Point>();

	////////////////////////////////
	//		CONSTRUCTOR
	////////////////////////////////

	public PanelMap(){
		addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e){}

			public void mousePressed(MouseEvent e){
				formMousePressed(e);
			}

			public void mouseReleased(MouseEvent e){
				formMouseReleased(e);
			}

			public void mouseEntered(MouseEvent e){
				formMouseEntered(e);
			}

			public void mouseExited(MouseEvent e){
				formMouseExited(e);
			}

		});
		addMouseMotionListener(new MouseMotionListener(){

			public void mouseDragged(MouseEvent e){
				formMouseDragged(e);
			}

			public void mouseMoved(MouseEvent e){
				formMouseMoved(e);
			}

		});
		
		synchronized(_oScrollerThread){
			_bWaitScrollerThread = true;
			_oScrollerThread.setDaemon(true);
			_oScrollerThread.setPriority(Thread.MIN_PRIORITY);
			_oScrollerThread.start();

		}

		synchronized(_oAnimationThread){
			_oAnimationThread.setDaemon(true);
			_oAnimationThread.setPriority(Thread.MIN_PRIORITY);
			_oAnimationThread.start();
		}

	}


	////////////////////////////////
	//		EVENTS HANDLERS
	////////////////////////////////

	public void addPanelMapListener(PanelMapListener o){
		synchronized(_oPanelMapListeners){
			_oPanelMapListeners.add(o);
		}
	}

	public void removePanelMapListener(PanelMapListener o){
		synchronized(_oPanelMapListeners){
			_oPanelMapListeners.remove(o);
		}
	}

	public void fireMapChanged(){
		PanelMapEvent evt = new PanelMapEvent(this);
		synchronized(_oPanelMapListeners){
			Iterator<PanelMapListener> oIterator = _oPanelMapListeners.iterator();
			while(oIterator.hasNext()){
				PanelMapListener o = oIterator.next();
				o.mapChanged(evt);
			}
		}
	}


	////////////////////////////////
	//		PROPERTIES
	////////////////////////////////

	/**
	 * Sets the map that will be used by this PanelMap
	 *
	 * @param oMap the map
	 */
	public void setMap(GameMap oMap){
		_oMap = oMap;
		updateAutotiles(_oMap.getMapLayer(0));
	}

	/**
	 * Returns the map used by this PanelMap
	 *
	 * @return the map used by this PanelMap
	 */
	public GameMap getMap(){
		return _oMap;
	}

	/**
	 * Sets the tileset, and loads the images
	 *
	 * @param o the tileset
	 */
	public void setTileset(Tileset o){
		_oTileset = o;
		try{
			_oImageTileset = ImageIO.read(new File(Paths.PATH_GRAPHICS_TILESETS + "/" + _oTileset.getFilename()));
			for(int i = 0; i < _oTileset.autotiles().length; i++){
				if(_oTileset.autotiles()[i] == null || _oTileset.autotiles()[i].length() < 1)
					continue;
				_oImageAutotiles[i] = ImageIO.read(new File(Paths.PATH_GRAPHICS_AUTOTILES + "/" + _oTileset.autotiles()[i]));
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Returns the tileset
	 *
	 * @return the tileset
	 */
	public Tileset getTileset(){
		return _oTileset;
	}

	/**
	 * Sets the selected layer
	 *
	 * @param i index of the layer
	 */
	public void setSelectedLayer(int i){
		_iSelectedLayer = i;
	}

	/**
	 * Returns the selected layer
	 *
	 * @return the selected layer
	 */
	public int getSelectedLayer(){
		return _iSelectedLayer;
	}

	/**
	 * Equivalent to <code>setPaintingTiles(new int[][]{{i}})</code>
	 *
	 * @param i the painting tile
	 */
	public void setPaintingTile(int i){
		setPaintingTiles(new int[][]{{i}});
	}

	/**
	 * Set the tiles that will be used when in drawing mode
	 *
	 * @param iSelectedTiles
	 */
	public void setPaintingTiles(int[][] iSelectedTiles){
		_iSelectedTiles = iSelectedTiles;
	}

	/**
	 * Set the DimOtherLayers option
	 *
	 * @param value if true, layers other than the selected one will be drawn with 60% opacity
	 */
	public void setDimOtherLayers(boolean value){
		_bOptionsDimOtherLayers = value;
	}

	/**
	 * Returns the DimOtherLayers flag
	 *
	 * @return true if DimOtherLayers is activated, false otherwize
	 */
	public boolean getDimOtherLayers(){
		return _bOptionsDimOtherLayers;
	}

	/**
	 * Set the operating mode
	 *
	 * @param e the operating mode
	 */
	public void setMode(Mode e){
		_eMode = e;
	}

	/**
	 * Returns the operating mode
	 *
	 * @return the operating mode
	 */
	public Mode getMode(){
		return _eMode;
	}

	/**
	 * Set the drawing mode
	 *
	 * @param e the drawing mode
	 */
	public void setDrawMode(DrawMode e){
		_eDrawMode = e;
	}

	/**
	 * Returns the current drawing mode
	 *
	 * @return the current drawing mode
	 */
	public DrawMode getDrawMode(){
		return _eDrawMode;
	}

	/**
	 * Returns the x location of the cursor, measured in tiles
	 *
	 * @return the x location of the cursor, measured in tiles
	 */
	public int getCursorX(){
		return _iCursorX;
	}

	/**
	 * Returns the y location of the cursor, measured in tiles
	 *
	 * @return the y location of the cursor, measured in tiles
	 */
	public int getCursorY(){
		return _iCursorY;
	}

	/**
	 * Returns the temporary rectangle used while drawing in rectangle mode
	 *
	 * @return the temporary rectangle used while drawing in rectangle mode
	 */
	public Rectangle getDrawRectangle(){
		return _oRectangleDraw;
	}

	/**
	 * Sets the zoom value that will be used when painting the component.
	 *
	 * @param i the desired zoom value, must be  <code>&gt; 0</code>
	 */
	public void setZoom(float i){
		if(i <= 0)
			throw new IllegalArgumentException("i <= 0");
		_iZoom = i;
	}

	/**
	 * Returns the zoom value used by this component.
	 *
	 * @return the zoom value used by this component.
	 */
	public float getZoom(){
		return _iZoom;
	}

	/**
	 * Sets whether a grid will be displayed or not, and calls rapint();
	 *
	 * @param value true to display the grid, false otherwize
	 */
	public void setDrawGrid(boolean value){
		_bDrawGrid = value;
		repaint();
	}

	/**
	 * Gets the DrawGrid value
	 *
	 * @return the DrawGrid value
	 */
	public boolean getDrawGrid(){
		return _bDrawGrid;
	}

	/**
	 * Returns the position of the selected tile of the map, measured in tiles
	 *
	 * @return the position of the selected tile of the map, measured in tiles
	 */
	public int getSelectedTileX(){
		return _iSelectedTileX;
	}

	/**
	 * Returns the position of the selected tile of the map, measured in tiles
	 *
	 * @return the position of the selected tile of the map, measured in tiles
	 */
	public int getSelectedTileY(){
		return _iSelectedTileY;
	}

	public void addObject(Point o){
		_oObjects.add(o);
	}

	////////////////////////////////
	//		PAINTING
	////////////////////////////////

	@Override
	public void paintComponent(Graphics oGraphics){
		super.paintComponent(oGraphics);
		Graphics2D g = (Graphics2D)oGraphics;

		if(_oMap != null && _oImageTileset != null && _oTileset != null){
			for(int i = 0; i < _oMap.getLayerCount(); i++){
				if(i == _iSelectedLayer && !_bScrolling && _iTemporalLayer != null){
					paintLayer(g, _iTemporalLayer);
				}else{
					if(_bOptionsDimOtherLayers && _iSelectedLayer != i)
						paintLayer(g, _oMap.getMapLayer(i), .6f);
					else
						paintLayer(g, _oMap.getMapLayer(i));
				}
				if(!_bScrolling && i == _iSelectedLayer && _iSelectedTiles != null && (_bMouseDragging || _bMouseInside)){
					if(i != 0)
						g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					if(_eMode == Mode.Draw && _eDrawMode == DrawMode.Pencil && !_bMouseDragging){
						paintSelectedTilesPencil(g);
					}else if(_eMode == Mode.Draw && _eDrawMode == DrawMode.Rectangle && !_bCanceledPainting && _bMouseDragging){
						paintSelectedTilesRectangle(g);
					}
					if(i != 0)
						g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				}
			}

			if(!_oObjects.isEmpty()){
				for(Point o : _oObjects){
					g.setColor(Color.white);
					g.drawRect(o.x * 32 + 3 - _iCameraX, o.y * 32 + 3 - _iCameraY, 26, 26);
					g.setColor(new Color(1, 1, 1, .5f));
					g.fillRect(o.x * 32 + 3 - _iCameraX, o.y * 32 + 3 - _iCameraY, 26, 26);
				}
			}

			if(_bDrawGrid){
				paintGrid(g);
			}
			if(!_bScrolling || _eMode == Mode.Objects)
				drawSelectionRectangle(g);

		}

		if(_bScrolling)
			g.drawImage(Cursors.ScrollButtonAll, _iScrollingX, _iScrollingY, this);
		
		super.paintBorder(oGraphics);

	}

	/**
	 * Paints the passed layer on <code>g</code>, using the passed opacity
	 *
	 * @param g the graphics context on which to paint the layer
	 * @param iLayer layer data to paint
	 * @param iAlpha opacity to use, ranging from 0 (fully invisible) to 1 (fully visible)
	 */
	public void paintLayer(Graphics2D g, int[][] iLayer, float iAlpha){
		Composite oOldComposite = g.getComposite();
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, iAlpha));

		paintLayer(g, iLayer);

//		if(_bOptionsDimOtherLayers){
//			if(i == _iSelectedLayer){
//				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
//			}else{
//				if(i > 0){
//					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
//				}else if(i == 0){
//					g.setColor(new Color(0, 0, 0));
//					g.fillRect(0, 0, getWidth(), getHeight());
//					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
//				}
//			}
//
//		}else{
//
//		}
//		if(_bOptionsDimOtherLayers){
			g.setComposite(oOldComposite);
//		}
	}

	/**
	 * Paints the passed layer on the passed graphics context
	 *
	 * @param g
	 * @param iLayer
	 */
	public void paintLayer(Graphics2D g, int[][] iLayer){
		int iTile = 0;
		int iTileX = 0, iTileY = 0;
		int iStartX = _iCameraX / 32;
		int iStartY = _iCameraY / 32;
		int iEndX = Math.min(_oMap.getWidth(), getWidth() / 32 + 2);
		int iEndY = Math.min(_oMap.getHeight(), getHeight() / 32 + 2);
		int iTileIndex;
		boolean bIsAutotile;

		for(int x = 0; x < iEndX; x++){
			for(int y = 0; y < iEndY; y++){
				if(x + iStartX > _oMap.getWidth() - 1 || y + iStartY > _oMap.getHeight() - 1)
					continue;
				if(!_oMap.isInsideMap(x + iStartX, y + iStartY))
					continue;

				iTile = iLayer[x + iStartX][y + iStartY];
				iTileIndex = iTile & 0xFFFF;
				bIsAutotile = ((iTile & RavenShrineConstants.RS_AUTOTILE_BIT) != 0);

				if(iTileIndex == GameMap.RS_NULL_TILE)
					continue;

				if(!bIsAutotile){
					iTileX = iTileIndex % (_oImageTileset.getWidth(null) / 32);
					iTileY = iTileIndex / (_oImageTileset.getWidth(null) / 32);
					g.drawImage(_oImageTileset, x * 32 - _iCameraX % 32, y * 32 - _iCameraY % 32, x * 32 + (int)Math.floor(RavenShrineConstants.RS_TILE_SIZE * _iZoom) - _iCameraX % 32, y * 32 + 32 - _iCameraY % 32, iTileX * 32, iTileY * 32, iTileX * 32 + 32, iTileY * 32 + 32, null);
				}else{
					iTileIndex = iTile & 0x7FFF;
					if(_oImageAutotiles[iTileIndex] == null){
						continue;
					}
					int iAutotile[] = new int[4];
					int iAutotileX, iAutotileY;
					for(int i = 0; i < 4; i++){
						iAutotile[i] = (iTile >>> (16 + (4 * i))) & 0xF;

						if(iAutotile[i] < 9){
							iAutotileX = (iAutotile[i] % 3) * 32;
							iAutotileY = 32 + ((int) Math.floor(iAutotile[i] / 3)) * 32;
						}else if(iAutotile[i] == 9){
							iAutotileX = 64;
							iAutotileY = 0;
						}else{
							iAutotileX = 0;
							iAutotileY = 0;
						}

						if(i == 1 || i == 3)
							iAutotileX += 16;
						if(i == 2 || i == 3)
							iAutotileY += 16;

						if(_oImageAutotiles[iTileIndex].getWidth() > 32 * 3)
						iAutotileX += _iAnimation * 32 * 3;

						int dx1, dx2, dy1, dy2;
						dx1 = x * 32 - _iCameraX % 32 + (i % 2) * 16;
						dy1 = y * 32 - _iCameraY % 32 + (int)Math.floor(i / 2) * 16;
						dx2 = dx1 + 16;
						dy2 = dy1 + 16;
						g.drawImage(_oImageAutotiles[iTileIndex], dx1, dy1, dx2, dy2, iAutotileX, iAutotileY, iAutotileX + 16, iAutotileY + 16, null);

					}
				}
			}
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

	public void drawSelectionRectangle(Graphics2D g){
		if(_eMode != Mode.Objects)
			drawSelectionRectangle(g, _iCursorX * 32 - _iCameraX % 32, _iCursorY * 32 - _iCameraY % 32, 31, 31);
		else
			drawSelectionRectangle(g, _iSelectedTileX * 32 - _iCameraX, _iSelectedTileY * 32 - _iCameraY, 31, 31);
	}

	/**
	 * Copies the selected tiles into the temporal layer, at the position of the cursor.
	 */
	public void putSelectedTilesPencil(){
		if(_iSelectedTiles == null)
			return;
		int iSelectionW = 0;
		int iSelectionH = 0;
		iSelectionW = _iSelectedTiles.length;
		if(iSelectionW < 1)
			return;
		iSelectionH = _iSelectedTiles[0].length;
		if(iSelectionH < 1)
			return;
		int iTileDestX;
		int iTileDestY;
		
		for(int x = 0; x < iSelectionW; x++){
			for(int y = 0; y < iSelectionH; y++){
				iTileDestX = x + _iCursorX + _iCameraX / 32;
				iTileDestY = y + _iCursorY + _iCameraY / 32;
				if(iTileDestX < 0 || iTileDestY < 0 || iTileDestX > _oMap.getWidth() - 1 || iTileDestY > _oMap.getHeight() - 1)
					continue;
				_iTemporalLayer[iTileDestX][iTileDestY] = (short)_iSelectedTiles[x][y];
			}
		}

	}

	/**
	 * Copies the selected tiles into the temporal layer, at the position of the cursor,
	 * and covering the destination rectangle.
	 */
	public void putSelectedTilesRectangle(){
		if(_iSelectedTiles == null)
			return;
		int iTile;
		Rectangle oNewRect = new Rectangle(
				Math.min(_oRectangleDraw.x, _oRectangleDraw.width), Math.min(_oRectangleDraw.y, _oRectangleDraw.height),
				Math.abs(_oRectangleDraw.x - _oRectangleDraw.width) + 1, Math.abs(_oRectangleDraw.y - _oRectangleDraw.height) + 1
				);
		
		int iTileDestX;
		int iTileDestY;
		
		for(int x = 0; x < oNewRect.width; x++){
			for(int y = 0; y < oNewRect.height; y++){
				iTileDestX = x + oNewRect.x  + _iCameraX / 32;
				iTileDestY = y + oNewRect.y + _iCameraY / 32;
				if(iTileDestX < 0 || iTileDestY < 0 || iTileDestX > _oMap.getWidth() - 1 || iTileDestY > _oMap.getHeight() - 1)
					continue;
				iTile = _iSelectedTiles[x % _iSelectedTiles.length][y % _iSelectedTiles[0].length];
				_oMap.setMapData(iTileDestX, iTileDestY, _iSelectedLayer, iTile);
			}
		}
		updateAutotiles(_oMap.getMapData()[0]);
		repaint();
	}

	/**
	 * Paints the selected tiles in the destination Graphics2D object.
	 *
	 * @param g a Graphics2D object to paint the tiles on
	 */
	public void paintSelectedTilesPencil(Graphics2D g){
		if(_iSelectedTiles == null)
			return;

		int iSelectionW = 0;
		int iSelectionH = 0;
		int iTile = 0, iTileX = 0, iTileY = 0;
		iSelectionW = _iSelectedTiles.length;
		if(iSelectionW < 1)
			return;
		iSelectionH = _iSelectedTiles[0].length;
		if(iSelectionH < 1)
			return;
		for(int x = 0; x < iSelectionW; x++){
			for(int y = 0; y < iSelectionH; y++){
				iTile = _iSelectedTiles[x][y];
				if(iTile == RavenShrineConstants.RS_NULL_TILE)
					continue;
				if((iTile & RavenShrineConstants.RS_AUTOTILE_BIT) != 0){
					iTile = (short) (iTile ^ RavenShrineConstants.RS_AUTOTILE_BIT);
					g.drawImage(_oImageAutotiles[iTile], _iCursorX * 32 + x * 32 - _iCameraX % 32, _iCursorY * 32 + y * 32 - _iCameraY % 32, _iCursorX  * 32 + x * 32 + 32 - _iCameraX % 32, _iCursorY  * 32 + y * 32 + 32 - _iCameraY % 32, 0, 0, 32, 32, null);
				}else{
					iTileX = iTile % (_oImageTileset.getWidth(null) / 32);
					iTileY = iTile / (_oImageTileset.getWidth(null) / 32);
					g.drawImage(_oImageTileset, _iCursorX * 32 + x * 32 - _iCameraX % 32, _iCursorY * 32 + y * 32 - _iCameraY % 32, _iCursorX  * 32 + x * 32 + 32 - _iCameraX % 32, _iCursorY  * 32 + y * 32 + 32 - _iCameraY % 32, iTileX * 32, iTileY * 32, iTileX * 32 + 32, iTileY * 32 + 32, null);
				}
				
			}
		}
	}

	/**
	 * Paints the selected tiles in the destination Graphics2D object,
	 * covering the destination rectangle.
	 *
	 * @param g a Graphics2D object to paint the tiles on
	 */
	public void paintSelectedTilesRectangle(Graphics2D g){
		int iTile, iTileX, iTileY;
		Rectangle oNewRect = new Rectangle(
				Math.min(_oRectangleDraw.x, _oRectangleDraw.width), Math.min(_oRectangleDraw.y, _oRectangleDraw.height),
				Math.abs(_oRectangleDraw.x - _oRectangleDraw.width) + 1, Math.abs(_oRectangleDraw.y - _oRectangleDraw.height) + 1
				);
		
		
		for(int x = 0; x < oNewRect.width; x++){
			for(int y = 0; y < oNewRect.height; y++){
				iTile = _iSelectedTiles[x % _iSelectedTiles.length][y % _iSelectedTiles[0].length];
				if(iTile == -1){
					g.setColor(new Color(255, 0, 0, 128));
					g.fillRect(oNewRect.x * 32 + x * 32 - _iCameraX % 32, oNewRect.y * 32 + y * 32 - _iCameraY % 32, 32, 32);
				}else{
					if((iTile & RavenShrineConstants.RS_AUTOTILE_BIT) != 0){
						iTile = (short) (iTile ^ RavenShrineConstants.RS_AUTOTILE_BIT);
						g.drawImage(_oImageAutotiles[iTile], oNewRect.x * 32 + x * 32 - _iCameraX % 32, oNewRect.y * 32 + y * 32 - _iCameraY % 32, oNewRect.x * 32 + x * 32 + 32 - _iCameraX % 32, oNewRect.y  * 32 + y * 32 + 32 - _iCameraY % 32, 0, 0, 32, 32, null);
					}else{
						iTileX = iTile % (_oImageTileset.getWidth(null) / 32);
						iTileY = iTile / (_oImageTileset.getWidth(null) / 32);
						g.drawImage(_oImageTileset, oNewRect.x * 32 + x * 32 - _iCameraX % 32, oNewRect.y * 32 + y * 32 - _iCameraY % 32, oNewRect.x * 32 + x * 32 + 32 - _iCameraX % 32, oNewRect.y  * 32 + y * 32 + 32 - _iCameraY % 32, iTileX * 32, iTileY * 32, iTileX * 32 + 32, iTileY * 32 + 32, null);
					}
					
				}
				
			}
		}
	}

	/**
	 * Creates a new temporal layer, copying data from the currently
	 * selected layer.
	 */
	private void createTemporalLayer(){
		_iTemporalLayer = new int[_oMap.getWidth()][_oMap.getHeight()];
		for(int x = 0; x < _oMap.getWidth(); x++){
			for(int y = 0; y <  _oMap.getHeight(); y++){
				_iTemporalLayer[x][y] = _oMap.getMapData(x, y, _iSelectedLayer);
			}
		}
	}

	/**
	 * Replaces the selected layer in the map with the temporal layer
	 */
	private void saveTemporalLayer(){
		updateAutotiles(_iTemporalLayer);
		_oMap.getMapData()[_iSelectedLayer] = _iTemporalLayer;
		_iTemporalLayer = null;
	}

	/**
	 * Paints a grid on the Graphics object
	 * @param g the Graphics2D objects to draw on
	 */
	private void paintGrid(Graphics2D g){
		int iStartX = _iCameraX / 32;
		int iStartY = _iCameraY / 32;
		int iEndX = Math.min(_oMap.getWidth(), getWidth() / 32 + 2);
		int iEndY = Math.min(_oMap.getHeight(), getHeight() / 32 + 2);
		Color oldColor = g.getColor();
		Stroke oldStroke = g.getStroke();
		g.setColor(new Color(0, 0, 0, .1f));
		g.setStroke(new BasicStroke(2));
		for(int x = 0; x < iEndX; x++){
			for(int y = 0; y < iEndY; y++){
				g.drawRect(x * 32 - _iCameraX % 32, y * 32 - _iCameraY % 32, 32, 32);
			}
		}
		g.setColor(oldColor);
		g.setStroke(oldStroke);
	}
	

	////////////////////////////////
	//		EVENTS RECEIVE
	////////////////////////////////

	private void formMouseDragged(MouseEvent e) {
		if(_eMode == Mode.Objects)
			return;
		
		_bMouseDragging = true;

		int _iOldCursorX = _iCursorX;
		int _iOldCursorY = _iCursorY;

		_iCursorX = (e.getX() + _iCameraX % 32) / 32 ;
		_iCursorY = (e.getY() + _iCameraY % 32) / 32 ;

		
		if(_iCursorX == _iOldCursorX && _iCursorY == _iOldCursorY)
			return;

		if(!_bScrolling && !_bCanceledPainting && _eMode == Mode.Draw){
			switch(_eDrawMode){
				case Pencil:
					putSelectedTilesPencil();
					updateAutotiles(_iTemporalLayer);
					repaint();
					break;
				case Rectangle:
					_oRectangleDraw.width = _iCursorX;
					_oRectangleDraw.height = _iCursorY;
					repaint();
					break;
			}
		}
	}

	private void formMouseMoved(MouseEvent e) {
		_iMouseX = e.getX();
		_iMouseY = e.getY();

		if(_bScrolling){
			int x, y;

			if(e.getX() <  _iScrollingX - 16){
				x = 0;
			}else if(e.getX() >  _iScrollingX + 32){
				x = 2;
			}else{
				x = 1;
			}
			if(e.getY() <  _iScrollingY - 16){
				y = 0;
			}else if(e.getY() >  _iScrollingY + 32){
				y = 2;
			}else{
				y = 1;
			}
			
			setCursor(Cursors.Scroll[x][y]);
			_bScrollingMoveX = x != 1;
			_bScrollingMoveY = y != 1;

			return;
		}

		int _iOldCursorX = _iCursorX;
		int _iOldCursorY = _iCursorY;

		if(_eMode == Mode.Draw){
			_iCursorX = (e.getX() + _iCameraX % 32) / 32 ;
			_iCursorY = (e.getY() + _iCameraY % 32) / 32 ;
			if(_iCursorX == _iOldCursorX && _iCursorY == _iOldCursorY)
				return;

		}

		if(_eMode == Mode.Draw && _eDrawMode == DrawMode.Rectangle){
			_oRectangleDraw.x = _iCursorX;
			_oRectangleDraw.y = _iCursorY;
			_oRectangleDraw.width = _iCursorX;
			_oRectangleDraw.height = _iCursorY;
		}
		repaint();
	}

	private void formMousePressed(MouseEvent e){
		if(e.getButton() != 2){
			_iCursorX = (e.getX() + _iCameraX % 32) / 32 ;
			_iCursorY = (e.getY() + _iCameraY % 32) / 32 ;
		}
		
		if(_bScrolling){
			_bScrolling = false;
			_bIgnoreOneMouseRelease = true;
			_bWaitScrollerThread = true;
			setCursor(null);
		}else if(e.getButton() == 2){
			if(!_bScrolling){
				_bScrolling = true;
				_bScrollingMoveX = false;
				_bScrollingMoveY = false;
				synchronized(_oScrollerThread){
					_oScrollerThread.notify();
				}
				_iScrollingX = e.getX() - 16;
				_iScrollingY = e.getY() - 16;
				setCursor(Cursors.ScrollAll);
			}
		}else if(_eMode == Mode.Draw && _eDrawMode == DrawMode.Pencil){
			if(e.getButton() == 1){
				_bCanceledPainting = false;
				createTemporalLayer();
				putSelectedTilesPencil();
			}else if(e.getButton() == 3){
				_bCanceledPainting = true;
				_iTemporalLayer = null;
			}
		}else if(_eMode == Mode.Draw && _eDrawMode == DrawMode.Rectangle){
			if(e.getButton() == 1){
				_oRectangleDraw.x = _iCursorX;
				_oRectangleDraw.y = _iCursorY;
				_bCanceledPainting = false;
			}else if(e.getButton() == 3){
				_bCanceledPainting = true;
			}
		}else if(_eMode == Mode.Draw && _eDrawMode == DrawMode.FloodFill){
			if(e.getButton() == 1){
				floodFill(_iCursorX + _iCameraX / 32, _iCursorY + _iCameraY / 32);
				fireMapChanged();
			}
		}else if(_eMode == Mode.Objects){
			if(e.getButton() == 1){
				_iSelectedTileX = (e.getX() + _iCameraX) / 32 ;
				_iSelectedTileY = (e.getY() + _iCameraY) / 32 ;
			}
		}
		repaint();

	}

	private void formMouseReleased(MouseEvent e) {
		_bMouseDragging = false;
		if(_bIgnoreOneMouseRelease){
			_bIgnoreOneMouseRelease = false;
			return;
		}
		if(_eMode == Mode.Draw && _eDrawMode == DrawMode.Pencil){
			if(e.getButton() == 1){
				if(!_bCanceledPainting){
					saveTemporalLayer();
					fireMapChanged();
				}
			}else if(e.getButton() == 3){
				_iTemporalLayer = null;
			}
		}else if(_eMode == Mode.Draw && _eDrawMode == DrawMode.Rectangle){
			if(e.getButton() == 1){
				if(!_bCanceledPainting){
					putSelectedTilesRectangle();
					fireMapChanged();
				}
			}else if(e.getButton() == 3){
				
			}
		}
		repaint();
	}

	private void formMouseEntered(MouseEvent e) {
		_bMouseInside = true;
	}

	private void formMouseExited(MouseEvent e) {
		if(_eMode != Mode.Objects){
			_iCursorX = -1;
			_iCursorY = -1;
			_bMouseInside = false;
			repaint();
		}
	}


	////////////////////////////////
	//		ETC
	////////////////////////////////

	/**
	 * Updates autotile data for the passed layer.
	 * @param oLayer a layer of tiles
	 */
	public static void updateAutotiles(int[][] oLayer){
		if(oLayer.length < 1 || oLayer[0].length < 1)
			return;

		int w = oLayer.length, h = oLayer[0].length;
		int iTile;
		boolean[][] bSameTile = new boolean[3][3];
		int iAutotile;
		int iJoker = 0xFFFF & (RavenShrineConstants.RS_AUTOTILE_BIT | 1); // TODO: autotiles-siblings & manual autotiles
		
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				iTile = oLayer[x][y];
				if((iTile & RavenShrineConstants.RS_AUTOTILE_BIT) == 0)
					continue;

				for(int x2 = 0; x2 < 3; x2++)
				for(int y2 = 0; y2 < 3; y2++){
					bSameTile[x2][y2] = ((x + x2 - 1) < 0) || ((x + x2 - 1) > w - 1) || ((y + y2 - 1) < 0) || ((y + y2 - 1) > h - 1) || ((oLayer[x + x2 - 1][y + y2 - 1] & 0xFFFF) == (iTile & 0xFFFF)) || ((oLayer[x + x2 - 1][y + y2 - 1] & 0xFFFF) == iJoker) ;
					
				}
				iTile &= 0xFFFF;

				for (int i = 0; i < 4; i++) {
					iAutotile = 0;

					if(bSameTile[1][0]){
						iAutotile += 3;
						if(!bSameTile[1][2])
							iAutotile += 3;
					}else{
						if(!bSameTile[1][2] && (i == 2 || i == 3))
							iAutotile += 6;
					}
					
					if(bSameTile[0][1]){
						iAutotile += 1;
						if(!bSameTile[2][1])
							iAutotile += 1;
					}else{
						if(!bSameTile[2][1] && (i == 1 || i == 3))
							iAutotile += 2;
					}

					if(i == 0 && !bSameTile[0][0] && bSameTile[1][0] && bSameTile[0][1])
						iAutotile = 9;
					else if(i == 1 && !bSameTile[2][0] && bSameTile[1][0] && bSameTile[2][1])
						iAutotile = 9;
					else if(i == 2 && !bSameTile[0][2] && bSameTile[0][1] && bSameTile[1][2])
						iAutotile = 9;
					else if(i == 3 && !bSameTile[2][2] && bSameTile[2][1] && bSameTile[1][2])
						iAutotile = 9;

					iTile |= (iAutotile << (16 + 4 * i));
					
				}
				oLayer[x][y] = iTile;
				
			}
		}
		
	}

	/**
	 * "Paints" the tile at (x, y) and all adyacent equal ones with
	 * the selected tiles, and the adyacent of the adyacent, and so forth.
	 * Returns immediately, without doing any painting, if the tile at (x, y)
	 * equals the first selected tile (that is, the top-left tile of the rectangle
	 * of selected tiles).
	 * <p>
	 * Uses the stack-and-scanline algorithm.
	 * <p>
	 * Updates the autotiles of the modified layer and calls repaint() when done
	 * 
	 * @param x x-coordinate of the point where to start the floodfill at
	 * @param y y-coordinate of the point where to start the floodfill at
	 */
	public void floodFill(int x, int y){
		Stack<Point> oStack = new Stack<Point>();
		int y1;
		boolean spanLeft, spanRight;
		int[][] layer = _oMap.getMapLayer(_iSelectedLayer);
		int oldColor = layer[x][y] & 0xFFFF;

		if((layer[x][y] & 0xFFFF) == (_iSelectedTiles[0][0] & 0xFFFF))
			return;

		oStack.push(new Point(x, y));

		while(!oStack.isEmpty()){
			Point oPoint = oStack.pop();
			x = oPoint.x;
			y = oPoint.y;
			
			y1 = y;
			while(y1 >= 0 && (layer[x][y1] & 0xFFFF) == oldColor)
				y1--;
			y1++;
			spanLeft = spanRight = false;
			while(y1 < _oMap.getHeight() && (layer[x][y1] & 0xFFFF) == oldColor){
				layer[x][y1] = _iSelectedTiles[0][0];

				if(!spanLeft && x > 0 && (layer[x - 1][y1] & 0xFFFF) == oldColor){
					oStack.push(new Point(x - 1, y1));
					spanLeft = true;
				}else if(spanLeft && x > 0 && (layer[x - 1][y1] & 0xFFFF) != oldColor){
					spanLeft = false;
				}

				if(!spanRight && x < _oMap.getWidth() - 1 && (layer[x + 1][y1] & 0xFFFF) == oldColor){
					oStack.push(new Point(x + 1, y1));
					spanRight = true;
				}else if(spanRight && x < _oMap.getWidth() - 1 && (layer[x + 1][y1] & 0xFFFF) != oldColor){
					spanRight = false;
				}
				
				y1++;
			}
		}

		updateAutotiles(layer);
		repaint();

	}



}
