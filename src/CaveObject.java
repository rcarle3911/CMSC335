/**
 * <li>FileName: GameObject.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 2
 * <li>Author: Robert Lee Carle
 * <li>Date: 1/17/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Basic GameObject class that holds an index, parentIndex, and type fields.
 * <li>Due: 2/8/2016
 */
public abstract class CaveObject extends CaveElement{
	private int parentIndex;
	
	public CaveObject(int index, String type, int parentIndex) {
		this(index, type, parentIndex, "");
	}
	
	public CaveObject(int index, String type, int parentIndex, String name) {
		super(index, name, type);
		setParentIndex(parentIndex);
	}
	
	public int getParentIndex() {
		return parentIndex;
	}
	
	public void setParentIndex(int parentIndex) {
		this.parentIndex = parentIndex;
	}
	
	@Override
	public String toString() {
		return "(" + getType() + ", " + getIndex() + ")";
	}
	
}
