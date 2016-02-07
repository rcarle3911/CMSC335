import java.util.ArrayList;

/**
 * <li>FileName: CaveElement.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 2
 * <li>Author: Robert Lee Carle
 * <li>Date: 1/17/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: CaveElement class is the parent class that all elements of the game derive from. Most importantly enforces everything has an index.
 * <li>Due: 2/8/2016
 */
public abstract class CaveElement {
	private String name;
	private String type;
	private int index;
	
	public CaveElement(int index) {
		this(index, "");
	}
	
	public CaveElement(int index, String name) {
		setIndex(index);
		setName(name);
	}
	
	public CaveElement(int index, String name, String type) {
		this(index, name);
		setType(type);
	}
	
	private void setIndex(int index) {
		this.index = index;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	/**
	 * Default search method checks if this object is a match and returns an array of consisting of itself if true.
	 * @param index
	 * @return CaveElement Objects that matched the search criteria
	 */
	public ArrayList<CaveElement> searchIndex(int index) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
		if (getIndex() == index) matched.add(this);
		return matched;
	}
	
	/**
	 * Default search method checks if this object is a match and returns an array of consisting of itself if true.
	 * @param target
	 * @return CaveElement Objects that matched the search criteria
	 */
	public ArrayList<CaveElement> searchName(String target) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
		if (getName().equals(target)) matched.add(this);
		return matched;
	}
	
	/**
	 * Default search method checks if this object is a match and returns an array of consisting of itself if true.
	 * @param target
	 * @return CaveElement Objects that matched the search criteria
	 */
	public ArrayList<CaveElement> searchType(String target) {
		ArrayList<CaveElement> matched = new ArrayList<CaveElement>();
		if (getType().equals(target)) matched.add(this);
		return matched;
	}
	
	@Override
	public String toString() {
		return "(" + name + ", " + index + ")";
	}
}
