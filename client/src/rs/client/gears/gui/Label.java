package rs.client.gears.gui;

import rs.client.gears.AngelCodeFont;
import rs.client.gears.Color;

/**
 *
 * @author lainmaster
 */
public class Label extends Window{
	protected String _sText = "";
	protected AngelCodeFont _oFont;
	protected TextAlign _eTextAlign = TextAlign.Left;
	
	public void setText(String s){
		_sText = s;
	}

	public String getText(){
		return _sText;
	}

	public void setFont(AngelCodeFont o){
		_oFont = o;
	}

	public AngelCodeFont getFont(){
		return _oFont;
	}

	public void setTextAlign(TextAlign e){
		_eTextAlign = e;
	}

	public TextAlign getTextAlign(){
		return _eTextAlign;
	}

	@Override
	public void draw(int x, int y){
		super.draw(x, y);

		if(_sText != null && _oFont != null){
			Color.white.bind();

			int dx = 0, dy;

			dy =  y + _oBounds.getY() + _oBounds.getHeight() / 2 - _oFont.getHeight(_sText) / 2;

			switch(_eTextAlign){
				case Left:
					dx = x + _oBounds.getX() + 3;
					break;
				case Middle:
					dx = x + _oBounds.getX() + _oBounds.getWidth() / 2 - _oFont.getWidth(_sText) / 2;
					break;
				case Right:
					dx = x + _oBounds.getX() + _oBounds.getWidth() - _oFont.getWidth(_sText) - 3;
					break;
			}

			_oFont.drawString(dx, dy, _sText);
		}
	}

}
