/**
 * <li>FileName: CaveElement.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 2
 * <li>Author: Robert Lee Carle
 * <li>Date: 1/17/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: CaveElement class is the parent class that all elements of the game derive from. Most importantly enforces everything has an index.
 * <li>Due: 1/24/2016
 */
public abstract class CaveElement {
	private String name;
	private int index;
	
	public CaveElement(int index) {
		this(index, "");
	}
	
	public CaveElement(int index, String name) {
		setIndex(index);
		setName(name);
	}
	
	private void setIndex(int index) {
		this.index = index;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "(" + name + ", " + index + ")";
	}
}
