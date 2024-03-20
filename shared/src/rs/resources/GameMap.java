package rs.resources;

import rs.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import rs.RavenShrineConstants;
import rs.resources.Tileset.Tile;

public class GameMap extends GameMapReference implements RavenShrineConstants{
	private int _iWidth;
	private int _iHeight;
	private int _iLayerCount;
	private int[][][] _iMapData;
	private int[][][] _oSortedMap;

	private int _iPrivateVersion;
	private int _iPublicVersion;
	public static final int CLASS_VERSION = 4;
	private int _iMapNorth = -1;
	private int _iMapSouth = -1;
	private int _iMapWest = -1;
	private int _iMapEast = -1;

	private int _iTileset;

	public GameMap(){
		
	}

	public GameMap(int x, int y, int z){
		setSize(x, y, z);
	}

	public void setSize(int x, int y, int z){
		_iWidth = x;
		_iHeight = y;
		_iLayerCount = z;
		_iMapData = new int[z][x][y];
		setMapLayer(0, 1);
		for(int i = 1; i < z; i++)
			setMapLayer(i, RS_NULL_TILE);
	}

	public int getWidth(){
		return _iWidth;
	}

	public int getHeight(){
		return _iHeight;
	}

	public int getLayerCount(){
		return _iLayerCount;
	}

	public void setTileset(int iTilesetId){
		_iTileset = iTilesetId;
	}
	
	public int getTileset(){
		return _iTileset;
	}
	
	public void setMapData(int x, int y, int z, int value){
		_iMapData[z][x][y] = value;
	}

	public int getMapData(int x, int y, int z){
		//if(x == 0 || x == _iWidth - 1 || y == 0 || y == _iHeight - 1)
			//return 7;
		return _iMapData[z][x][y];
	}

	public void setMapData(int[][][] iData){
		_iMapData = iData;
	}

	public int[][][] getMapData(){
		return _iMapData;
	}

	public int[][] getMapLayer(int z){
		return _iMapData[z];
	}

	public void setMapLayer(int z, short value){
		for(int x = 0; x < _iWidth; x++){
			for(int y = 0; y < _iHeight; y++){
				_iMapData[z][x][y] = value;
			}
		}
	}

	public void setMapLayer(int z, int value){
		setMapLayer(z, (short)value);
	}

	@Override
	public String toString(){
		return "Map{id: " + getId() + "; name: " + getName() + " ; width: " + _iWidth + "; height: " + _iHeight + "; layerCount: " + _iLayerCount + "; tileset: " + _iTileset + ";}";
	}

