/**
 * <li>FileName: Artifact.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Final
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/27/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Artifact class.
 * <li>3/7/2016
 */
public class Artifact extends CaveElement{
	
	/**
	 * Constructor for an artifact.
	 * @param index Integer which follows the format 4XXXX.
	 * @param type String representation of the artifact's type i.e. wand, potion etc.
	 * @param creatureIndex Index of owning creature
	 */
	public Artifact(int index, String type) {
		super(index, "", type);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("T: " + getType() + ", I: " + getIndex());
		if (!getName().equals("")) sb.insert(0, getName() + ", ");
		return sb.toString();		
	}
	
}
