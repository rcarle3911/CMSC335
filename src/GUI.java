import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.*;

/**
 * <li>FileName: GUI.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Final
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/27/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Provides and graphical interface to the game. Executes main method.
 * <li>Due: 3/7/2016
 */
public class GUI extends JFrame{
	/**
	 * Default Serial
	 */
	private static final long serialVersionUID = 1234L;
	private Cave cave;
	private JTextArea text = new JTextArea(10,10);
	private JComboBox <String> searchCbx = new JComboBox <String>();
	private JComboBox <String> sortCbx = new JComboBox <String>();
	private JTextField tField = new JTextField(10);
	private JPanel jobPanel = new JPanel();
	private JTabbedPane tabPane = new JTabbedPane();
	DefaultTreeModel model;
	JTree tree;

	
	public GUI(Cave cave) {
		this.cave = cave;
		setTitle("The Cave");
		setSize(1200,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add(tabPane, BorderLayout.CENTER);
				
		setButtonPanel();
		setTextArea();
		setTreeArea();
		setJobArea();	
			
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
		
		ActionListener ldAction = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				//Returns an integer based on if the user clicks open or cancel.
				int fcReturn = fc.showOpenDialog(GUI.this);
				//If the users clicked open, then proceed.
				if (fcReturn == JFileChooser.APPROVE_OPTION) {
					text.setText("Loading " + fc.getSelectedFile().getName() + "\n");
					cave.clear();
					jobPanel.removeAll();
					loadFile(fc.getSelectedFile(), jobPanel, model);
					model.setRoot(cave);
					text.append("File loaded");
				}				
			}
		};
		
		ld.addActionListener(ldAction);
		
		//Display button simply prints the cave.display() to the text field.
		JButton dsp = new JButton("Display");
		
