package rs.server.sockets;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import rs.server.database.Database;
import rs.sockets.NioServer;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import rs.resources.Paths;
import rs.resources.Player;
import rs.sockets.Messaging;

public class WindowServer extends javax.swing.JFrame implements ServerMessengerListener{
    NioServer _oServerThread;
	Database _oDatabase = new Database();
	private boolean _bMySqlConnected;
	static public final int SERVER_PORT = 50100;
	private List<Player> _oPlayers;
	private rs.server.maker.tilesets.Main _oWindowTilesets;
	private boolean _bIgnoreOneStateChange;

	private ServerMessenger _oServerMessenger;
	
	private long _iPingTime;

	public static String socketChannelToString(SocketChannel o){
		return o.socket().getInetAddress().getHostAddress() + " : " + o.socket().getPort();
	}

	public WindowServer() {
        initComponents();

		lstClients.setModel(new DefaultListModel(){
			@Override
			public Object getElementAt(int index){
				SocketChannel o = (SocketChannel) super.getElementAt(index);
				if(o == null)
					return "null";
				if(o.socket().getInetAddress() == null)
					return "null";
				if(o.socket().getInetAddress().getHostAddress() == null)
					return "null";

				synchronized(_oServerMessenger.getPlayers()){
					Player oPlayer = _oServerMessenger.getPlayers().get(o);
					if(oPlayer == null)
						return socketChannelToString(o);
					else
						return oPlayer.Username;
				}

			}
		});

		lstUsers.setModel(new DefaultListModel(){
				@Override
				public Object getElementAt(int index){
					return _oPlayers.get(index);
				}

				@Override
				public Object get(int index){
					return _oPlayers.get(index).Id + ": " + _oPlayers.get(index).Username;
				}

				@Override
				public int getSize(){
					if(_oPlayers != null)
						return _oPlayers.size();
					else
						return 0;
				}
			});

		KeyListener oKeyListener = new KeyListener(){
			public void keyTyped(KeyEvent e) {
				btnDbCancel.setEnabled(true);
				btnDbSave.setEnabled(true);
			}

			public void keyPressed(KeyEvent e) {
				btnDbCancel.setEnabled(true);
				btnDbSave.setEnabled(true);
			}

			public void keyReleased(KeyEvent e) {
				btnDbCancel.setEnabled(true);
				btnDbSave.setEnabled(true);
			}
		};

		ChangeListener oChangeListener = new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				if(!((JSpinner.DefaultEditor)((JSpinner)e.getSource()).getEditor()).getTextField().isFocusOwner())
					return;
				btnDbCancel.setEnabled(true);
				btnDbSave.setEnabled(true);
			}

		};

		txtDbLoggedIn.addChangeListener(oChangeListener);
		txtDbLocMap.addChangeListener(oChangeListener);
		txtDbLocX.addChangeListener(oChangeListener);
		txtDbLocY.addChangeListener(oChangeListener);
		txtDbPrivileges.addChangeListener(oChangeListener);

		((JSpinner.DefaultEditor)txtDbLoggedIn.getEditor()).getTextField().addKeyListener(oKeyListener);
		((JSpinner.DefaultEditor)txtDbLocMap.getEditor()).getTextField().addKeyListener(oKeyListener);
		((JSpinner.DefaultEditor)txtDbLocX.getEditor()).getTextField().addKeyListener(oKeyListener);
		((JSpinner.DefaultEditor)txtDbLocY.getEditor()).getTextField().addKeyListener(oKeyListener);
		((JSpinner.DefaultEditor)txtDbPrivileges.getEditor()).getTextField().addKeyListener(oKeyListener);
		txtDbIp.addKeyListener(oKeyListener);

		mnu_File_DbOn_ActionPerformed(null);
		mnu_File_TurnOn_ActionPerformed(null);

