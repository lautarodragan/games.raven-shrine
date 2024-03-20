package rs.sockets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * HttpDownload provides a means to download a file located in an usual web server.
 * It also provides methods to know the download progress, size, etc.
 * 
 * @author lainmaster
 */
public class HttpDownload implements Runnable{
	public static interface HttpDownloadListener{
		/**
		 * Informs the listener that new data has been read from the remove file and written to the local file
		 */
		public void dataReceived(HttpDownload o);
		/**
		 * Informs the listener that the transfer has finished successfully
		 */
		public void finished(HttpDownload o);
		/**
		 * Informs the listener that the connection has been stablished successfully
		 */
		public void connected(HttpDownload o);
	}

	private String _sLocalPath;
	private URL _oUrl;
	private long _iProgress = 0;
	private long _iLength;
	private long _iStartTime;
	private double _iTimeLength;
	private boolean _bIsRunning;
	private boolean _bStop;
	
	protected final LinkedHashSet<HttpDownloadListener> _oListeners = new LinkedHashSet<HttpDownloadListener>();
	
	public HttpDownload(String sLocal, URL oRemove){
		_sLocalPath = sLocal;
		_oUrl = oRemove;
	}

	public void run() {
		if(_bIsRunning){
			throw new RuntimeException("Another thread is already using this HttpDownload");
		}else{
			_bIsRunning = true;
			_bStop = false;
		}

		Iterator<HttpDownloadListener> it;
		DataInputStream oIn = null;

		try {
			_iStartTime = System.nanoTime();
			URLConnection oUrlConn = _oUrl.openConnection();
			oUrlConn.connect();

			_iLength = oUrlConn.getContentLength();

			_iTimeLength = ((System.nanoTime() - _iStartTime) / 1000000);

			synchronized(_oListeners){
				it = _oListeners.iterator();
				while (it.hasNext()) {
					HttpDownloadListener o = it.next();
					o.connected(this);
				}
			}

			oIn = new DataInputStream(new BufferedInputStream(oUrlConn.getInputStream()));
			DataOutputStream oOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(_sLocalPath)));
			byte[] oBytes = new byte[1024 * 32];

			_iStartTime = System.nanoTime();

			while(!_bStop){
				int iRead = oIn.read(oBytes);
				if(iRead == -1){
					break;
				}
				_iProgress += iRead;
				oOut.write(oBytes, 0, iRead);

				_iTimeLength = ((System.nanoTime() - _iStartTime) / 1000000);

				synchronized(_oListeners){
					it = _oListeners.iterator();
					while (it.hasNext()) {
						HttpDownloadListener o = it.next();
						o.dataReceived(this);
					}
				}
				
			}

			oOut.close();
			oIn.close();

			if(!_bStop){
				synchronized(_oListeners){
					it = _oListeners.iterator();
					while (it.hasNext()) {
						HttpDownloadListener o = it.next();
						o.finished(this);
					}
				}
			}

		} catch (MalformedURLException ex) {

		} catch (IOException ex) {

		}

		_bIsRunning = false;

	}

	/**
	 * Stops the download.
	 */
	public void stop(){
		_bStop = true;
	}

	/**
	 * Adds the HttpDownloadListener to the listen list, if not already there
	 * @param o the HttpDownloadListener
	 */
	public void addHttpDownloadListener(HttpDownloadListener o){
		synchronized(_oListeners){
			_oListeners.add(o);
		}
	}

	/**
	 * Removes the HttpDownloadListener from the listen list, if found.
	 * @param o the HttpDownloadListener
	 */
	public void removeHttpDownloadListener(HttpDownloadListener o){
		synchronized(_oListeners){
			_oListeners.remove(o);
		}
	}

	/**
	 * Returns the total amount of bytes read from the remove file and written to the local file
	 * @return
	 */
	public long progress(){
		return _iProgress;
	}

	/**
	 * Returns the file length
	 * @return the file length
	 */
	public long length(){
		return _iLength;
	}

	/**
	 * Returns the time transcurred from the start of the download to
	 * the last data received.
	 * @return the time transcurred from the start of the download to
	 * the last data received.
	 */
	public double timeTaken(){
		return _iTimeLength;
	}

	/**
	 * Returns the path to the local file downloaded data is written to
	 * @return the path to the local file downloaded data is written to
	 */
	public String localPath(){
		return _sLocalPath;
	}

	/**
	 * Returns true if this HttpDownload's <code>run()</code> method has been called
	 * and hasn't yet returned, false otherwize
	 * @return true if this HttpDownload's <code>run()</code> method has been called
	 * and hasn't yet returned, false otherwize
	 */
	public boolean isRunning(){
		return _bIsRunning;
	}
	
}
