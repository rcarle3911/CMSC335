package jTreePBar;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.tree.*;

public class MyGUI extends JFrame{
	public MyGUI() {
		setTitle("JTreePBar");
		setSize(800,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());	
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		TreeModel model = new DefaultTreeModel(root);
		root.add(new DefaultMutableTreeNode("One"));
		root.add(new DefaultMutableTreeNode("Two"));
		root.add(new DefaultMutableTreeNode("Three"));
		root.add(new DefaultMutableTreeNode("Four"));
		JTree tree = new JTree(model);
		JPanel panel = new JPanel();
		panel.add(tree);
		add(panel, BorderLayout.CENTER);
		
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		MyGUI gui = new MyGUI();
	}
}
