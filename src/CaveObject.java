/**
 * <li>FileName: GameObject.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 2
 * <li>Author: Robert Lee Carle
 * <li>Date: 1/17/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Basic GameObject class that holds an index, parentIndex, and type fields.
 * <li>Due: 1/24/2016
 */
public abstract class CaveObject extends CaveElement{
	private int parentIndex;
	private String type;
	
	public CaveObject(int index, String type, int parentIndex) {
		super(index);	
		setType(type);
		setParentIndex(parentIndex);
	}
	
	public CaveObject(int index, String type, int parentIndex, String name) {
		super(index, name);
		setType(type);
		setParentIndex(parentIndex);
	}
	
	public int getParentIndex() {
		return parentIndex;
	}
	
	public String getType() {
		return type;
	}
	
	public void setParentIndex(int parentIndex) {
		this.parentIndex = parentIndex;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "(" + type + ", " + getIndex() + ")";
	}
	
}
