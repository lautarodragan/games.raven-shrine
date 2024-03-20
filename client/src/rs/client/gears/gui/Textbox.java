package rs.client.gears.gui;

import rs.client.gears.Color;
import rs.client.gears.Timer;

/**
 *
 * @author lainmaster
 */
public class Textbox extends Label{
	private boolean _bCaretBlinkState;
	private float _iCaretBlinkLastUpdate;

	@Override
	public void focus(){
		super.focus();
		_iCaretBlinkLastUpdate = Timer.getSystemTime();
		_bCaretBlinkState = true;
	}

	@Override
	public void draw(int x, int y){
		super.draw(x, y);
		
		if(Timer.getSystemTime() - _iCaretBlinkLastUpdate > 400){
			_iCaretBlinkLastUpdate = Timer.getSystemTime();
			_bCaretBlinkState = !_bCaretBlinkState;
		}

		if(_bCaretBlinkState && _bHasFocus)
			_oFont.drawString(x + _oBounds.getX() + 3 + _oFont.getWidth(_sText) - 1, y + _oBounds.getY() + 3, "|", Color.white);
	}

	
}
