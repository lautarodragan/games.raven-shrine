package rs.client;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainApplet extends Applet{
	Main _oMain;
	Canvas _oCanvas;
	Thread _oGameThread;

	@Override
	public void init(){
		try {
			_oCanvas = new Canvas() {
				@Override
				public final void addNotify() {
					super.addNotify();
					startLWJGL();
				}
				@Override
				public final void removeNotify() {
					_oMain.stop();
					super.removeNotify();
				}
			};
			_oMain = new Main(_oCanvas);
			setSize(800, 600);
			this.addComponentListener(new ComponentListener() {

				public void componentResized(ComponentEvent e) {
					_oCanvas.setBounds(0, 0, getWidth(), getHeight());
					_oMain.changeResolutionLater(getWidth(), getHeight());
				}

				public void componentMoved(ComponentEvent e) {
				}

				public void componentShown(ComponentEvent e) {
					_oCanvas.setBounds(0, 0, getWidth(), getHeight());
					_oMain.changeResolutionLater(getWidth(), getHeight());
				}

				public void componentHidden(ComponentEvent e) {
				}
			});
			
			_oCanvas.setBounds(0, 0, getWidth(), getHeight());
			add(_oCanvas);

			_oCanvas.setFocusable(true);
			_oCanvas.requestFocus();
			_oCanvas.setIgnoreRepaint(true);
			setVisible(true);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public void startLWJGL() {
		_oGameThread = new Thread(_oMain);
		_oGameThread.start();
		_oMain.changeResolutionLater(getWidth(), getHeight());
	}
}
