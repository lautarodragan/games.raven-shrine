package rs.client.gears;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import rs.RavenShrineConstants;
import rs.resources.Tileset;

/**
 * Wraps around a Tileset, adding OpenGL functionality
 * @author lainmaster
 */
public class GLTileset implements RavenShrineConstants{
	private Texture _oTilesetTexture;
	private Texture[] _oAutotilesTextures;
	private Tileset _oTileset;

	private int _iTilesPerRow;

	private float _iAutotileTime = 0;
	private int _iAutotileFrame = 0;

	public int _iLastTexture = -1;
	private float _iDepth;

	public GLTileset(Tileset o){
		setTileset(o);
	}

	///////////////////////////////////////////
	//          PROPERTIES
	///////////////////////////////////////////

	public void setTileset(Tileset o){
		_oTileset = o;
	}
	
	public Tileset getTileset(){
		return _oTileset;
	}

	public void setTilesetTexture(Texture oTexture){
		_oTilesetTexture = oTexture;
		_iTilesPerRow = _oTilesetTexture.getWidth() / _oTileset.getTileWidth();
	}

	public Texture getTilesetTexture(){
		return _oTilesetTexture;
	}

	/**
	 * Sets the value that will be passed as the z coordinate to the drawing methods
	 *
	 * @param z
	 */
	public void setDrawingDepth(float z){
		_iDepth = z;
	}

	///////////////////////////////////////////
	//          METHODS
	///////////////////////////////////////////

	/**
	 * Loads tileset and autotiles textures reading the paths from the Tileset object
	 */
	public void loadTextures(String sTilesetPath, String sAutotilesPath){
		if(_oTileset == null)
			throw new RuntimeException("no tileset set");
		setTilesetTexture(new Texture(sTilesetPath + _oTileset.getFilename()));

		_oAutotilesTextures = new Texture[_oTileset.autotiles().length];
		for(int i = 0; i < _oTileset.autotiles().length; i++){
			String sPath = sAutotilesPath + _oTileset.autotiles()[i];
			File oFile = new File(sPath);
			if(!oFile.exists() || !oFile.isFile())
				continue;
			System.out.println("loaded " + i + " " + sPath);
			_oAutotilesTextures[i] = new Texture(sPath);
		}

	}

	/**
	 * Renders a tile on screen. This method checks whether the tile is an autotile,
	 * and paints accordingly. It also handles opening and closing of glBegin/glEnd blocks,
	 * keeping track of the last used texture by this method.
	 * 
	 * @param x destination point on screen
	 * @param y destination point on screen
	 * @param iTile tile index with all data (autotile bit, autotile data, etc)
	 */
	public void paintTile(float x, float y, int iTile){
		if(iTile == RS_NULL_TILE){
			if(_iLastTexture != -1){
				_iLastTexture = -1;
				GL11.glEnd();
			}

			Color.black.bind();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x + 32, y);
			GL11.glVertex2f(x + 32, y + 32);
			GL11.glVertex2f(x, y + 32);
			GL11.glEnd();
			Color.white.bind();

		}else if((iTile & RS_AUTOTILE_BIT) == 0){
			if(_iLastTexture == -1){
				_oTilesetTexture.bind();
				GL11.glBegin(GL11.GL_QUADS);
			}else if(_iLastTexture != _oTilesetTexture.getId()){
				GL11.glEnd();
				_oTilesetTexture.bind();
				GL11.glBegin(GL11.GL_QUADS);
			}
			_oTilesetTexture.setZ(_iDepth);
			paintTilesetTile(x, y, iTile % _iTilesPerRow,  (float)Math.floor(iTile / _iTilesPerRow));
			_iLastTexture = _oTilesetTexture.getId();
		}else{
			if(Timer.getSystemTime() - _iAutotileTime > 300){
				if(_iAutotileFrame < 3)
					_iAutotileFrame++;
				else
					_iAutotileFrame = 0;
				_iAutotileTime = Timer.getSystemTime();
			}
			int iTileIndex = iTile & 0x7FFF;
			if(_iLastTexture == -1){
				_oAutotilesTextures[iTileIndex].bind();
				GL11.glBegin(GL11.GL_QUADS);
			}else if(_iLastTexture != _oAutotilesTextures[iTileIndex].getId()){
				GL11.glEnd();
				_oAutotilesTextures[iTileIndex].bind();
				GL11.glBegin(GL11.GL_QUADS);
			}
			_oAutotilesTextures[iTileIndex].setZ(_iDepth);
			paintAutotile(x, y, iTile, _iAutotileFrame);
			_iLastTexture = _oAutotilesTextures[iTileIndex].getId();
		}
	}

	/**
	 * Renders a tile from the non-auto tileset on screen.
	 * 
	 * @param x destination point to draw at
	 * @param y destination point to draw at
	 * @param iTileSourceX source point to start drawing from, measured in tiles
	 * @param iTileSourceY source point to start drawing from, measured in tiles
	 */
	public void paintTilesetTile(float x, float y, float iTileSourceX, float iTileSourceY){
		_oTilesetTexture.drawEmbedded(x, y, _oTileset.getTileWidth(), _oTileset.getTileHeight(), iTileSourceX * _oTileset.getTileWidth(), iTileSourceY * _oTileset.getTileHeight(), _oTileset.getTileWidth(), _oTileset.getTileHeight());
	}

	/**
	 * Renders an autotile on screen.
	 *
	 * @param x destination point to draw at
	 * @param y destination point to draw at
	 * @param iTile index of the tile with all tile data
	 * @param iFrame frame of the animation to be used, if the autotile is animated
	 */
	public void paintAutotile(float x, float y, int iTile, int iFrame){
		int iAutotile[] = new int[4];
		int iAutotileX, iAutotileY;
		int iTileIndex = iTile & 0x7FFF;

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

			if(_oAutotilesTextures[iTileIndex].getWidth() > iFrame * 32 * 3)
				iAutotileX += iFrame * 32 * 3;

			_oAutotilesTextures[iTileIndex].drawEmbedded(x + (i % 2) * 16, y + (int)Math.floor(i / 2) * 16, 16, 16, iAutotileX, iAutotileY, 16, 16);

		}
		
	}
}
