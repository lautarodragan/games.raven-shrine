package rs.client.gears;

/**
 *
 * @author lainmaster
 */
public class Timer {
	public enum TimerMode{
		JavaNanoTime,
		LwjglTimer;
	}
	private static TimerMode _oTimerMode = TimerMode.JavaNanoTime;


	/**
	 * Uses System.nanoTime or lwjgl's Sys.getTime to get the time
	 * @return the time in milliseconds
	 */
	public static float getSystemTime(){
		if(_oTimerMode == TimerMode.JavaNanoTime){
			return System.nanoTime() / (float)1000000;
		}else if(_oTimerMode == TimerMode.LwjglTimer){
			return ((float)org.lwjgl.Sys.getTime() / (float)org.lwjgl.Sys.getTimerResolution()) * 1000;
		}else{
			return 0;
		}
	}

	public static void setTimerMode(TimerMode oTimerMode){
		_oTimerMode = oTimerMode;
	}

	private float _iStart;
	private float _iAlarm;

	public Timer(){
		start();
	}

	public static TimerMode getTimerMode(){
		return _oTimerMode;
	}

	public void start(){
		_iStart = getSystemTime();
	}

	public void setAlarm(float iTimeMillis){
		_iAlarm = iTimeMillis;
	}

	public float getTime(){
		return getSystemTime() - _iStart;
	}

	

}
