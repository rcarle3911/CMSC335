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
	public CaveElement search(int index) {
		if (getIndex() == index) return this;
		CaveObject co = null;
		for(Creature c : creatures) {
			co = c.search(index);
			if (co != null) break;
		}
		return co;
	}
	
	/**
	 * First checks if it's a match. Otherwise, it continues the search through its creatures.
	 * Throws exception if type input is invalid.
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
			if (getName().equals(target)) return this;
			
		case 2:
			CaveObject co = null;
			for(Creature c : creatures) {
				co = c.search(type, target);
				if (co != null) return co;
			}
			break;
		default:
			throw new Exception(type + ": Invalid search selection. Must be 0,1, or 2.");				
		}
		
		return null;
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
