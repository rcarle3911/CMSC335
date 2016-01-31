/**
 * FileName: Artifact.java
 * Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * Project 1
 * Author: Robert Lee Carle
 * Date: 1/11/2016
 * Platform/Compiler: Java 8 with Eclipse IDE
 * Instructor: Nicholas Duchon
 * Purpose: Artifact class that is extending but not adding anything to GameObject.
 * Due: 1/24/2016
 */
public class Artifact extends CaveObject{
	
	/**
	 * Constructor for an artifact.
	 * @param index Integer which follows the format 4XXXX.
	 * @param type String representation of the artifact's type i.e. wand, potion etc.
	 * @param creatureIndex Index of owning creature
	 */
	public Artifact(int index, String type, int creatureIndex) {
		super(index, type, creatureIndex);
	}
	
}
