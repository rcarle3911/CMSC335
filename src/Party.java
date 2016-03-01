import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * <li>FileName: Party.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Final
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/27/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Party class which holds a list of creatures.
 * <li>Due: 3/7/2016
 */
public class Party extends CaveElement{
	protected TreeMap<String, Integer> resources = new TreeMap<String, Integer>();
	private ArrayList<JProgressBar> pbList = new ArrayList<JProgressBar>();
	private boolean busyFlag;
	private JPanel partyPanel;
	private JLabel rsrcLbl;
	JPanel pbPanel = new JPanel();
	
	public Party(int index, String name) {
		super(index, name);		
	}
	
	public void addCreature(Creature c) {
		add(c);
	}
	
	public Vector<CaveElement> getCreatures() {
		return this.getChildren();
	}
	
	public void setBusyFlag(boolean b) {
		busyFlag = b;
	}
	
	public boolean getBusyFlag() {
		return busyFlag;
	}
	
	public void sortName() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {				
				return c1.getName().compareTo(c2.getName());
			}
		});
	}
	
	public void sortAge() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {				
				return Double.compare(((Creature) c1).getAge(), ((Creature) c2).getAge());
			}
		});
	}
	
	public void sortHeight() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {				
				return Double.compare(((Creature) c1).getHeight(), ((Creature) c2).getHeight());
			}
		});
	}
	
	public void sortWeight() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {				
				return Double.compare(((Creature) c1).getWeight(), ((Creature) c2).getWeight());
			}
		});
	}
	
	public void sortEmpathy() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {
				return ((Creature) c1).getEmpathy() - ((Creature) c2).getEmpathy();
			}
		});
	}
	
	public void sortFear() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {
				return ((Creature) c1).getFear() - ((Creature) c2).getFear();
			}
		});
	}
	
	public void sortCarryCap() {
		Collections.sort(getCreatures(), new Comparator<CaveElement>() {
			@Override
			public int compare(CaveElement c1, CaveElement c2) {
				return Double.compare(((Creature) c1).getCarryCap(), ((Creature) c2).getCarryCap());
			}
		});
	}
	
	public void countResources(JPanel jobPanel) {
		for (CaveElement creature : children) {
			if (((Creature) creature).getArtifacts() != null) {
				for (CaveElement artifact : ((Creature) creature).getArtifacts()) {
					String type = artifact.getType();
					if (type.endsWith("s")) type = type.substring(0, type.length() - 1);
					if (resources.containsKey(type)) {
						resources.put(type, resources.get(type) + 1);
					} else {
						resources.put(type, 1);
					}
				}
			}			
		}
		
		for (Map.Entry<String, Integer> e : resources.entrySet()) {
			JProgressBar pb = new JProgressBar(0, e.getValue());
			pb.setString(e.getKey());
			pb.setStringPainted(true);
			pbList.add(pb);			
		}
		
		rsrcLbl = new JLabel();	
		
		setResourceText();
		rsrcLbl.setFont(new Font("Dialog", Font.BOLD, 18));
		rsrcLbl.setPreferredSize(new Dimension(680, 20));
		rsrcLbl.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray));
		partyPanel = new JPanel(new BorderLayout());
		partyPanel.add(rsrcLbl, BorderLayout.WEST);
		partyPanel.add(pbPanel, BorderLayout.EAST);
	}
	
	public synchronized int numRsrcAvail(String type) {
		if (resources.get(type) == null) return 0;
		return resources.get(type);
	}
	
	public synchronized void reserveResources(HashMap<String, Integer> rqmnts) {
		int current;
		for(Map.Entry<String, Integer> e : rqmnts.entrySet()) {			
			current = resources.get(e.getKey());
			resources.replace(e.getKey(), current - e.getValue());
		}
		
		setResourceText();
	}
	
	public synchronized void releaseResources(HashMap<String, Integer> rqmnts) {
		int current;
		for(Map.Entry<String, Integer> e : rqmnts.entrySet()) {
			current = resources.get(e.getKey());
			resources.replace(e.getKey(), current + e.getValue());
		}
		
		setResourceText();
	}
	
	public void setResourceText() {
		rsrcLbl.setText(getName() + " Resources: " + resources);
		int count = 0;
		for (Map.Entry<String, Integer> e : resources.entrySet()) {
			pbList.get(count).setValue(e.getValue());
			count++;
		}
	}
	
	public JPanel getResourcePanel() {
		return partyPanel;
	}
	
	public ArrayList<JProgressBar> getPBList() {
		return pbList;
	}
	
	@Override
	public String toString() {		
		return (getName() + ", I: " + getIndex());
	}
}
