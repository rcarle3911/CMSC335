import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;


/**
 * <li>FileName: CaveElement.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 3
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/8/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: CaveElement class is the parent class that all elements of the game derive from. Most importantly enforces everything has an index
 * and search functions.
 * <li>Due: 2/22/2016
 */
public class CaveElement extends DefaultMutableTreeNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234L;
	private String name, type;
	private int caveIndex;
	
	public CaveElement(int index) {
		this(index, "");
	}
	
	public CaveElement(int index, String name) {
		this(index, name, "");
		
	}
	
	public CaveElement(int index, String name, String type) {
		setIndex(index);
		setName(name);
		setType(type);
	}
	
	private void setIndex(int index) {
		this.caveIndex = index;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setParent(CaveElement parent) {
		super.setParent(parent);;
	}
	
	public int getIndex() {
		return caveIndex;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public CaveElement getParent() {
		return (CaveElement) super.getParent();
	}
	
	@SuppressWarnings("unchecked")
	public Vector<CaveElement> getChildren() {
		return children;
	}
	
	/**
	 * Default search method checks if this object is a match and returns an array of consisting of itself if true.
	 * @param index
	 * @return CaveElement Objects that matched the search criteria
	 */
	public Vector<CaveElement> searchIndex(int index) {		
		Vector<CaveElement> matched = new Vector<CaveElement>();
		if (index < 0) return matched;
		if (getIndex() == index) matched.add(this);
		
		if (!isLeaf()) {
			for(Object c : children) {
				matched.addAll(((CaveElement) c).searchIndex(index));
			}	
		}
			
		return matched;
	}
	
	/**
	 * Default search method checks if this object is a match and returns an array of consisting of itself if true.
	 * @param target
	 * @return CaveElement Objects that matched the search criteria
	 */
	public Vector<CaveElement> searchName(String target) {
		Vector<CaveElement> matched = new Vector<CaveElement>();
		if (getName().equals(target)) matched.add(this);
		
		if (!isLeaf()) {
			for(Object c : children) {
				matched.addAll(((CaveElement) c).searchName(target));
			}	
		}
			
		return matched;
	}
	
	/**
	 * Default search method checks if this object is a match and returns an array of consisting of itself if true.
	 * @param target
	 * @return CaveElement Objects that matched the search criteria
	 */
	public Vector<CaveElement> searchType(String target) {
		Vector<CaveElement> matched = new Vector<CaveElement>();
		if (getType().equals(target)) matched.add(this);
		
		if (!isLeaf()) {
			for(Object c : children) {
				matched.addAll(((CaveElement) c).searchType(target));
			}	
		}
			
		return matched;
	}
	
	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public void add(MutableTreeNode e) {
		super.insert(e, getChildCount());
	}
	
	@Override
	public String toString() {		
		return name;
	}
}
