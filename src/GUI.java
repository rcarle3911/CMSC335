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
 * <li>Due: 2/8/2016
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
		setSize(800,600);
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
		
		//Now opens in the program's directory.
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
		ActionListener search = new ActionListener() {
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
		};		

		JButton searchBtn = new JButton("Search");
		searchBtn.setToolTipText("Searches for matches based on drop down selection and text field");
		JLabel searchLbl = new JLabel("Search Target");
		
		//Adds search action listener to when search button is pressed and when enter is pressed from the text field.
		searchBtn.addActionListener(search);
		tField.addActionListener(search);
		
		//Sort button
		JButton sortBtn = new JButton("Sort");
		sortBtn.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				sort(((String)sortCbx.getSelectedItem()));
				text.setText(cave.toString());
			}
		});
		sortBtn.setToolTipText("Sorts bases on drop down selection");		
		
		//Order matters here as the index is used by the search method.
		searchCbx.addItem("Index");		
		searchCbx.addItem("Name");
		searchCbx.addItem("Type");
		searchCbx.setToolTipText("Select what to base search on");
		
		//Order does not matter here as the string objects are compared.
		sortCbx.addItem("Name");
		sortCbx.addItem("Age");
		sortCbx.addItem("Height");
		sortCbx.addItem("Weight");
		sortCbx.addItem("Empathy");
		sortCbx.addItem("Fear");
		sortCbx.addItem("Carrying Capacity");
		sortCbx.addItem("Value");
		sortCbx.setToolTipText("Select what to base sorting on");
		
		JPanel panel = new JPanel();
		panel.add(ld);
		panel.add(dsp);
		panel.add(tField);
		panel.add(searchLbl);	
		panel.add(searchCbx);
		panel.add(searchBtn);
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
	 * Checks if a string is a positive integer by iterating through each character and checking if it's a digit.
	 * @param string
	 * @return boolean if string is a number.
	 */
	public boolean isNumber(String string) {
		if (string.isEmpty()) return false;
		
		int length = string.length();
		
		for (int i = 0; i < length; i++) {
			char c = string.charAt(i);
			if (c < '0' || c > '9') return false;
		}
		return true;				
	}
	
	/**
	 * Sorts the internal data structure based on the input string.
	 * @param target
	 */
	public void sort(String target) {
		switch (target) {
		case "Name":
			cave.sortName();
			break;
		case "Age":
			cave.sortAge();
			break;
		case "Height":
			cave.sortHeight();
			break;
		case "Weight":
			cave.sortWeight();
			break;
		case "Empathy":
			cave.sortEmpathy();
			break;
		case "Fear":
			cave.sortFear();
			break;
		case "Carrying Capacity":
			cave.sortCarryCap();
			break;				
		case "Value":
			cave.sortValue();
			break;
		default:
			text.setText("Error: Sorting Failed!");
		}
	}
	
	public static void main(String[] args) {
		Cave cave = new Cave();
		GUI gui = new GUI(cave);		
	}
	
	
}
