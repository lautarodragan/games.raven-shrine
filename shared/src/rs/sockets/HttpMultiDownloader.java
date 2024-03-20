package rs.sockets;

import java.util.LinkedHashSet;
import java.util.concurrent.LinkedBlockingQueue;
import rs.sockets.HttpDownload.HttpDownloadListener;

/**
 * HttpMultiDownloader provides a means to
 * queue and process multiple downloads
 * @author lainmaster
 */
public class HttpMultiDownloader implements Runnable{
	private final LinkedBlockingQueue _oDownloads = new LinkedBlockingQueue();
	private boolean _bStop;
	private final LinkedHashSet<HttpDownloadListener> _oListeners = new LinkedHashSet<HttpDownloadListener>();
	private HttpDownload _oCurrentDownload;

	/**
	 * Adds a download to the end of the queue. 
	 * @param o the download
	 */
	public void add(HttpDownload o){
		_oDownloads.add(o);
	}

	/**
	 * Stops this HttpMultiDownloader from processing any more Downloads in the queue
	 * and exits the <code>run()</code> method. Will not stop an ongoing download.
	 */
	public void stopAfter(){
		_bStop = true;
		_oDownloads.add(new Object());
	}

	/**
	 * Calls <code>stopAfter()</code> on this HttpMultiDownloader, and
	 * <code>stop()</code> on the active download, if there is one.
	 */
	public void stop(){
		stopAfter();
		if(_oCurrentDownload != null){
			_oCurrentDownload.stop();
		}
	}

	/**
	 * Starts the HttpMultiDownloader. This method enters a loop that will consume
	 * downloads in the queue one by one, calling their <code>run()</code> method, until
	 * <code>stop()</code> has is called in this HttpMultiDownloader
	 */
	public void run() {
		while(true){
			try {
				Object o = _oDownloads.take();
				if(_bStop)
					break;
				if(o instanceof HttpDownload){
					synchronized(_oListeners){
						((HttpDownload)o)._oListeners.addAll(_oListeners);
					}

					_oCurrentDownload = (HttpDownload)o;
					_oCurrentDownload.run();
					_oCurrentDownload = null;
				}else{
					break;
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void addHttpDownloadListener(HttpDownloadListener o){
		synchronized(_oListeners){
			_oListeners.add(o);
		}
		if(_oCurrentDownload != null)
			_oCurrentDownload.addHttpDownloadListener(o);
	}

	public void removeHttpDownloadListener(HttpDownloadListener o){
		synchronized(_oListeners){
			_oListeners.remove(o);
		}
		if(_oCurrentDownload != null)
			_oCurrentDownload.removeHttpDownloadListener(o);
	}

	/**
	 * Returns the active download if there is one, or null otherwize
	 * @return the active download if there is one, or null otherwize
	 */
	public HttpDownload getActiveDownload(){
		return _oCurrentDownload;
	}

}
