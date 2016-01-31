import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * <li>FileName: Cave.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 2
 * <li>Author: Robert Lee Carle
 * <li>Date: 1/11/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Cave class which holds the parties and loads a data file.
 * <li>Due: 1/24/2016
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
					addJob(line, hm);
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
	private void addParty(String line, HashMap<Integer, CaveElement> hm) {
		ArrayDeque<String> queue = splitLine(line);
		Party p = new Party(Integer.parseInt(queue.remove()),			//index
							queue.remove());							//name
		hm.put(p.getIndex(), p);
		addParty(p);
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
	private void addCreature(String line, HashMap<Integer, CaveElement> hm) {
		try {
			ArrayDeque<String> queue = splitLine(line);
			Creature c = new Creature(
					Integer.parseInt(queue.remove()), 					//index
					queue.remove(),										//type
					queue.remove(),										//name
					Integer.parseInt(queue.remove()),					//parent index
					Integer.parseInt(queue.remove()),					//empathy
					Integer.parseInt(queue.remove()),					//fear
					Double.parseDouble(queue.remove()));				//carrying capacity
			
			hm.put(c.getIndex(), c);
			
			if (c.getParentIndex() != 0) {
				CaveElement p = hm.get(c.getParentIndex());
				if (p instanceof Party) {
					((Party) p).addCreature(c);
				} else {
					throw new Exception("Invalid parent index at creature: " + c.toString());
				}
			} else {
				looseObj.add(c);
			}
			
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
			CaveElement p = searchIndex(c.getParentIndex()).get(0);
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
	private void addTreasure(String line, HashMap<Integer, CaveElement> hm) {
		try {
			ArrayDeque<String> queue = splitLine(line);
			Treasure t = new Treasure(
					Integer.parseInt(queue.remove()),		//index
					queue.remove(),							//type
					Integer.parseInt(queue.remove()),		//parent index
					Double.parseDouble(queue.remove()),		//weight
					Double.parseDouble(queue.remove()));	//value			
			
			if (t.getParentIndex() != 0) {
				CaveElement c = hm.get(t.getParentIndex());
				if (c instanceof Creature) {
					((Creature) c).addTreasure(t);
				} else {
					throw new Exception("Invalid parent index at treasure: " + t.toString());
				}
			} else {
				looseObj.add(t);
			}
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
			CaveElement c = searchIndex(t.getParentIndex()).get(0);
						
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
	private void addArtifact(String line, HashMap<Integer, CaveElement> hm) {		
		try {
			ArrayDeque<String> queue = splitLine(line);
			Artifact a = new Artifact(
					Integer.parseInt(queue.remove()),		//index
					queue.remove(),							//type
					Integer.parseInt(queue.remove()));		//parent index
			
			if (a.getParentIndex() != 0) {
				CaveElement c = hm.get(a.getParentIndex());
				if (c instanceof Creature) {
					((Creature) c).addArtifact(a);
				} else {
					throw new Exception("Invalid parent index at treasure: " + a.toString());
				}
			} else {
				looseObj.add(a);
			}			
			
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
			CaveElement c = searchIndex(a.getParentIndex()).get(0);
			
			if (c instanceof Creature) {
				((Creature) c).addArtifact(a);
			} else {
				throw new Exception("Invalid creature index at artifact: " + a.toString());
			}
		} else {
			looseObj.add(a);
		}	
	}
	
	private void addJob(String line, HashMap<Integer, CaveElement> hm) {
		
	}
	
	/**
	 * Removes a game element based on its index.
	 * @param index
	 */
	public void remove(int index) {
		parties.remove(searchIndex(index));
	}
	
	/**
	 * Iterates through every object in the game until a match is found.
	 * @param index
	 * @return
	 */
	public ArrayList<CaveElement> searchIndex(int index) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
		
		for (Party p : parties) {
			matched.addAll(p.searchIndex(index));
		}
		
		for (CaveElement ce : looseObj) {
			matched.addAll(ce.searchIndex(index));
		}
		
		return matched;
	}
	
	public ArrayList<CaveElement> searchName(String target) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();	
		
		for (Party p : parties) {
			matched.addAll(p.searchName(target));
		}
		
		for (CaveElement ce : looseObj) {
			matched.addAll(ce.searchName(target));
		}
		
		return matched;
	}
	
	public ArrayList<CaveElement> searchType(String target) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();	
		
		for (Party p : parties) {
			matched.addAll(p.searchType(target));
		}
		
		for (CaveElement ce : looseObj) {
			matched.addAll(ce.searchType(target));
		}
		return matched;
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