		ActionListener dspAction = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if (cave != null) text.setText(cave.display());
			}
		};
		
		dsp.addActionListener(dspAction);
		
		//Searches through the underlying structure for a match.
		ActionListener search = new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				text.setText("");
				try {
					Vector<CaveElement> matches = new Vector<CaveElement>();
					switch (searchCbx.getSelectedIndex()) {
					
					case 0: 
						String string = tField.getText();
						if (isNumber(string)) matches = cave.searchIndex(Integer.parseInt(string));
						else text.setText("Error: Attempted to use \"" + tField.getText() + "\" as an index. This must be a number!");
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
		searchLbl.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		//Adds search action listener to when search button is pressed and when enter is pressed from the text field.
		searchBtn.addActionListener(search);
		tField.addActionListener(search);
		
		//Sort button
		JButton sortBtn = new JButton("Sort");
		sortBtn.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				sort(((String)sortCbx.getSelectedItem()));
				text.setText(cave.display());
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
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
	
		panel.add(ld, c);
		panel.add(dsp, c);
		c.weightx = 1.0;
		panel.add(tField, c);
		c.weightx = 0.05;
		panel.add(searchLbl, c);	
		panel.add(searchCbx, c);
		panel.add(searchBtn, c);
		panel.add(sortCbx, c);
		panel.add(sortBtn, c);		
		
		add(panel, BorderLayout.PAGE_START);
	}
	
	/**
	 * Sets the text area to default and adds it to the tabbed pane
	 */
	private void setTextArea() {
	
		text.setLineWrap(true);
		JScrollPane pane = new JScrollPane(text);
		text.setText("Welcome to The Cave");
		
		tabPane.addTab("Text Area", pane);
	}
	
	/**
	 * Builds the JTree and adds it to the tabbed pane.
	 */
	private void setTreeArea() {
		JButton expandAll = new JButton("Expand All");
		expandAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int row = 0;
					while (row < tree.getRowCount()) {
						tree.expandRow(row);
						row++;
					}
				}				
			});
		
		JButton collapseAll = new JButton("Collapse All");
		collapseAll.addActionListener(new ActionListener() {

			@Override			
			public void actionPerformed(ActionEvent e) {
				int row = tree.getRowCount() - 1;
				while (row >= 0) {
					tree.collapseRow(row);
					row--;
				}
			}			
		});
		
		
		JPanel btnPanel = new JPanel(new GridLayout(1,1));
		btnPanel.add(expandAll);
		btnPanel.add(collapseAll);
				
		model = new DefaultTreeModel(cave);
		tree = new JTree(model);		
		
		JScrollPane treeScrollPane = new JScrollPane(tree);
		JPanel treePanel = new JPanel(new BorderLayout());
		treePanel.add(btnPanel, BorderLayout.NORTH);
		treePanel.add(treeScrollPane, BorderLayout.CENTER);
		
		tabPane.add("Tree Area", treePanel);
		
	}
	
	/**
	 * Builds the job panel.
	 */
	private void setJobArea() {
		JPanel jobTab = new JPanel(new BorderLayout());
		jobPanel.setLayout(new GridLayout(0,1));
						
		JButton toggleAllBtn = new JButton("Toggle All");
		toggleAllBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cave.toggleAll();
			}
			
		});		
				
		jobTab.add(toggleAllBtn, BorderLayout.NORTH);		
		jobTab.add(jobPanel, BorderLayout.CENTER);
		
		JScrollPane pane = new JScrollPane(jobTab);
		tabPane.add("Job Area", pane);
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
		
		model.reload();
	}
	
	/**
	 * Iterates line by line through the input file. The first character of the line determines how it's processed.
	 * Uses a hashmap to collect all objects read in.
	 * @param file 
	 */
	public void loadFile(File file, JPanel jobPanel, DefaultTreeModel model) {
		try {
			HashMap<Integer, Party> hmParty = new HashMap<Integer, Party>();
			HashMap<Integer, Creature> hmCrea = new HashMap<Integer, Creature>();
			HashMap<Party, ArrayList<Job>> partyJobList = new HashMap<Party, ArrayList<Job>>();
			
			Scanner s = new Scanner(file);
			String line;
			while (s.hasNextLine()) {
				line = s.nextLine();
				if (line.isEmpty()) continue;
				switch (line.charAt(0)) {
				case '/':
					continue;
				case 'p':
					Party p = cave.addParty(line);
					hmParty.put(p.getIndex(), p);
					partyJobList.put(p, new ArrayList<Job>());
					break;
				case 'c':					
					Creature c = cave.addCreature(line, hmParty);
					hmCrea.put(c.getIndex(), c);
					break;
				case 't':
					cave.addTreasure(line, hmCrea);
					break;
				case 'a':
					cave.addArtifact(line, hmCrea);
					break;
				case 'j':
					Job j = cave.addJob(line, hmCrea, model);
					Party par = (Party) j.getParent().getParent().getParent();
					partyJobList.get(par).add(j);
					break;
				default:
					throw new Exception("Incorrect Format");
				}
			}
			s.close();
			
			for (Map.Entry<Integer, Party> p : hmParty.entrySet()) {
				p.getValue().countResources();
			}
			
			int current = -100; // Initially set to an index that cannot exist.
			Border border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray);
						
			for(Map.Entry<Party, ArrayList<Job>> e : partyJobList.entrySet()) {							
				
				jobPanel.add(e.getKey().getResourcePanel());
											
				for (JProgressBar pb : e.getKey().getPBList()) {
					jobPanel.add(pb);
				}
				
				jobPanel.add(createHeader());
				
				for (Job job : e.getValue()) {
					job.startJob();
					if (job.getParent().getParent().getIndex() != current) {
						job.availLbl.setBorder(border);
						job.nameLbl.setBorder(border);
						job.needsLbl.setBorder(border);
						current = job.getParent().getParent().getIndex();
					}
					
					jobPanel.add(job.getPBar());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a header panel for use in the job area
	 * @return JPanel header
	 */
	private JPanel createHeader() {
		Border hBorder = BorderFactory.createLineBorder(Color.lightGray);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		int height = 15;
		
		JPanel header = new JPanel(new GridBagLayout());
		JLabel nameH = new JLabel("Name");
		nameH.setPreferredSize(new Dimension(180, height));
		nameH.setBorder(hBorder);
		nameH.setHorizontalAlignment(JLabel.CENTER);
		header.add(nameH, c);
		
		JLabel availH = new JLabel("Available");
		availH.setPreferredSize(new Dimension(250, height));
		availH.setBorder(hBorder);
		availH.setHorizontalAlignment(JLabel.CENTER);
		header.add(availH, c);
		
		JLabel needH = new JLabel("Needs");
		needH.setPreferredSize(new Dimension(250, height));
		needH.setBorder(hBorder);
		needH.setHorizontalAlignment(JLabel.CENTER);
		header.add(needH, c);
		
		JLabel pausH = new JLabel("Pause");
		pausH.setPreferredSize(new Dimension(80, height));
		pausH.setBorder(hBorder);
		pausH.setHorizontalAlignment(JLabel.CENTER);
		header.add(pausH, c);
		
		JLabel stopH = new JLabel("Cancel");
		stopH.setPreferredSize(new Dimension(80, height));
		stopH.setBorder(hBorder);
		stopH.setHorizontalAlignment(JLabel.CENTER);
		header.add(stopH, c);
		
		JLabel pbH = new JLabel("Progress");
		pbH.setPreferredSize(new Dimension(200, height));
		pbH.setBorder(hBorder);
		pbH.setHorizontalAlignment(JLabel.CENTER);
		c.weightx = 1.0;
		header.add(pbH, c);
		
		return header;
	}
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			@Override public void run() {
				Cave cave = new Cave("Sorcerer's Cave");
				GUI gui = new GUI(cave);
				MyRenderer renderer = new MyRenderer();
				gui.tree.setCellRenderer(renderer);
			}
		});
		
	}
	
	
}
