package rs.server.maker.maps;

import java.util.List;
import rs.resources.Tileset;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import rs.resources.GameMapReference;

public class FileNew extends javax.swing.JDialog {
	public boolean Canceled = false;
	private List<Tileset> _oTilesets;
	private Vector<GameMapReference> _oMapReferences;

	public static class GameMapsComboBoxModel implements ComboBoxModel{
		private GameMapReference _oSelectedItem;
		private Vector<GameMapReference> _oMapReferences;
		private static final String _sNoMapString = "<no map>";
		private static final GameMapReference _oNoMap = new GameMapReference(_sNoMapString, -1);
		
		public GameMapsComboBoxModel(Vector<GameMapReference> oMapReferences){
			_oMapReferences = oMapReferences;
		}

		public void setSelectedItem(Object o) {
			if(o instanceof GameMapReference)
				_oSelectedItem = (GameMapReference)o;
			else
				_oSelectedItem = null;
		}

		public Object getSelectedItem() {
			if(_oSelectedItem != null)
				return _oSelectedItem;
			else
				return _oNoMap;
		}

		public int getSize() {
			return _oMapReferences.size() + 1;
		}

		public Object getElementAt(int index) {
			if(index > 0)
				return _oMapReferences.get(index - 1);
			else
				return _oNoMap;
		}

		public void addListDataListener(ListDataListener l) {
			//throw new UnsupportedOperationException("Not supported yet.");
		}

		public void removeListDataListener(ListDataListener l) {
			//throw new UnsupportedOperationException("Not supported yet.");
		}
	}

    public FileNew(java.awt.Frame parent, boolean modal, Vector<GameMapReference> oMapReferences) {
        super(parent, modal);
        initComponents();

		_oTilesets = rs.resources.Resourcebase.readTilesets();
		if(_oTilesets.size() > 0){
			lstTilesets.setModel(new ComboBoxModel() {
				private Object _oSelectedItem;
				public void setSelectedItem(Object anItem) {
					_oSelectedItem = anItem;
				}

				public Object getSelectedItem() {
					return _oSelectedItem;
				}

				public int getSize() {
					return _oTilesets.size();
				}

				public Object getElementAt(int index) {
					return _oTilesets.get(index);
				}

				public void addListDataListener(ListDataListener l) {
					//throw new UnsupportedOperationException("Not supported yet.");
				}

				public void removeListDataListener(ListDataListener l) {
					//throw new UnsupportedOperationException("Not supported yet.");
				}
			});
			lstTilesets.setSelectedIndex(0);
		}else{
			btnAccept.setEnabled(false);

		}

		_oMapReferences = oMapReferences;
		if(_oMapReferences.size() > 0){
			lstNeighbourNorth.setModel(new GameMapsComboBoxModel(_oMapReferences));
			lstNeighbourSouth.setModel(new GameMapsComboBoxModel(_oMapReferences));
			lstNeighbourWest.setModel(new GameMapsComboBoxModel(_oMapReferences));
			lstNeighbourEast.setModel(new GameMapsComboBoxModel(_oMapReferences));
			lstNeighbourNorth.setSelectedIndex(0);
			lstNeighbourSouth.setSelectedIndex(0);
			lstNeighbourWest.setSelectedIndex(0);
			lstNeighbourEast.setSelectedIndex(0);
		}else{
//			btnAccept.setEnabled(false);

		}
		
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblName = new javax.swing.JLabel();
        lblDimensions = new javax.swing.JLabel();
        txtWidth = new javax.swing.JSpinner();
        txtHeight = new javax.swing.JSpinner();
        txtLayers = new javax.swing.JSpinner();
        lblLayers = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnAccept = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblTileset = new javax.swing.JLabel();
        lstTilesets = new javax.swing.JComboBox();
        lblPrivateVersion = new javax.swing.JLabel();
        txtPrivateVersion = new javax.swing.JSpinner();
        lblPublicVersion = new javax.swing.JLabel();
        txtPublicVersion = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        lstNeighbourNorth = new javax.swing.JComboBox();
        lstNeighbourWest = new javax.swing.JComboBox();
        lstNeighbourEast = new javax.swing.JComboBox();
        lstNeighbourSouth = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Map");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(300, 200));
        setModal(true);

        lblName.setText("Name");

        lblDimensions.setText("Dimensions");

        txtWidth.setModel(new javax.swing.SpinnerNumberModel(30, 20, 500, 1));

        txtHeight.setModel(new javax.swing.SpinnerNumberModel(30, 20, 500, 1));

        txtLayers.setModel(new javax.swing.SpinnerNumberModel(3, 1, 10, 1));

        lblLayers.setText("Layers");

        btnAccept.setText("Accept");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccept_ActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancel_ActionPerformed(evt);
            }
        });

        lblTileset.setText("Tileset");

        lstTilesets.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No tilesets available. " }));

        lblPrivateVersion.setText("Private Version");

        txtPrivateVersion.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        lblPublicVersion.setText("Public Version");

        txtPublicVersion.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel1.setText("Neighbour Maps");

        lstNeighbourNorth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "North" }));

        lstNeighbourWest.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "West" }));

        lstNeighbourEast.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "East" }));

        lstNeighbourSouth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "South" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblName)
                    .addComponent(lblDimensions)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txtLayers, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblLayers))
                .addGap(34, 34, 34))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lstTilesets, 0, 289, Short.MAX_VALUE))
                    .addComponent(lblTileset))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblPrivateVersion))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(txtPrivateVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPublicVersion)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(txtPublicVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(73, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(232, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lstNeighbourWest, 0, 127, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lstNeighbourEast, 0, 128, Short.MAX_VALUE)
                .addGap(30, 30, 30))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(158, Short.MAX_VALUE)
                        .addComponent(btnAccept))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lstNeighbourSouth, 0, 137, Short.MAX_VALUE)
                            .addComponent(lstNeighbourNorth, 0, 137, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDimensions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblLayers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLayers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTileset)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lstTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPrivateVersion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrivateVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPublicVersion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPublicVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lstNeighbourNorth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lstNeighbourWest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lstNeighbourEast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lstNeighbourSouth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAccept)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void btnAccept_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccept_ActionPerformed
		dispose();
}//GEN-LAST:event_btnAccept_ActionPerformed

	private void btnCancel_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancel_ActionPerformed
		this.Canceled = true;
		dispose();
}//GEN-LAST:event_btnCancel_ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblDimensions;
    private javax.swing.JLabel lblLayers;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPrivateVersion;
    private javax.swing.JLabel lblPublicVersion;
    private javax.swing.JLabel lblTileset;
    public javax.swing.JComboBox lstNeighbourEast;
    public javax.swing.JComboBox lstNeighbourNorth;
    public javax.swing.JComboBox lstNeighbourSouth;
    public javax.swing.JComboBox lstNeighbourWest;
    public javax.swing.JComboBox lstTilesets;
    public javax.swing.JSpinner txtHeight;
    public javax.swing.JSpinner txtLayers;
    public javax.swing.JTextField txtName;
    public javax.swing.JSpinner txtPrivateVersion;
    public javax.swing.JSpinner txtPublicVersion;
    public javax.swing.JSpinner txtWidth;
    // End of variables declaration//GEN-END:variables

}
