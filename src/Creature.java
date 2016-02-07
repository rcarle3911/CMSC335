import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * <li>FileName: Creature.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 2
 * <li>Author: Robert Lee Carle
 * <li>Date: 1/11/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Creature class which holds the characteristics of the creature and a list of artifacts and treasures.
 * <li>Due: 2/8/2016
 */
public class Creature extends CaveObject{
	private int empathy, fear;
	private double carryingCapacity, age = -1.0, height = -1.0, weight = -1.0;
	private ArrayList<Treasure> treasures = new ArrayList<Treasure>();
	private ArrayList<Artifact> artifacts = new ArrayList<Artifact>();
	

	public Creature(int index, String type, String name, int partyIndex, int empathy, int fear, double carryingCapacity) {
		super(index, type, partyIndex, name);
		setEmpathy(empathy);
		setFear(fear);
		setCarryCap(carryingCapacity);
	}
	
	public void addTreasure(Treasure t) {
		treasures.add(t);
	}
	
	public void addArtifact(Artifact a) {
		artifacts.add(a);
	}
	
	public int getEmpathy() {
		return empathy;
	}
	
	public int getFear() {
		return fear;
	}
	
	public double getCarryCap() {
		return carryingCapacity;
	}
	
	public ArrayList<Treasure> getTreasures() {
		return treasures;
	}
	
	public ArrayList<Artifact> getArtifacts() {
		return artifacts;
	}
	
	public double getAge() {
		return age;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setEmpathy(int empathy) {
		this.empathy = empathy;
	}
	
	public void setFear(int fear) {
		this.fear = fear;
	}
	
	public void setCarryCap(double carryingCapacity) {
		this.carryingCapacity = carryingCapacity;
	}
	
	public void setAge(double age) {
		this.age = age;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	/**
	 * First checks if this object is a match. Then, it continues the search through the treasures and artifacts.
	 * @param index
	 * @return CaveElement Objects that matched the search criteria
	 */
	@Override
	public ArrayList<CaveElement> searchIndex(int index) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
		if (getIndex() == index) matched.add(this);
		
		for(Treasure t : treasures) {
			if (t.getIndex() == index) matched.add(t);
		}
		
		for(Artifact a : artifacts) {
			if (a.getIndex() == index) matched.add(a);
		}
		
		return matched;
	}
	
	/**
	 * First checks if this object is a match. Then, it continues the search through the treasures and artifacts.
	 * @param target
	 * @return CaveElement Objects that matched the search criteria
	 */
	@Override
	public ArrayList<CaveElement> searchName(String target) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
		if (getName().equals(target)) matched.add(this);
		
		for(Treasure t : treasures) {
			if (t.getName().equals(target)) matched.add(t);
		}
		
		for(Artifact a : artifacts) {
			if (a.getName().equals(target)) matched.add(a);
		}
		return matched;
	}
	
	/**
	 * First checks if this object is a match. Then, it continues the search through the treasures and artifacts.
	 * @param target
	 * @return CaveElement Objects that matched the search criteria
	 */
	@Override
	public ArrayList<CaveElement> searchType(String target) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
		if (getType().equals(target)) matched.add(this);
		
		for(Treasure t : treasures) {
			if (t.getType().equals(target)) matched.add(t);
		}
		
		for(Artifact a : artifacts) {
			if (a.getType().equals(target)) matched.add(a);
		}
		return matched;
	}
	
	public void sortName() {
		Collections.sort(artifacts, new Comparator<Artifact>() {
			@Override
			public int compare(Artifact a1, Artifact a2) {				
				return a1.getName().compareTo(a2.getName());
			}
		});
	}
	
	public void sortWeight() {
		Collections.sort(treasures, new Comparator<Treasure>() {
			@Override
			public int compare(Treasure t1, Treasure t2) {				
				return Double.compare(t1.getWeight(), t2.getWeight());
			}
		});
	}
	
	public void sortValue() {
		Collections.sort(treasures, new Comparator<Treasure>() {
			@Override
			public int compare(Treasure t1, Treasure t2) {				
				return Double.compare(t1.getValue(), t2.getValue());
			}
		});
	}

	@Override
	public String toString() { 
		StringBuilder sb = new StringBuilder(super.toString());
		sb.insert(1, getName() + ", ");
		if (getAge() >= 0) sb.insert(sb.length() - 1, ", " + getAge());
		if (getHeight() >= 0) sb.insert(sb.length() - 1, ", " + getHeight());
		if (getWeight() >= 0) sb.insert(sb.length() - 1, ", " + getWeight());
		if (!artifacts.isEmpty()) sb.append("\n\tArtifacts: " + artifacts.toString());
		if (!treasures.isEmpty()) sb.append("\n\tTreasures: " + treasures.toString());
		return sb.toString();
	}
}
