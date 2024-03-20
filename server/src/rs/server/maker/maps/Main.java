package rs.server.maker.maps;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.resources.GameMap;
import rs.resources.GameMapReference;
import rs.resources.Tileset;
import rs.resources.Resourcebase;
import rs.resources.Paths;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import rs.RavenShrineConstants;

public class Main extends javax.swing.JFrame implements RavenShrineConstants {

	private Vector<GameMapReference> _oMapReferences = new Vector<GameMapReference>();
	private GameMapReference _oMapReference;
	private GameMap _oMap;
	private List<Tileset> _oTilesets;
	private Tileset _oTileset;
	private rs.server.maker.tilesets.Main _oWindowTilesets;

	public Main() {
		initComponents();
		BufferedImage o;
		try {
			InputStream is = getClass().getResourceAsStream("treasure-map16.png");
			o = ImageIO.read(is);
			setIconImage(o);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}


		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		setMaximizedBounds(env.getMaximumWindowBounds());
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

		jPanel1.setBorder(new CompoundBorder(UIManager.getDefaults().getBorder("TextField.border"), new EmptyBorder(1, 1, 1, 1)));
	}

	public static void main(String[] sArgs) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Main o = new Main();
		o.setVisible(true);
	}

	public void initializeComponentsForMap() {
		pnlTileset.setTileset(_oTileset);
		pnlTileset.loadImage(Paths.PATH_GRAPHICS_TILESETS + "/" + _oTileset.getFilename(), false);
		pnlTileset.updateAutotileImages();

		pnlMap.setTileset(_oTileset);
		pnlMap.setMap(_oMap);
		pnlMap.updateUI();
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpLayers = new javax.swing.ButtonGroup();
        grpEditModes = new javax.swing.ButtonGroup();
        tbr = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        optIncrementVersionOnSave = new javax.swing.JToggleButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnLayer0 = new javax.swing.JToggleButton();
        btnLayer1 = new javax.swing.JToggleButton();
        btnLayer2 = new javax.swing.JToggleButton();
        btnLayerObjects = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnPencilEraser = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnEditModePencil = new javax.swing.JToggleButton();
        btnEditModeRectangle = new javax.swing.JToggleButton();
        btnEditModeFloodfill = new javax.swing.JToggleButton();
        btnEditModeSelection = new javax.swing.JToggleButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnOptionsDimOtherLayers = new javax.swing.JToggleButton();
        btnOptionsGrid = new javax.swing.JToggleButton();
        pnlCenter = new javax.swing.JPanel();
        pnlTilesetScroll = new javax.swing.JScrollPane();
        pnlTileset = new rs.server.maker.tilesets.PanelTileset();
        pnlStatusBar = new javax.swing.JPanel();
        lblStatusMouse = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        pnlMap = new rs.server.maker.maps.PanelMap();
        mnu = new javax.swing.JMenuBar();
        mnu_File = new javax.swing.JMenu();
        mnu_File_New = new javax.swing.JMenuItem();
        mnu_File_Open = new javax.swing.JMenuItem();
        mnu_File_Separator1 = new javax.swing.JSeparator();
        mnu_File_Save = new javax.swing.JMenuItem();
        mnu_File_Separator2 = new javax.swing.JSeparator();
        mnu_File_ShiftMap = new javax.swing.JMenuItem();
        mnu_File_Properties = new javax.swing.JMenuItem();
        mnu_File_Separator3 = new javax.swing.JSeparator();
        mnu_File_Exit = new javax.swing.JMenuItem();
        mnu_Tools = new javax.swing.JMenu();
        mnu_Tools_Tilesets = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Map Editor");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        tbr.setFloatable(false);
        tbr.setRollover(true);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/filesave.png"))); // NOI18N
        btnSave.setEnabled(false);
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbr.add(btnSave);

        optIncrementVersionOnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/filesave-verinc.png"))); // NOI18N
        optIncrementVersionOnSave.setSelected(true);
        optIncrementVersionOnSave.setFocusable(false);
        optIncrementVersionOnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        optIncrementVersionOnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbr.add(optIncrementVersionOnSave);
        tbr.add(jSeparator4);

        grpLayers.add(btnLayer0);
        btnLayer0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/Button_Layer_Bottom.png"))); // NOI18N
        btnLayer0.setSelected(true);
        btnLayer0.setFocusable(false);
        btnLayer0.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLayer0.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLayer0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayer0ActionPerformed(evt);
            }
        });
        tbr.add(btnLayer0);

        grpLayers.add(btnLayer1);
        btnLayer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/Button_Layer_Middle.png"))); // NOI18N
        btnLayer1.setFocusable(false);
        btnLayer1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLayer1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLayer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayer1_ActionPerformed(evt);
            }
        });
        tbr.add(btnLayer1);

        grpLayers.add(btnLayer2);
        btnLayer2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/Button_Layer_Top.png"))); // NOI18N
        btnLayer2.setFocusable(false);
        btnLayer2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLayer2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLayer2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayer2_ActionPerformed(evt);
            }
        });
        tbr.add(btnLayer2);

        grpLayers.add(btnLayerObjects);
        btnLayerObjects.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/Button_Layer_Objects.png"))); // NOI18N
        btnLayerObjects.setFocusable(false);
        btnLayerObjects.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLayerObjects.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLayerObjects.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayerObjectsActionPerformed(evt);
            }
        });
        tbr.add(btnLayerObjects);
        tbr.add(jSeparator1);

        btnPencilEraser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/eraser.png"))); // NOI18N
        btnPencilEraser.setFocusable(false);
        btnPencilEraser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPencilEraser_ActionPerformed(evt);
            }
        });
        tbr.add(btnPencilEraser);
        tbr.add(jSeparator2);

        grpEditModes.add(btnEditModePencil);
        btnEditModePencil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/Edit_Pencil.png"))); // NOI18N
        btnEditModePencil.setSelected(true);
        btnEditModePencil.setFocusable(false);
        btnEditModePencil.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditModePencil.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditModePencil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditModePencil_ActionPerformed(evt);
            }
        });
        tbr.add(btnEditModePencil);

        grpEditModes.add(btnEditModeRectangle);
        btnEditModeRectangle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/Edit_Rectangle.png"))); // NOI18N
        btnEditModeRectangle.setFocusable(false);
        btnEditModeRectangle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditModeRectangle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditModeRectangle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditModeRectangle_ActionPerformed(evt);
            }
        });
        tbr.add(btnEditModeRectangle);

        grpEditModes.add(btnEditModeFloodfill);
        btnEditModeFloodfill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/Edit_Floodfill.png"))); // NOI18N
        btnEditModeFloodfill.setFocusable(false);
        btnEditModeFloodfill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditModeFloodfill.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditModeFloodfill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditModeFloodfill_ActionPerformed(evt);
            }
        });
        tbr.add(btnEditModeFloodfill);

        grpEditModes.add(btnEditModeSelection);
        btnEditModeSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/Edit_Selection.png"))); // NOI18N
        btnEditModeSelection.setFocusable(false);
        btnEditModeSelection.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditModeSelection.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditModeSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditModeSelection_ActionPerformed(evt);
            }
        });
        tbr.add(btnEditModeSelection);
        tbr.add(jSeparator3);

        btnOptionsDimOtherLayers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/layers-dim.png"))); // NOI18N
        btnOptionsDimOtherLayers.setSelected(true);
        btnOptionsDimOtherLayers.setFocusable(false);
        btnOptionsDimOtherLayers.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOptionsDimOtherLayers.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOptionsDimOtherLayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionsDimOtherLayers_ActionPerformed(evt);
            }
        });
        tbr.add(btnOptionsDimOtherLayers);

        btnOptionsGrid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/grid.png"))); // NOI18N
        btnOptionsGrid.setSelected(true);
        btnOptionsGrid.setFocusable(false);
        btnOptionsGrid.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOptionsGrid.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOptionsGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionsGridActionPerformed(evt);
            }
        });
        tbr.add(btnOptionsGrid);

        getContentPane().add(tbr, java.awt.BorderLayout.NORTH);

        pnlTilesetScroll.setViewportBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        pnlTileset.setDrawCheckeredBackground(false);
        pnlTileset.setDrawGrid(true);
        pnlTileset.setMode(rs.server.maker.tilesets.PanelTileset.Mode.SelectTiles);
        pnlTileset.addPanelTilesetListener(new rs.server.maker.tilesets.PanelTileset.PanelTilesetListener() {
            public void selectionChanged(rs.server.maker.tilesets.PanelTileset.PanelTilesetEvent evt) {
                pnlTilesetSelectionChanged(evt);
            }
        });

        javax.swing.GroupLayout pnlTilesetLayout = new javax.swing.GroupLayout(pnlTileset);
        pnlTileset.setLayout(pnlTilesetLayout);
        pnlTilesetLayout.setHorizontalGroup(
            pnlTilesetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 292, Short.MAX_VALUE)
        );
        pnlTilesetLayout.setVerticalGroup(
            pnlTilesetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 516, Short.MAX_VALUE)
        );

        pnlTilesetScroll.setViewportView(pnlTileset);

        pnlStatusBar.setLayout(new javax.swing.BoxLayout(pnlStatusBar, javax.swing.BoxLayout.LINE_AXIS));

        lblStatusMouse.setText("Mouse Coordinates");
        pnlStatusBar.add(lblStatusMouse);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(""), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        pnlMap.setBackground(new java.awt.Color(102, 102, 102));
        pnlMap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlMapMousePressed(evt);
            }
        });
        pnlMap.addPanelMapListener(new rs.server.maker.maps.PanelMap.PanelMapListener() {
            public void mapChanged(rs.server.maker.maps.PanelMap.PanelMapEvent evt) {
                pnlMapMapChanged(evt);
            }
        });
        pnlMap.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlMapMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pnlMapMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout pnlMapLayout = new javax.swing.GroupLayout(pnlMap);
        pnlMap.setLayout(pnlMapLayout);
        pnlMapLayout.setHorizontalGroup(
            pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 412, Short.MAX_VALUE)
        );
        pnlMapLayout.setVerticalGroup(
            pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
        );

        jPanel1.add(pnlMap);

        javax.swing.GroupLayout pnlCenterLayout = new javax.swing.GroupLayout(pnlCenter);
        pnlCenter.setLayout(pnlCenterLayout);
        pnlCenterLayout.setHorizontalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCenterLayout.createSequentialGroup()
                        .addComponent(pnlTilesetScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 412, Short.MAX_VALUE))
                    .addComponent(pnlStatusBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCenterLayout.setVerticalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTilesetScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(pnlCenter, java.awt.BorderLayout.CENTER);

        mnu_File.setText("File");

        mnu_File_New.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mnu_File_New.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/filenew.png"))); // NOI18N
        mnu_File_New.setText("New");
        mnu_File_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_New_ActionPerformed(evt);
            }
        });
        mnu_File.add(mnu_File_New);

        mnu_File_Open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mnu_File_Open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/fileopen.png"))); // NOI18N
        mnu_File_Open.setText("Open");
        mnu_File_Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_Open_ActionPerformed(evt);
            }
        });
        mnu_File.add(mnu_File_Open);
        mnu_File.add(mnu_File_Separator1);

        mnu_File_Save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mnu_File_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/server/maker/icons/filesave.png"))); // NOI18N
        mnu_File_Save.setText("Save");
        mnu_File_Save.setEnabled(false);
        mnu_File_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_Save_ActionPerformed(evt);
            }
        });
        mnu_File_Save.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                mnu_File_SavePropertyChange(evt);
            }
        });
        mnu_File.add(mnu_File_Save);
        mnu_File.add(mnu_File_Separator2);

        mnu_File_ShiftMap.setText("Shift Map");
        mnu_File_ShiftMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_ShiftMapActionPerformed(evt);
            }
        });
        mnu_File.add(mnu_File_ShiftMap);

        mnu_File_Properties.setText("Properties");
        mnu_File_Properties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_Properties_ActionPerformed(evt);
            }
        });
        mnu_File.add(mnu_File_Properties);
        mnu_File.add(mnu_File_Separator3);

        mnu_File_Exit.setText("Exit");
        mnu_File_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_Exit_ActionPerformed(evt);
            }
        });
        mnu_File.add(mnu_File_Exit);

        mnu.add(mnu_File);

        mnu_Tools.setText("Tools");

        mnu_Tools_Tilesets.setText("Tilesets");
        mnu_Tools_Tilesets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_Tools_Tilesets_ActionPerformed(evt);
            }
        });
        mnu_Tools.add(mnu_Tools_Tilesets);

        mnu.add(mnu_Tools);

        setJMenuBar(mnu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void mnu_File_Exit_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_Exit_ActionPerformed
		this.dispose();
}//GEN-LAST:event_mnu_File_Exit_ActionPerformed

	private void mnu_File_Open_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_Open_ActionPerformed
		FileOpen oWindow = new FileOpen(this, true);
		oWindow.setVisible(true);
		if (oWindow.Canceled) {
			return;
		}

		_oMapReference = _oMapReferences.get(oWindow.lst.getSelectedIndex());
		_oMap = Resourcebase.readMap(_oMapReference);
		_oTileset = _oTilesets.get(_oMap.getTileset());
		_oMap.setId(_oMapReference.getId()); // The ID isn't saved in the map but is used when saving the map
		initializeComponentsForMap();

		setTitle("Map Editor | " + _oMap.getId() + ": " + _oMap.getName());
}//GEN-LAST:event_mnu_File_Open_ActionPerformed

	private void mnu_File_New_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_New_ActionPerformed
		FileNew oWindow = new FileNew(this, true, _oMapReferences);
		oWindow.setVisible(true);
		if (oWindow.Canceled) {
			return;
		}

		_oTileset = (Tileset) oWindow.lstTilesets.getSelectedItem();

		_oMap = new GameMap((Integer) oWindow.txtWidth.getValue(), (Integer) oWindow.txtHeight.getValue(), (Integer) oWindow.txtLayers.getValue());
		_oMap.setId(Resourcebase.getFreeMapId(_oMapReferences));
		_oMap.setName(oWindow.txtName.getText());
		_oMap.setTileset(_oTileset.getId());
		_oMap.setPrivateVersion((Integer) oWindow.txtPrivateVersion.getValue());
		_oMap.setPublicVersion((Integer) oWindow.txtPublicVersion.getValue());

		_oMapReference = new GameMapReference(_oMap.getName(), _oMap.getId());
		_oMapReferences.add(_oMapReference);

		initializeComponentsForMap();

		setTitle("Map Editor | " + _oMap.getId() + ": " + _oMap.getName());
	}//GEN-LAST:event_mnu_File_New_ActionPerformed

	private void btnPencilEraser_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPencilEraser_ActionPerformed
		pnlMap.setPaintingTile(RS_NULL_TILE);
		pnlTileset.clearSelection();
}//GEN-LAST:event_btnPencilEraser_ActionPerformed

	private void mnu_File_Save_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_Save_ActionPerformed
		mnu_File_Save.setEnabled(false);
		if (optIncrementVersionOnSave.isSelected()) {
			_oMap.setPublicVersion(_oMap.getPublicVersion() + 1);
			_oMap.setPrivateVersion(_oMap.getPrivateVersion() + 1);
		}
		Resourcebase.writeMapReferences(_oMapReferences);
		Resourcebase.writeMap(_oMap);
	}//GEN-LAST:event_mnu_File_Save_ActionPerformed

	private void btnLayer0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayer0ActionPerformed
		pnlMap.setMode(PanelMap.Mode.Draw);
		pnlMap.setSelectedLayer(0);
		pnlMap.updateUI();
}//GEN-LAST:event_btnLayer0ActionPerformed

	private void btnLayer1_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayer1_ActionPerformed
		pnlMap.setMode(PanelMap.Mode.Draw);
		pnlMap.setSelectedLayer(1);
		pnlMap.updateUI();
	}//GEN-LAST:event_btnLayer1_ActionPerformed

	private void btnLayer2_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayer2_ActionPerformed
		pnlMap.setMode(PanelMap.Mode.Draw);
		pnlMap.setSelectedLayer(2);
		pnlMap.updateUI();
	}//GEN-LAST:event_btnLayer2_ActionPerformed

	private void btnOptionsDimOtherLayers_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOptionsDimOtherLayers_ActionPerformed
