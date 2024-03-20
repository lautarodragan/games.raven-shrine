package rs.resources;

public class Space {
	public float x;
	public float y;
	/**
	* The amount of pixels to move each frame, if the frame rate was constant
	 */
	public float DesiredSpeed;
	/**
	 * The amount if pixels this space will actually move, relative to the variations on the frame rate
	 */
	public float Speed;

	public Space(){
		DesiredSpeed = 1;
		Speed = 1;
	}

	public void move(int x, int y){
		this.x += x;
		this.y += y;
	}
	public void move(float x, float y){
		this.x += x;
		this.y += y;
	}
	public void moveRight(){
		move(Speed, 0);
	}
	public void moveLeft(){
		move(-Speed, 0);
	}
	public void moveUp(){
		move(0, -Speed);
	}
	public void moveDown(){
		move(0, Speed);
	}
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	public void set(Space o){
		set(o.x, o.y);
	}

	/**
	 * Sets the position of this Space to be inside of the desired bounds
	 * @param x1 <code>if(x &#60; x1) x = x1;</code>
	 * @param y1 <code>if(x &#62; x2) x = x2;</code>
	 * @param x2 <code>if(y &#60; y1) y = y1;</code>
	 * @param y2 <code>if(y &#62; y2) y = y2;</code>
	 */
	public void setBounds(int x1, int y1, int x2, int y2){
		if(x < x1)
			x = x1;
		if(x > x2)
			x = x2;
		if(y < y1)
			y = y1;
		if(y > y2)
			y = y2;
	}
	
	@Override
	public String toString(){
		return "Space{x: " + x + "; y: " + y + "}";
	}
}
