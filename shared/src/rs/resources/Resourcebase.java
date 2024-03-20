package rs.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Vector;

public class Resourcebase {
	public static List<Tileset> readTilesets(){
		List<Tileset> oTilesets = null;

		oTilesets = Tileset.readTilesets(Paths.PATH_DATA + "/tilesets.db");

		return oTilesets;
	}

	public static void writeTilesets(List<Tileset> oTilesets){
		Tileset.writeTilesets(Paths.PATH_DATA + "/tilesets.db", oTilesets);
	}
	
	public static void writeMap(rs.resources.GameMap o){
		o.write(Paths.PATH_DATA + "/" + o.getId() + ".map");
	}

	public static GameMap readMap(int iId){
		return GameMap.read(Paths.PATH_DATA + "/" + iId + ".map");
	}

	public static GameMap readMap(GameMapReference o){
		return readMap(o.getId());
	}

	public static Vector<GameMapReference> readMapReferences(){
		if(!new File(Paths.PATH_DATA + "/maps.db").exists())
			return null;
		Vector<GameMapReference> oMapReferences = new Vector<GameMapReference>();
		GameMapReference oMapReference;
		try{
			ObjectInputStream oFile = new ObjectInputStream(new FileInputStream(Paths.PATH_DATA + "/maps.db"));
			//oMapReferences = (Vector<MapReference>)oFile.readObject();
			while(true){
				oMapReference = GameMapReference.read(oFile);
				if(oMapReference == null)
					break;
				oMapReferences.add(oMapReference);
			}

			oFile.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return oMapReferences;
	}

	public static void writeMapReferences(Vector<GameMapReference> oMapReferences){
		if(oMapReferences == null){
			System.err.println("Could not write map references: oMapReferences = null");
			return;
		}

		try{
			ObjectOutputStream oFile = new ObjectOutputStream(new FileOutputStream(Paths.PATH_DATA + "/maps.db"));
			for(GameMapReference o : oMapReferences){
				o.write(oFile);
			}
			oFile.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static GameMapReference getMapReferenceById(Vector<GameMapReference> oMapReferences, int iId){
		for(GameMapReference o : oMapReferences){
			if(o.getId() == iId)
				return o;
		}

		return null;
	}

	public static int getFreeMapId(Vector<GameMapReference> oMapReferences){
		if(oMapReferences == null)
			return 0;
		
		int i = 0;
		for(GameMapReference o : oMapReferences){
			if(i <= o.getId())
				i = o.getId() + 1;
		}
		return i;
	}

	public static int getFreeTilesetId(List<Tileset> oTilesets){
		if(oTilesets == null)
			return 0;

		int i = 0;
		for(Tileset o : oTilesets){
			if(i <= o.getId())
				i = o.getId() + 1;
		}
		return i;
	}

	public static Tileset getTilesetById(List<Tileset> oTilesets, int iId){
		if(oTilesets == null)
			throw new IllegalArgumentException("oTilesets == null");

		for(Tileset o : oTilesets){
			if(iId == o.getId())
				return o;
		}
		return null;
	}

}
