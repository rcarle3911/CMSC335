import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * FileName: Cave.java
 * Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * Project 1
 * Author: Robert Lee Carle
 * Date: 1/11/2016
 * Platform/Compiler: Java 8 with Eclipse IDE
 * Instructor: Nicholas Duchon
 * Purpose: Cave class which holds the parties and loads a data file.
 * Due: 1/24/2016
 */
public class Cave {
	private ArrayList<Party> parties = new ArrayList<Party>();
	private ArrayList<CaveObject> looseObj = new ArrayList<CaveObject>();
	
	public Cave() {}
	
	/**
	 * Creates a cave object by loading the input file into the underlying data structure
	 * @param file
	 */
	public Cave(File file) {
		loadFile(file);
	}
	
	/**
	 * Iterates line by line through the input file. The first character of the line determines how it's processed.
	 * @param file
	 */
	public void loadFile(File file) {
		try {
			Scanner s = new Scanner(file);
			String line;
			while (s.hasNextLine()) {
				line = s.nextLine();
				if (line.isEmpty()) continue;
				switch (line.charAt(0)) {
				case '/':
					continue;
				case 'p':
					addParty(line);
					break;
				case 'c':
					addCreature(line);
					break;
				case 't':
					addTreasure(line);
					break;
				case 'a':
					addArtifact(line);
					break;
				case 'j':
					addJob(line);
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
	 */
	private void addParty(String line){
		ArrayDeque<String> queue = splitLine(line);
		addParty(new Party(Integer.parseInt(queue.remove()), queue.remove()));
	}
	
	/**
	 * Simply adds the input Party object to the collection.
	 * @param p Party object
	 */
	public void addParty(Party p) {		
		parties.add(p);
	}
	
	/**
	 * Splits input string into a queue and builds a Creature with it. Adds this to the party indicated by parentIndex.
	 * @param line
	 */
	private void addCreature(String line) {
		try {
			ArrayDeque<String> queue = splitLine(line);
			addCreature(new Creature(
					Integer.parseInt(queue.remove()), 					//index
					queue.remove(),										//type
					queue.remove(),										//name
					Integer.parseInt(queue.remove()),					//parent index
					Integer.parseInt(queue.remove()),					//empathy
					Integer.parseInt(queue.remove()),					//fear
					Double.parseDouble(queue.remove())));				//carrying capacity	
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
		if (c.getParentIndex() != 0) {
			CaveElement p = search(c.getParentIndex());
			if (p instanceof Party) {
				((Party) p).addCreature(c);
			} else {
				throw new Exception("Invalid parent index at creature: " + c.toString());
			}
		} else {
			looseObj.add(c);
		}
		
		
	}
	
	/**
	 * Splits the input string into a queue and uses that to build a Treasure object. Adds this to the creature indicated by parentIndex.
	 * @param line
	 */
	private void addTreasure(String line) {
		try {
			ArrayDeque<String> queue = splitLine(line);
			addTreasure(new Treasure(
					Integer.parseInt(queue.remove()),		//index
					queue.remove(),							//type
					Integer.parseInt(queue.remove()),		//parent index
					Double.parseDouble(queue.remove()),		//weight
					Double.parseDouble(queue.remove())));	//value
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
		if (t.getParentIndex() != 0) {
			CaveElement c = search(t.getParentIndex());
						
			if (c instanceof Creature) {
				((Creature) c).addTreasure(t);
			} else {
				throw new Exception("Invalid parent index at treasure: " + t.toString());
			}
			
		} else {
			looseObj.add(t);
		}
		
		
	}
	
	/**
	 * Takes a specifically formatted string input and splits into a queue. It uses this to make an Artifact object.
	 * @param line
	 */
	private void addArtifact(String line) {		
		try {
			ArrayDeque<String> queue = splitLine(line);
			addArtifact(new Artifact(
					Integer.parseInt(queue.remove()),		//index
					queue.remove(),							//type
					Integer.parseInt(queue.remove())));		//parent index
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
	
		if (a.getParentIndex() != 0) {
			CaveElement c = search(a.getParentIndex());
			
			if (c instanceof Creature) {
				((Creature) c).addArtifact(a);
			} else {
				throw new Exception("Invalid creature index at artifact: " + a.toString());
			}
		} else {
			looseObj.add(a);
		}	
	}
	
	public void addJob(String line) {
		
	}
	
	/**
	 * Removes a game element based on its index.
	 * @param index
	 */
	public void remove(int index) {
		parties.remove(search(index));
	}
	
	/**
	 * Iterates through every object in the game until a match is found.
	 * @param index
	 * @return
	 */
	public CaveElement search(int index) {
		CaveElement ce = null;
		for (Party p : parties) {
			ce = p.search(index);
			if (ce != null) return ce;
		}		
		return ce;
	}
	
	/**
	 * Iterates through every object in the game until a match is found. Throws exception for invalid input.
	 * @param type 0 for index, 1 for Name, 2 for Type
	 * @param target
	 * @return
	 * @throws Exception
	 */
	public CaveElement search(int type, String target) throws Exception {
		switch (type) {
		case 0:
			return search(Integer.parseInt(target));
		case 1:
		case 2:
			CaveElement ce = null;
			for (Party p : parties) {
				ce = p.search(type, target);
				if (ce != null) return ce;
			}
			break;
		default:
			throw new Exception(type + ": Invalid search selection. Must be 0, 1, or 2.");
		}
		
		return null;
	}
	/**
	 * Prints each party with the name and index. Each creature's name, type, and index belonging to the party is printed below and tabbed over.
	 * Each treasure and artifact's type and index is printed below the creature it belongs to.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Party p : parties) {
			sb.append(p + "\n");
		}
		if (!looseObj.isEmpty()) sb.append("Loose Objects:\n" + looseObj.toString());
		return sb.toString();
	}
}
