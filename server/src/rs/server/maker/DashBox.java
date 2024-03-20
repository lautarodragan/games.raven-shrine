package rs.server.maker;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

/**
 *
 * @author lainmaster
 */
public class DashBox extends javax.swing.JPanel {
	private AbstractBorder oSelectedBorder = new AbstractBorder() {
			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int w, int h){
				g.setColor(new Color(204, 102, 0));

				for (int i = 0; i < w; i+=2) {
					g.drawLine(x+i, y, x+i, y);
					g.drawLine(x+i, y+h-1, x+i, y+h-1);
				}
				for (int i = 0; i < w; i+=2) {
					g.drawLine(x, y+i, x, y+i);
					g.drawLine(x+w-1, y+i, x+w-1, y+i);
				}

			}

			@Override
			public Insets getBorderInsets(Component c){
//				return new Insets(1, 3, 1, 1);
				return new Insets(2, 2, 2, 2);
			}

			@Override
			public Insets getBorderInsets(Component c, Insets insets){
				insets.left = insets.top = insets.right = insets.bottom = 2;
//				insets.left = 3;
				return insets;
			}
		};
	private Border oEmptyBorder =  BorderFactory.createEmptyBorder(2, 2, 2, 2);
	protected javax.swing.event.EventListenerList listeners = new javax.swing.event.EventListenerList();
	private ActionEvent _oActionEvent;

    /** Creates new form DashBox */
    public DashBox() {
        initComponents();
		setBorder(UIManager.getDefaults().getBorder("TextField.border"));
		lbl.setText("");
		lbl.setBorder(oEmptyBorder);

	}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn = new javax.swing.JButton();
        pnl = new javax.swing.JPanel();
        lbl = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(32767, 20));
        setMinimumSize(new java.awt.Dimension(148, 20));
        setPreferredSize(new java.awt.Dimension(148, 22));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                formAncestorResized(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });
        setLayout(null);

        btn.setText("...");
        btn.setMargin(new java.awt.Insets(1, 0, 1, 0));
        btn.setMaximumSize(new java.awt.Dimension(30, 30));
        btn.setMinimumSize(new java.awt.Dimension(20, 18));
        btn.setPreferredSize(new java.awt.Dimension(22, 20));
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionPerformed(evt);
            }
        });
        btn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnFocusLost(evt);
            }
        });
        add(btn);
        btn.setBounds(110, 0, 22, 20);

        pnl.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pnl.setOpaque(false);
        pnl.setLayout(new java.awt.GridLayout(1, 0));
        add(pnl);
        pnl.setBounds(0, 0, 120, 4);

        lbl.setBackground(javax.swing.UIManager.getDefaults().getColor("List.selectionBackground"));
        lbl.setText("jLabel1");
        lbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblMousePressed(evt);
            }
        });
        add(lbl);
        lbl.setBounds(0, 0, 116, 14);
    }// </editor-fold>//GEN-END:initComponents

	private void btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActionPerformed
		Object[] oListeners = listeners.getListenerList();
		for (int i = oListeners.length-2; i>=0; i-=2) {
			if (oListeners[i] == ActionListener.class) {
				if (_oActionEvent == null)
					_oActionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, evt.getActionCommand());
				((ActionListener)oListeners[i+1]).actionPerformed(_oActionEvent);
			}
		}
	}//GEN-LAST:event_btnActionPerformed

	private void lblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMouseClicked
		if(evt.getClickCount() == 2){
			btn.doClick(0);
		}
	}//GEN-LAST:event_lblMouseClicked

	private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
		onFocusGained();
	}//GEN-LAST:event_formFocusGained

	private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
		onFocusLost();
	}//GEN-LAST:event_formFocusLost

	private void lblMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMousePressed
		requestFocusInWindow();
	}//GEN-LAST:event_lblMousePressed

	private void btnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnFocusGained
		onFocusGained();
	}//GEN-LAST:event_btnFocusGained

	private void btnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnFocusLost
		onFocusLost();

	}//GEN-LAST:event_btnFocusLost

	private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
		requestFocusInWindow();
	}//GEN-LAST:event_formMousePressed

	private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
		int btnSize = getHeight() - getBorder().getBorderInsets(this).bottom - getBorder().getBorderInsets(this).top ;
		btn.setBounds(getWidth() - btnSize - getBorder().getBorderInsets(this).right, getBorder().getBorderInsets(this).top, btnSize, btnSize);
		lbl.setBounds(getBorder().getBorderInsets(this).left + 2, getBorder().getBorderInsets(this).top + 2, getWidth() - btn.getWidth() - getBorder().getBorderInsets(this).right - getBorder().getBorderInsets(this).left - 3, getHeight() - getBorder().getBorderInsets(this).bottom - getBorder().getBorderInsets(this).top - 4);
	}//GEN-LAST:event_formComponentResized

	private void formAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_formAncestorResized
		formComponentResized(null);
	}//GEN-LAST:event_formAncestorResized

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn;
    private javax.swing.JLabel lbl;
    private javax.swing.JPanel pnl;
    // End of variables declaration//GEN-END:variables

	public void addActionListener(ActionListener o){
		listeners.add(ActionListener.class, o);
	}

	public void removeActionListener(ActionListener o){
		listeners.remove(ActionListener.class, o);
	}

	public String getValue(){
		return lbl.getText();
	}

	public void setValue(String s){
		lbl.setText(s);
	}

	private void onFocusGained(){
		lbl.setBorder(oSelectedBorder);
		lbl.setOpaque(true);
		lbl.setForeground(Color.white);
		lbl.repaint();
	}

	private void onFocusLost(){
		lbl.setBorder(oEmptyBorder);
		lbl.setOpaque(false);
		lbl.setForeground(Color.black);
		lbl.repaint();
	}

}
