package rs.client.gears.gui;

import org.lwjgl.input.Mouse;
import rs.client.gears.AngelCodeFont;
import rs.client.gears.Color;
import rs.client.gears.Texture;

/**
 *
 * @author lainmaster
 */
public class Button extends Label{
	protected Texture _oImageHover;
	protected Texture _oImagePressed;
	protected ButtonState _eButtonState = ButtonState.Normal;

	public Button(){
		_eTextAlign = TextAlign.Middle;
	}

	public void setImageHover(Texture o){
		_oImageHover = o;
	}

	public Texture getImageHover(){
		return _oImageHover;
	}

	public void setImagePressed(Texture o){
		_oImagePressed = o;
	}

	public Texture getImagePressed(){
		return _oImagePressed;
	}

	public void setButtonState(ButtonState e){
		_eButtonState = e;
	}

	public ButtonState getButtonState(){
		return _eButtonState;
	}

	@Override
	public void draw(int x, int y){
		Texture oBackgroundImage = _oBackgroundImage;
		Color oBackgroundColor = _oBackgroundColor;
		Color oBorderColor = _oBorderColor;

		if(_eButtonState == ButtonState.Hover){
			if(_oImageHover != null)
				_oBackgroundImage = _oImageHover;
			if(_oBackgroundColor != null)
				_oBackgroundColor = _oBackgroundColor.brighter(.5f);
			if(_oBorderColor != null)
				_oBorderColor = _oBorderColor.brighter(.5f);
		}else if(_eButtonState == ButtonState.Pressed){
			if(_oImagePressed != null)
				_oBackgroundImage = _oImagePressed;
			if(_oBackgroundColor != null)
				_oBackgroundColor = _oBackgroundColor.darker(.2f);
			if(_oBorderColor != null)
				_oBorderColor = _oBorderColor.darker(.2f);
		}

		super.draw(x, y);

		_oBackgroundImage = oBackgroundImage;
		_oBackgroundColor = oBackgroundColor;
		_oBorderColor = oBorderColor;
	}

	@Override
	public void mouseMoved(int x, int y){
		super.mouseMoved(x, y);

		if(getBounds().contains(x + getBounds().getX(), y + getBounds().getY())){
			if(Mouse.isButtonDown(0))
				setButtonState(ButtonState.Pressed);
			else
				setButtonState(ButtonState.Hover);
		}else{
			setButtonState(ButtonState.Normal);
		}
	}

}
