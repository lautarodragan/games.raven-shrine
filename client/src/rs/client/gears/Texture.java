package rs.client.gears;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Texture {
	public static final int TEXTURE_SIZE_MODE_SPLIT = 0;
	public static final int TEXTURE_SIZE_MODE_IGNORE = 1;

	private static int _iTextureSizeMode = TEXTURE_SIZE_MODE_SPLIT;
	private static int _iTextureMaxSize = 512;
	private static boolean _bLogLoads = false;
	private static final LinkedList<Texture> _oLoadedTextures = new LinkedList<Texture>();

	private int _iId;
	private int _iHeight;
	private int _iWidth;
	Texture[][] _oSplitTextures;
	private boolean _bIsSplit;
	private int _iSplitX, _iSplitY;
	private int _iSplitMaxSize;
	private float _iZ;
	private String _sName;
//	private boolean _bIsNV;

	private static int _iBoundTexture;

	public Texture() {
		
	}

	public Texture(String sPath) {
		_sName = sPath;
		_sName = _sName.replace("\\", "/");
		if(_sName.lastIndexOf("/") > -1)
			_sName = _sName.substring(_sName.lastIndexOf("/"));
		load(sPath);
	}

	public Texture(java.net.URL oUrl) {
		_sName = oUrl.toString();
		_sName = _sName.replace("\\", "/");
		_sName = _sName.substring(_sName.lastIndexOf("/"));
		load(oUrl);
	}

	public Texture(String sPath, int x, int y, int w, int h) {
		_sName = sPath;
		_sName = _sName.replace("\\", "/");
		_sName = _sName.substring(_sName.lastIndexOf("/"));
		load(getBufferedImage(sPath), x, y, w, h);
	}

	public Texture(java.net.URL oUrl, int x, int y, int w, int h) {
		_sName = oUrl.toString();
		_sName = _sName.replace("\\", "/");
		_sName = _sName.substring(_sName.lastIndexOf("/"));
		load(getBufferedImage(oUrl), x, y, w, h);
	}

	@Override
	public String toString() {
		return "texture{id:" + _iId + "; width:" + _iWidth + "; height:" + _iHeight;
	}

	public int getId() {
		return _iId;
	}

	public int getWidth() {
		return _iWidth;
	}

	public int getHeight() {
		return _iHeight;
	}

	public static BufferedImage getBufferedImage(String sPath){
		BufferedImage oBufferedImage = null;

		try {
			oBufferedImage = ImageIO.read(new File(sPath));
		} catch (IOException e) {
			System.err.println("Error@getBufferedImage(" + sPath + ")");
			e.printStackTrace();
		}

		return oBufferedImage;
	}
	
	public static BufferedImage getBufferedImage(java.net.URL input){
		BufferedImage oBufferedImage = null;

		try {
			oBufferedImage = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return oBufferedImage;
	}

	public void load(String sPath) {
		load(getBufferedImage(sPath));
	}

	public void load(java.net.URL input) {
		load(getBufferedImage(input));
	}

	public void load(BufferedImage oBufferedImage) {
		if( _iTextureSizeMode == TEXTURE_SIZE_MODE_IGNORE || (oBufferedImage.getWidth() <= _iTextureMaxSize && oBufferedImage.getHeight() <= _iTextureMaxSize))
			load(oBufferedImage, 0, 0, oBufferedImage.getWidth(), oBufferedImage.getHeight());
		else
			loadSplit(oBufferedImage, _iTextureMaxSize);
	}

	/**
	 * Generates a texture of given width and height, using the pixel data in oPixelsBuffer
	 * @param oPixelsBuffer ByteBuffer with all the pixel data
	 * @param iWidth width of the image
	 * @param iHeight height of the image
	 * @param iDepth either GL11.GL_RGB or GL11.GL_RGBA
	 * @return the texture id
	 */
	private int _genTextures(ByteBuffer oPixelsBuffer, int iWidth, int iHeight, int iDepth){
		oPixelsBuffer.rewind();
		IntBuffer oIntBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
		GL11.glGenTextures(oIntBuffer);
		int iTexture = oIntBuffer.get(0);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, oIntBuffer.get(0));
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, iDepth, iWidth, iHeight, 0, iDepth, GL11.GL_UNSIGNED_BYTE, oPixelsBuffer);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

		return iTexture;
	}

	/**
	 * Creates an OpenGL texture out of a part of an oBufferedImage
	 * @param oBufferedImage the image to craete the texture from
	 * @param x source x
	 * @param y source y
	 * @param w source width
	 * @param h source height
	 */
	public void load(BufferedImage oBufferedImage, int x, int y, int w, int h) {
		if (oBufferedImage == null)
			throw new IllegalArgumentException("Null image!");

		_iWidth = w;
		_iHeight = h;

		DataBufferByte oDataBufferByte = ((DataBufferByte) oBufferedImage.getRaster().getDataBuffer());
		byte[] iData = oDataBufferByte.getData();
		ByteBuffer oPixelsBuffer;
//		ByteBuffer oPixelsBuffer = ByteBuffer.allocateDirect(w * h * 4).order(ByteOrder.nativeOrder());
		boolean bLoadEntireImage = (x == 0 && y == 0 && oBufferedImage.getWidth() == w && oBufferedImage.getHeight() == h);

		if (oBufferedImage.getColorModel().getPixelSize() == 32){
			oPixelsBuffer = BufferUtils.createByteBuffer(w * h * 4);
			//System.out.println("load " + bLoadEntireImage);
			if(bLoadEntireImage){
				for (int i = 0; i < iData.length; i += 4) {
					oPixelsBuffer.put(iData[i + 3]);	// r
					oPixelsBuffer.put(iData[i + 2]);	// g
					oPixelsBuffer.put(iData[i + 1]);	// b
					oPixelsBuffer.put(iData[i + 0]);	// a
				}


			}else{
				System.out.println("cut " + x + ", " + y + ", " + w + ", " + h);
				for (int i = 0; i < h; i++){
					for (int j = 0; j < w; j++) {
						int iPosition = ((i + y) * oBufferedImage.getWidth() + j + x) * 4;
						oPixelsBuffer.put(iData[iPosition + 3]);	// r
						oPixelsBuffer.put(iData[iPosition + 2]);	// g
						oPixelsBuffer.put(iData[iPosition + 1]);	// b
						oPixelsBuffer.put(iData[iPosition + 0]);	// a
					}

				}
//				for (int i = 0; i < h; i++)
//					oPixelsBuffer.put(iData, (x + (y + i) * oBufferedImage.getWidth()) * 4, w * 4);
			}
			_iId = _genTextures(oPixelsBuffer, _iWidth, _iHeight, GL11.GL_RGBA);
		}else if (oBufferedImage.getColorModel().getPixelSize() == 8){
			oPixelsBuffer = BufferUtils.createByteBuffer(w * h * 4);
			if(bLoadEntireImage){
				for (int i = 0; i < iData.length; i++) {
					oPixelsBuffer.put((byte) (oBufferedImage.getColorModel().getRed(iData[i])));
					oPixelsBuffer.put((byte) (oBufferedImage.getColorModel().getGreen(iData[i])));
					oPixelsBuffer.put((byte) (oBufferedImage.getColorModel().getBlue(iData[i])));
					oPixelsBuffer.put((byte) (oBufferedImage.getColorModel().getAlpha(iData[i])));
				}
			}else{
				int i;
				for(int iy = y; iy < y+h; iy++){
					for(int ix = x; ix < x+w; ix++){
						i = iy * oBufferedImage.getWidth() + ix;
						oPixelsBuffer.put((byte) (oBufferedImage.getColorModel().getRed(iData[i])));
						oPixelsBuffer.put((byte) (oBufferedImage.getColorModel().getGreen(iData[i])));
						oPixelsBuffer.put((byte) (oBufferedImage.getColorModel().getBlue(iData[i])));
						oPixelsBuffer.put((byte) (oBufferedImage.getColorModel().getAlpha(iData[i])));
					}

				}
			}

			_iId = _genTextures(oPixelsBuffer, _iWidth, _iHeight, GL11.GL_RGBA);
		}else if (oBufferedImage.getColorModel().getPixelSize() == 24){
			oPixelsBuffer = BufferUtils.createByteBuffer(w * h * 3);
			if(bLoadEntireImage){
				for (int i = 0; i < iData.length; i += 3) {
					oPixelsBuffer.put(iData[i + 2]);
					oPixelsBuffer.put(iData[i + 1]);
					oPixelsBuffer.put(iData[i + 0]);
				}
			}else{
				int i;
				for(int iy = y; iy < y+h; iy++){
					for(int ix = x; ix < x+w; ix++){
						i = (iy * oBufferedImage.getWidth() + ix) * 3;
						oPixelsBuffer.put(iData[i + 2]);
						oPixelsBuffer.put(iData[i + 1]);
						oPixelsBuffer.put(iData[i + 0]);
					}

				}
			}
			_iId = _genTextures(oPixelsBuffer, _iWidth, _iHeight, GL11.GL_RGB);
		}
		_oLoadedTextures.add(this);
		if(_bLogLoads)
			System.out.printf("Texture.load(%d)(%d, \"%s\")\n", _oLoadedTextures.size(), _iId, _sName);
	}

	/**
	 * generates varios textures out of the image, limiting their size
	 * @param oBufferedImage image to craate the texture from
	 * @param iWidth desired max width for the textures
	 * @param iHeight desired max height for the textures
	 */
	public void loadSplit(BufferedImage oBufferedImage, int iTextureMaxSize){
		_iWidth = oBufferedImage.getWidth();
		_iHeight = oBufferedImage.getHeight();
		_bIsSplit = true;
		_iSplitMaxSize = iTextureMaxSize;
		
		float fMaxX, fMaxY;
		int iMaxX, iMaxY;
		fMaxX = _iWidth / (float)iTextureMaxSize;
		fMaxY = _iHeight / (float)iTextureMaxSize;
		
		if(fMaxX == Math.floor(fMaxX))
			iMaxX = (int) fMaxX;
		else
			iMaxX = (int) Math.floor(fMaxX) + 1;
		if(fMaxY == Math.floor(fMaxY))
			iMaxY = (int) fMaxY;
		else
			iMaxY = (int) Math.floor(fMaxY) + 1;

		_iSplitX = iMaxX;
		_iSplitY = iMaxY;
		_oSplitTextures = new Texture[iMaxX][iMaxY];

		System.out.printf("loadSplit (%d %d) (%f %f) (%d %d) (%d %d)\n", iMaxX, iMaxY, fMaxX, fMaxY, _iWidth, _iHeight, iTextureMaxSize, iTextureMaxSize);

		for(int x = 0; x < iMaxX; x++){
			for(int y = 0; y < iMaxY; y++){
				int w = Math.min(iTextureMaxSize, _iWidth - iTextureMaxSize * x);
				int h = Math.min(iTextureMaxSize, _iHeight - iTextureMaxSize * y);

				System.out.printf("\tloadSplit (%d %d) (%d %d)\n", x, y, w, h);
				Texture o = new Texture();
				o.load(oBufferedImage, x * iTextureMaxSize, y * iTextureMaxSize, w, h);
				_oSplitTextures[x][y] = o;
			}
		}

	}

	/**
	 * Frees this texture's memory
	 */
	public void unload(){
		IntBuffer oIntBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer(); // IntBuffer.wrap(new int[]{_iId});
		oIntBuffer.put(_iId);
		oIntBuffer.flip();
		GL11.glDeleteTextures(oIntBuffer);
		_oLoadedTextures.remove(this);
		if(_bLogLoads)
			System.out.printf("Texture.unload(%d)(%d, \"%s\")\n", _oLoadedTextures.size(), _iId, _sName);
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, _iId);
		_iBoundTexture = _iId;
	}

	public void draw(float x, float y) {
		if(!_bIsSplit)
			_drawFull(x, y);
		else
			_drawSplit(x, y);
	}

	private void _drawFull(float x, float y) {
		GL11.glTranslatef(x, y, 0);
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, _iId);
		bind();

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(0, 0);

		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(_iWidth, 0);

		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(_iWidth, _iHeight);

		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(0, _iHeight);

		GL11.glEnd();

		GL11.glTranslatef(-x, -y, 0);
	}

	private void _drawSplit(float iDrawX, float iDrawY){
		int xlag = 0, ylag = 0;
		int x, y;
		for(y = 0; y < _iSplitY; y++){
			for(x = 0; x < _iSplitX; x++){
				_oSplitTextures[x][y].draw(iDrawX + xlag, iDrawY + ylag);
				xlag += _oSplitTextures[x][y].getWidth();
				if(x == _iSplitX - 1){
					ylag += _oSplitTextures[x][y].getHeight();
					xlag = 0;
				}
			}
			
		}
	}

	public void draw(float x, float y, float w, float h, float sx, float sy, float sw, float sh) {
		if(!_bIsSplit){
			bind();
			GL11.glBegin(GL11.GL_QUADS);
			_drawEmbeddedFull(x, y, w, h, sx, sy, sw, sh);
			GL11.glEnd();
		}else{
			_drawSplit(x, y, w, h, sx, sy, sw, sh);
		}
			
	}

	public void drawEmbedded(float x, float y, float w, float h, float sx, float sy, float sw, float sh) {
		if(!_bIsSplit)
			_drawEmbeddedFull(x, y, w, h, sx, sy, sw, sh);
		else
			_drawSplit(x, y, w, h, sx, sy, sw, sh);
	}

	private void _drawEmbeddedFull(float x, float y, float w, float h, float sx, float sy, float sw, float sh) {
		float fsw = sw / (float) _iWidth;
		float fsh = sh / (float) _iHeight;
		float fsx = sx / (float) _iWidth;
		float fsy = sy / (float) _iHeight;

		GL11.glTexCoord2f(fsx, fsy); //Lower left
		GL11.glVertex3f(x, y, _iZ);

		GL11.glTexCoord2f(fsw + fsx, fsy); // Lower right
		GL11.glVertex3f(x + w, y, _iZ);

		GL11.glTexCoord2f(fsw + fsx, fsh + fsy); //Upper right
		GL11.glVertex3f(x + w, y + h, _iZ);

		GL11.glTexCoord2f(fsx, fsh + fsy); //Upper left
		GL11.glVertex3f(x, y + h, _iZ);
	}

	private void _drawSplit(float x, float y, float w, float h, float sx, float sy, float sw, float sh) {
		int iSourceImageX = (int) Math.floor(sx / _iSplitMaxSize);
		int iSourceImageY = (int) Math.floor(sy / _iSplitMaxSize);

		_oSplitTextures[iSourceImageX][iSourceImageY].draw(x, y, w, h, sx - iSourceImageX * _iSplitMaxSize, sy - iSourceImageY * _iSplitMaxSize, sw, sh);
		
	}

	public void drawEmbedded(float w, float h, float sx, float sy, float sw, float sh) {
		float fsw = sw / (float) _iWidth;
		float fsh = sh / (float) _iHeight;
		float fsx = sx / (float) _iWidth;
		float fsy = sy / (float) _iHeight;

		GL11.glTexCoord2f(fsx, fsy); //Lower left
		GL11.glVertex3f(0, 0, 0);

		GL11.glTexCoord2f(fsw + fsx, fsy); // Lower right
		GL11.glVertex3f(w, 0, 0);

		GL11.glTexCoord2f(fsw + fsx, fsh + fsy); //Upper right
		GL11.glVertex3f(w, h, 0);

		GL11.glTexCoord2f(fsx, fsh + fsy); //Upper left
		GL11.glVertex3f(0, h, 0);
	}

