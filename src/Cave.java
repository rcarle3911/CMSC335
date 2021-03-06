import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.tree.*;

/**
 * <li>FileName: Cave.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Final
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/27/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Cave class which holds the parties and loads a data file.
 * <li>Due: 3/7/2016
 */
public class Cave extends CaveElement{	
	
	public Cave(String name) {
		super(-1, name);
		
		add(new CaveElement(-1, "Parties"));
		add(new CaveElement(-1, "Loose Objects"));
	}	

	
	
	/**
	 * Takes an input string and separates each value between the colons (:). Trims the extra space and puts it in a queue.
	 * @param line
	 * @return Queue A queue of string inputs.
	 */
	private ArrayDeque<String> splitLine(String line) {
		ArrayDeque<String> queue = new ArrayDeque<String>();
		Scanner s = new Scanner(line);
		s.useDelimiter(":");
		s.next();
		
		while (s.hasNext()) {
			queue.add(s.next().trim());
		}
		
		s.close();		
		return queue;
	}
	
	/**
	 * Takes a line of inputs in a specific format, splits it, and creates a new Party object. Adds it to the collection.
	 * @param line String of inputs
	 * @param hm Hashmap of objects read in
	 */
	protected Party addParty(String line) {
		ArrayDeque<String> queue = splitLine(line);
		Party p = new Party(Integer.parseInt(queue.poll()),
							queue.poll());
		addParty(p);
		return p;
	}
	
	/**
	 * Simply adds the input Party object to the collection.
	 * @param p Party object
	 */
	public void addParty(Party p) {		
		((CaveElement) getChildAt(0)).add(p);
	}
	
