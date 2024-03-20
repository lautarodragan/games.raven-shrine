package rs.server.maker.maps;

import rs.resources.GameMapReference;
import java.util.Vector;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class FileOpen extends javax.swing.JDialog {
	public boolean Canceled = false;
	private Vector<GameMapReference> _oMapReferences = new Vector<GameMapReference>();
	
    public FileOpen(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
		_oMapReferences = rs.resources.Resourcebase.readMapReferences();
		lst.setModel(new ListModel(){
			public int getSize() {
				return _oMapReferences.size();
			}

			public Object getElementAt(int index) {
				return _oMapReferences.get(index);
			}

			public void addListDataListener(ListDataListener l) {
				//throw new UnsupportedOperationException("Not supported yet.");
			}

			public void removeListDataListener(ListDataListener l) {
				//throw new UnsupportedOperationException("Not supported yet.");
			}

		});
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scr = new javax.swing.JScrollPane();
        lst = new javax.swing.JList();
        btnAccept1 = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lst.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scr.setViewportView(lst);

        btnAccept1.setText("Accept");
        btnAccept1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccept1_ActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancel_ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scr, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnAccept1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)
                        .addGap(29, 29, 29))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scr, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAccept1)
                    .addComponent(btnCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void btnAccept1_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccept1_ActionPerformed
		dispose();
}//GEN-LAST:event_btnAccept1_ActionPerformed

	private void btnCancel_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancel_ActionPerformed
		this.Canceled = true;
		dispose();
}//GEN-LAST:event_btnCancel_ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnAccept1;
    private javax.swing.JButton btnCancel;
    public javax.swing.JList lst;
    private javax.swing.JScrollPane scr;
    // End of variables declaration//GEN-END:variables

}