//		logEvent(System.getProperty("user.home"));
//		logEvent(System.getProperty("java.io.tmpdir"));


    }

	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel(new WindowsLookAndFeel());
		}catch (Exception Ex){
			System.err.println("Could not set LookAndFeel");
		}
		WindowServer o = new WindowServer();
		o.setVisible(true);
	}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tbp = new javax.swing.JTabbedPane();
        pneEvents = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstEvents = new javax.swing.JList();
        pneChat = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstChat = new javax.swing.JList();
        pneDownloads = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstDownloads = new javax.swing.JList();
        pneClients = new javax.swing.JPanel();
        scrClients = new javax.swing.JScrollPane();
        lstClients = new javax.swing.JList();
        pnePlayer = new javax.swing.JPanel();
        btnUpdatePosition = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblPlayerPosition = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnPing = new javax.swing.JButton();
        lblPing = new javax.swing.JLabel();
        btnSetPosition = new javax.swing.JButton();
        txtSetPositionX = new javax.swing.JSpinner();
        txtSetPositionY = new javax.swing.JSpinner();
        pneUsers = new javax.swing.JPanel();
        scrUsers = new javax.swing.JScrollPane();
        lstUsers = new javax.swing.JList();
        pneUser = new javax.swing.JPanel();
        pneUserLblUser = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDbLocMap = new javax.swing.JSpinner();
        txtDbLocX = new javax.swing.JSpinner();
        txtDbLocY = new javax.swing.JSpinner();
        txtDbIp = new javax.swing.JTextField();
        txtDbPrivileges = new javax.swing.JSpinner();
        txtDbLoggedIn = new javax.swing.JSpinner();
        txtDbUser = new javax.swing.JTextField();
        btnDbSave = new javax.swing.JButton();
        btnDbCancel = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtDbSprite = new rs.server.maker.DashBox();
        mnu = new javax.swing.JMenuBar();
        mnu_File = new javax.swing.JMenu();
        mnu_File_TurnOn = new javax.swing.JMenuItem();
        mnu_File_TurnOff = new javax.swing.JMenuItem();
        mnu_File_Separator1 = new javax.swing.JPopupMenu.Separator();
        mnu_File_DbOn = new javax.swing.JMenuItem();
        mnu_File_DbOff = new javax.swing.JMenuItem();
        mnu_File_Separator2 = new javax.swing.JPopupMenu.Separator();
        mnu_File_SimPing = new javax.swing.JCheckBoxMenuItem();
        mnu_File_Separator3 = new javax.swing.JPopupMenu.Separator();
        mnu_File_Exit = new javax.swing.JMenuItem();
        mnu_Editors = new javax.swing.JMenu();
        mnu_Editors_Map = new javax.swing.JMenuItem();
        mnu_Editors_Tilesets = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Server");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindow_Closing(evt);
            }
        });

        lstEvents.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(lstEvents);

        javax.swing.GroupLayout pneEventsLayout = new javax.swing.GroupLayout(pneEvents);
        pneEvents.setLayout(pneEventsLayout);
        pneEventsLayout.setHorizontalGroup(
            pneEventsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pneEventsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                .addContainerGap())
        );
        pneEventsLayout.setVerticalGroup(
            pneEventsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pneEventsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addContainerGap())
        );

        tbp.addTab("Events", pneEvents);

        lstChat.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(lstChat);

        javax.swing.GroupLayout pneChatLayout = new javax.swing.GroupLayout(pneChat);
        pneChat.setLayout(pneChatLayout);
        pneChatLayout.setHorizontalGroup(
            pneChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pneChatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                .addContainerGap())
        );
        pneChatLayout.setVerticalGroup(
            pneChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pneChatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addContainerGap())
        );

        tbp.addTab("Chat", pneChat);

        lstDownloads.setModel(new DefaultListModel());
        jScrollPane3.setViewportView(lstDownloads);

        javax.swing.GroupLayout pneDownloadsLayout = new javax.swing.GroupLayout(pneDownloads);
        pneDownloads.setLayout(pneDownloadsLayout);
        pneDownloadsLayout.setHorizontalGroup(
            pneDownloadsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pneDownloadsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                .addContainerGap())
        );
        pneDownloadsLayout.setVerticalGroup(
            pneDownloadsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pneDownloadsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addContainerGap())
        );

        tbp.addTab("Downloads", pneDownloads);

        lstClients.setModel(new DefaultListModel());
        lstClients.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstClients_ValueChanged(evt);
            }
        });
        scrClients.setViewportView(lstClients);

        pnePlayer.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnUpdatePosition.setText("Update Position");
        btnUpdatePosition.setToolTipText("Sends an \"updatePosition\" message");
        btnUpdatePosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdatePosition_ActionPerformed(evt);
            }
        });

        jLabel1.setText("Position: ");

        lblPlayerPosition.setText("8888, 8888");

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresh_ActionPerformed(evt);
            }
        });

        btnPing.setText("Ping");
        btnPing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPing_ActionPerformed(evt);
            }
        });

        lblPing.setText("0");

        btnSetPosition.setText("Set Position");
        btnSetPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetPositionActionPerformed(evt);
            }
        });

        txtSetPositionX.setModel(new javax.swing.SpinnerNumberModel());

        txtSetPositionY.setModel(new javax.swing.SpinnerNumberModel());

        javax.swing.GroupLayout pnePlayerLayout = new javax.swing.GroupLayout(pnePlayer);
        pnePlayer.setLayout(pnePlayerLayout);
        pnePlayerLayout.setHorizontalGroup(
            pnePlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnePlayerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnePlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnePlayerLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPlayerPosition)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                        .addComponent(btnRefresh))
                    .addGroup(pnePlayerLayout.createSequentialGroup()
                        .addComponent(btnPing)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPing, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnePlayerLayout.createSequentialGroup()
                        .addComponent(btnUpdatePosition)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSetPosition)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSetPositionX, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSetPositionY, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnePlayerLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtSetPositionX, txtSetPositionY});

        pnePlayerLayout.setVerticalGroup(
            pnePlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnePlayerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnePlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblPlayerPosition)
                    .addComponent(btnRefresh))
                .addGap(22, 22, 22)
                .addGroup(pnePlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdatePosition)
                    .addComponent(btnSetPosition)
                    .addComponent(txtSetPositionX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSetPositionY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnePlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPing)
                    .addComponent(lblPing))
                .addContainerGap(193, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pneClientsLayout = new javax.swing.GroupLayout(pneClients);
        pneClients.setLayout(pneClientsLayout);
        pneClientsLayout.setHorizontalGroup(
            pneClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pneClientsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrClients, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnePlayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pneClientsLayout.setVerticalGroup(
            pneClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pneClientsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pneClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnePlayer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrClients, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
                .addContainerGap())
        );

        tbp.addTab("Clients", pneClients);

        lstUsers.setModel(new DefaultListModel());
        lstUsers.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstUsers_ValueChanged(evt);
            }
        });
        scrUsers.setViewportView(lstUsers);

        pneUser.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        pneUserLblUser.setText("Username");

        jLabel2.setText("Logged In?");

        jLabel3.setText("Privileges");

        jLabel4.setText("Last IP");

        jLabel5.setText("Location");

        txtDbLocMap.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        txtDbLocMap.setEnabled(false);

        txtDbLocX.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(8.0f)));
        txtDbLocX.setEnabled(false);

        txtDbLocY.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(8.0f)));
        txtDbLocY.setEnabled(false);

        txtDbIp.setEnabled(false);

        txtDbPrivileges.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        txtDbPrivileges.setEnabled(false);

        txtDbLoggedIn.setModel(new javax.swing.SpinnerListModel(new String[] {"true", "false"}));
        txtDbLoggedIn.setEnabled(false);

        txtDbUser.setEditable(false);

        btnDbSave.setText("Save");
        btnDbSave.setEnabled(false);
        btnDbSave.setPreferredSize(new java.awt.Dimension(70, 23));
        btnDbSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDbSaveActionPerformed(evt);
            }
        });

        btnDbCancel.setText("Cancel");
        btnDbCancel.setEnabled(false);
        btnDbCancel.setPreferredSize(new java.awt.Dimension(70, 23));
        btnDbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDbCancelActionPerformed(evt);
            }
        });

        jLabel6.setText("Sprite");

        txtDbSprite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDbSpriteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pneUserLayout = new javax.swing.GroupLayout(pneUser);
        pneUser.setLayout(pneUserLayout);
        pneUserLayout.setHorizontalGroup(
            pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pneUserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pneUserLayout.createSequentialGroup()
                        .addComponent(btnDbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pneUserLayout.createSequentialGroup()
                        .addGroup(pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pneUserLblUser)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(21, 21, 21)
                        .addGroup(pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDbPrivileges, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDbIp, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                            .addComponent(txtDbLoggedIn, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDbUser, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                            .addGroup(pneUserLayout.createSequentialGroup()
                                .addComponent(txtDbLocMap, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtDbLocX, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDbLocY, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtDbSprite, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pneUserLayout.setVerticalGroup(
            pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pneUserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDbUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pneUserLblUser))
                .addGap(18, 18, 18)
                .addGroup(pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDbLoggedIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDbPrivileges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDbIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDbLocMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDbLocY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDbLocX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(txtDbSprite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(pneUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDbSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout pneUsersLayout = new javax.swing.GroupLayout(pneUsers);
        pneUsers.setLayout(pneUsersLayout);
        pneUsersLayout.setHorizontalGroup(
            pneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pneUsersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pneUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pneUsersLayout.setVerticalGroup(
            pneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pneUsersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pneUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrUsers, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
                .addContainerGap())
        );

        tbp.addTab("Users", pneUsers);

        mnu_File.setText("File");

        mnu_File_TurnOn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mnu_File_TurnOn.setText("Turn On");
        mnu_File_TurnOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_TurnOn_ActionPerformed(evt);
            }
        });
        mnu_File.add(mnu_File_TurnOn);

        mnu_File_TurnOff.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        mnu_File_TurnOff.setText("Turn Off");
        mnu_File_TurnOff.setEnabled(false);
        mnu_File_TurnOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_TurnOff_ActionPerformed(evt);
            }
        });
        mnu_File.add(mnu_File_TurnOff);
        mnu_File.add(mnu_File_Separator1);

        mnu_File_DbOn.setText("Connect to Database");
        mnu_File_DbOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_DbOn_ActionPerformed(evt);
            }
        });
        mnu_File.add(mnu_File_DbOn);

        mnu_File_DbOff.setText("Disconnect Database");
        mnu_File_DbOff.setEnabled(false);
        mnu_File_DbOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_DbOff_ActionPerformed(evt);
            }
        });
        mnu_File.add(mnu_File_DbOff);
        mnu_File.add(mnu_File_Separator2);

        mnu_File_SimPing.setText("Simulate High Ping");
        mnu_File_SimPing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_SimPingActionPerformed(evt);
            }
        });
        mnu_File.add(mnu_File_SimPing);
        mnu_File.add(mnu_File_Separator3);

        mnu_File_Exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        mnu_File_Exit.setText("Exit");
        mnu_File_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_File_Exit_ActionPerformed(evt);
            }
        });
        mnu_File.add(mnu_File_Exit);

        mnu.add(mnu_File);

        mnu_Editors.setText("Editors");

        mnu_Editors_Map.setText("Maps");
        mnu_Editors_Map.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_Editors_Map_ActionPerformed(evt);
            }
        });
        mnu_Editors.add(mnu_Editors_Map);

        mnu_Editors_Tilesets.setText("Tilesets");
        mnu_Editors_Tilesets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_Editors_TilesetsActionPerformed(evt);
            }
        });
        mnu_Editors.add(mnu_Editors_Tilesets);

        mnu.add(mnu_Editors);

        setJMenuBar(mnu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbp, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbp, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void formWindow_Closing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindow_Closing
		mnu_File_TurnOff_ActionPerformed(null);
		if(_bMySqlConnected)
			_oDatabase.disconnect();
	}//GEN-LAST:event_formWindow_Closing

	private void mnu_File_TurnOn_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_TurnOn_ActionPerformed
		try {
			_oServerThread = new NioServer(null, SERVER_PORT);

			_oServerMessenger = new ServerMessenger(_oServerThread, _oDatabase);
			_oServerMessenger.addServerMessengerListener(this);
			_oServerThread.setServerListener(_oServerMessenger);

			Thread oThread = new Thread(_oServerThread);
			oThread.setName("Server");
			oThread.start();

			_oServerMessenger.startDownloader();
			
			mnu_File_TurnOn.setEnabled(false);
			mnu_File_TurnOff.setEnabled(true);
			logEvent("<html><span style='font-weight: bold; '>Start Server</span></html>");
		}catch(java.net.BindException ex){
			logEvent("<html><span style='font-weight: bold; color: red;'>Cannot Start Server: There's a server running in this computer, already!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//GEN-LAST:event_mnu_File_TurnOn_ActionPerformed

	private void mnu_File_TurnOff_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_TurnOff_ActionPerformed
		mnu_File_TurnOn.setEnabled(true);
		mnu_File_TurnOff.setEnabled(false);
		if(_oServerThread != null)
			_oServerThread.stop();
		logEvent("<html><span style='font-weight: bold; '>" + "Stop Server"+ "</span></html>");
	}//GEN-LAST:event_mnu_File_TurnOff_ActionPerformed

	private void mnu_File_Exit_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_Exit_ActionPerformed
		if(_oServerThread != null)
			_oServerThread.stop();
		if(_bMySqlConnected)
			_oDatabase.disconnect();
		dispose();
	}//GEN-LAST:event_mnu_File_Exit_ActionPerformed

	private void lstClients_ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstClients_ValueChanged
		if(lstClients.getSelectedIndex() < 0)
			return;
		SocketChannel oSocketChannel = (SocketChannel) ((DefaultListModel)lstClients.getModel()).get(lstClients.getSelectedIndex());
		synchronized(_oServerMessenger.getPlayers()){
			Player oPlayer = _oServerMessenger.getPlayers().get(oSocketChannel);

			pnePlayer.setEnabled(oPlayer != null);
			lblPing.setText("");

			if(oPlayer == null){
				((TitledBorder)pnePlayer.getBorder()).setTitle(socketChannelToString(oSocketChannel));
			}else{
				((TitledBorder)pnePlayer.getBorder()).setTitle(oPlayer.Username);
				lblPlayerPosition.setText(oPlayer.Map + ", " + oPlayer.MapPosition.x + ", " + oPlayer.MapPosition.y);
			}
		}