	public void write(String sFile){
		try{
			ObjectOutputStream oFile = new ObjectOutputStream(new FileOutputStream(sFile));
			oFile.writeInt(CLASS_VERSION);
			oFile.writeInt(_iWidth);
			oFile.writeInt(_iHeight);
			oFile.writeInt(_iLayerCount);
			oFile.writeInt(_iTileset);
			oFile.writeObject(Util.compress(_iMapData));
			oFile.writeObject(this.getName());
			oFile.writeInt(_iPrivateVersion);
			oFile.writeInt(_iPublicVersion);
			oFile.writeInt(_iMapWest);
			oFile.writeInt(_iMapEast);
			oFile.writeInt(_iMapNorth);
			oFile.writeInt(_iMapSouth);
			oFile.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * Attempts to read sFile into a GameMap.
	 *
	 * @param sFile path to the specified file
	 * @param bCheckClassVersion if true, and read class version doesn't match
	 * GameMap's class version, method will return null
	 * @return a new GameMap with all the data read from sFile, or <code>null</code>
	 * if <code>bCheckClassVersion == true</code> and versions don't match.
	 */
	public static GameMap read(String sFile, boolean bCheckClassVersion){
		if(!new File(sFile).exists())
			return null;
		GameMap oMap = null;

		int iClassVersion;

		try{
			ObjectInputStream oFile = new ObjectInputStream(new FileInputStream(sFile));
			oMap = new GameMap();
			iClassVersion = oFile.readInt();
			
			if(bCheckClassVersion && iClassVersion != CLASS_VERSION){
				oFile.close();
				return null;
			}

			int x, y, z, t;
			x = oFile.readInt();
			y = oFile.readInt();
			z = oFile.readInt();
			oMap.setSize(x, y, z);
			t = oFile.readInt();
			oMap.setTileset(t);

			byte[] oCompressedMapData = (byte[])oFile.readObject();
			int[][][] oMapData = (int[][][])Util.bytesToObject(Util.decompress(oCompressedMapData));
			oMap.setMapData(oMapData);

			oMap.setName((String)oFile.readObject());

			if(iClassVersion >= 2){
				oMap.setPrivateVersion(oFile.readInt());
				oMap.setPublicVersion(oFile.readInt());
			}
			if(iClassVersion >= 3){
				oMap.setMapWest(oFile.readInt());
				oMap.setMapEast(oFile.readInt());
				oMap.setMapNorth(oFile.readInt());
				oMap.setMapSouth(oFile.readInt());
			}

			oFile.close();
		}catch(java.io.InvalidClassException ex){
			ex.printStackTrace();
		}catch(java.lang.ClassNotFoundException ex){
			ex.printStackTrace();
		}catch(java.io.IOException ex){
			ex.printStackTrace();
		}

		return oMap;
	}

	/**
	 * Calls <code>read(sFile, false)</code>
	 * @see #read(String, boolean)
	 */
	public static GameMap read(String sFile){
		return read(sFile, false);
	}

	public static int readClassVersion(String sFile){
		int iClassVersion;

		try{
			ObjectInputStream oFile = new ObjectInputStream(new FileInputStream(sFile));
			iClassVersion = oFile.readInt();
			oFile.close();
			return iClassVersion;
		}catch(java.io.InvalidClassException ex){
			ex.printStackTrace();
		}catch(java.io.IOException ex){
			ex.printStackTrace();
		}

		throw new RuntimeException();
//		return iClassVersion;
	}

	public boolean isInsideMap(int x, int y){
		return (x >= 0 && y >= 0 && x < _iWidth && y < _iHeight);
	}

	public boolean isInsideMap(float x, float y){
		return (x >= 0 && y >= 0 && x < _iWidth && y < _iHeight);
	}

	/**
	 * Checks if this map's tile at (x, y) is passable for the passed Tileset.
	 *
	 * @param x location of the tile in the map, measured in tiles
	 * @param y location of the tile in the map, measured in tiles
	 * @param oTileset tileset of the map
	 * @param iOffsetX location of the point to validate, inside the tile, measured in pixels
	 * @param iOffsetY location of the point to validate, inside the tile, measured in pixels
	 * @return true if the tile is passable and inside the map, false otherwize
	 */
	public boolean isTilePassable(int x, int y, Tileset oTileset, int iOffsetX, int iOffsetY){
		if(!isInsideMap(x, y))
			throw new IllegalArgumentException("isInsideMap(x, y) == false (x: " + x + "; " + "y: " + y + ")");
//		if(x < 0)
//			return _iMapWest != -1;
//		else if(x >= _iWidth){
////			System.out.println("OLOLOL");
////			return _iMapEast != -1;
//			return true;
//		}
//		else if(y < 0)
//			return _iMapNorth != -1;
//		else if(y >= _iHeight)
//			return _iMapSouth != -1;
		Tile oTile;
		int iTile;
		boolean bIsPassable;

		for(int i = getLayerCount() - 1; i > -1; i--){
			iTile = _iMapData[i][x][y];
			if((0xFFFF & iTile) == RS_NULL_TILE)
				continue;
			oTile = oTileset.getTile(iTile);
			if(oTile.Priority != 0)
				continue;

			bIsPassable = oTile.isPassable();

			if(iOffsetY < 8)
				bIsPassable = bIsPassable && oTile.isPassable(RS_DIR_N);
			else if(iOffsetY >= 24)
				bIsPassable = bIsPassable &&  oTile.isPassable(RS_DIR_S);
			if(iOffsetX < 8)
				bIsPassable = bIsPassable &&  oTile.isPassable(RS_DIR_W);
			else if(iOffsetX >= 24)
				bIsPassable = bIsPassable &&  oTile.isPassable(RS_DIR_E);

			return bIsPassable;
		}
		
		return false;
	}

	public int[][][] sortByPriority(Tileset oTileset){
		int iPriorityCount = 5 + _iLayerCount;
		int[][][] oSorted = new int[iPriorityCount][_iWidth][_iHeight];
		int iTile;
		byte iTilePriority;

		for(int x = 0; x < _iWidth; x++){
			for(int y = 0; y < _iHeight; y++){
				for(int z = 0; z < iPriorityCount; z++){
					oSorted[z][x][y] = -1;
				}
			}
		}

		for(int x = 0; x < _iWidth; x++){
			for(int y = 0; y < _iHeight; y++){
				for(int z = 0; z < _iLayerCount; z++){
					iTile = _iMapData[z][x][y];
					if((iTile & RS_NULL_TILE) == RS_NULL_TILE)
						continue;
//					if((iTile & AUTOTILE_BIT) != 0) // is autotile
//						continue;
//					iTile &= NULL_TILE;
					iTilePriority = oTileset.getTile(iTile & RS_NULL_TILE).Priority;
					if(z > 0)
						iTilePriority++;
					oSorted[iTilePriority][x][y] = iTile;
				}
			}
		}

		return oSorted;
	}

	public void setPrivateVersion(int i){
		_iPrivateVersion = i;
	}

	public int getPrivateVersion(){
		return _iPrivateVersion;
	}
	
	public void setPublicVersion(int i){
		_iPublicVersion = i;
	}

	public int getPublicVersion(){
		return _iPublicVersion;
	}

	public int getMapSouth() {
		return _iMapSouth;
	}

	public void setMapSouth(int i) {
		_iMapSouth = i;
	}

	public int getMapWest() {
		return _iMapWest;
	}

	public void setMapWest(int i) {
		_iMapWest = i;
	}

	public int getMapEast() {
		return _iMapEast;
	}

	public void setMapEast(int i) {
		_iMapEast = i;
	}

	public int getMapNorth() {
		return _iMapNorth;
	}

	public void setMapNorth(int i) {
		_iMapNorth = i;
	}

	public int[][][] getSortedMap() {
		return _oSortedMap;
	}

	public void setSortedMap(int[][][] o) {
		_oSortedMap = o;
	}

	public void setSortedMap(Tileset o) {
		_oSortedMap = sortByPriority(o);
	}

}
