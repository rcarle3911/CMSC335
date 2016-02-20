import java.awt.Component;
import java.util.EventObject;

import javax.swing.JProgressBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;


public class MyEditor extends DefaultTreeCellEditor{
	private JProgressBar pm = new JProgressBar();
	
	public MyEditor(JTree t, DefaultTreeCellRenderer r) {
		super(t, r);
	}
	
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		System.out.println(value.toString());
		Component defaultRender = renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
		if (value instanceof Creature) return pm;
		else return defaultRender;
	}
	
	@Override
	public boolean isCellEditable(EventObject a) {
		return true;
	}
	
	
}