//		lblDownloadName.setText("");
	}//GEN-LAST:event_lstClients_ValueChanged

	private void mnu_Editors_Map_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_Editors_Map_ActionPerformed
		rs.server.maker.maps.Main o = new rs.server.maker.maps.Main();
		o.setVisible(true);
	}//GEN-LAST:event_mnu_Editors_Map_ActionPerformed

	private void mnu_File_DbOn_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_DbOn_ActionPerformed
		_bMySqlConnected = _oDatabase.connect();
		if(!_bMySqlConnected){
			logEvent("<html><span style='font-weight: bold; color: red;'>" + "Could not connect to mySql" + "</span></html>");
		}else{
			logEvent("<html><span style='font-weight: bold; '>" + "Connected to mySql" + "</span></html>");
			mnu_File_DbOn.setEnabled(false);
			mnu_File_DbOff.setEnabled(true);
			_oPlayers = _oDatabase.getUsers();
			lstUsers.repaint();
			for(java.awt.Component o : pneUser.getComponents()){
				if(o instanceof javax.swing.JTextField || o instanceof javax.swing.JSpinner)
					o.setEnabled(true);
			}
		}
	}//GEN-LAST:event_mnu_File_DbOn_ActionPerformed

	private void mnu_File_DbOff_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_DbOff_ActionPerformed
		if(_bMySqlConnected){
			_oDatabase.disconnect();
			_bMySqlConnected = false;
			logEvent("<html><span style='font-weight: bold; '>" + "Disconnected mySql" + "</span></html>");
			mnu_File_DbOn.setEnabled(true);
			mnu_File_DbOff.setEnabled(false);
			_oPlayers = null;
			lstUsers.repaint();
			for(java.awt.Component o : pneUser.getComponents()){
				if(o instanceof javax.swing.JTextField || o instanceof javax.swing.JSpinner || o instanceof javax.swing.JButton)
					o.setEnabled(false);
			}
		}
	}//GEN-LAST:event_mnu_File_DbOff_ActionPerformed

	private void btnUpdatePosition_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePosition_ActionPerformed
		if(lstClients.getSelectedIndex() < 0)
			return;
		SocketChannel oSocketChannel = (SocketChannel) ((DefaultListModel)lstClients.getModel()).get(lstClients.getSelectedIndex());
		Player oPlayer = _oServerMessenger.getPlayers().get(oSocketChannel);

		_oServerThread.send(oSocketChannel, Messaging.updatePlayerPosition(oPlayer.Id, oPlayer.MapPosition.x, oPlayer.MapPosition.y));
	}//GEN-LAST:event_btnUpdatePosition_ActionPerformed

	private void btnRefresh_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh_ActionPerformed
		lstClients_ValueChanged(null);
	}//GEN-LAST:event_btnRefresh_ActionPerformed

	private void mnu_File_SimPingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_File_SimPingActionPerformed
		if(_oServerMessenger != null)
			_oServerMessenger._bSimulateHighPing = mnu_File_SimPing.isSelected();
	}//GEN-LAST:event_mnu_File_SimPingActionPerformed

	private void btnPing_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPing_ActionPerformed
		if(lstClients.getSelectedIndex() < 0)
			return;
		SocketChannel oSocketChannel = (SocketChannel) ((DefaultListModel)lstClients.getModel()).get(lstClients.getSelectedIndex());
		_iPingTime = System.nanoTime();
		_oServerThread.send(oSocketChannel, Messaging.ping32(false));
	}//GEN-LAST:event_btnPing_ActionPerformed

	private void lstUsers_ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstUsers_ValueChanged
		Player oPlayer = (Player) lstUsers.getSelectedValue();
		if(oPlayer == null)
			return;
		oPlayer = _oDatabase.getUser(oPlayer.Id);
		txtDbUser.setText(oPlayer.Username);
		txtDbLocMap.setValue(oPlayer.Map);
		txtDbLocX.setValue(oPlayer.MapPosition.x);
		txtDbLocY.setValue(oPlayer.MapPosition.y);
		txtDbLoggedIn.setValue(oPlayer.IsLoggedIn?"true":"false");
		txtDbIp.setText(oPlayer.LastIp);
		txtDbPrivileges.setValue(oPlayer.Privileges);
		txtDbSprite.setValue(oPlayer.SpriteFilename);
	}//GEN-LAST:event_lstUsers_ValueChanged

	private void btnDbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDbCancelActionPerformed
		btnDbCancel.setEnabled(false);
		btnDbSave.setEnabled(false);
		lstUsers_ValueChanged(null);
	}//GEN-LAST:event_btnDbCancelActionPerformed

	private void btnDbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDbSaveActionPerformed
		Player oPlayer = (Player) lstUsers.getSelectedValue();
		btnDbCancel.setEnabled(false);
		btnDbSave.setEnabled(false);
		_oDatabase.setCoords(oPlayer.Id, (Float)txtDbLocX.getValue(), (Float)txtDbLocY.getValue());
		_oDatabase.setMap(oPlayer.Id, (Integer)txtDbLocMap.getValue());
		_oDatabase.setIp(oPlayer.Id, txtDbIp.getText());
		_oDatabase.setPrivileges(oPlayer.Id, (Integer)txtDbPrivileges.getValue());
		_oDatabase.setSprite(oPlayer.Id, txtDbSprite.getValue());
	}//GEN-LAST:event_btnDbSaveActionPerformed

	private void btnSetPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetPositionActionPerformed
		if(lstClients.getSelectedIndex() < 0)
			return;
		SocketChannel oSocketChannel = (SocketChannel) ((DefaultListModel)lstClients.getModel()).get(lstClients.getSelectedIndex());
		Player oPlayer = _oServerMessenger.getPlayers().get(oSocketChannel);
		
		oPlayer.MapPosition.x = (Integer)txtSetPositionX.getValue();
		oPlayer.MapPosition.y = (Integer)txtSetPositionY.getValue();
		
		_oServerThread.send(oSocketChannel, Messaging.updatePlayerPosition(oPlayer.Id, oPlayer.MapPosition.x, oPlayer.MapPosition.y));
	}//GEN-LAST:event_btnSetPositionActionPerformed

	private void mnu_Editors_TilesetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_Editors_TilesetsActionPerformed
		if(_oWindowTilesets == null){
			_oWindowTilesets = new rs.server.maker.tilesets.Main();
		}
		if(!_oWindowTilesets.isVisible()){
			_oWindowTilesets.setVisible(true);
			_oWindowTilesets.toFront();
		}
	}//GEN-LAST:event_mnu_Editors_TilesetsActionPerformed

	private void txtDbSpriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDbSpriteActionPerformed
		String s = rs.server.maker.ImageChooser.show(this, Paths.PATH_GRAPHICS_CHARACTERS, "Sprites", txtDbSprite.getValue());
		if(s == null)
			return;
		txtDbSprite.setValue(s);
		btnDbSave.setEnabled(true);
		btnDbCancel.setEnabled(true);
	}//GEN-LAST:event_txtDbSpriteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDbCancel;
    private javax.swing.JButton btnDbSave;
    private javax.swing.JButton btnPing;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSetPosition;
    private javax.swing.JButton btnUpdatePosition;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblPing;
    private javax.swing.JLabel lblPlayerPosition;
    private javax.swing.JList lstChat;
    private javax.swing.JList lstClients;
    private javax.swing.JList lstDownloads;
    private javax.swing.JList lstEvents;
    private javax.swing.JList lstUsers;
    private javax.swing.JMenuBar mnu;
    private javax.swing.JMenu mnu_Editors;
    private javax.swing.JMenuItem mnu_Editors_Map;
    private javax.swing.JMenuItem mnu_Editors_Tilesets;
    private javax.swing.JMenu mnu_File;
    private javax.swing.JMenuItem mnu_File_DbOff;
    private javax.swing.JMenuItem mnu_File_DbOn;
    private javax.swing.JMenuItem mnu_File_Exit;
    private javax.swing.JPopupMenu.Separator mnu_File_Separator1;
    private javax.swing.JPopupMenu.Separator mnu_File_Separator2;
    private javax.swing.JPopupMenu.Separator mnu_File_Separator3;
    private javax.swing.JCheckBoxMenuItem mnu_File_SimPing;
    private javax.swing.JMenuItem mnu_File_TurnOff;
    private javax.swing.JMenuItem mnu_File_TurnOn;
    private javax.swing.JPanel pneChat;
    private javax.swing.JPanel pneClients;
    private javax.swing.JPanel pneDownloads;
    private javax.swing.JPanel pneEvents;
    private javax.swing.JPanel pnePlayer;
    private javax.swing.JPanel pneUser;
    private javax.swing.JLabel pneUserLblUser;
    private javax.swing.JPanel pneUsers;
    private javax.swing.JScrollPane scrClients;
    private javax.swing.JScrollPane scrUsers;
    private javax.swing.JTabbedPane tbp;
    private javax.swing.JTextField txtDbIp;
    private javax.swing.JSpinner txtDbLocMap;
    private javax.swing.JSpinner txtDbLocX;
    private javax.swing.JSpinner txtDbLocY;
    private javax.swing.JSpinner txtDbLoggedIn;
    private javax.swing.JSpinner txtDbPrivileges;
    private rs.server.maker.DashBox txtDbSprite;
    private javax.swing.JTextField txtDbUser;
    private javax.swing.JSpinner txtSetPositionX;
    private javax.swing.JSpinner txtSetPositionY;
    // End of variables declaration//GEN-END:variables

	public void logEvent(String s){
		((DefaultListModel)(lstEvents.getModel())).addElement(s);
		lstEvents.setSelectedIndex(((DefaultListModel)(lstEvents.getModel())).getSize() - 1);
		lstEvents.ensureIndexIsVisible(((DefaultListModel)(lstEvents.getModel())).getSize() - 1);
	}

	public void logChat(String s){
		((DefaultListModel)(lstChat.getModel())).addElement(s);
		lstChat.setSelectedIndex(((DefaultListModel)(lstChat.getModel())).getSize() - 1);
		lstChat.ensureIndexIsVisible(((DefaultListModel)(lstChat.getModel())).getSize() - 1);
	}

	public void logDownload(String s){
		((DefaultListModel)(lstDownloads.getModel())).addElement(s);
		lstDownloads.setSelectedIndex(((DefaultListModel)(lstDownloads.getModel())).getSize() - 1);
		lstDownloads.ensureIndexIsVisible(((DefaultListModel)(lstDownloads.getModel())).getSize() - 1);
	}

	public void event(String s) {
		logEvent(s);
	}
	
	public void publicChat(String sUsername, String sMessage) {
		logChat("<html><span style='color: gray;'>" + sUsername + "</span>: " + sMessage);
	}

	public void acceptedConnection(SocketChannel oSocketChannel) {
		((DefaultListModel)lstClients.getModel()).addElement(oSocketChannel);
	}

	public void closedConnection(SocketChannel oSocketChannel) {
		((DefaultListModel)lstClients.getModel()).removeElement(oSocketChannel);
	}

	public void playerLoggedIn(int iId, String sUsername) {
		playerMoved(iId);
	}

	public void ping() {
		float iTime = (float)(System.nanoTime() - _iPingTime) ;
		iTime = (float)Math.floor(iTime / 10000) / 100;
		lblPing.setText("" + iTime);
	}

	public void playerMoved(int iId) {
		if(lstClients.getSelectedIndex() < 0)
			return;
		SocketChannel oSocketChannel = (SocketChannel) ((DefaultListModel)lstClients.getModel()).get(lstClients.getSelectedIndex());
		synchronized(_oServerMessenger.getPlayers()){
			Player oPlayer = _oServerMessenger.getPlayers().get(oSocketChannel);

			if(oPlayer.Id == iId){
				lblPlayerPosition.setText(oPlayer.Map + ", " + oPlayer.MapPosition.x + ", " + oPlayer.MapPosition.y);
			}
		}
	}

	public void fileRequested(String sUser, String sFile){
		logDownload("<html><span style='color: gray;'>" + sUser + "</span> requested <span style='color: #DAA520;'>" + sFile + "</span></html>");
	}

	public void invalidFileRequested(String sUser, String sFile){
		logDownload("<html><span style='color: gray;'>" + sUser + "</span> requested <span style='color: red;'>" + sFile + "</span></html>");
	}
	
}
