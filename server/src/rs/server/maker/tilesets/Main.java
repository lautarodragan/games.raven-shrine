package rs.server.maker.tilesets;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import rs.resources.Paths;
import rs.resources.Resourcebase;
import rs.resources.Tileset;
import rs.server.maker.DashBox;
import rs.server.maker.ImageChooser;

public class Main extends javax.swing.JFrame {
	private List<Tileset> _oTilesets;
	private final ArrayList<DashBox> _oDashBoxes = new ArrayList<DashBox>();
	private Tileset _oSelectedTileset;

    public Main() {
		super();
		
        initComponents();
		_oDashBoxes.add(txtAutotile1);
		_oDashBoxes.add(txtAutotile2);
		_oDashBoxes.add(txtAutotile3);
		_oDashBoxes.add(txtAutotile4);
		_oDashBoxes.add(txtAutotile5);
		_oDashBoxes.add(txtAutotile6);
		_oDashBoxes.add(txtAutotile7);
		_oDashBoxes.add(txtAutotile8);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		setLocation(env.getCenterPoint().x - getWidth() / 2, env.getCenterPoint().y - getHeight() / 2);


    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpEditModes = new javax.swing.ButtonGroup();
        pnl = new rs.server.maker.JPanelB();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstTilesets = new javax.swing.JList();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        pnlData = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblImage = new javax.swing.JLabel();
        txtImage = new rs.server.maker.DashBox();
        lblAutotiles = new javax.swing.JLabel();
        txtAutotile1 = new rs.server.maker.DashBox();
        txtAutotile2 = new rs.server.maker.DashBox();
        txtAutotile3 = new rs.server.maker.DashBox();
        txtAutotile4 = new rs.server.maker.DashBox();
        txtAutotile5 = new rs.server.maker.DashBox();
        txtAutotile6 = new rs.server.maker.DashBox();
        txtAutotile7 = new rs.server.maker.DashBox();
        txtAutotile8 = new rs.server.maker.DashBox();
        optPassability = new javax.swing.JToggleButton();
        optPriority = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        optCheckeredBackground = new javax.swing.JCheckBox();
        optGrid = new javax.swing.JCheckBox();
        optPassabilityDir = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnlTileset = new rs.server.maker.tilesets.PanelTileset();
        btnAccept = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tilesets");
        setMinimumSize(new java.awt.Dimension(800, 620));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        pnl.setBackground(new java.awt.Color(255, 255, 255));

        lstTilesets.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstTilesets.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstTilesetsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstTilesets);

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnRemove.setText("Remove");

        pnlData.setOpaque(false);

