import java.awt.Component;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.*;

/**
 * <li>FileName: Cave.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 3
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/8/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Cave class which holds the parties and loads a data file.
 * <li>Due: 2/22/2016
 */
public class Cave extends CaveElement{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234L;
	
	//private ArrayList<CaveElement> children = new ArrayList<CaveElement>();
	//private ArrayList<CaveElement> parties = new ArrayList<CaveElement>();
	//private ArrayList<CaveElement> looseObj = new ArrayList<CaveElement>();
	
	public Cave(String name) {
		super(-1, name);
		
		add(new CaveElement(-1, "Parties"));
		add(new CaveElement(-1, "Loose Objects"));
	}	

	/**
	 * Iterates line by line through the input file. The first character of the line determines how it's processed.
	 * Uses a hashmap to collect all objects read in.
	 * @param file 
	 */
	public void loadFile(File file, JPanel jobPanel, DefaultTreeModel model) {
		try {
			HashMap<Integer, CaveElement> hm = new HashMap<Integer, CaveElement>();
			Scanner s = new Scanner(file);
			String line;
			while (s.hasNextLine()) {
				line = s.nextLine();
				if (line.isEmpty()) continue;
				switch (line.charAt(0)) {
				case '/':
					continue;
				case 'p':
					addParty(line, hm);
					break;
				case 'c':
					addCreature(line, hm);
					break;
				case 't':
					addTreasure(line, hm);
					break;
				case 'a':
					addArtifact(line, hm);
					break;
				case 'j':
					addJob(line, hm, jobPanel, model);
					break;
				default:
					throw new Exception("Incorrect Format");
				}
			}
			s.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes an input string and separates each value between the colons (:). Trims the extra space and puts it in a queue.
	 * @param line
	 * @return Queue A queue of string inputs.
	 */
	private ArrayDeque<String> splitLine(String line) {
		ArrayDeque<String> queue = new ArrayDeque<String>();
		Scanner s = new Scanner(line);
		s.useDelimiter(":");
		s.next();
		
		while (s.hasNext()) {
			queue.add(s.next().trim());
		}
		
		s.close();		
		return queue;
	}
	
	/**
	 * Takes a line of inputs in a specific format, splits it, and creates a new Party object. Adds it to the collection.
	 * @param line String of inputs
	 * @param hm Hashmap of objects read in
	 */
	private void addParty(String line, HashMap<Integer, CaveElement> hm) {
		ArrayDeque<String> queue = splitLine(line);
		Party p = new Party(Integer.parseInt(queue.poll()),			//index
							queue.poll());							//name
		hm.put(p.getIndex(), p);
		addParty(p);
	}
	
	/**
	 * Simply adds the input Party object to the collection.
	 * @param p Party object
	 */
	public void addParty(Party p) {		
		((CaveElement) getChildAt(0)).add(p);
	}
	
	/**
	 * Splits input string into a queue and builds a Creature with it. Adds this to the party indicated by parentIndex.
	 * @param line
	 * @param hm Hashmap of objects read in
	 */
	private void addCreature(String line, HashMap<Integer, CaveElement> hm) {
		try {
			ArrayDeque<String> queue = splitLine(line);
			
			int index =	Integer.parseInt(queue.poll()); 				//index
			String type = queue.poll();									//type
			String name = queue.poll();									//name
			CaveElement parent = hm.get(Integer.parseInt(queue.poll()));			//parent
			int emp = Integer.parseInt(queue.poll());					//empathy
			int fear = Integer.parseInt(queue.poll());					//fear
			double carryCap = Double.parseDouble(queue.poll());				//carrying capacity
			
			Creature c = new Creature(index, type, name, emp, fear, carryCap);
			
			if (!queue.isEmpty()) {
				c.setAge(Double.parseDouble(queue.poll()));
				if (!queue.isEmpty()) {
					c.setHeight(Double.parseDouble(queue.poll()));
					if (!queue.isEmpty()) {
						c.setWeight(Double.parseDouble(queue.poll()));
					}
				}
			}			
			
			hm.put(c.getIndex(), c);
			
			if (parent != null) parent.add(c);
			else addCreature(c);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the input creature to the party indicated by parentIndex. Throws an Exception when there is no match or parent is not an instance of Party.
	 * Adds any creature with parentIndex 0 to the looseObj collection.
	 * @param c Creature
	 * @throws Exception
	 */
	public void addCreature(Creature c) throws Exception {		
		if (c.getParent() != null) {
			CaveElement p = c.getParent();
			if (p instanceof Party) {
				((Party) p).addCreature(c);
			} else {
				throw new Exception("Invalid parent index at creature: " + c.toString());
			}
		} else {
			((CaveElement) getChildAt(1)).add(c); //Adds to loose objects
		}
	}
	
	/**
	 * Splits the input string into a queue and uses that to build a Treasure object. Adds this to the creature indicated by parentIndex.
	 * @param line
	 * @param hm Hashmap of objects read in
	 */
	private void addTreasure(String line, HashMap<Integer, CaveElement> hm) {
		try {
			ArrayDeque<String> queue = splitLine(line);
			
			int index = Integer.parseInt(queue.poll());			//index
			String type = queue.poll();							//type
			CaveElement parent = hm.get(Integer.parseInt(queue.poll()));	//parent
			double weight = Double.parseDouble(queue.poll());		//weight
			double value = Double.parseDouble(queue.poll());		//value
			
			Treasure t = new Treasure(index, type, weight, value);
			
			if (parent != null) ((Creature) parent).addTreasure(t);
			else addTreasure(t);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Adds the input Treasure object to the creature indicated by the parentIndex. 
	 * Throws an Exception when parent is not found or it is not an instance of Creature.
	 * Adds anything with parentIndex 0 to the looseObj Collection.
	 * @param t Treasure
	 * @throws Exception
	 */
	public void addTreasure(Treasure t) throws Exception {
		if (t.getParent() != null) {
			CaveElement c = t.getParent();
			if (c instanceof Creature) {
				((Creature) c).addTreasure(t);
			} else {
				throw new Exception("Invalid parent index at treasure: " + t.toString());
			}
		} else {
			((CaveElement) getChildAt(1)).add(t); //Adds to loose objects
		}			
	}
	
	/**
	 * Takes a specifically formatted string input and splits into a queue. It uses this to make an Artifact object.
	 * @param line
	 * @param hm Hashmap of objects read in
	 */
	private void addArtifact(String line, HashMap<Integer, CaveElement> hm) {		
		try {
			ArrayDeque<String> queue = splitLine(line);
			
			int index = Integer.parseInt(queue.poll());				//index
			String type = queue.poll();								//type
			CaveElement parent = hm.get(Integer.parseInt(queue.poll()));	//parent
			
			Artifact a = new Artifact(index, type);
			
			if (!queue.isEmpty()) a.setName(queue.poll());
			if (parent != null) ((Creature) parent).addArtifact(a);
			else addArtifact(a);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * Adds the input Artifact to the Creature indicated by the parentIndex.
	 * Throws an exception when the parent does not exist or is not a Creature.
	 * Adds any object with parentIndex 0 to the looseObj collection.
	 * @param a Artifact
	 * @throws Exception
	 */
	public void addArtifact(Artifact a) throws Exception {
	
		if (a.getParent() != null) {
			CaveElement c = a.getParent();
			if (c instanceof Creature) {
				((Creature) c).addArtifact(a);
			} else {
				throw new Exception("Invalid parent index at treasure: " + a.toString());
			}
		} else {
			((CaveElement) getChildAt(1)).add(a); //Adds to loose objects
		}			
	}
	
	private void addJob(String line, HashMap<Integer, CaveElement> hm, JPanel jobPanel, DefaultTreeModel model) {
		try {
			ArrayDeque<String> queue = splitLine(line);
			
			int index = Integer.parseInt(queue.poll());			//index
			String name = queue.poll();								//name
			CaveElement parent = hm.get(Integer.parseInt(queue.poll()));		//parent
			Long jobTime = Long.parseLong(queue.poll());				//job time
			
			Job job = new Job(index, name, jobTime, model);
			
			while (!queue.isEmpty()) {
				job.addReq(queue.poll(), Integer.parseInt(queue.poll()));
			}	
			
			parent.add(job);			
			job.startJob();
			
			jobPanel.add(job.getPBar());
			jobPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			
		} catch (Exception e) {
			
		}
	}	
	
	/**
	 * Iterates through every object in the game and collects all matches.
	 * @param index
	 * @return Array of CaveElements with the given index.
	 */
	public Vector<CaveElement> searchIndex(int index) {
		Vector<CaveElement> matched = ((CaveElement) getChildAt(0)).searchIndex(index);
		matched.addAll(((CaveElement) getChildAt(1)).searchIndex(index));
		return matched;		
	}
	
	/**
	 * Iterates through every object in the game and collects all matches.
	 * @param target
	 * @return Array of CaveElements with the given name.
	 */
	public Vector<CaveElement> searchName(String target) {
		Vector<CaveElement> matched = ((CaveElement) getChildAt(0)).searchName(target);
		matched.addAll(((CaveElement) getChildAt(1)).searchName(target));
		return matched;		
	}
	
	/**
	 * Iterates through every object in the game and collects all matches.
	 * @param target
	 * @return Array of CaveElements with the given type.
	 */
	public Vector<CaveElement> searchType(String target) {
		Vector<CaveElement> matched = ((CaveElement) getChildAt(0)).searchType(target);
		matched.addAll(((CaveElement) getChildAt(1)).searchType(target));
		return matched;
	}
	
	public Vector<CaveElement> getParties() {
		return ((CaveElement) getChildAt(0)).getChildren();
	}
	
	public Vector<CaveElement> getLooseObj() {
		return ((CaveElement) getChildAt(1)).getChildren();
	}
	
	public void sortName() {
		Collections.sort(getParties(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement p1, CaveElement p2) {				
				return p1.getName().compareTo(p2.getName());
			}
		});
		
		for (CaveElement p : getParties()) {
			((Party) p).sortName();
			for (CaveElement c : ((Party) p).getCreatures()) {
				((Creature) c).sortName();
			}
		}
	}
	
	public void sortAge() {
		for (CaveElement p : getParties()) {
			((Party) p).sortAge();
		}
	}
	
	public void sortHeight() {
		for (CaveElement p : getParties()) {
			((Party) p).sortHeight();
		}
	}
	
	public void sortWeight() {
		for (CaveElement p : getParties()) {
			((Party) p).sortWeight();
			for (CaveElement c : ((Party) p).getCreatures()) {
				((Creature) c).sortWeight();
			}
		}
	}
	
	public void sortEmpathy() {
		for (CaveElement p : getParties()) {
			((Party) p).sortEmpathy();
		}
	}
	
	public void sortFear() {
		for (CaveElement p : getParties()) {
			((Party) p).sortFear();
		}
	}
	
	public void sortCarryCap() {
		for (CaveElement p : getParties()) {
			((Party) p).sortCarryCap();
		}
	}
	
	public void sortValue() {
		for (CaveElement p : getParties()) {
			for (CaveElement c : ((Party) p).getCreatures()) {
				((Creature) c).sortValue();
			}
		}
	}

	@Override
	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public CaveElement getParent() {
		return null;
	}
}