//		if(pnlMap != null){
		pnlMap.setDimOtherLayers(btnOptionsDimOtherLayers.isSelected());
		pnlMap.updateUI();
//		}
	}//GEN-LAST:event_btnOptionsDimOtherLayers_ActionPerformed

	private void mnu_File_Properties_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_Properties_ActionPerformed
		FileNew oWindow = new FileNew(this, true, _oMapReferences);

		oWindow.txtName.setText(_oMap.getName());
		oWindow.lstTilesets.setSelectedIndex(_oTileset.getId());
		oWindow.txtWidth.setValue(_oMap.getWidth());
		oWindow.txtHeight.setValue(_oMap.getHeight());
		oWindow.txtLayers.setValue(_oMap.getLayerCount());
		oWindow.txtPrivateVersion.setValue(_oMap.getPrivateVersion());
		oWindow.txtPublicVersion.setValue(_oMap.getPublicVersion());
		oWindow.lstNeighbourNorth.setSelectedItem(Resourcebase.getMapReferenceById(_oMapReferences, _oMap.getMapNorth()));
		oWindow.lstNeighbourSouth.setSelectedItem(Resourcebase.getMapReferenceById(_oMapReferences, _oMap.getMapSouth()));
		oWindow.lstNeighbourWest.setSelectedItem(Resourcebase.getMapReferenceById(_oMapReferences, _oMap.getMapWest()));
		oWindow.lstNeighbourEast.setSelectedItem(Resourcebase.getMapReferenceById(_oMapReferences, _oMap.getMapEast()));

		oWindow.setVisible(true);
		if (oWindow.Canceled) {
			return;
		}

		_oTileset = (Tileset) oWindow.lstTilesets.getSelectedItem();

		_oMap.setName(oWindow.txtName.getText());
		_oMap.setTileset(_oTileset.getId());
		_oMap.setPrivateVersion((Integer) oWindow.txtPrivateVersion.getValue());
		_oMap.setPublicVersion((Integer) oWindow.txtPublicVersion.getValue());
		_oMap.setMapNorth(((GameMapReference) oWindow.lstNeighbourNorth.getSelectedItem()).getId());
		_oMap.setMapSouth(((GameMapReference) oWindow.lstNeighbourSouth.getSelectedItem()).getId());
		_oMap.setMapWest(((GameMapReference) oWindow.lstNeighbourWest.getSelectedItem()).getId());
		_oMap.setMapEast(((GameMapReference) oWindow.lstNeighbourEast.getSelectedItem()).getId());

		int[][][] iOldData = _oMap.getMapData();
		int iOldW = _oMap.getWidth();
		int iOldH = _oMap.getHeight();
		int iOldZ = _oMap.getLayerCount();
		_oMap.setSize((Integer) oWindow.txtWidth.getValue(), (Integer) oWindow.txtHeight.getValue(), _oMap.getLayerCount());
		int[][][] iNewData = _oMap.getMapData();

		for (int x = 0; x < Math.min(_oMap.getWidth(), iOldW); x++) {
			for (int y = 0; y < Math.min(_oMap.getHeight(), iOldH); y++) {
				for (int z = 0; z < Math.min(_oMap.getLayerCount(), iOldZ); z++) {
					iNewData[z][x][y] = iOldData[z][x][y];
				}
			}
		}

		_oMapReference.setId(_oMap.getId());
		_oMapReference.setName(_oMap.getName());

		initializeComponentsForMap();

		setTitle("Map Editor | " + _oMap.getId() + ": " + _oMap.getName());

		mnu_File_Save.setEnabled(true);
	}//GEN-LAST:event_mnu_File_Properties_ActionPerformed

	private void btnEditModePencil_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditModePencil_ActionPerformed
