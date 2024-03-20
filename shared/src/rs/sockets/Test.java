package rs.sockets;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test extends javax.swing.JFrame {
	private final HttpMultiDownloader oDownloader = new HttpMultiDownloader();
	private Thread oDownloaderThread;
	private HttpDownload.HttpDownloadListener listener = new HttpDownload.HttpDownloadListener() {

		public void dataReceived(HttpDownload o) {
			System.out.println(o.localPath() + " " + o.progress());
		}

		public void finished(HttpDownload o) {
			System.out.println(o.localPath() + " finished");
		}

		public void connected(HttpDownload o) {
			System.out.println(o.localPath() + " connected");
		}

	};

	public Test() {
        initComponents();
		oDownloader.addHttpDownloadListener(listener);
		oDownloaderThread = new Thread(oDownloader);
		oDownloaderThread.setDaemon(true);
		oDownloaderThread.start();
		btnDownload.setEnabled(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnDownload = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnDownload.setText("Download");
        btnDownload.setEnabled(false);
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });

        btnStop.setText("Stop");
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDownload)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnStop)
                .addContainerGap(246, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDownload)
                    .addComponent(btnStop))
                .addContainerGap(266, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void btnDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadActionPerformed
		try {
			oDownloader.add(new HttpDownload("E:/background-a1.png", new URL("http://www.smashtek.com/background-a1.png")));
			oDownloader.add(new HttpDownload("E:/Cursor.Mario.Normal.png", new URL("http://www.smashtek.com/Cursor.Mario.Normal.png")));
			oDownloader.add(new HttpDownload("E:/soundbank-deluxe.gm", new URL("http://www.smashtek.com/soundbank-deluxe.gm")));
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
		
	}//GEN-LAST:event_btnDownloadActionPerformed

	private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
		oDownloader.stop();
	}//GEN-LAST:event_btnStopActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Test().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDownload;
    private javax.swing.JButton btnStop;
    // End of variables declaration//GEN-END:variables

}
