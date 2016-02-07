import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * <li>FileName: Party.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 2
 * <li>Author: Robert Lee Carle
 * <li>Date: 1/11/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Party class which holds a list of creatures and can perform search operations.
 * <li>Due: 2/8/2016
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
	 * First checks if this object is a match. Then, it continues the search through the creatures.
	 * @param index
	 * @return CaveElement Objects that matched the search criteria
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
	
	/**
	 * First checks if this object is a match. Then, it continues the search through the creatures.
	 * @param target
	 * @return CaveElement Objects that matched the search criteria
	 */
	@Override
	public ArrayList<CaveElement> searchName(String target) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
		if (getName().equals(target)) matched.add(this);
		
		for(Creature c : creatures) {
			matched.addAll(c.searchName(target));
		}
		return matched;
	}
	
	/**
	 * First checks if this object is a match. Then, it continues the search through the creatures.
	 * @param target
	 * @return CaveElement Objects that matched the search criteria
	 */
	@Override
	public ArrayList<CaveElement> searchType(String target) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
				
		for(Creature c : creatures) {
			matched.addAll(c.searchType(target));
		}
		return matched;
	}

	public void sortName() {
		Collections.sort(creatures, new Comparator<Creature>() {
			@Override
			public int compare(Creature c1, Creature c2) {				
				return c1.getName().compareTo(c2.getName());
			}
		});
	}
	
	public void sortAge() {
		Collections.sort(creatures, new Comparator<Creature>() {
			@Override
			public int compare(Creature c1, Creature c2) {				
				return Double.compare(c1.getAge(), c2.getAge());
			}
		});
	}
	
	public void sortHeight() {
		Collections.sort(creatures, new Comparator<Creature>() {
			@Override
			public int compare(Creature c1, Creature c2) {				
				return Double.compare(c1.getHeight(), c2.getHeight());
			}
		});
	}
	
	public void sortWeight() {
		Collections.sort(creatures, new Comparator<Creature>() {
			@Override
			public int compare(Creature c1, Creature c2) {				
				return Double.compare(c1.getWeight(), c2.getWeight());
			}
		});
	}
	
	public void sortEmpathy() {
		Collections.sort(creatures, new Comparator<Creature>() {
			@Override
			public int compare(Creature c1, Creature c2) {
				return c1.getEmpathy() - c2.getEmpathy();
			}
		});
	}
	
	public void sortFear() {
		Collections.sort(creatures, new Comparator<Creature>() {
			@Override
			public int compare(Creature c1, Creature c2) {
				return c1.getFear() - c2.getFear();
			}
		});
	}
	
	public void sortCarryCap() {
		Collections.sort(creatures, new Comparator<Creature>() {
			@Override
			public int compare(Creature c1, Creature c2) {
				return Double.compare(c1.getCarryCap(), c2.getCarryCap());
			}
		});
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		for (Creature c : creatures) {
			sb.append("\n\t" + c + "\n");
		}
		
		return sb.toString();
				
	}
}