//		if(pnlMap != null){
		pnlMap.setMode(PanelMap.Mode.Draw);
		pnlMap.setDrawMode(PanelMap.DrawMode.Pencil);
//		}
	}//GEN-LAST:event_btnEditModePencil_ActionPerformed

	private void btnEditModeRectangle_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditModeRectangle_ActionPerformed
//		if(pnlMap != null){
		pnlMap.setMode(PanelMap.Mode.Draw);
		pnlMap.setDrawMode(PanelMap.DrawMode.Rectangle);
//		}
	}//GEN-LAST:event_btnEditModeRectangle_ActionPerformed

	private void btnEditModeFloodfill_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditModeFloodfill_ActionPerformed
//		if(pnlMap != null){
		pnlMap.setMode(PanelMap.Mode.Draw);
		pnlMap.setDrawMode(PanelMap.DrawMode.FloodFill);
//		}
	}//GEN-LAST:event_btnEditModeFloodfill_ActionPerformed

	private void btnEditModeSelection_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditModeSelection_ActionPerformed
//		if(pnlMap != null){
		pnlMap.setMode(PanelMap.Mode.Select);
//		}
	}//GEN-LAST:event_btnEditModeSelection_ActionPerformed

	private void mnu_Tools_Tilesets_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_Tools_Tilesets_ActionPerformed
		if (_oWindowTilesets == null) {
			_oWindowTilesets = new rs.server.maker.tilesets.Main();
		}
		if (!_oWindowTilesets.isVisible()) {
			setEnabled(false);
			_oWindowTilesets.setVisible(true);
			_oWindowTilesets.toFront();
			_oWindowTilesets.addWindowListener(new java.awt.event.WindowListener() {

				public void windowOpened(WindowEvent e) {
				}

				public void windowClosing(WindowEvent e) {
				}

				public void windowClosed(WindowEvent e) {
					_oTilesets = rs.resources.Resourcebase.readTilesets();
					_oTileset = _oTilesets.get(_oMap.getTileset());
					initializeComponentsForMap();
					setEnabled(true);
					toFront();
				}

				public void windowIconified(WindowEvent e) {
				}

				public void windowDeiconified(WindowEvent e) {
				}

				public void windowActivated(WindowEvent e) {
				}

				public void windowDeactivated(WindowEvent e) {
				}
			});
		}
	}//GEN-LAST:event_mnu_Tools_Tilesets_ActionPerformed

	private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
		_oTilesets = rs.resources.Resourcebase.readTilesets();

		pnlMap.setMode(PanelMap.Mode.Draw);

		_oMapReferences = Resourcebase.readMapReferences();
		if (_oMapReferences == null) {
			_oMapReferences = new Vector<GameMapReference>();
		}

		if (_oMapReferences.size() > 0) {
			_oMapReference = _oMapReferences.get(0);
			_oMap = Resourcebase.readMap(_oMapReference);
			if (_oMap != null) {
				_oTileset = _oTilesets.get(_oMap.getTileset());
				initializeComponentsForMap();
			}
		} else {
			_oMapReference = null;
			_oMap = null;

		}

	}//GEN-LAST:event_formWindowOpened

	private void pnlMapMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMapMouseDragged
		pnlMapMouseMoved(null);
	}//GEN-LAST:event_pnlMapMouseDragged

	private void pnlMapMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMapMouseMoved
		if (pnlMap == null) {
			return;
		}
		String sText;

		if (pnlMap.getDrawMode() == pnlMap.getDrawMode().Pencil) {
			sText = pnlMap.getCursorX() + ", " + pnlMap.getCursorY();
		} else if (pnlMap.getDrawMode() == pnlMap.getDrawMode().Rectangle) {
			sText = pnlMap.getDrawRectangle().x + ", " + pnlMap.getDrawRectangle().y + " -> " + pnlMap.getDrawRectangle().width + ", " + pnlMap.getDrawRectangle().height;
		} else {
			sText = "";
		}

		lblStatusMouse.setText(sText);
	}//GEN-LAST:event_pnlMapMouseMoved

	private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
		if (_oWindowTilesets != null && _oWindowTilesets.isVisible()) {
			_oWindowTilesets.toFront();
		}
	}//GEN-LAST:event_formWindowActivated

	private void pnlMapMapChanged(rs.server.maker.maps.PanelMap.PanelMapEvent evt) {//GEN-FIRST:event_pnlMapMapChanged
		mnu_File_Save.setEnabled(true);
	}//GEN-LAST:event_pnlMapMapChanged

	private void pnlTilesetSelectionChanged(rs.server.maker.tilesets.PanelTileset.PanelTilesetEvent evt) {//GEN-FIRST:event_pnlTilesetSelectionChanged
		if (pnlMap != null) {
			pnlMap.setPaintingTiles(evt.selection());
		}
	}//GEN-LAST:event_pnlTilesetSelectionChanged

	private void mnu_File_ShiftMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_ShiftMapActionPerformed
		final WindowShiftMap wnd = new WindowShiftMap();
		wnd.addWindowListener(new WindowListener() {

			public void windowOpened(WindowEvent e) {
			}

			public void windowClosing(WindowEvent e) {
			}

			public void windowClosed(WindowEvent e) {
				if (wnd.isCancelled()) {
					return;
				}
				if (wnd.getHorizontalValue() == 0 && wnd.getVerticalValue() == 0) {
					return;
				}
				for (int i = 0; i < _oMap.getLayerCount(); i++) {
					int[][] iLayer = _oMap.getMapLayer(i);
					for (int x = 0; x < _oMap.getWidth(); x++) {
						for (int y = 0; y < _oMap.getHeight(); y++) {
							int a = x, b = y;
							if (wnd.getHorizontalValue() >= 0) {
								a = _oMap.getWidth() - x - 1;
							}
							if (wnd.getVerticalValue() >= 0) {
								b = _oMap.getHeight() - y - 1;
							}
							if (_oMap.isInsideMap(a - wnd.getHorizontalValue(), b - wnd.getVerticalValue())) {
								iLayer[a][b] = iLayer[a - wnd.getHorizontalValue()][b - wnd.getVerticalValue()];
							} else {
								iLayer[a][b] = i == 0 ? 1 : RS_NULL_TILE;
							}
						}
					}
				}

				pnlMap.repaint();
				mnu_File_Save.setEnabled(true);
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}
		});
		wnd.setVisible(true);
	}//GEN-LAST:event_mnu_File_ShiftMapActionPerformed

	private void mnu_File_SavePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_mnu_File_SavePropertyChange
