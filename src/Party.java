import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * <li>FileName: Party.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 3
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/8/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Party class which holds a list of creatures.
 * <li>Due: 2/22/2016
 */
public class Party extends CaveElement{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1234L;

	public Party(int index, String name) {
		super(index, name);
		
	}
	
	public void addCreature(Creature c) {
		add(c);
	}
	
	public Vector<CaveElement> getCreatures() {
		return this.getChildren();
	}
	
	public void sortName() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {				
				return c1.getName().compareTo(c2.getName());
			}
		});
	}
	
	public void sortAge() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {				
				return Double.compare(((Creature) c1).getAge(), ((Creature) c2).getAge());
			}
		});
	}
	
	public void sortHeight() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {				
				return Double.compare(((Creature) c1).getHeight(), ((Creature) c2).getHeight());
			}
		});
	}
	
	public void sortWeight() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {				
				return Double.compare(((Creature) c1).getWeight(), ((Creature) c2).getWeight());
			}
		});
	}
	
	public void sortEmpathy() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {
				return ((Creature) c1).getEmpathy() - ((Creature) c2).getEmpathy();
			}
		});
	}
	
	public void sortFear() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {
				return ((Creature) c1).getFear() - ((Creature) c2).getFear();
			}
		});
	}
	
	public void sortCarryCap() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {
				return Double.compare(((Creature) c1).getCarryCap(), ((Creature) c2).getCarryCap());
			}
		});
	}
	
	@Override
	public String toString() {		
		return (getName() + ", I: " + getIndex());
	}
}
