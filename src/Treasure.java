/**
 * FileName: Treasure.java
 * Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * Project 1
 * Author: Robert Lee Carle
 * Date: 1/11/2016
 * Platform/Compiler: Java 8 with Eclipse IDE
 * Instructor: Nicholas Duchon
 * Purpose: Treasure class that extends GameObject and adds weight and value fields.
 * Due: 1/24/2016
 */
public class Treasure extends CaveObject{
	private double weight, value;
	
	/**
	 * Constructor method for treasure.
	 * @param index Integer which follows the format 3XXXX.
	 * @param value Value of the treasure as a double.
	 * @param type String representation of the treasure's type i.e. gold, gems etc.
	 * @param weight Weight of the treasure as a double.
	 */
	public Treasure(int index, String type, int parentIndex, double weight, double value) {
		super(index, type, parentIndex);		
		setWeight(weight);
		setValue(value);
	}
	
	
	/**
	 * Setter for value field.
	 * @param value An integer.
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
	/**
	 * Setter for weight field.
	 * @param weight A double.
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	/**
	 * Getter for value field.
	 * @return The integer representation of value.
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * Getter for weight value.
	 * @return The double representation of weight.
	 */
	public double getWeight() {
		return weight;
	}
	
}