//		System.out.println(evt.getPropertyName());
		if (evt.getPropertyName().equals("enabled")) {
			btnSave.setEnabled((Boolean) evt.getNewValue());
		}
	}//GEN-LAST:event_mnu_File_SavePropertyChange

	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
		mnu_File_Save_ActionPerformed(evt);
	}//GEN-LAST:event_btnSaveActionPerformed

	private void btnLayerObjectsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayerObjectsActionPerformed
		pnlMap.setMode(PanelMap.Mode.Objects);
	}//GEN-LAST:event_btnLayerObjectsActionPerformed

	private void btnOptionsGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOptionsGridActionPerformed
		pnlMap.setDrawGrid(btnOptionsGrid.isSelected());
	}//GEN-LAST:event_btnOptionsGridActionPerformed

	private void pnlMapMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMapMousePressed
		if (evt.getClickCount() == 2) {
			System.out.println(pnlMap.getSelectedTileX() + ", " + pnlMap.getSelectedTileX());
			pnlMap.addObject(new java.awt.Point(pnlMap.getSelectedTileX(), pnlMap.getSelectedTileY()));
		}
	}//GEN-LAST:event_pnlMapMousePressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnEditModeFloodfill;
    private javax.swing.JToggleButton btnEditModePencil;
    private javax.swing.JToggleButton btnEditModeRectangle;
    private javax.swing.JToggleButton btnEditModeSelection;
    private javax.swing.JToggleButton btnLayer0;
    private javax.swing.JToggleButton btnLayer1;
    private javax.swing.JToggleButton btnLayer2;
    private javax.swing.JToggleButton btnLayerObjects;
    private javax.swing.JToggleButton btnOptionsDimOtherLayers;
    private javax.swing.JToggleButton btnOptionsGrid;
    private javax.swing.JButton btnPencilEraser;
    private javax.swing.JButton btnSave;
    private javax.swing.ButtonGroup grpEditModes;
    private javax.swing.ButtonGroup grpLayers;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JLabel lblStatusMouse;
    private javax.swing.JMenuBar mnu;
    private javax.swing.JMenu mnu_File;
    private javax.swing.JMenuItem mnu_File_Exit;
    private javax.swing.JMenuItem mnu_File_New;
    private javax.swing.JMenuItem mnu_File_Open;
    private javax.swing.JMenuItem mnu_File_Properties;
    private javax.swing.JMenuItem mnu_File_Save;
    private javax.swing.JSeparator mnu_File_Separator1;
    private javax.swing.JSeparator mnu_File_Separator2;
    private javax.swing.JSeparator mnu_File_Separator3;
    private javax.swing.JMenuItem mnu_File_ShiftMap;
    private javax.swing.JMenu mnu_Tools;
    private javax.swing.JMenuItem mnu_Tools_Tilesets;
    private javax.swing.JToggleButton optIncrementVersionOnSave;
    private javax.swing.JPanel pnlCenter;
    private rs.server.maker.maps.PanelMap pnlMap;
    private javax.swing.JPanel pnlStatusBar;
    private rs.server.maker.tilesets.PanelTileset pnlTileset;
    private javax.swing.JScrollPane pnlTilesetScroll;
    private javax.swing.JToolBar tbr;
    // End of variables declaration//GEN-END:variables
}