//	public void draw(float x, float y, float z, float w, float h, float sx, float sy, float sw, float sh) {
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, _iId);
//		GL11.glLoadIdentity();
//		GL11.glTranslatef(x, y, z);
//		GL11.glBegin(GL11.GL_QUADS);
//		drawEmbedded(w, h, sx, sy, sw, sh);
//		GL11.glEnd();
//		GL11.glLoadIdentity();
//	}

	public static void setTextureSizeMode(int i){
		_iTextureSizeMode = i;
	}

	public static int getTextureSizeMode(){
		return _iTextureSizeMode;
	}

	public static void setTextureMaxSize(int i){
		_iTextureMaxSize = i;
	}

	public static int getTextureMaxSize(){
		return _iTextureMaxSize;
	}

	public boolean isSplit(){
		return _bIsSplit;
	}

	public void setZ(float z){
		_iZ = z;
		if(_oSplitTextures != null){
			int a =_oSplitTextures.length;
			if(a > 0){
				int b =_oSplitTextures[0].length;
				for(int x = 0; x < a; x++)
				for(int y = 0; y < b; y++)
					if(_oSplitTextures[x][y] != null)
						_oSplitTextures[x][y].setZ(z);
			}
		}
	}

	public String getName(){
		return _sName;
	}

	public void setName(String s){
		_sName = s;
	}

	/**
	 * Calls unload() for every existing Texture which has had it's load() method called
	 */
	public static void unloadAll(){
		Iterator<Texture> o = _oLoadedTextures.iterator();
		while(o.hasNext()){
			Texture t = o.next();
			o.remove();
			t.unload();
		}
	}

	public static int getBoundTexture(){
		return _iBoundTexture;
	}

	public static void setBoundTexture(int i){
		_iBoundTexture = i;
	}
}
