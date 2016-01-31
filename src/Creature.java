import java.util.ArrayList;

/**
 * FileName: Creature.java
 * Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * Project 1
 * Author: Robert Lee Carle
 * Date: 1/11/2016
 * Platform/Compiler: Java 8 with Eclipse IDE
 * Instructor: Nicholas Duchon
 * Purpose: Creature class which holds the characteristics of the creature and a list of artifacts and treasures.
 * Due: 1/24/2016
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
	
	/**
	 * First checks if this object is a match. Otherwise, it continues the search through the creatures.
	 * @param index
	 * @return CaveObject
	 */
	public CaveObject search(int index) {
		if (getIndex() == index) return this;
		
		for (Treasure t : treasures) {
			if (t.getIndex() == index) return t;
		}
		
		for (Artifact a : artifacts) {
			if (a.getIndex() == index) return a;
		}
		
		return null;
	}
	
	/**
	 * First checks if it's a match. Otherwise, it continues the search through its treasures and artifacts.
	 * Throws exception if type input is invalid.
	 * @param type 0 for index, 1 for Name, 2 for Type
	 * @param target
	 * @return
	 * @throws Exception
	 */
	public CaveObject search(int type, String target) throws Exception {
			
		switch (type) {
		case 0:
			return search(Integer.parseInt(target));
		case 1:
			if (getName().equals(target)) return this;
			
			for (Treasure t : treasures) {
				if (t.getName().equals(target)) return t;
			}
			
			for (Artifact a : artifacts) {
				if (a.getName().equals(target)) return a;
			}
			
			break;
			
		case 2:
			if (getType().equals(target)) return this;
			
			for (Treasure t : treasures) {
				if (t.getType().equals(target)) return t;
			}
			
			for (Artifact a : artifacts) {
				if (a.getType().equals(target)) return a;
			}
			
			break;
		default:
			throw new Exception(type + ": Invalid search selection. Must be 0, 1, or 2");
		}
		
		return null;
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
