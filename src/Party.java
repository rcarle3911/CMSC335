import java.util.ArrayList;

/**
 * <li>FileName: Party.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 2
 * <li>Author: Robert Lee Carle
 * <li>Date: 1/11/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Party class which holds a list of creatures and can perform search operations.
 * <li>Due: 1/24/2016
 */
public class Party extends CaveElement {
	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	
	public Party(int index, String name) {
		super(index, name);
	}
	
	public void addCreature(Creature c) {
		creatures.add(c);
	}
	
	public ArrayList<Creature> getCreatures() {
		return creatures;
	}
	
	/**
	 * First checks if this object is a match. Otherwise, it continues the search through the creatures.
	 * @param index
	 * @return CaveElement
	 */
	@Override
	public ArrayList<CaveElement> searchIndex(int index) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
		if (getIndex() == index) matched.add(this);
		
		for(Creature c : creatures) {
			matched.addAll(c.searchIndex(index));
		}
		return matched;
	}
	
	@Override
	public ArrayList<CaveElement> searchName(String target) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
		if (getName().equals(target)) matched.add(this);
		
		for(Creature c : creatures) {
			matched.addAll(c.searchName(target));
		}
		return matched;
	}
	
	@Override
	public ArrayList<CaveElement> searchType(String target) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
				
		for(Creature c : creatures) {
			matched.addAll(c.searchType(target));
		}
		return matched;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		for (Creature c : creatures) {
			sb.append("\n\t" + c);
		}
		
		return sb.toString();
				
	}
}
