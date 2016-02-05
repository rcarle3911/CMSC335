import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * <li>FileName: GUI.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 2
 * <li>Author: Robert Lee Carle
 * <li>Date: 1/11/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Provides and graphical interface to the game. Executes main method.
 * <li>Due: 1/24/2016
 */
@SuppressWarnings("serial")
public class GUI extends JFrame{
	private Cave cave;
	private JTextArea text = new JTextArea(10,10);
	private JComboBox <String> searchCbx = new JComboBox <String>();
	private JComboBox <String> sortCbx = new JComboBox <String>();
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
		
		JFileChooser fc = new JFileChooser(".");
		
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
				text.setText("");
				try {
					ArrayList<CaveElement> matches = new ArrayList<CaveElement>();
					switch (searchCbx.getSelectedIndex()) {
					
					case 0: 
						String string = tField.getText();
						if (isNumber(string)) matches = cave.searchIndex(Integer.parseInt(string));
						else text.setText("Error: Attempted to use: " + tField.getText() + "as an index. This must be a number!");
						break;
					
					case 1:
						matches = cave.searchName(tField.getText());
						break;
					
					case 2:
						matches = cave.searchType(tField.getText());
						break;
						
					default:
						throw new Exception("Invalid index");					
					}
					
					//If there is a match set the text field to it.
					if (!matches.isEmpty()) {
						text.setText(matches.toString());
					//If not say it was not found.
					} else {						
						text.setText(tField.getText() + " Not Found!\n" + text.getText());
					}
				} catch (NumberFormatException e0) {
					
					e0.printStackTrace();
					System.out.println("Attempted to use: "+ tField.getText());
				} catch (Exception e1) {
					
					e1.printStackTrace();
					System.out.println("ComboBox: " + searchCbx.getSelectedIndex());
				}
			}
		});
		
		JButton sortBtn = new JButton("Sort");
		sortBtn.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				switch (((String)sortCbx.getSelectedItem())) {
				case "Name":
					for (Party p : cave.getParties()) {
						p.sortName();
						for (Creature c : p.getCreatures()) {
							c.sortName();
						}
					}
					break;
				case "Age":
					for (Party p : cave.getParties()) {
						p.sortAge();
					}
					break;
				case "Height":
					for (Party p : cave.getParties()) {
						p.sortHeight();
					}
					break;
				case "Weight":
					for (Party p : cave.getParties()) {
						p.sortWeight();
						for (Creature c : p.getCreatures()) {
							c.sortWeight();
						}
					}
					break;
				case "Empathy":
					for (Party p : cave.getParties()) {
						p.sortEmpathy();
					}
					break;
				case "Fear":
					for (Party p : cave.getParties()) {
						p.sortFear();
					}
					break;
				case "Carrying Capacity":
					for (Party p : cave.getParties()) {
						p.sortCarryCap();
					}
					break;				
				case "Value":
					for (Party p : cave.getParties()) {
						for (Creature c : p.getCreatures()) {
							c.sortValue();
						}
					}
					break;
				default:
					text.setText("Error: Sorting Failed!");
				}
			}
		});
		
		JLabel srcLbl = new JLabel("Search Target");		
		
		//Order matters here as the index is used by the internal methods.
		searchCbx.addItem("Index");		
		searchCbx.addItem("Name");
		searchCbx.addItem("Type");
		
		sortCbx.addItem("Name");
		sortCbx.addItem("Age");
		sortCbx.addItem("Height");
		sortCbx.addItem("Weight");
		sortCbx.addItem("Empathy");
		sortCbx.addItem("Fear");
		sortCbx.addItem("Carrying Capacity");
		sortCbx.addItem("Value");
		
		JPanel panel = new JPanel();
		panel.add(ld);
		panel.add(dsp);
		panel.add(tField);
		panel.add(srcLbl);	
		panel.add(searchCbx);
		panel.add(src);
		panel.add(sortCbx);
		panel.add(sortBtn);
		
		
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
	
	/**
	 * Checks if a string is an integer by iterating through each character and checking if it's a digit.
	 * @param string
	 * @return boolean if string is a number.
	 */
	public boolean isNumber(String string) {
		if (string.isEmpty()) return false;
		
		int i = 0;
		
		if (string.charAt(0) == '-') {
			if (string.length() == 1) return false;
			i = 1;
		}
		
		for (; i < string.length(); i++) {
			char c = string.charAt(i);
			if (c < '0' || c > '9') return false;
		}
		
		return true;
				
	}
	
	public static void main(String[] args) {
		Cave cave = new Cave();
		GUI gui = new GUI(cave);
		
	}
	
	
}
