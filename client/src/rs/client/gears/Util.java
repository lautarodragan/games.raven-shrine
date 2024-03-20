package rs.client.gears;

import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.Rectangle;

/**
 *
 * @author lainmaster
 */
public class Util {
	public static void DrawRectangle(Rectangle r, boolean fill, Color c){
		c.bind();
		DrawRectangle(r, fill);
	}
	
	public static void DrawRectangle(Rectangle r, boolean fill){
		GL11.glLoadIdentity();
		GL11.glTranslatef(r.getX(), r.getY(), 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glLineWidth(2);

		GL11.glBegin(fill?GL11.GL_QUADS:GL11.GL_LINE_LOOP);
		GL11.glVertex2f(0, 0);
		GL11.glVertex2f(r.getWidth(), 0);
		GL11.glVertex2f(r.getWidth(), r.getHeight());
		GL11.glVertex2f(0, r.getHeight());

		GL11.glEnd();
		GL11.glLoadIdentity();
	}

	/**
	 * Outputs some system information to <code>System.out</code>
	 */
	public static void printSystemInfo(){
		System.out.println();
		System.out.println("System.nanoTime(): " + System.nanoTime());
		System.out.println("ByteOrder: " + ByteOrder.nativeOrder().toString());
		System.out.println("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
		System.out.println("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
		System.out.println("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
		IntBuffer o = BufferUtils.createIntBuffer(16);
		GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE, o);
		int iMexTex = o.get();
		System.out.println("GL_MAX_TEXTURE_SIZE: " + iMexTex);
		System.out.println("Possible Max VRAM: " + rs.util.Util.getSizeString(iMexTex*iMexTex*4));
		System.out.println("glLoadTransposeMatrixfARB() supported: " + GLContext.getCapabilities().GL_ARB_transpose_matrix);
		System.out.println("OS: " + System.getProperty("os.name"));
		System.out.println("Available Processors:" + java.lang.Runtime.getRuntime().availableProcessors());
		System.out.println("Total Memory: " + rs.util.Util.getSizeString( Runtime.getRuntime().totalMemory()));
		System.out.println();
	}

	/**
	 * Attempts to set the Display Mode adequate to the parameters
	 * @param iWidth Desired screen or window width
	 * @param iHeight Desired screen or window height
	 * @param bFullscreen True to attempt to set fullscreen, false for windowed mode
	 * @throws java.lang.Exception
	 */
	public static void setDisplayMode(int iWidth, int iHeight, boolean bFullscreen) throws Exception{
//		System.err.printf("setDisplayMode(%d, %d, %b);\n", iWidth, iHeight, bFullscreen);
		if(!bFullscreen){
			try {
				Display.setDisplayMode(new DisplayMode(iWidth, iHeight));
			} catch (LWJGLException ex) {

			}
		}else{
			Display.setFullscreen(true);
			try{
//				DisplayMode oDisplayModes[] = org.lwjgl.util.Display.getAvailableDisplayModes(320, 240, -1, -1, -1, -1, 60, 85);
				DisplayMode oDisplayModes[] = Display.getAvailableDisplayModes();
				for(DisplayMode o : oDisplayModes){
					if(o.getWidth() == iWidth && o.getHeight() == iHeight){
						Display.setDisplayMode(o);
						break;
					}
				}
			} catch(Exception e){
				System.err.println("Error: Could not start full screen, switching to windowed mode");
				Display.setFullscreen(false);
				Display.setDisplayMode(new DisplayMode(iWidth, iHeight));
			}
		}
	}

	/**
	 * Returns the path to the cache directory
	 * @return
	 */
	public static String getCachePath(){
		String sCachePath;

		sCachePath = System.getProperty("user.home");
		if(!sCachePath.endsWith("/"))
			sCachePath = sCachePath + "/";

		sCachePath = sCachePath + "ecl/";

		return sCachePath;

	}

}
