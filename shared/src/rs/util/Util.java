package rs.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Util {

	/**
	 * Returns size in biggest unit, as long as there is more than one of that unit.
	 * For example, if <code>iBytes &gt; 1 KiB &amp;&amp; iBytes &lt; 1 MiB</code>, it will return size in KiBs
	 * @param iBytes size in bytes
	 * @return size in biggest unit, as long as there is more than one of that unit.
	 */
	public static String getSizeString(double iBytes){
		String[] sUnits = new String[]{"B", "KiB", "MiB", "GiB", "TiB"};
		String sResponse;
		int iUnit = 0;

		if(iBytes < 1024){
			return iBytes + " B";
		}

		while(iBytes >= 1024 && iUnit < 5){
			iBytes /= 1024;
			iUnit++;
		}

		sResponse = floor(iBytes, 2) + " " + sUnits[iUnit];

		return sResponse;
	}

	public static double floor(double iNumber, int iDigits){
		return Math.floor(iNumber * Math.pow(10, iDigits)) / Math.pow(10, iDigits);
	}

	public static byte[] objectToBytes(Object o){
		ByteArrayOutputStream oObjectAsBytes = new ByteArrayOutputStream();
		ObjectOutputStream oObjectOutputStream;

		try {
			oObjectOutputStream = new ObjectOutputStream(oObjectAsBytes);
			oObjectOutputStream.writeObject(o);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return oObjectAsBytes.toByteArray();
	}

	public static Object bytesToObject(byte[] oInput) throws IOException, ClassNotFoundException{
		ObjectInputStream oMapBytes = new ObjectInputStream(new ByteArrayInputStream(oInput));
		return oMapBytes.readObject();

	}

	public static byte[] compress(byte[] oInput){
		Deflater oDeflater = new Deflater();
		oDeflater.setLevel(Deflater.DEFAULT_COMPRESSION);
		oDeflater.setInput(oInput);
		oDeflater.finish();

		ByteArrayOutputStream oByteArrayOutputStream = new ByteArrayOutputStream(oInput.length);

		byte[] buf = new byte[1024];
		while (!oDeflater.finished()) {
			int count = oDeflater.deflate(buf);
			oByteArrayOutputStream.write(buf, 0, count);
		}
		try {
			oByteArrayOutputStream.close();
		} catch (IOException e) {
		}

		return oByteArrayOutputStream.toByteArray();
	}

	public static byte[] compress(Object o){
		return compress(objectToBytes(o));
	}

	public static byte[] decompress(byte[] oInput){
		Inflater oInflater = new Inflater();
		oInflater.setInput(oInput);

		ByteArrayOutputStream oByteArrayOutputStream = new ByteArrayOutputStream(oInput.length);

		byte[] oBuffer = new byte[1024];
		while (!oInflater.finished()) {
			try {
				int iCount = oInflater.inflate(oBuffer);
				oByteArrayOutputStream.write(oBuffer, 0, iCount);
			} catch (DataFormatException e) {
			}
		}
		try {
			oByteArrayOutputStream.close();
		} catch (IOException e) {
		}

		return oByteArrayOutputStream.toByteArray();
	}

}
