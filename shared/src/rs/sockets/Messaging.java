package rs.sockets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class Messaging {
	public static final short MSG_NULL = 0;
	public static final short MSG_SERVER_SHUTTING_DOWN = 1;
	public static final short MSG_LOGIN = 4;
	public static final short MSG_LOGOUT = 6;
	public static final short MSG_PLAYER_LOGGED_OUT = 7;
	public static final short MSG_PUBLICCHAT = 8;
	public static final short MSG_PLAYER_LOGGED_IN = 9;
	public static final short MSG_REQUESTFILE = 10;
	public static final short MSG_SENDFILE = 11;
	public static final short MSG_SENDFILE_APPEND = 12;
	public static final short MSG_INPUT_MOVE = 13;
	public static final short MSG_PLAYER_MOVE = 14;
	public static final short MSG_VERSION_MAP = 15;
	public static final short MSG_UPDATE_POSITION = 16;
	public static final short MSG_PLAYER_SPRITE_FILENAME = 17;
	public static final short MSG_PING_32 = 18;

	public static class SendFile{
		public String FileName;
		public byte[] FileData;
		public long FileLength;
		public SendFile(){}
		public SendFile(String sFile, long iFileLength, byte[] iData){
			this.FileName = sFile;
			this.FileData = iData;
			this.FileLength = iFileLength;
		}

	}

	/**
	 * Retrieve byte array from buffer
	 * @param o
	 * @return
	 */
	public static byte[] getBytes(ByteBuffer o){
		int i = o.getInt();
		byte[] iBytes = new byte[i];
		o.get(iBytes);
		return iBytes;
	}

	/**
	 * Put bytes from source array into destination buffer
	 * @param o destination buffer
	 * @param s source array
	 */
	public static void putBytes(ByteBuffer o, byte[] s){
		o.putInt(s.length);
		o.put(s);
	}
	
	/**
	 * Copies iLength bytes from source array to destination buffer
	 * @param o destination buffer
	 * @param s source array
	 * @param iLength amount of bytes to copy
	 */
	public static void putBytes(ByteBuffer o, byte[] s, int iLength){
		o.putInt(iLength);
		o.put(s, 0, iLength);
	}

//	public static byte[] getBytes(ByteBuffer o, int iCount){
//		byte[] iBytes = new byte[iCount];
//		o.get(iBytes);
//		return iBytes;
//	}

	public static String getString(ByteBuffer o){
		return new String(getBytes(o));
	}

	public static void putString(ByteBuffer o, String s){
		putBytes(o, s.getBytes());
	}

	public static ByteBuffer stringEncode(String s){
		ByteBuffer oEncodedChat;
		Charset oCharset = Charset.forName("utf8");
		CharsetEncoder encoder = oCharset.newEncoder();

		try {
			oEncodedChat = encoder.encode(CharBuffer.wrap(s));
			return oEncodedChat;

		} catch (CharacterCodingException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String stringDecode(ByteBuffer oByteBuffer){
		CharBuffer oDecodedChat;
		Charset charset = Charset.forName("utf8");
		CharsetDecoder decoder = charset.newDecoder();
		
		try {
			oDecodedChat = decoder.decode(oByteBuffer);
			return oDecodedChat.toString();

		} catch (CharacterCodingException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static ByteBuffer allocateByteBuffer(int iCapacity){
		ByteBuffer oByteBuffer = ByteBuffer.allocate(iCapacity + 4);
		oByteBuffer.putInt(iCapacity);
		return oByteBuffer;
	}

	public static ByteBuffer publicChat(int iId, String sMessage){
		ByteBuffer oStringBuffer = stringEncode(sMessage);
		ByteBuffer oByteBuffer = allocateByteBuffer(oStringBuffer.limit() + 10);
		oByteBuffer.putShort(MSG_PUBLICCHAT);
		oByteBuffer.putInt(iId);
		oByteBuffer.putInt(oStringBuffer.limit());
		oByteBuffer.put(oStringBuffer);
		oByteBuffer.flip();
		return oByteBuffer;
	}
	
	public static ByteBuffer publicChat(String sMessage){
		ByteBuffer oStringBuffer = stringEncode(sMessage);
		ByteBuffer oByteBuffer = allocateByteBuffer(oStringBuffer.limit() + 6);
		oByteBuffer.putShort(MSG_PUBLICCHAT);
		oByteBuffer.putInt(oStringBuffer.limit());
		oByteBuffer.put(oStringBuffer);
		oByteBuffer.flip();
		return oByteBuffer;
	}
	
	public static ByteBuffer playerLoggedOut(int iId){
		ByteBuffer oByteBuffer = allocateByteBuffer(6);
		oByteBuffer.putShort(MSG_PLAYER_LOGGED_OUT);
		oByteBuffer.putInt(iId);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer logIn(String sUsername, String sPassword){
		ByteBuffer oByteBuffer = allocateByteBuffer(sUsername.getBytes().length + sPassword.getBytes().length + 10);
		oByteBuffer.putShort(MSG_LOGIN);
		putString(oByteBuffer, sUsername);
		putString(oByteBuffer, sPassword);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer playerLoggedIn(int iId, String sUsername, float x, float y, int iMap){
		ByteBuffer oByteBuffer = allocateByteBuffer(sUsername.getBytes().length + 10 + 12);
		oByteBuffer.putShort(MSG_PLAYER_LOGGED_IN);
		oByteBuffer.putInt(iId);
		putString(oByteBuffer, sUsername);
		oByteBuffer.putFloat(x);
		oByteBuffer.putFloat(y);
		oByteBuffer.putInt(iMap);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer loginResponse(byte i, int iId, float x, float y, int iMap){
		ByteBuffer oByteBuffer = allocateByteBuffer(7+4*2+4);
		oByteBuffer.putShort(MSG_LOGIN);
		oByteBuffer.put(i);
		oByteBuffer.putInt(iId);
		oByteBuffer.putFloat(x);
		oByteBuffer.putFloat(y);
		oByteBuffer.putInt(iMap);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer logOut(){
		ByteBuffer oByteBuffer = allocateByteBuffer(2);
		oByteBuffer.putShort(MSG_LOGOUT);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer requestFile(String sFile, long iOffset){
		ByteBuffer oByteBuffer = allocateByteBuffer(sFile.getBytes().length + 14);
		oByteBuffer.putShort(MSG_REQUESTFILE);
		putString(oByteBuffer, sFile);
		oByteBuffer.putLong(iOffset);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer sendFile(String sFile, byte[] iData, int iDataLength, long iFileLength){
		ByteBuffer oByteBuffer = allocateByteBuffer(sFile.getBytes().length + iDataLength + 18);
		oByteBuffer.putShort(MSG_SENDFILE);
		putString(oByteBuffer, sFile);
		oByteBuffer.putLong(iFileLength);
		putBytes(oByteBuffer, iData, iDataLength);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer sendFileAppend(String sFile, byte[] iData, int iLength){
		ByteBuffer oByteBuffer = allocateByteBuffer(sFile.getBytes().length + iLength + 10);
		oByteBuffer.putShort(MSG_SENDFILE_APPEND);
		putString(oByteBuffer, sFile);
		putBytes(oByteBuffer, iData, iLength);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer versionMap(int iMap){
		ByteBuffer oByteBuffer = allocateByteBuffer(6);
		oByteBuffer.putShort(MSG_VERSION_MAP);
		oByteBuffer.putInt(iMap);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer versionMap(int iMap, int iVersion){
		ByteBuffer oByteBuffer = allocateByteBuffer(10);
		oByteBuffer.putShort(MSG_VERSION_MAP);
		oByteBuffer.putInt(iMap);
		oByteBuffer.putInt(iVersion);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer inputMove(byte iDirection, byte iFlag){
		ByteBuffer oByteBuffer = allocateByteBuffer(4);
		oByteBuffer.putShort(MSG_INPUT_MOVE);
		oByteBuffer.put(iDirection);
		oByteBuffer.put(iFlag);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer mapObjectMove(int iId, byte iDirection, byte iFlag){
		ByteBuffer oByteBuffer = allocateByteBuffer(8);
		oByteBuffer.putShort(MSG_PLAYER_MOVE);
		oByteBuffer.putInt(iId);
		oByteBuffer.put(iDirection);
		oByteBuffer.put(iFlag);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer updatePlayerPosition(int iId, float x, float y){
		ByteBuffer oByteBuffer = allocateByteBuffer(14);
		oByteBuffer.putShort(MSG_UPDATE_POSITION);
		oByteBuffer.putInt(iId);
		oByteBuffer.putFloat(x);
		oByteBuffer.putFloat(y);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer playerSpriteFilename(int iId, String sSpriteFilename){
		ByteBuffer oByteBuffer = allocateByteBuffer(10 + sSpriteFilename.getBytes().length);
		oByteBuffer.putShort(MSG_PLAYER_SPRITE_FILENAME);
		oByteBuffer.putInt(iId);
		putString(oByteBuffer, sSpriteFilename);
		oByteBuffer.flip();
		return oByteBuffer;
	}

	public static ByteBuffer ping32(boolean bResponse){
		ByteBuffer oByteBuffer = allocateByteBuffer(2 + 32);
		oByteBuffer.putShort(MSG_PING_32);
		byte[] oBytes = new byte[28];
		oBytes[0] = (byte) (bResponse ? 1 : 0);
		putBytes(oByteBuffer, oBytes);
//		oByteBuffer.limi
		oByteBuffer.flip();
		return oByteBuffer;
	}
}
