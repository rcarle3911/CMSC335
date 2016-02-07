/**
 * <li>FileName: Treasure.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 2
 * <li>Author: Robert Lee Carle
 * <li>Date: 1/11/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Treasure class that extends GameObject and adds weight and value fields.
 * <li>Due: 2/8/2016
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.insert(sb.length() - 1, ", " + this.getWeight() + ", " + this.getValue());
		return sb.toString();		
	}
	
}
