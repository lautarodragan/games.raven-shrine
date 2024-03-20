package rs.client.gears;

import rs.resources.Space;

public class Camera extends Space{
	private int _iScreenWidth;
	private int _iScreenHeight;

	public void setScreenSize(int iWidth, int iHeight){
		_iScreenWidth = iWidth;
		_iScreenHeight = iHeight;
	}
	public int getScreenWidth(){
		return _iScreenWidth;
	}
	public int getScreenHeight(){
		return _iScreenHeight;
	}
}
