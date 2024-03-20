package rs.client.gears;

import rs.resources.Space;
import rs.resources.Animation;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

public class Sprite implements InputListener{
	private int _iImagesX = 1;		// Amount of different images in _oTexture, horizontally
	private int _iImagesY = 1;		// Amount of different images in _oTexture, vertically

	private Space _oSpace;
	private Texture _oTexture;		// Whole image
	private ArrayList<Animation> _oAnimations;
	private int _iAnimation;

	private boolean _bInputEnabled;
	private boolean _bVisible;

	private String _sFilename;

	public float rotation=0;

	public Sprite(){
		_bInputEnabled = true;
		_bVisible = true;
		_oAnimations = new ArrayList<Animation>();
		_oSpace = new Space();
		_oTexture = new Texture();
	}
	public Sprite(int iImagesX, int iImagesY){
		this();
		_iImagesX = iImagesX;
		_iImagesY = iImagesY;
	}
	public Sprite(String sImagePath, int iImagesX, int iImagesY){
		this(iImagesX, iImagesY);
		_oTexture = new Texture(sImagePath);
	}
	public Sprite(String sImagePath){
		this(sImagePath, 1, 1);
	}
	public Sprite(java.net.URL oUrl, int iImagesX, int iImagesY){
		this(iImagesX, iImagesY);
		_oTexture = new Texture(oUrl);
	}
	public Sprite(java.net.URL oUrl){
		this(oUrl, 1, 1);
	}

	public void setInputEnabled(boolean bInputEnabled){
		_bInputEnabled = bInputEnabled;
	}
	public boolean isInputEnabled(){
		return _bInputEnabled;
	}

	public void setVisible(boolean bVisible){
		_bVisible = bVisible;
	}
	public boolean isVisible(){
		return _bVisible;
	}
	
	public void setAnimationIndex(int i){
		if(_iAnimation == i)
			return;
		_iAnimation = i;
//		_oAnimations.get(_iAnimation).reset();
	}
	public int getAnimationIndex(){
		return _iAnimation;
	}
	
	public  Animation getAnimation(){
		return _oAnimations.get(_iAnimation);
	}
	public  Animation getAnimation(int i){
		return _oAnimations.get(i);
	}

	public int getImagesX(){
		return _iImagesX;
	}
	public int getImagesY(){
		return _iImagesY;
	}

	public int getFrameWidth(){
		return getTexture().getWidth() / getImagesX();
	}
	public int getFrameHeight(){
		return getTexture().getHeight() / getImagesY();
	}

	public  ArrayList<Animation> getAnimations(){
		return _oAnimations;
	}

	public Space getSpace(){
		return _oSpace;
	}

	public void setTexture(Texture o){
		_oTexture = o;
	}
	public Texture getTexture(){
		return _oTexture;
	}

	public void setFilename(String s){
		_sFilename = s;
	}
	public String getFilename(){
		return _sFilename;
	}

	public void draw(){
		drawTile(_oSpace.x, _oSpace.y, _oAnimations.get(_iAnimation).Frames.get(_oAnimations.get(_iAnimation).Frame));
	}
	public void draw(float x, float y){
		drawTile(x, y, _oAnimations.get(_iAnimation).Frames.get(_oAnimations.get(_iAnimation).Frame));
	}
	public void drawTile(float x, float y, float iTile){
		float ix = (iTile % (float)_iImagesX);
		float iy = (float)Math.floor(iTile / (double)_iImagesY);
		drawTile(x, y, ix, iy);
	}
	public void drawTile(float x, float y, float iTileX, float iTileY){
		float w = _oTexture.getWidth() / _iImagesX;
		float h = _oTexture.getHeight() / _iImagesY;
		float sx = iTileX * w;
		float sy = iTileY * h;
		
		_oTexture.draw((float) Math.floor(x), (float) Math.floor(y), w, h, sx, sy, w, h);
	}

	public void moveRight(){
		_oSpace.moveRight();
	}
	public void moveLeft(){
		_oSpace.moveLeft();
	}
	public void moveUp(){
		_oSpace.moveUp();
	}
	public void moveDown(){
		_oSpace.moveDown();
	}

	public void KeyPressedLeft(){}
	public void KeyPressedRight(){}
	public void KeyPressedUp(){}
	public void KeyPressedDown(){}
	public void KeyPressedAccept(){}
	public void KeyPressedCancel(){}
	public void handleInput(){}

	public void KeyPressed(int iKey){
		
	}
}
