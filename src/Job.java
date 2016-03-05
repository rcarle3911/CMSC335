import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

/**
 * <li>FileName: Job.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Final
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/27/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Represents jobs performed by the creatures.
 * <li>Due: 3/7/2016
 */
public class Job extends CaveElement{
	
	static Random rn = new Random ();
	
	private HashMap<String, Integer> rqmnts = new HashMap<String, Integer>();
	private Creature worker;
	private Party party;
	SwingWorker<Integer, Integer> swingWorker;
	
	private long jobTime;
	private int timeWorked;
	private boolean goFlag = true, noKillFlag = true;
		
	protected JLabel nameLbl = new JLabel();
	protected JLabel availLbl = new JLabel();
	protected JLabel needsLbl = new JLabel();
	protected JPanel panel = new JPanel();
	protected JProgressBar pm = new JProgressBar();
	private JButton jbGo = new JButton ("Stop");
	private JButton jbKill = new JButton ("Cancel");
	
	private Status status = Status.SUSPENDED;
	
	enum Status {RUNNING, SUSPENDED, WAITING, DONE};
	
	public Job(int index, String name, long jobTime, DefaultTreeModel model) {
		super(index, name, "");		
		setJobTime(jobTime);	
		this.timeWorked = 0;
		
		MouseAdapter mouseHover = new MouseAdapter() {
			Color defColor = panel.getBackground();
			@Override
			public void mouseEntered(MouseEvent e) {
				panel.setBackground(Color.lightGray);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				panel.setBackground(defColor);
			}
		};
		
		nameLbl.addMouseListener(mouseHover);
		availLbl.addMouseListener(mouseHover);
		needsLbl.addMouseListener(mouseHover);
		jbGo.addMouseListener(mouseHover);
		jbKill.addMouseListener(mouseHover);
		pm.addMouseListener(mouseHover);
		
		
		//I used SwingWorker because I wanted to update a progress bar in the JTree. Code structure is a mix of Java tutorials and the professor's example code.
		swingWorker = new SwingWorker<Integer, Integer>() {
			long time = 0;
			long startTime = time;
			long stopTime = time + 1000 * jobTime;
			double duration = stopTime - time;
			
			@Override
			protected Integer doInBackground() throws Exception {
				synchronized (party) {
					while (worker.getBusyFlag() || !hasResources(party)) {
						if (goFlag) showStatus (Status.WAITING);
						else showStatus(Status.SUSPENDED);
						try {					
							party.wait();					
						} catch (InterruptedException e) {}
					}			
					worker.setBusyFlag(true);
					setWorkerJob();
					party.reserveResources(rqmnts);
					showStatus (Status.RUNNING);
				}
				while (time < stopTime && noKillFlag) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
					if (goFlag) {
						showStatus (Status.RUNNING);
						time += 100;
						timeWorked = (int)(((time - startTime) / duration) * 100);
						setProgress(timeWorked);						//Kicks off a property change event
					} else {
						showStatus (Status.SUSPENDED);
					}
				}
				
				return timeWorked;
			}
			
			@Override
			protected void done() {
				noKillFlag = false;
				pm.setValue(100);
				worker.setJob(null);
				worker.setUserObject(0);
				showStatus (Status.DONE);
				synchronized (party) {
					worker.setBusyFlag(false);
					party.releaseResources(rqmnts);
					party.notifyAll();
				}
				model.nodeChanged(worker);  			//Lets the tree model know this node has changed
			}
		};
		swingWorker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				//Fires off when setProgress is called.
				if (e.getPropertyName().equals("progress")) {
					int i = (int) e.getNewValue();
					pm.setValue(i);						//This progress bar is in the Jobs area tab
					worker.setUserObject(i);			//The worker node's user object is used to update the JTree progress bar
					model.nodeChanged(worker); 			//Lets the tree model know this node has changed
					
				}
			}
		});
	}
	
	public void setWorkerJob() {
		worker.setJob(this);
	}
	
	public void startJob() {
		worker = (Creature) getParent().getParent();
		party = (Party) worker.getParent();
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		int height = 15;

		panel.setLayout(new GridBagLayout());
		
		nameLbl.setText(worker.getName() + ": " + getName());
		nameLbl.setPreferredSize(new Dimension(180, height));
		nameLbl.setBackground(new Color(getName().hashCode() % 255));
		
		panel.add(nameLbl, c);	
		
		availLbl.setPreferredSize(new Dimension(250, height));
		panel.add(availLbl, c);
		
		needsLbl.setPreferredSize(new Dimension(250, height));
		panel.add(needsLbl, c);		
		
		jbGo.setToolTipText("Click to pause");
		jbGo.setPreferredSize(new Dimension(80, height));
		jbKill.setPreferredSize(new Dimension(80, height));

		panel.add(jbGo, c);
		panel.add(jbKill, c);
		
		pm.setPreferredSize(new Dimension(200, height));	
		pm.setForeground(new Color(worker.getName().hashCode()));
		
		c.weightx = 1.0;	
		
		panel.add(pm, c);
		
		jbGo.addActionListener(e -> toggleGoFlag());		
		jbKill.addActionListener(e -> setKillFlag());
		
		pm.setStringPainted(true);
				
		(new Thread (swingWorker, worker.getName() + ": " + getName())).start();
	}
	
	public void addReq(String type, int num) {
		if (num <= 0) return;
		if (type.endsWith("s")) type = type.substring(0, type.length() - 1);
		if (rqmnts.containsKey(type)) {
			rqmnts.put(type, rqmnts.get(type) + num);
		} else {
			rqmnts.put(type, num);
		}
	}
	
	public long getJobTime() {
		return jobTime;
	}
	
	public int getTimeWorked() {
		return timeWorked;
	}
	
	public void setJobTime(long jobTime) {
		this.jobTime = jobTime;
	}
	
	public JPanel getPBar() {		
		return panel;
	}
	
	public void toggleGoFlag() {
		goFlag = !goFlag;
		if (noKillFlag) {
			if(!goFlag) showStatus (Status.SUSPENDED);
			else if(this.equals(worker.getJob()) && worker.getBusyFlag()) showStatus(Status.RUNNING);
			else showStatus(Status.WAITING);
		}
	}
	
	public void setKillFlag() {
		if (noKillFlag) {
			noKillFlag = false;
			jbKill.setBackground(Color.red);
		}		
	}
	
	public void showStatus (Status st) {
		HashMap<String, Integer> needs = null;
		HashMap<String, Integer> avail = null;
		
		status = st;
		switch (status) {
		case RUNNING:
			jbGo.setBackground(Color.green);
			jbGo.setText("Running");
			avail = trimRqmnts();
			break;
		case SUSPENDED:
			jbGo.setBackground(Color.yellow);
			jbGo.setText("Suspended");
			if (this.equals(worker.getJob())) 
				avail = trimRqmnts();
			else {
				avail = resourcesAvailable();
				needs = resourcesNeeded();
			}
			break;
		case WAITING:
			jbGo.setBackground(Color.orange);
			jbGo.setText("Waiting");
			avail = resourcesAvailable();
			needs = resourcesNeeded();
			break;
		case DONE:
			jbGo.setBackground(Color.red);
			jbGo.setText("Done");
			break;
		}
		
		availLbl.setText("" + ((avail == null || avail.isEmpty()) ? "None" : avail));
		needsLbl.setText("" + ((needs == null || needs.isEmpty()) ? "None" : needs));
	}
	
	public HashMap <String, Integer> trimRqmnts () {
		HashMap<String, Integer> avail = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> e : rqmnts.entrySet()) {
			avail.put(e.getKey().substring(0,3), e.getValue());
		}
		return avail;
	}
	
	public HashMap<String, Integer> resourcesNeeded() {
		HashMap<String, Integer> needs = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> e : rqmnts.entrySet()) {
			int needed = e.getValue() - party.numRsrcAvail(e.getKey());
			if (needed > 0 ) needs.put(e.getKey().substring(0, 3), needed);
		}
		return needs;
	}
	
	public HashMap<String, Integer> resourcesAvailable() {
		HashMap<String, Integer> avail = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> e : rqmnts.entrySet()) {
			int has = Integer.min(e.getValue(), party.numRsrcAvail(e.getKey()));			
			if (has > 0 ) avail.put(e.getKey().substring(0, 3), has);
		}
		return avail;
	}
	
	public boolean hasResources(Party party) {
		
		for (Map.Entry<String, Integer> e : rqmnts.entrySet()) {
			if (party.numRsrcAvail(e.getKey()) - e.getValue() < 0) return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return getName() + ", I: " + getIndex() + ", T: " + jobTime;
	}
}
