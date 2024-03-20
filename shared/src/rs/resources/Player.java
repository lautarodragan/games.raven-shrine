package rs.resources;

import java.util.Map;

public class Player {
	public static final long SECOND_IN_NANOS = (long) Math.pow(10, 9);

	public String Username;
	public int Id;
	public int Map;
	public Space MapPosition = new Space();
	public String SpriteFilename;
	public boolean IsLoggedIn;
	public String LastIp;
	public int Privileges;
	
	public byte[] IsKeyDown = new byte[4];
	public long[] LastKeyChange = new long[4]; // System.nanoTime for the last time a movement input message was recieved
	public boolean JustLoggedIn = true;
	
	public Tileset oTileset;
	public GameMap oGameMap;
	public Map<Integer, GameMap> _oMapGameMaps;

	public Player(){
		MapPosition.DesiredSpeed = 2.5f; // per frame @ 60 FPS = 150 pixels per second
	}

	public void updatePosition(byte iDirection, byte iFlag){
		// TODO: (Server) update walking to check for change of map, optimize walking code
		long iTimeLength = 0;
		long iTime = System.nanoTime();
		float iPixels;
		float x =0, y = 0, iNewX = 0, iNewY = 0;
		float iFinalX = 0, iFinalY = 0;
		float iTileX = 0, iTileY = 0;

		if(IsKeyDown[0] == 1) // if was walking right
			iTimeLength = System.nanoTime() - LastKeyChange[0];
		else if(IsKeyDown[1] == 1) // if was walking right
			iTimeLength = System.nanoTime() - LastKeyChange[1];
		else if(IsKeyDown[2] == 1) // if was walking right
			iTimeLength = System.nanoTime() - LastKeyChange[2];
		else if(IsKeyDown[3] == 1) // if was walking right
			iTimeLength = System.nanoTime() - LastKeyChange[3];

		int iFrameWidth = 32;
		int iFrameHeight = 48;
		boolean bMove = false;
		boolean bIsMovingX = IsKeyDown[0] == 1 || IsKeyDown[1] == 1;
		boolean bIsMovingY = IsKeyDown[2] == 1 || IsKeyDown[3] == 1;

		if(!(iFlag == 1 && JustLoggedIn)){
			if(iTimeLength > 0){
				iPixels = (iTimeLength / (float)SECOND_IN_NANOS) * 150;
				for(;;){
					if(bIsMovingX){ // if was walking west or east
						x += 1;
					}
					if(bIsMovingY){ // if was walking north or south
						y += 1;
					}

					iNewX = x;
					iNewY = y;
					bMove = true;

					GameMap map = oGameMap;

					for(float x2 = 0; x2 < iFrameWidth; x2++){
						for(float y2 = 0; y2 < 16; y2++){
							if(IsKeyDown[0] == 1)
								iTileX = ((MapPosition.x - x + x2) );
							else if(IsKeyDown[1] == 1)
								iTileX = ((MapPosition.x + x + x2) );
							else
								iTileX = ((MapPosition.x + x2) );
							if(IsKeyDown[2] == 1)
								iTileY = ((MapPosition.y - y + y2) );
							else if(IsKeyDown[3] == 1)
								iTileY = ((MapPosition.y + y + y2) );
							else
								iTileY = ((MapPosition.y + y2) );

							if(!oGameMap.isInsideMap((int)Math.floor(iTileX / 32), (int)Math.floor(iTileY / 32))){
								if(iTileX < 0){
									map = _oMapGameMaps.get(oGameMap.getMapWest());

									if(map == null){
										System.out.println("null map left!");
									}else{
										iTileX += (map.getWidth()  - 0) * 32;
									}
								}else if(iTileX >= (oGameMap.getWidth() - 1) * 32){
									map = _oMapGameMaps.get(oGameMap.getMapEast());

									if(map == null){
										System.out.println("null right map! " + oGameMap.getName() + ", " + oGameMap.getMapEast());
									}else{
										iTileX -= (map.getWidth()  - 0) * 32;
									}
								}else if(iTileY < 0){
									map = _oMapGameMaps.get(oGameMap.getMapNorth());

									if(map == null){
										System.out.println("null map north!");
									}else{
										iTileX += (map.getWidth()  - 0) * 32;
									}
								}
							}

							if(map == null || !map.isTilePassable((int)Math.floor(iTileX / 32), (int)Math.floor(iTileY / 32), oTileset, (int)Math.floor(iTileX % 32), (int)Math.floor(iTileY % 32))){
								bMove = false;
								break;
							}
						}
						if(!bMove) break;
					}

					if(bMove){
						iFinalX = x;
						iFinalY = y;
					}

					if(!bMove || x >= iPixels || y >= iPixels)
						break;
					
				}
			}

			if(IsKeyDown[0] == 1)
				MapPosition.x -= iFinalX;
			else if(IsKeyDown[1] == 1)
				MapPosition.x += iFinalX;
			if(IsKeyDown[2] == 1)
				MapPosition.y -= iFinalY;
			else if(IsKeyDown[3] == 1)
				MapPosition.y += iFinalY;

			if(MapPosition.x < 0){
				System.out.println("left");
				MapPosition.x += oGameMap.getWidth() * 32 - 16;
				Map = oGameMap.getMapWest();
				oGameMap = _oMapGameMaps.get(Map);
			}else if(MapPosition.x > oGameMap.getWidth() * 32){
				System.out.println("right");
				MapPosition.x -= oGameMap.getWidth() * 32 + 0;
				Map = oGameMap.getMapEast();
				oGameMap = _oMapGameMaps.get(Map);
			}

			if(MapPosition.y < 0){
				MapPosition.y += oGameMap.getHeight() * 32 - 32;
				Map = oGameMap.getMapNorth();
				oGameMap = _oMapGameMaps.get(Map);
			}else if(MapPosition.y > oGameMap.getHeight() * 32){
				MapPosition.y -= oGameMap.getHeight() * 32 - 32;
				Map = oGameMap.getMapSouth();
				oGameMap = _oMapGameMaps.get(Map);
			}
			
		}

		JustLoggedIn = false;
		IsKeyDown[iDirection] = iFlag;
		LastKeyChange[0] = System.nanoTime();
		LastKeyChange[1] = System.nanoTime();
		LastKeyChange[2] = System.nanoTime();
		LastKeyChange[3] = System.nanoTime();
	}

	@Override
	public String toString(){
		return Username;
	}
}
