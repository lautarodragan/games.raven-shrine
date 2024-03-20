package rs.server.maker.cursors;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

/**
 *
 * @author lainmaster
 */
public class Cursors {
	public static final Image ScrollButtonAll;
	public static final Cursor ScrollAll;
	public static final Cursor ScrollHorizontal;
	public static final Cursor ScrollVertical;
	public static final Cursor ScrollNorth;
	public static final Cursor ScrollSouth;
	public static final Cursor ScrollWest;
	public static final Cursor ScrollEast;
	public static final Cursor ScrollNorthWest;
	public static final Cursor ScrollNorthEast;
	public static final Cursor ScrollSouthWest;
	public static final Cursor ScrollSouthEast;

	public static final Cursor[][] Scroll = new Cursor[3][3];

	static{
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		Cursors o = new Cursors();
		Image oCursorImage;

		ScrollButtonAll = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("scroll_button_all.png"));

		oCursorImage = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("wheel_all.png"));
		ScrollAll = defaultToolkit.createCustomCursor(oCursorImage, new Point(15, 15), "Custom Cursor");

		oCursorImage = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("wheel_hor.png"));
		ScrollHorizontal = defaultToolkit.createCustomCursor(oCursorImage, new Point(15, 15), "Custom Cursor");

		oCursorImage = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("wheel_ver.png"));
		ScrollVertical = defaultToolkit.createCustomCursor(oCursorImage, new Point(15, 15), "Custom Cursor");

		oCursorImage = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("wheel_n.png"));
		ScrollNorth = defaultToolkit.createCustomCursor(oCursorImage, new Point(15, 15), "Custom Cursor");

		oCursorImage = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("wheel_s.png"));
		ScrollSouth = defaultToolkit.createCustomCursor(oCursorImage, new Point(15, 15), "Custom Cursor");

		oCursorImage = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("wheel_w.png"));
		ScrollWest = defaultToolkit.createCustomCursor(oCursorImage, new Point(15, 15), "Custom Cursor");

		oCursorImage = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("wheel_e.png"));
		ScrollEast = defaultToolkit.createCustomCursor(oCursorImage, new Point(15, 15), "Custom Cursor");

		oCursorImage = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("wheel_nw.png"));
		ScrollNorthWest = defaultToolkit.createCustomCursor(oCursorImage, new Point(15, 15), "Custom Cursor");

		oCursorImage = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("wheel_ne.png"));
		ScrollNorthEast = defaultToolkit.createCustomCursor(oCursorImage, new Point(15, 15), "Custom Cursor");

		oCursorImage = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("wheel_sw.png"));
		ScrollSouthWest = defaultToolkit.createCustomCursor(oCursorImage, new Point(15, 15), "Custom Cursor");

		oCursorImage = Toolkit.getDefaultToolkit().getImage(o.getClass().getResource("wheel_se.png"));
		ScrollSouthEast = defaultToolkit.createCustomCursor(oCursorImage, new Point(15, 15), "Custom Cursor");




		Scroll[1][1] = ScrollAll;

		Scroll[1][0] = ScrollNorth;
		Scroll[1][2] = ScrollSouth;
		Scroll[0][1] = ScrollWest;
		Scroll[2][1] = ScrollEast;

		Scroll[0][0] = ScrollNorthWest;
		Scroll[2][0] = ScrollNorthEast;
		Scroll[0][2] = ScrollSouthWest;
		Scroll[2][2] = ScrollSouthEast;
	}
}
