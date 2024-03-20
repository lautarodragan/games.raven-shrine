package rs.client.gears;

import org.lwjgl.util.Rectangle;
import rs.resources.Space;
import rs.resources.GameMap;
import rs.resources.Tileset;
import rs.resources.Tileset.Tile;

/**
 * Any animated object inside the visible GameMap
 */
public class Actor implements InputListener{
	private Sprite _oSprite;
	private Space _oSpace;
	private GameMap _oMap;
	private Camera _oCamera;
	private Tileset _oTileset;
	private int _iPriority;
	private boolean _bWalkThrough;
	private boolean _bMovingLeft;
	private boolean _bMovingRight;
	private boolean _bMovingUp;
	private boolean _bMovingDown;
	public GameMap[][] _oGameMaps;

	private Space _oPlayerDestLocation = new Space();
	
	public Actor(GameMap oMap, Camera oCamera, Tileset oTileset){
		this();
		setMap(oMap);
		setCamera(oCamera);
		setTileset(oTileset);
	}
	public Actor(){
		setSpace(new Space());
		_oSpace.DesiredSpeed = 2.5f;
		_iPriority = 1;
	}

	public void setSprite(Sprite o){
		_oSprite = o;
	}
	public Sprite getSprite(){
		return _oSprite;
	}

	public void setSpace(Space o){
		_oSpace = o;
	}
	public Space getSpace(){
		return _oSpace;
	}

	public void setMap(GameMap o){
		_oMap = o;
	}
	public GameMap getMap(){
		return _oMap;
	}

	public void setCamera(Camera o){
		_oCamera = o;
	}
	public Space getCamera(){
		return _oCamera;
	}

	public void setTileset(Tileset o){
		_oTileset = o;
	}
	public Tileset getTileset(){
		return _oTileset;
	}

	public void setPriority(int value){
		_iPriority = value;
	}
	public int getPriority(){
		return _iPriority;
	}

	public void setWalkThrough(boolean value){
		_bWalkThrough = value;
	}
	public boolean getWalkThrough(){
		return _bWalkThrough;
	}

	public void updateSpriteSpace(){
		_oSprite.getSpace().x = _oSpace.x - (int)_oCamera.x + _oCamera.getScreenWidth() / 2;
		_oSprite.getSpace().y = _oSpace.y - (int)_oCamera.y + _oCamera.getScreenHeight() / 2 - _oSprite.getFrameHeight() + _oSprite.getFrameHeight() / 3;
	}

	public void handleInput() {
//		for(int i = 0; i < Controllers.getControllerCount(); i++){
//			if(Controllers.getController(i).getAxisCount() > 1){
////				System.out.println(i + ": " + Controllers.getController(i).getName());
////				System.out.println(Controllers.next());
//
//				bKeyLeft = bKeyLeft || (Controllers.getController(0).getAxisValue(1) < 0);
//				bKeyRight = bKeyRight || (Controllers.getController(0).getAxisValue(1) > 0);
//				bKeyUp = bKeyUp || Controllers.getController(0).getAxisValue(0) < 0;
//				bKeyDown = bKeyDown || Controllers.getController(0).getAxisValue(0) > 0;
//			}
//		}

		// TODO: optimize walking code (client)

		int iFrameWidth = _oSprite.getFrameWidth();
		int iFrameHeight = _oSprite.getFrameHeight();
//		int iFeetTopY = (int)(_oSpace.y);
//		int iFeetBottomY = (int)(_oSpace.y + iFrameHeight / 3);
		float iNewX = _oSpace.x, iNewY = _oSpace.y;
		int iAnimationIndex = 0;
		boolean bMove = false;

		if(_bMovingLeft && !_bMovingRight){
			iNewX -= _oSpace.Speed;
			iAnimationIndex = 1;
			bMove = true;
			_oSprite.setAnimationIndex(iAnimationIndex);
		}else if(_bMovingRight && !_bMovingLeft){
			iNewX += _oSpace.Speed;
			iAnimationIndex = 2;
			bMove = true;
			_oSprite.setAnimationIndex(iAnimationIndex);
		}
		if(_bMovingUp && !_bMovingDown){
			iNewY -= _oSpace.Speed;
			iAnimationIndex = 3;
			bMove = true;
			_oSprite.setAnimationIndex(iAnimationIndex);
		}else if(_bMovingDown && !_bMovingUp){
			iNewY += _oSpace.Speed;
			iAnimationIndex = 0;
			bMove = true;
			_oSprite.setAnimationIndex(iAnimationIndex);
		}

		GameMap map = _oMap;
		int ix, iy;
		
		for(float x = iNewX; x < iNewX + iFrameWidth; x++){
			for(float y = iNewY; y < iNewY + 16; y++){
				ix = (int)Math.floor(x / 32);
				iy = (int)Math.floor(y / 32);
				
				if(!map.isInsideMap(ix, iy)){
					if(_oGameMaps == null){
						map = null;
					}else{
						if(ix < 0){
							map = _oGameMaps[0][1];
							if(map != null)
								ix = map.getWidth() - 1;
						}else if(ix >= map.getWidth()){
							map = _oGameMaps[2][1];
							if(map != null)
								ix = 0;
						}else if(iy < 0){
							map = _oGameMaps[1][0];
							if(map != null)
								iy = map.getHeight() - 1;
						}

					}
				}
				if(map == null || !map.isTilePassable(ix, iy, _oTileset, (int)Math.floor(x % 32), (int)Math.floor(y % 32))){
					bMove = false;
					break;
				}
			}
			if(!bMove) break;
		}
			
		if(bMove){
			_oSpace.x = iNewX;
			_oSpace.y = iNewY;
		}else{
			if(_oSprite.getAnimationIndex() < 4)
				_oSprite.setAnimationIndex(_oSprite.getAnimationIndex() + 4);
		}

		updateSpriteSpace();

	}

	public boolean isInputEnabled() {
		return true;
	}

	public Space getPlayerDestLocation(){
		return _oPlayerDestLocation;
	}

	public void setMovingRight(boolean b){
		_bMovingRight = b;
	}

	public boolean isMovingRight(){
		return _bMovingRight;
	}

	public void setMovingLeft(boolean b){
		_bMovingLeft = b;
	}

	public boolean isMovingLeft(){
		return _bMovingLeft;
	}

	public void setMovingDown(boolean b){
		_bMovingDown = b;
	}

	public boolean isMovingDown(){
		return _bMovingDown;
	}

	public void setMovingUp(boolean b){
		_bMovingUp = b;
	}

	public boolean isMovingUp(){
		return _bMovingUp;
	}

	/**
	 * Equivalent to calling setMovingUp(b); setMovingDown(b); etc.
	 * @param b True for moving, false otherwize.
	 */
	public void setMoving(boolean b){
		_bMovingRight = b;
		_bMovingLeft = b;
		_bMovingDown = b;
		_bMovingUp = b;
	}
}