        lblName.setText("Name");

        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNameKeyReleased(evt);
            }
        });

        lblImage.setText("Image");

        txtImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImageActionPerformed(evt);
            }
        });

        lblAutotiles.setText("Autotiles");

        txtAutotile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAutotileActionPerformed(evt);
            }
        });

        txtAutotile2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAutotileActionPerformed(evt);
            }
        });

        txtAutotile3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAutotileActionPerformed(evt);
            }
        });

        txtAutotile4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAutotileActionPerformed(evt);
            }
        });

        txtAutotile5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAutotileActionPerformed(evt);
            }
        });

        txtAutotile6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAutotileActionPerformed(evt);
            }
        });

        txtAutotile7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAutotileActionPerformed(evt);
            }
        });

        txtAutotile8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAutotileActionPerformed(evt);
            }
        });

        grpEditModes.add(optPassability);
        optPassability.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/Cursor_Circle_Opaque.png"))); // NOI18N
        optPassability.setToolTipText("Passability");
        optPassability.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optPassabilityActionPerformed(evt);
            }
        });

        grpEditModes.add(optPriority);
        optPriority.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/Cursor_Star1_Opaque.png"))); // NOI18N
        optPriority.setToolTipText("Priority");
        optPriority.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optPriorityActionPerformed(evt);
            }
        });

        jLabel1.setText("Edit Mode");

        jLabel2.setText("Options");

        optCheckeredBackground.setText("Checkered Background");
        optCheckeredBackground.setOpaque(false);
        optCheckeredBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optCheckeredBackgroundActionPerformed(evt);
            }
        });

        optGrid.setSelected(true);
        optGrid.setText("Grid");
        optGrid.setOpaque(false);
        optGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optGridActionPerformed(evt);
            }
        });

        grpEditModes.add(optPassabilityDir);
        optPassabilityDir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/Passability4.png"))); // NOI18N
        optPassabilityDir.setToolTipText("Directional passability");
        optPassabilityDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optPassabilityDirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDataLayout = new javax.swing.GroupLayout(pnlData);
        pnlData.setLayout(pnlDataLayout);
        pnlDataLayout.setHorizontalGroup(
            pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataLayout.createSequentialGroup()
                .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblName)
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                    .addComponent(lblImage)
                    .addComponent(lblAutotiles)
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtAutotile1, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtAutotile2, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtAutotile3, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtAutotile4, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtAutotile5, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtAutotile6, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtAutotile7, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtAutotile8, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtImage, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                    .addComponent(jLabel1)
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(optPassability, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(optPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(optPassabilityDir, 0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel2)
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(optGrid)
                            .addComponent(optCheckeredBackground))))
                .addContainerGap())
        );

        pnlDataLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {optPassability, optPassabilityDir, optPriority});

        pnlDataLayout.setVerticalGroup(
            pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataLayout.createSequentialGroup()
                .addComponent(lblName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(lblAutotiles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAutotile1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAutotile2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAutotile3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAutotile4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAutotile5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAutotile6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAutotile7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAutotile8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optPassability, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addComponent(optPassabilityDir)
                        .addGap(18, 18, 18))
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addComponent(optPriority, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                        .addGap(18, 18, 18)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optCheckeredBackground)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optGrid)
                .addGap(27, 27, 27))
        );

        pnlDataLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {optPassability, optPassabilityDir, optPriority});

        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        pnlTileset.setDrawCheckeredBackground(false);
        pnlTileset.setDrawGrid(true);
        pnlTileset.setMode(rs.server.maker.tilesets.PanelTileset.Mode.EditPassability);

        javax.swing.GroupLayout pnlTilesetLayout = new javax.swing.GroupLayout(pnlTileset);
        pnlTileset.setLayout(pnlTilesetLayout);
        pnlTilesetLayout.setHorizontalGroup(
            pnlTilesetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 321, Short.MAX_VALUE)
        );
        pnlTilesetLayout.setVerticalGroup(
            pnlTilesetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 518, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(pnlTileset);

        javax.swing.GroupLayout pnlLayout = new javax.swing.GroupLayout(pnl);
        pnl.setLayout(pnlLayout);
        pnlLayout.setHorizontalGroup(
            pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAdd, btnRemove});

        pnlLayout.setVerticalGroup(
            pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                    .addComponent(pnlData, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd)
                            .addComponent(btnRemove))))
                .addContainerGap())
        );

        btnAccept.setText("Accept");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
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
                        .addComponent(pnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnAccept))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void txtImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImageActionPerformed
		String s = ImageChooser.show(this, Paths.PATH_GRAPHICS_TILESETS, "Choose Tileset", txtImage.getValue());
		if(s == null)
			return;
		
		_oSelectedTileset.setFilename(s);
		txtImage.setValue(s);
		pnlTileset.loadImage(Paths.PATH_GRAPHICS_TILESETS + "/" + s, true);
	}//GEN-LAST:event_txtImageActionPerformed

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		dispose();
	}//GEN-LAST:event_btnCancelActionPerformed

	private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
		readTilesets();
	}//GEN-LAST:event_formWindowOpened

	private void lstTilesetsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstTilesetsValueChanged
		Tileset oTileset;

		try{
			oTileset = _oTilesets.get(lstTilesets.getSelectedIndex());
		}catch(IndexOutOfBoundsException ex){
			_oSelectedTileset = null;
			pnlData.setEnabled(false);
			pnlTileset.setEnabled(false);
			return;
		}

		txtName.setText(oTileset.getName());
		txtImage.setValue(oTileset.getFilename());
		
		for(int i = 0; i < oTileset.autotiles().length; i++){
			_oDashBoxes.get(i).setValue(oTileset.autotiles()[i]);
		}

		pnlTileset.setTileset(oTileset);

		if(oTileset.getFilename().length() < 1 || !new File(Paths.PATH_GRAPHICS_TILESETS + "/" + oTileset.getFilename()).exists()){
			pnlTileset.setBlank(true);
		}else{
			pnlTileset.loadImage(Paths.PATH_GRAPHICS_TILESETS + "/" + oTileset.getFilename(), false);
		}

		_oSelectedTileset = oTileset;

		pnlData.setEnabled(true);
		pnlTileset.setEnabled(true);
	}//GEN-LAST:event_lstTilesetsValueChanged

	private void optPassabilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optPassabilityActionPerformed
		pnlTileset.setMode(PanelTileset.Mode.EditPassability);
	}//GEN-LAST:event_optPassabilityActionPerformed

	private void optPriorityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optPriorityActionPerformed
		pnlTileset.setMode(PanelTileset.Mode.EditPriority);
	}//GEN-LAST:event_optPriorityActionPerformed

	private void optCheckeredBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optCheckeredBackgroundActionPerformed
		pnlTileset.setDrawCheckeredBackground(optCheckeredBackground.isSelected());
	}//GEN-LAST:event_optCheckeredBackgroundActionPerformed

	private void optGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optGridActionPerformed
		pnlTileset.setDrawGrid(optGrid.isSelected());
	}//GEN-LAST:event_optGridActionPerformed

	private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
		writeTilesets();
		dispose();
	}//GEN-LAST:event_btnAcceptActionPerformed

	private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
		newTileset();
	}//GEN-LAST:event_btnAddActionPerformed

	private void txtAutotileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAutotileActionPerformed
		DashBox oSource = (DashBox)(evt.getSource());

		Tileset oTileset = _oTilesets.get(lstTilesets.getSelectedIndex());
		String sFile = ImageChooser.show(this, Paths.PATH_GRAPHICS_AUTOTILES, "Choose Autotile", oSource.getValue());

		oSource.setValue(sFile);

		oTileset.autotiles()[_oDashBoxes.indexOf(oSource)] = sFile;
		pnlTileset.updateAutotileImages();
	}//GEN-LAST:event_txtAutotileActionPerformed

	private void txtNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyPressed
		if(_oSelectedTileset == null)
			return;

		_oSelectedTileset.setName(txtName.getText());
		lstTilesets.repaint();
	}//GEN-LAST:event_txtNameKeyPressed

	private void txtNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyReleased
		if(_oSelectedTileset == null)
			return;

		_oSelectedTileset.setName(txtName.getText());
		lstTilesets.repaint();
	}//GEN-LAST:event_txtNameKeyReleased

	private void optPassabilityDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optPassabilityDirActionPerformed
		pnlTileset.setMode(PanelTileset.Mode.EditPassabilityWays);
	}//GEN-LAST:event_optPassabilityDirActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
				try{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}catch (Exception Ex){
					System.err.println("Could not set LookAndFeel");
				}
                Main dialog = new Main();
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

	public void readTilesets(){
		_oTilesets = rs.resources.Resourcebase.readTilesets();

		if(_oTilesets == null || lstTilesets.getModel().getSize() < 1){
			_oTilesets = new ArrayList<Tileset>();
			newTileset();
		}

		lstTilesets.setListData(_oTilesets.toArray());
		lstTilesets.setSelectedIndex(0);

	}

	public void newTileset(){
		Tileset oTileset = new Tileset("New tileset");
		oTileset.setId(Resourcebase.getFreeTilesetId(_oTilesets));
		_oTilesets.add(oTileset);
		lstTilesets.setListData(_oTilesets.toArray());
		lstTilesets.setSelectedIndex(lstTilesets.getModel().getSize() - 1);
	}

	public void writeTilesets(){
		rs.resources.Resourcebase.writeTilesets(_oTilesets);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRemove;
    private javax.swing.ButtonGroup grpEditModes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAutotiles;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblName;
    private javax.swing.JList lstTilesets;
    private javax.swing.JCheckBox optCheckeredBackground;
    private javax.swing.JCheckBox optGrid;
    private javax.swing.JToggleButton optPassability;
    private javax.swing.JToggleButton optPassabilityDir;
    private javax.swing.JToggleButton optPriority;
    private rs.server.maker.JPanelB pnl;
    private javax.swing.JPanel pnlData;
    private rs.server.maker.tilesets.PanelTileset pnlTileset;
    private rs.server.maker.DashBox txtAutotile1;
    private rs.server.maker.DashBox txtAutotile2;
    private rs.server.maker.DashBox txtAutotile3;
    private rs.server.maker.DashBox txtAutotile4;
    private rs.server.maker.DashBox txtAutotile5;
    private rs.server.maker.DashBox txtAutotile6;
    private rs.server.maker.DashBox txtAutotile7;
    private rs.server.maker.DashBox txtAutotile8;
    private rs.server.maker.DashBox txtImage;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables

}
