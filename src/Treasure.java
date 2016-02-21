/**
 * <li>FileName: Treasure.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 3
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/8/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Treasure class a CaveObject that holds weight and value fields.
 * <li>Due: 2/22/2016
 */
public class Treasure extends CaveElement{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234L;
	private double weight, value;
	
	/**
	 * Constructor method for treasure.
	 * @param index Integer which follows the format 3XXXX.
	 * @param value Value of the treasure as a double.
	 * @param type String representation of the treasure's type i.e. gold, gems etc.
	 * @param weight Weight of the treasure as a double.
	 */
	public Treasure(int index, String type, double weight, double value) {
		super(index, "", type);		
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
		return (getType() + ", I: " + getIndex() + ", W: " + getWeight() + ", V: " + getValue());
	}
	
}
