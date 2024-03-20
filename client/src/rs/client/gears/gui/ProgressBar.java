package rs.client.gears.gui;

import rs.client.gears.Color;
import rs.client.gears.Util;

/**
 *
 * @author lainmaster
 */
public class ProgressBar extends Window{
	private long _iProgress= 0;
	private long _iMax = 100;
	private Color _oBarColor = Color.white;

	public void setProgress(long i){
		if(i < 0 || i >= _iMax)
			throw new RuntimeException("Progress must be greater than zero and less than Max");

		_iProgress = i;
	}

	public long getProgress(){
		return _iProgress;
	}

	public void setMax(long i){
		if(i < 1)
			throw new RuntimeException("i must be >= 1");

		_iMax = i;
	}

	public long getMax(){
		return _iMax;
	}

	public void setBarColor(Color o){
		if(o == null)
			throw new RuntimeException("Color can't be null");

		_oBarColor = o;
	}

	public Color getBarColor(){
		return _oBarColor;
	}

	@Override
	public void draw(int x, int y){
		super.draw(x, y);

		_oBounds.grow(-6, -3);
		int w = _oBounds.getWidth();
		_oBounds.setWidth((int) (((float) _iProgress / (float) _iMax) * w));
		Util.DrawRectangle(_oBounds, true, _oBarColor);
		_oBounds.setWidth(w);
		_oBounds.grow(6, 3);
	}

}
