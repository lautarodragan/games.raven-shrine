package rs.resources;

import java.io.Serializable;
import java.util.ArrayList;

public class Animation implements Serializable{
	public ArrayList<Integer> Frames;
	public int Interval; 
	public boolean PlayOnce;

	public int Frame;
	public float IntervalTaken;
	public boolean PlayedOnce;
	/**
	 *  For variant FPS modulating. Value to increase IntervalTaken by, in each frame.
	 */
	public float IntervalRelativeSpeed;

	public Animation(){
		Frames = new ArrayList<Integer>();
		reset();

	}

	public void reset(){
		Frame = 0;
		IntervalTaken = 0;
		PlayedOnce = false;
		IntervalRelativeSpeed = 1;
	}

	public void nextFrame(){
		if(Frames.size() < 2)
			return;
		if(IntervalTaken < Interval - 1){
			IntervalTaken += IntervalRelativeSpeed;
		}else{
			IntervalTaken = 0;
			if(Frame < Frames.size() - 1){
				Frame++;
			}else{
				PlayedOnce = true;
				if(!PlayOnce){
					Frame = 0;
				}
			}
		}

	}
}
