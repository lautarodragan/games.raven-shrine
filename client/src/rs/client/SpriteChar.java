package rs.client;

import rs.client.gears.Sprite;
import rs.resources.Animation;
import rs.resources.Paths;

public class SpriteChar extends Sprite{
	private static final int ANIMATION_INTERVAL_WALK = 6;

	public SpriteChar(String sFile){
		super(sFile, 4, 4);
		_addAnimations();
	}

	public SpriteChar(){
		super(4, 4);
		_addAnimations();
	}

	public SpriteChar(java.net.URL oUrl){
		super(oUrl, 4, 4);
		_addAnimations();
	}

	private void _addAnimations(){
		Animation oAnimation;

		// WALKING

		oAnimation = new Animation();
		oAnimation.Interval = ANIMATION_INTERVAL_WALK;
		oAnimation.PlayOnce = false;
		for(int i = 0; i < 4; i++)
			oAnimation.Frames.add(i);
		getAnimations().add(oAnimation);

		oAnimation = new Animation();
		oAnimation.Interval = ANIMATION_INTERVAL_WALK;
		oAnimation.PlayOnce = false;
		for(int i = 4; i < 8; i++)
			oAnimation.Frames.add(i);
		getAnimations().add(oAnimation);

		oAnimation = new Animation();
		oAnimation.Interval = ANIMATION_INTERVAL_WALK;
		oAnimation.PlayOnce = false;
		for(int i = 8; i < 12; i++)
			oAnimation.Frames.add(i);
		getAnimations().add(oAnimation);

		oAnimation = new Animation();
		oAnimation.Interval = ANIMATION_INTERVAL_WALK;
		oAnimation.PlayOnce = false;
		for(int i = 12; i < 16; i++)
			oAnimation.Frames.add(i);
		getAnimations().add(oAnimation);

		// STANDING

		for(int i = 0; i < 4; i++){
			oAnimation = new Animation();
			oAnimation.Interval = ANIMATION_INTERVAL_WALK;
			oAnimation.PlayOnce = false;
			oAnimation.Frames.add(i * 4);
			getAnimations().add(oAnimation);
		}

		getSpace().DesiredSpeed = 2f;

		setAnimationIndex(4);
	}
}