	/**
	 * Splits input string into a queue and builds a Creature with it. Adds this to the party indicated by parentIndex.
	 * @param line
	 * @param hm Hashmap of objects read in
	 */
	protected Creature addCreature(String line, HashMap<Integer, Party> hm) {
		try {
			ArrayDeque<String> queue = splitLine(line);
			
			int index =	Integer.parseInt(queue.poll()); 				
			String type = queue.poll();									
			String name = queue.poll();	
			Party parent = hm.get(Integer.parseInt(queue.poll()));
			int emp = Integer.parseInt(queue.poll());
			int fear = Integer.parseInt(queue.poll());	
			double carryCap = Double.parseDouble(queue.poll());	
			
			Creature c = new Creature(index, type, name, emp, fear, carryCap);
			
			if (!queue.isEmpty()) {
				c.setAge(Double.parseDouble(queue.poll()));
				if (!queue.isEmpty()) {
					c.setHeight(Double.parseDouble(queue.poll()));
					if (!queue.isEmpty()) {
						c.setWeight(Double.parseDouble(queue.poll()));
					}
				}
			}			
			
			if (parent != null) parent.add(c);
			else addCreature(c);
			
			return c;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Adds the input creature to the party indicated by parentIndex. Throws an Exception when there is no match or parent is not an instance of Party.
	 * Adds any creature with parentIndex 0 to the looseObj collection.
	 * @param c Creature
	 * @throws Exception
	 */
	public void addCreature(Creature c) throws Exception {		
		if (c.getParent() != null) {
			CaveElement p = c.getParent();
			if (p instanceof Party) {
				((Party) p).addCreature(c);
			} else {
				throw new Exception("Invalid parent index at creature: " + c.toString());
			}
		} else {
			((CaveElement) getChildAt(1)).add(c); //Adds to loose objects
		}
	}
	
	/**
	 * Splits the input string into a queue and uses that to build a Treasure object. Adds this to the creature indicated by parentIndex.
	 * @param line
	 * @param hm Hashmap of objects read in
	 */
	protected void addTreasure(String line, HashMap<Integer, Creature> hm) {
		try {
			ArrayDeque<String> queue = splitLine(line);
			
			int index = Integer.parseInt(queue.poll());	
			String type = queue.poll();		
			Creature parent = hm.get(Integer.parseInt(queue.poll()));
			double weight = Double.parseDouble(queue.poll());
			double value = Double.parseDouble(queue.poll());
			
			Treasure t = new Treasure(index, type, weight, value);
			
			if (parent != null) parent.addTreasure(t);
			else addTreasure(t);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Adds the input Treasure object to the creature indicated by the parentIndex. 
	 * Throws an Exception when parent is not found or it is not an instance of Creature.
	 * Adds anything with parentIndex 0 to the looseObj Collection.
	 * @param t Treasure
	 * @throws Exception
	 */
	public void addTreasure(Treasure t) throws Exception {
		if (t.getParent() != null) {
			CaveElement c = t.getParent();
			if (c instanceof Creature) {
				((Creature) c).addTreasure(t);
			} else {
				throw new Exception("Invalid parent index at treasure: " + t.toString());
			}
		} else {
			((CaveElement) getChildAt(1)).add(t); //Adds to loose objects
		}			
	}
	
	/**
	 * Takes a specifically formatted string input and splits into a queue. It uses this to make an Artifact object.
	 * @param line
	 * @param hm Hashmap of objects read in
	 */
	protected void addArtifact(String line, HashMap<Integer, Creature> hm) {		
		try {
			ArrayDeque<String> queue = splitLine(line);
			
			int index = Integer.parseInt(queue.poll());	
			String type = queue.poll();
			Creature parent = hm.get(Integer.parseInt(queue.poll()));
			
			Artifact a = new Artifact(index, type);
			
			if (!queue.isEmpty()) a.setName(queue.poll());
			if (parent != null) parent.addArtifact(a);
			else addArtifact(a);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * Adds the input Artifact to the Creature indicated by the parentIndex.
	 * Throws an exception when the parent does not exist or is not a Creature.
	 * Adds any object with parentIndex 0 to the looseObj collection.
	 * @param a Artifact
	 * @throws Exception
	 */
	public void addArtifact(Artifact a) throws Exception {
	
		if (a.getParent() != null) {
			CaveElement c = a.getParent();
			if (c instanceof Creature) {
				((Creature) c).addArtifact(a);
			} else {
				throw new Exception("Invalid parent index at treasure: " + a.toString());
			}
		} else {
			((CaveElement) getChildAt(1)).add(a); //Adds to loose objects
		}			
	}
	
	protected Job addJob(String line, HashMap<Integer, Creature> hm, DefaultTreeModel model) {
		try {
			ArrayDeque<String> queue = splitLine(line);

			int index = Integer.parseInt(queue.poll());
			String name = queue.poll();
			Creature parent = hm.get(Integer.parseInt(queue.poll()));		
			Long jobTime = (long) Double.parseDouble(queue.poll());
			
			Job job = new Job(index, name, jobTime, model);
			
			while (!queue.isEmpty()) {
				job.addReq(queue.poll(), Integer.parseInt(queue.poll()));
			}	
			
			parent.addJob(job);
			return job;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}	
	
	/**
	 * Iterates through every object in the game and collects all matches.
	 * @param index
	 * @return Array of CaveElements with the given index.
	 */
	public Vector<CaveElement> searchIndex(int index) {
		Vector<CaveElement> matched = ((CaveElement) getChildAt(0)).searchIndex(index);
		matched.addAll(((CaveElement) getChildAt(1)).searchIndex(index));
		return matched;		
	}
	
	/**
	 * Iterates through every object in the game and collects all matches.
	 * @param target
	 * @return Array of CaveElements with the given name.
	 */
	public Vector<CaveElement> searchName(String target) {
		Vector<CaveElement> matched = ((CaveElement) getChildAt(0)).searchName(target);
		matched.addAll(((CaveElement) getChildAt(1)).searchName(target));
		return matched;		
	}
	
	/**
	 * Iterates through every object in the game and collects all matches.
	 * @param target
	 * @return Array of CaveElements with the given type.
	 */
	public Vector<CaveElement> searchType(String target) {
		Vector<CaveElement> matched = ((CaveElement) getChildAt(0)).searchType(target);
		matched.addAll(((CaveElement) getChildAt(1)).searchType(target));
		return matched;
	}
	
	public Vector<CaveElement> getParties() {

		return ((CaveElement) getChildAt(0)).getChildren();
	}
	
	public Vector<CaveElement> getLooseObj() {
		return ((CaveElement) getChildAt(1)).getChildren();
	}
	
	public void sortName() {
		if (getParties() == null) return;
		
		Collections.sort(getParties(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement p1, CaveElement p2) {				
				return p1.getName().compareTo(p2.getName());
			}
		});
		
		for (CaveElement p : getParties()) {
			((Party) p).sortName();
			for (CaveElement c : ((Party) p).getCreatures()) {
				((Creature) c).sortName();
			}
		}
	}
	
	public void sortAge() {
		if (getParties() == null) return;
		
		for (CaveElement p : getParties()) {
			((Party) p).sortAge();
		}
	}
	
	public void sortHeight() {
		if (getParties() == null) return;
		
		for (CaveElement p : getParties()) {
			((Party) p).sortHeight();
		}
	}
	
	public void sortWeight() {
		if (getParties() == null) return;
		
		for (CaveElement p : getParties()) {
			((Party) p).sortWeight();
			for (CaveElement c : ((Party) p).getCreatures()) {
				((Creature) c).sortWeight();
			}
		}
	}
	
	public void sortEmpathy() {
		if (getParties() == null) return;
		
		for (CaveElement p : getParties()) {
			((Party) p).sortEmpathy();
		}
	}
	
	public void sortFear() {
		if (getParties() == null) return;
		
		for (CaveElement p : getParties()) {
			((Party) p).sortFear();
		}
	}
	
	public void sortCarryCap() {
		if (getParties() == null) return;
		
		for (CaveElement p : getParties()) {
			((Party) p).sortCarryCap();
		}
	}
	
	public void sortValue() {
		if (getParties() == null) return;
		
		for (CaveElement p : getParties()) {
			for (CaveElement c : ((Party) p).getCreatures()) {
				((Creature) c).sortValue();
			}
		}
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public CaveElement getParent() {
		return null;
	}
	
	public void toggleAll() {
		if (getParties() != null) {
			for (CaveElement p : getParties()) {
				if (!p.isLeaf()) {
					for (CaveElement c : p.getChildren()) {
						if (((Creature) c).getJobs() != null) {
							for (CaveElement j : ((Creature) c).getJobs()) {
								((Job) j).toggleGoFlag();
							}
						}						
					}
				}
			}
		}
		
		if (getLooseObj() != null) {
			for (CaveElement p : getLooseObj()) {
				if (p instanceof Party && p.getChildren() != null) {
					for (CaveElement c : p.getChildren()) {
						if (((Creature) c).getJobs() != null) {
							for (CaveElement j : ((Creature) c).getJobs()) {
								((Job) j).toggleGoFlag();
							}
						}	
					}
				} else if (p instanceof Creature && ((Creature) p).getJobs() != null) {
					if (((Creature) p).getJobs() != null) {
						for (CaveElement j : ((Creature) p).getJobs()) {
							((Job) j).toggleGoFlag();
						}
					}	
				}
			}
		}
	}
	
	public void clear() {
		if (getParties() != null) {
			for (CaveElement p : getParties()) {
				if (!p.isLeaf()) {
					for (CaveElement c : p.getChildren()) {
						if (((Creature) c).getJobs() != null) {
							for (CaveElement j : ((Creature) c).getJobs()) {
								((Job) j).setKillFlag();
							}
						}						
					}
				}
			}
		}
		
		if (getLooseObj() != null) {
			for (CaveElement p : getLooseObj()) {
				if (p instanceof Party && p.getChildren() != null) {
					for (CaveElement c : p.getChildren()) {
						if (((Creature) c).getJobs() != null) {
							for (CaveElement j : ((Creature) c).getJobs()) {
								((Job) j).setKillFlag();
							}
						}	
					}
				} else if (p instanceof Creature && ((Creature) p).getJobs() != null) {
					if (((Creature) p).getJobs() != null) {
						for (CaveElement j : ((Creature) p).getJobs()) {
							((Job) j).setKillFlag();
						}
					}	
				}
			}
		}
		
		((CaveElement) getChildAt(0)).removeAllChildren();
		((CaveElement) getChildAt(1)).removeAllChildren();
	}
	
	public String display() {
		StringBuilder sb = new StringBuilder();
		sb.append(toString());
		
		if (getParties() == null) return sb.toString();
		
		for (CaveElement p : getParties()) {
			sb.append("\n\n" + p);			
			for (CaveElement c : p.getChildren()) {
				Creature c1 = (Creature) c;
				sb.append("\n\t" + c1);
				sb.append("\n\t\tTreasures: " + (c1.getTreasures() == null ? "None" : c1.getTreasures()));
				sb.append("\n\t\tArtifacts: " + (c1.getArtifacts() == null ? "None" : c1.getArtifacts()));
				sb.append("\n\t\tJobs: " + (c1.getJobs() == null ? "None" : c1.getJobs()));				
			}
		}
		
		if (getLooseObj() == null) return sb.toString();
		
		sb.append("\nLoose Objects:");
		for (CaveElement ce : getLooseObj()) {
			sb.append("\n" + ce);
		}		
		
		return sb.toString();
	}
}
