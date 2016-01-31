import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

/**
 * FileName: GUI.java
 * Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * Project 1
 * Author: Robert Lee Carle
 * Date: 1/11/2016
 * Platform/Compiler: Java 8 with Eclipse IDE
 * Instructor: Nicholas Duchon
 * Purpose: Provides and graphical interface to the game. Executes main method.
 * Due: 1/24/2016
 */
@SuppressWarnings("serial")
public class GUI extends JFrame{
	private Cave cave;
	private JTextArea text = new JTextArea(10,10);
	private JComboBox <String> cbx = new JComboBox <String>();
	private JTextField tField = new JTextField(10);
	
	public GUI(Cave cave) {
		this.cave = cave;
		setTitle("The Cave");
		setSize(600,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setButtonPanel();
		setTextArea();
		setVisible(true);
	}
	
	/**
	 * The button panel is made in this section of code.
	 */
	private void setButtonPanel() {
		
		JFileChooser fc = new JFileChooser();
		
		//Load button grabs a file and loads it into the cave object.
		JButton ld = new JButton("Load");
		ld.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				//Returns an integer based on if the user clicks open or cancel.
				int fcReturn = fc.showOpenDialog(GUI.this);
				//If the users clicked open, then proceed.
				if (fcReturn == JFileChooser.APPROVE_OPTION) {
					text.setText("Loading " + fc.getSelectedFile().getName() + "\n");
					cave.loadFile(fc.getSelectedFile());
					text.append("File loaded");
					
				}				
			}
		});
		
		//Display button simply prints the cave.toString() to the text field.
		JButton dsp = new JButton("Display");
		dsp.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				text.setText(cave.toString());
			}
		});
		
		//Searches through the underlying structure for a match.
		JButton src = new JButton("Search");
		src.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				try {
					CaveElement ce = cave.search(cbx.getSelectedIndex(), tField.getText());
					//If there is a match set the text field to it.
					if (ce != null) {
						text.setText(ce.toString());
					//If not say it was not found.
					} else {
						text.setText(tField.getText() + " Not Found!");
					}
				} catch (NumberFormatException e0) {
					
					e0.printStackTrace();
					System.out.println("Attempted to use: "+ tField.getText());
				} catch (Exception e1) {
					
					e1.printStackTrace();
					System.out.println("ComboBox: " + cbx.getSelectedIndex());
				}
			}
		});
		
		JLabel srcLbl = new JLabel("Search Target");		
		
		//Order matters here as the index is used by the internal methods.
		cbx.addItem("Index");		
		cbx.addItem("Name");
		cbx.addItem("Type");
		
		JPanel panel = new JPanel();
		panel.add(ld);
		panel.add(dsp);
		panel.add(tField);
		panel.add(srcLbl);	
		panel.add(cbx);
		panel.add(src);	
		
		add(panel, BorderLayout.PAGE_START);
	}
	
	/**
	 * Sets the text area to default.
	 */
	private void setTextArea() {
		text.setLineWrap(true);
		JScrollPane pane = new JScrollPane(text);
		text.setText("Welcome to The Cave");
		
		add(pane, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		Cave cave = new Cave();
		GUI gui = new GUI(cave);
		
	}
	
	
}
