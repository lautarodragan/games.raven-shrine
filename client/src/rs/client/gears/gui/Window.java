package rs.client.gears.gui;

import java.util.Iterator;
import java.util.LinkedList;
import org.lwjgl.util.Rectangle;
import rs.client.gears.Color;
import rs.client.gears.Texture;
import rs.client.gears.Util;

/**
 *
 * @author lainmaster
 */
public class Window implements MouseListener{
	protected boolean _bHasFocus;
	protected Rectangle _oBounds = new Rectangle();
	protected Color _oBackgroundColor;
	protected Color _oBorderColor;
	protected boolean _bHasBorder;
	protected boolean _bVisible = true;
	protected Texture _oBackgroundImage;
	protected ImageDrawMethod _eImageDrawMethod = ImageDrawMethod.Stretch;

	private final LinkedList<MouseListener> _oMouseListeners = new LinkedList<MouseListener>();
	private final LinkedList<Window> _oChildren = new LinkedList<Window>();

	public boolean isVisible(){
		return _bVisible;
	}

	public void setVisible(boolean b){
		_bVisible = b;
	}

	public Color getBackgroundColor() {
		return _oBackgroundColor;
	}

	public void setBackgroundColor(Color oBackgroundColor) {
		_oBackgroundColor = oBackgroundColor;
	}

	public Color getBorderColor() {
		return _oBorderColor;
	}

	public void setBorderColor(Color oBorderColor) {
		_oBorderColor = oBorderColor;
		_bHasBorder = true;
	}

	/**
	 * Returns a new rectangle, with the bounds of this object.
	 * Subsequent modifications to the returned rectangle won't
	 * affect this object.
	 * 
	 * @return the bounds of this object.
	 */
	public Rectangle getBounds(){
		return new Rectangle(_oBounds);
	}

	/**
	 * Sets the bounds of this object.
	 * Throws an IllegalArgumentException if the rectangle is null.
	 * Subsequent modifications to the passed rectangle won't affect
	 * this object.
	 *
	 * @param oRectangle new desired bounds.
	 */
	public void setBounds(Rectangle oRectangle){
		if(oRectangle == null)
			throw new IllegalArgumentException("oRectangle == null");
		_oBounds.setBounds(oRectangle);
	}

	/**
	 * Sets the bounds of this object.
	 */
	public void setBounds(int x, int y, int w, int h){
		_oBounds.setBounds(x, y, w, h);
	}

	public void setBorder(boolean b){
		_bHasBorder = b;
	}

	public boolean getBorder(){
		return _bHasBorder;
	}

	public void setBackgroundImage(Texture o){
		_oBackgroundImage = o;
	}

	public Texture getBackgroundImage(){
		return  _oBackgroundImage;
	}

	public void focus(){
		_bHasFocus = true;
	}

	public void blur(){
		_bHasFocus = false;
	}

	public final void draw(){
		if(!_bVisible)
			return;
		draw(0, 0);
	}

	/**
	 * Draws the window with offset (x, y) relative to it's position, and then
	 * calls the draw method on each child, if it's visible.
	 * 
	 * @param x offset x
	 * @param y offset y
	 */
	public void draw(int x, int y){
		Rectangle oDrawBounds = new Rectangle(_oBounds);

		oDrawBounds.translate(x, y);
		if(_oBackgroundColor != null)
			Util.DrawRectangle(oDrawBounds, true, _oBackgroundColor);

		if(_oBackgroundImage != null){
			Color.white.bind();
			if(_eImageDrawMethod == ImageDrawMethod.Stretch)
				_oBackgroundImage.draw(oDrawBounds.getX() + x, oDrawBounds.getY() + y, oDrawBounds.getWidth(), oDrawBounds.getHeight(), 0, 0, oDrawBounds.getWidth(), oDrawBounds.getHeight());
			else if(_eImageDrawMethod == ImageDrawMethod.Tile)
				_oBackgroundImage.draw(oDrawBounds.getX() + x, oDrawBounds.getY() + y, oDrawBounds.getWidth(), oDrawBounds.getHeight(), 0, 0, _oBackgroundImage.getWidth(), _oBackgroundImage.getHeight());
		}

		if(_bHasBorder && _oBorderColor != null)
			Util.DrawRectangle(oDrawBounds, false, _oBorderColor);

		synchronized(_oChildren){
			if(!_oChildren.isEmpty()){
				Iterator<Window> oIterator = _oChildren.iterator();
				Window o;
				while(oIterator.hasNext()){
					o = oIterator.next();
					if(o.isVisible())
						o.draw(oDrawBounds.getX(), oDrawBounds.getY());
				}
			}
		}
		
	}

	public void mouseMoved(int x, int y) {
		synchronized(_oChildren){
			if(!_oChildren.isEmpty()){
				Iterator<Window> oIterator = _oChildren.iterator();
				Window o;
				while(oIterator.hasNext()){
					o = oIterator.next();
					if(o.isVisible())
						o.mouseMoved(x - o.getBounds().getX(), y - o.getBounds().getY());
				}
			}
		}
		if(_oBounds.contains(_oBounds.getX() + x, _oBounds.getY() + y)){
			synchronized(_oMouseListeners){
				Iterator<MouseListener> oIterator = _oMouseListeners.iterator();
				MouseListener o;
				while(oIterator.hasNext()){
					o = oIterator.next();
					o.mouseMoved(x, y);
				}
			}
		}
	}

	public void mousePressed(int iButton, boolean bState, int x, int y) {
		synchronized(_oChildren){
			if(!_oChildren.isEmpty()){
				Iterator<Window> oIterator = _oChildren.iterator();
				Window o;
				while(oIterator.hasNext()){
					o = oIterator.next();
					if(o.isVisible() && o.getBounds().contains(x, y)){
						o.mousePressed(iButton, bState, x, y);
					}
				}
			}
		}
		synchronized(_oMouseListeners){
			Iterator<MouseListener> oIterator = _oMouseListeners.iterator();
			MouseListener o;
			while(oIterator.hasNext()){
				o = oIterator.next();
				o.mousePressed(iButton, bState, x, y);
			}
		}
	}

	public void addMouseListener(MouseListener o){
		_oMouseListeners.add(o);
	}

	public void removeMouseListener(MouseListener o){
		_oMouseListeners.remove(o);
	}

	public void addChild(Window o){
		_oChildren.add(o);
	}

	public void removeChild(Window o){
		_oChildren.remove(o);
	}


}
