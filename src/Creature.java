import java.util.ArrayList;

/**
 * <li>FileName: Creature.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 2
 * <li>Author: Robert Lee Carle
 * <li>Date: 1/11/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Creature class which holds the characteristics of the creature and a list of artifacts and treasures.
 * <li>Due: 1/24/2016
 */
public class Creature extends CaveObject{
	private int empathy, fear;
	private double carryingCapacity;
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
	
	public void setEmpathy(int empathy) {
		this.empathy = empathy;
	}
	
	public void setFear(int fear) {
		this.fear = fear;
	}
	
	public void setCarryCap(double carryingCapacity) {
		this.carryingCapacity = carryingCapacity;
	}
	
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
	
	@Override
	public String toString() { 
		StringBuilder sb = new StringBuilder(super.toString());
		sb.insert(1, getName() + ", ");
		if (!artifacts.isEmpty()) sb.append("\n\tArtifacts: " + artifacts.toString());
		if (!treasures.isEmpty()) sb.append("\n\tTreasures: " + treasures.toString());
		sb.append('\n');
		return sb.toString();
	}
}
