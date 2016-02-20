import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;


public class MyRenderer extends DefaultTreeCellRenderer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234L;
	
	private JPanel panel = new JPanel();
	private JLabel label = new JLabel();
	private JProgressBar pm = new JProgressBar(0, 100);
	private DefaultTreeCellRenderer defR = new DefaultTreeCellRenderer();

	public MyRenderer() {
		super();
		FlowLayout layout = (FlowLayout) panel.getLayout();
		layout.setVgap(0);
		layout.setHgap(0);
		//panel.setPreferredSize(new Dimension(600, 20));
	}
	
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		if (value instanceof Creature) {
			Creature c = (Creature) value;
			Job job = c.getJob();
			
			
			if (selected) panel.setBackground(defR.getBackgroundSelectionColor());
			else panel.setBackground(defR.getBackgroundNonSelectionColor());			
			
			if (job != null) {
				label.setText(job + " ");
				label.setFont(defR.getFont());
				pm.setValue((int) c.getUserObject());
				pm.setStringPainted(true);
				
				panel.add(comp);
				panel.add(label);
				panel.add(pm);
				
				pm.setPreferredSize(new Dimension(100, 15));
				
				return panel;
			}			
		}
		return comp;
	}
}
