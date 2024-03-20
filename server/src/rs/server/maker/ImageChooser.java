package rs.server.maker;

import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ImageChooser extends javax.swing.JDialog {
	private String _sDirectory;
	private String _sFile;
	private boolean _bCancelled = false;
	private JLabel lblImage;
	
    public ImageChooser(Window parent, boolean modal, String sDirectory) {
        super(parent, ModalityType.APPLICATION_MODAL);
        initComponents();
		setLocation(parent.getX() + parent.getWidth() / 2 - getWidth() / 2, parent.getY() + parent.getHeight() / 2 - getHeight() / 2);

		File oDirectory = new File(sDirectory);
		if(!oDirectory.exists()){
			throw new IllegalArgumentException("Directory doesn't exist");
		}
		lstFiles.setListData(oDirectory.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith("png") || name.endsWith("jpg");
			}
		}));
		
		_sDirectory = sDirectory;
    }

	public String getSelectedFile(){
		return _sFile;
	}

	public boolean getCancelled(){
		return _bCancelled;
	}
    
	public void setDefaultSelection(String sFile){
		for(int i = 0; i < lstFiles.getModel().getSize(); i++){
			if(lstFiles.getModel().getElementAt(i).toString().equalsIgnoreCase(sFile)){
				lstFiles.setSelectedIndex(i);
				lstFiles.ensureIndexIsVisible(i);
			}
		}
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFilesScroll = new javax.swing.JScrollPane();
        lstFiles = new javax.swing.JList();
        pnlImageScroll = new javax.swing.JScrollPane();
        btnCancel = new javax.swing.JButton();
        btnAccept = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lstFiles.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstFiles.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstFiles_ValueChanged(evt);
            }
        });
        pnlFilesScroll.setViewportView(lstFiles);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancel_ActionPerformed(evt);
            }
        });

        btnAccept.setText("Accept");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccept_ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlFilesScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlImageScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(222, 222, 222)
                        .addComponent(btnAccept)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)
                        .addGap(30, 30, 30))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlFilesScroll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addComponent(pnlImageScroll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnAccept))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void btnAccept_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccept_ActionPerformed
		_bCancelled = false;
		dispose();
	}//GEN-LAST:event_btnAccept_ActionPerformed

	private void btnCancel_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancel_ActionPerformed
		_bCancelled = true;
		dispose();
	}//GEN-LAST:event_btnCancel_ActionPerformed

	private void lstFiles_ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstFiles_ValueChanged
		_sFile = lstFiles.getSelectedValue().toString();
		if(lblImage == null)
			lblImage = new JLabel();

		lblImage.setIcon(new ImageIcon(_sDirectory + "/" + _sFile));
		lblImage.setHorizontalAlignment(SwingConstants.LEFT);
		lblImage.setVerticalAlignment(SwingConstants.TOP);
		pnlImageScroll.setViewportView(lblImage);
	}//GEN-LAST:event_lstFiles_ValueChanged

	private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
//		lstFiles.setSelectedIndex(((DefaultListModel)(lstFiles.getModel())).getSize() - 1);
//		lstFiles.ensureIndexIsVisible(((DefaultListModel)(lstFiles.getModel())).getSize() - 1);
	}//GEN-LAST:event_formWindowOpened

	private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		_bCancelled = true;
	}//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnCancel;
    private javax.swing.JList lstFiles;
    private javax.swing.JScrollPane pnlFilesScroll;
    private javax.swing.JScrollPane pnlImageScroll;
    // End of variables declaration//GEN-END:variables

	public static String show(Window parent, String directory, String title, String defaultSelection){
		ImageChooser o = new ImageChooser(parent, true, directory);
		o.setTitle("Choose Tileset");
		o.setDefaultSelection(defaultSelection);
		o.setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
		o.setVisible(true);

		if(o.getCancelled())
			return null;

		return o.getSelectedFile();
	}

}
