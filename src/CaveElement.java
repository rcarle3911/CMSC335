import java.util.*;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;


/**
 * <li>FileName: CaveElement.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Final
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/27/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: <code>CaveElement</code> is the parent class that all elements of the game derive from. Most importantly enforces everything has an index.
 * and search functions. I copied most of the code from <code>DefaultMutableTreeNode</code> and modified it a bit to get rid of warnings. I had to change the
 * <code>Vector</code> children from raw to generic type.
 * <li>Due: 3/7/2016
 */
public class CaveElement implements MutableTreeNode{
	
	private String name, type;
	private int caveIndex;
	protected Vector<CaveElement> children;
	protected CaveElement parent;
	transient protected Object userObject;
	static public final Enumeration<CaveElement> EMPTY_ENUMERATION = Collections.emptyEnumeration();

	
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
	
	protected void setParent(CaveElement parent) {
		this.parent = parent;
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
		return parent;
	}
	
	protected Vector<CaveElement> getChildren() {
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
			for(CaveElement c : children) {
				matched.addAll(c.searchType(target));
			}	
		}
			
		return matched;
	}
	
	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	public void add(CaveElement e) {
		if(e != null && e.getParent() == this)
            insert(e, getChildCount() - 1);
        else
            insert(e, getChildCount());
	}
	
	@Override
	public String toString() {		
		return name;
	}

	@Override
	public Enumeration<CaveElement> children() {
		if (children == null) {
            return EMPTY_ENUMERATION;
        } else {
            return children.elements();
        }

	}

	@Override
	public TreeNode getChildAt(int index) {
		if (children == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }
        return (TreeNode)children.elementAt(index);
	}

	@Override
	public int getChildCount() {
		if (children == null) {
            return 0;
        } else {
            return children.size();
        }

	}

	@Override
	public int getIndex(TreeNode child) {
		if (child == null) {
			throw new IllegalArgumentException("argument is null");
		}
		
	    return children.indexOf(child); 
	}

	@Override
	public boolean isLeaf() {
		return (getChildCount() == 0);
	}

	@Override
	public void insert(MutableTreeNode newChild, int index) {
		if (newChild == null) {
            throw new IllegalArgumentException("new child is null");
        }

        MutableTreeNode oldParent = (MutableTreeNode)newChild.getParent();

        if (oldParent != null) {
        	oldParent.remove(newChild);
        }
        newChild.setParent(this);
        if (children == null) {
        	children = new Vector<CaveElement>();
        }
        children.insertElementAt((CaveElement) newChild, index);

	}

	@Override
	public void remove(int childIndex) {
		if (childIndex == -1) throw new IllegalArgumentException("argument is not a child");
	    CaveElement child = (CaveElement)getChildAt(childIndex);
	    children.removeElementAt(childIndex);
	    child.setParent(null);
	    
		
	}

	@Override
	public void remove(MutableTreeNode aChild) {
		 if (aChild == null) {
			 throw new IllegalArgumentException("argument is null");
		 }	      
	     
		 remove(getIndex(aChild));
		
	}

	@Override
	public void removeFromParent() {
		CaveElement parent = (CaveElement)getParent();
        if (parent != null) {
            parent.remove(this);
        }		
	}

	@Override
	public void setParent(MutableTreeNode parent) {
		this.parent = (CaveElement) parent;
	}

	@Override
	public void setUserObject(Object obj) {
		userObject = obj;
		
	}
	
	public void removeAllChildren() {
		for (int i = getChildCount()-1; i >= 0; i--) {
            remove(i);
        }

	}

	public Object getUserObject() {
		return userObject;
	}
}
