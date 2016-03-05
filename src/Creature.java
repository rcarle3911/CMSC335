import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * <li>FileName: Creature.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Final
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/27/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Creature class which holds the characteristics of the creature and a list of artifacts and treasures.
 * <li>Due: 3/7/2016
 */
public class Creature extends CaveElement{
	
	private int empathy, fear;
	private double carryingCapacity, age = -1.0, height = -1.0, weight = -1.0;
	private boolean busyFlag = false;
	private Job job = null;

	public Creature(int index, String type, String name, int empathy, int fear, double carryingCapacity) {
		super(index, name, type);
		setEmpathy(empathy);
		setFear(fear);
		setCarryCap(carryingCapacity);
		
		setUserObject(0);
		
		add (new CaveElement(-1, "Treasures"));
		add (new CaveElement(-1, "Artifacts"));
		add (new CaveElement(-1, "Jobs"));
	}
	
	public void addTreasure(Treasure t) {
		((CaveElement) getChildAt(0)).add(t);
	}
	
	public void addArtifact(Artifact a) {
		((CaveElement) getChildAt(1)).add(a);		
	}
	
	public void addJob(Job j) {
		((CaveElement) getChildAt(2)).add(j);
	}
	
	public void setJob(Job j) {
		this.job = j;
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
	
	public Vector<CaveElement> getTreasures() {
		return ((CaveElement) getChildAt(0)).getChildren();
	}
	
	public Vector<CaveElement> getArtifacts() {
		return ((CaveElement) getChildAt(1)).getChildren();
	}
	
	public Vector<CaveElement> getJobs() {
		return ((CaveElement) getChildAt(2)).getChildren();
	}
	
	public Job getJob() {
		return job;
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
	
	public boolean getBusyFlag() {
		return busyFlag;
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
	
	public void setBusyFlag(boolean b) {
		busyFlag = b;
	}	
	
	public void sortName() {
		if (getArtifacts() == null) return;
		Collections.sort(getArtifacts(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement a1, CaveElement a2) {				
				return a1.getName().compareTo(a2.getName());
			}
		});
	}
	
	public void sortWeight() {
		if (getTreasures() == null) return;
		Collections.sort(getTreasures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement t1, CaveElement t2) {				
				return Double.compare(((Treasure) t1).getWeight(), ((Treasure) t2).getWeight());
			}
		});
	}
	
	public void sortValue() {
		if (getTreasures() == null) return;
		Collections.sort(getTreasures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement t1, CaveElement t2) {				
				return Double.compare(((Treasure) t1).getValue(), ((Treasure) t2).getValue());
			}
		});
	}
	
	@Override
	public String toString() { 
		StringBuilder sb = new StringBuilder();
		sb.append(getName() + ", I: " + getIndex() + ", T: " + getType() + ", E: " + getEmpathy() + ", F: " + getFear() + ", C: " + getCarryCap());
		if (getAge() >= 0) sb.insert(sb.length(), ", A: " + getAge());
		if (getHeight() >= 0) sb.insert(sb.length(), ", H: " + getHeight());
		if (getWeight() >= 0) sb.insert(sb.length(), ", W: " + getWeight());
		return sb.toString();
	}
}
