package rs.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Tileset implements rs.RavenShrineConstants{
	public static class Tile{
		/**
		 * Byte containing information about this tile's passability
		 */
		public byte Passage = RS_TILE_PASSABLE_BIT | RS_TILE_PASSABLE_N_BIT | RS_TILE_PASSABLE_S_BIT | RS_TILE_PASSABLE_W_BIT | RS_TILE_PASSABLE_E_BIT;
		/**
		 * Visual priority of this tile against other tiles.
		 * Tiles with higher priority should be seen over tiles with
		 * lower priority.
		 */
		public byte Priority;

		public boolean isPassable(){
			return (Passage & RS_TILE_PASSABLE_BIT) != 0;
		}

		public void setPassable(boolean value){
			if(value)
				Passage |= RS_TILE_PASSABLE_BIT;
			else
				Passage &= ~RS_TILE_PASSABLE_BIT;
		}

		public void setPassable(int dir, boolean value){
			int bit = getPassabilityBits(dir);
			
			if(value)
				Passage |= bit;
			else
				Passage &= ~bit;
		}

		public boolean isPassable(int dir){
			int bit = getPassabilityBits(dir);

			return (Passage & bit) != 0;
		}



	}

	public static int getPassabilityBits(int iDirection){
		int bit = 0;
		switch(iDirection){
			case RS_DIR_W:
				bit = RS_TILE_PASSABLE_W_BIT;
				break;
			case RS_DIR_N:
				bit = RS_TILE_PASSABLE_N_BIT;
				break;
			case RS_DIR_E:
				bit = RS_TILE_PASSABLE_E_BIT;
				break;
			case RS_DIR_S:
				bit = RS_TILE_PASSABLE_S_BIT;
				break;
			default:
				throw new IllegalArgumentException("Invalid enum");
		}
		return bit;
	}

	private final int CLASS_VERSION = 3;
	private int _iId;
	private String _sName = "";
	private String _sFilename = "";
	private Tile[] _oTiles;
	private int _iVersion;
	private int _iTileWidth = 32;
	private int _iTileHeight = 32;
	private final String[] _sAutoTiles = new String[8];
	private final Tile[] _oAutoTiles = new Tile[8];
	
	public Tileset(){
		for(int i = 0; i < _oAutoTiles.length; i++)
			_oAutoTiles[i] = new Tile();
	}
	
	public Tileset(String sName){
		this();
		setName(sName);
	}

	@Override
	public String toString(){
		return getName();
	}

	public int getId(){
		return _iId;
	}

	public void setId(int value){
		_iId = value;
	}

	public String getName(){
		return _sName;
	}

	public void setName(String sValue){
		_sName = sValue;
	}

	public String getFilename(){
		return _sFilename;
	}

	public void setFilename(String sValue){
		_sFilename = sValue;
	}

	public int getVersion() {
		return _iVersion;
	}

	public void setVersion(int i) {
		_iVersion = i;
	}

	public Tile[] getTiles(){
		return _oTiles;
	}

	public void setTiles(Tile[] o){
		_oTiles = o;
	}

	public Tile getTile(int iIndex){
		if((iIndex & RS_AUTOTILE_BIT) == 0)
			return _oTiles[iIndex];
		else{
			int i = iIndex & 0xFFFF & ~RS_AUTOTILE_BIT;
			if(i < 0 || i > 7)
				throw new RuntimeException("iIndex = autotile & (i < 0 || i > 7) (i == " + i + ")");
			return _oAutoTiles[i];
		}
	}

	public int getTileHeight() {
		return _iTileHeight;
	}

	public void setTileHeight(int i) {
		_iTileHeight = i;
	}

	public int getTileWidth() {
		return _iTileWidth;
	}

	public void setTileWidth(int i) {
		_iTileWidth = i;
	}

	public static Tileset read(String sPath){
		return read(new File(sPath));
	}

	public static Tileset read(File oFile){
		if(!oFile.exists())
			return null;

		ObjectInputStream oObjectInputStream;
		Tileset oTileset;
		try{
			oObjectInputStream = new ObjectInputStream(new FileInputStream(oFile));
			oTileset =  read(oObjectInputStream);
			oObjectInputStream.close();
			return oTileset;
		} catch(Exception e){
		}
		return null;
	}

	public static Tileset read(ObjectInputStream oObjectInputStream){
		Tileset oTileset = new Tileset();
		int iClassVersion;

		try{
			iClassVersion = oObjectInputStream.readInt();
			oTileset.setId(oObjectInputStream.readInt());
			oTileset.setName((String) oObjectInputStream.readObject());
			oTileset.setFilename((String) oObjectInputStream.readObject());
			oTileset.setVersion(oObjectInputStream.readInt());
			oTileset.setTileWidth(oObjectInputStream.readInt());
			oTileset.setTileHeight(oObjectInputStream.readInt());

			int iLength = oObjectInputStream.readInt();
			Tile[] oTiles = new Tile[iLength];
			for(int i = 0; i < iLength; i++){
				oTiles[i] = new Tile();
				oTiles[i].Passage = oObjectInputStream.readByte();
				oTiles[i].Priority = oObjectInputStream.readByte();
			}
			oTileset.setTiles(oTiles);

			if(iClassVersion >= 2)
			for(int i = 0; i < oTileset._sAutoTiles.length; i++){
				oTileset._sAutoTiles[i] = (String) oObjectInputStream.readObject();
			}

			if(iClassVersion >= 3)
			for(int i = 0; i < oTileset._sAutoTiles.length; i++){
				oTileset._oAutoTiles[i] = new Tile();
				oTileset._oAutoTiles[i].Passage = oObjectInputStream.readByte();
				oTileset._oAutoTiles[i].Priority = oObjectInputStream.readByte();
			}

		} catch(java.io.EOFException ex){
			return null;
		} catch(Exception ex){
			ex.printStackTrace();
			return null;
		}

		return oTileset;
	}

	public static List<Tileset> readTilesets(String sPath){
		return readTilesets(new File(sPath));
	}

	public static List<Tileset> readTilesets(File oFile){
//		if(!oFile.exists())
//			return null;

		FileInputStream oFileInputStream;
		ObjectInputStream oObjectInputStream;
		List<Tileset> oTileset;
		try{
			oFileInputStream = new FileInputStream(oFile);
			oObjectInputStream = new ObjectInputStream(oFileInputStream);
			oTileset = readTilesets(oObjectInputStream);
			oObjectInputStream.close();
			return oTileset;
		} catch(Exception e){
		}
		return null;
	}

	public static List<Tileset> readTilesets(ObjectInputStream oObjectInputStream){
		Tileset oTileset;
		List<Tileset> oTilesets = new ArrayList<Tileset>();
		
		while(true){
			oTileset = Tileset.read(oObjectInputStream);
			if(oTileset == null)
				break;
			oTilesets.add(oTileset);
		}
		
		return oTilesets;
	}

	public void writeTileset(String sPath){
		writeTileset(new File(sPath));
	}

	public void writeTileset(File oFile){
		ObjectOutputStream oObjectOutputStream;
		try{
			oObjectOutputStream = new ObjectOutputStream(new FileOutputStream(oFile));
			write(oObjectOutputStream);
			oObjectOutputStream.close();
		} catch(Exception e){
		}
	}

	public void write(ObjectOutputStream oObjectOutputStream){
		try{
			oObjectOutputStream.writeInt(CLASS_VERSION);
			oObjectOutputStream.writeInt(_iId);
			oObjectOutputStream.writeObject(_sName);
			oObjectOutputStream.writeObject(_sFilename);
			oObjectOutputStream.writeInt(_iVersion);
			oObjectOutputStream.writeInt(_iTileWidth);
			oObjectOutputStream.writeInt(_iTileHeight);
			if(_oTiles != null){
				oObjectOutputStream.writeInt(_oTiles.length);
				for(Tile o : _oTiles){
					oObjectOutputStream.writeByte(o.Passage);
					oObjectOutputStream.writeByte(o.Priority);
				}
			}else{
				oObjectOutputStream.writeInt(0);
			}
			for(int i = 0; i < _sAutoTiles.length; i++){
				oObjectOutputStream.writeObject(_sAutoTiles[i]);
			}
			for(int i = 0; i < _sAutoTiles.length; i++){
				if(_oAutoTiles[i] != null){
					oObjectOutputStream.writeByte(_oAutoTiles[i].Passage);
					oObjectOutputStream.writeByte(_oAutoTiles[i].Priority);
				}else{
					oObjectOutputStream.writeByte(0);
					oObjectOutputStream.writeByte(0);
				}
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void writeTilesets(String sPath, List<Tileset> oTilesets){
		writeTilesets(new File(sPath), oTilesets);
	}

	public static void writeTilesets(File oFile, List<Tileset> oTilesets){
		ObjectOutputStream oObjectOutputStream;
		try{
			oObjectOutputStream = new ObjectOutputStream(new FileOutputStream(oFile));
			writeTilesets(oObjectOutputStream, oTilesets);
			oObjectOutputStream.close();
		} catch(Exception e){
		}
	}

	public static void writeTilesets(ObjectOutputStream oObjectOutputStream, List<Tileset> oTilesets){
		for(Tileset o : oTilesets){
			o.write(oObjectOutputStream);
		}
	}

	public String[] autotiles(){
		return _sAutoTiles;
	}

}
