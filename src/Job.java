import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * <li>FileName: Job.java
 * <li>Class: CMSC 335 6380 Object-Oriented and Concurrent Programming
 * <li>Project 3
 * <li>Author: Robert Lee Carle
 * <li>Date: 2/8/2016
 * <li>Platform/Compiler: Java 8 with Eclipse IDE
 * <li>Instructor: Nicholas Duchon
 * <li>Purpose: Represents jobs performed by the creatures.
 * <li>Due: 2/22/2016
 */
public class Job extends CaveElement implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234L;
	
	static Random rn = new Random ();
	
	private HashMap<String, Integer> rqmnts = new HashMap<String, Integer>();
	private DefaultTreeModel model;

	
	private long jobTime;
	private int timeWorked;
	private boolean goFlag = true, noKillFlag = true;
	
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
		this.model = model;
		
	
	}
	
	public void startJob() {
		panel.add(pm);
		panel.add(new JLabel (getParent().getName(), SwingConstants.LEFT));
		panel.add(new JLabel (getName(), SwingConstants.LEFT));		
		
		panel.add(jbGo);
		panel.add(jbKill);
		
		jbGo.addActionListener(e -> toggleGoFlag());
		jbGo.setHorizontalAlignment(SwingConstants.LEFT);
		jbKill.addActionListener(e -> setKillFlag());
		jbKill.setHorizontalAlignment(SwingConstants.LEFT);
		
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		pm.setAlignmentX(Component.LEFT_ALIGNMENT);
		pm.setStringPainted(true);	
		
		(new Thread (this, getParent().getName() + " " + getName())).start();
	}
	
	public void addReq(String type, int num) {
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
	}
	
	public void setKillFlag() {
		noKillFlag = false;
		jbKill.setBackground(Color.red);
	}
	
	public void showStatus (Status st) {
		status = st;
		switch (status) {
		case RUNNING:
			jbGo.setBackground(Color.green);
			jbGo.setText("Running");
			break;
		case SUSPENDED:
			jbGo.setBackground(Color.yellow);
			jbGo.setText("Suspended");
			break;
		case WAITING:
			jbGo.setBackground(Color.orange);
			jbGo.setText("Waiting");
			break;
		case DONE:
			jbGo.setBackground(Color.red);
			jbGo.setText("Done");
			break;
		}
	}
	
	public void run() {
		Creature worker = (Creature) getParent();	
		
		

		synchronized (worker.getParent()) {
			while (worker.getBusyFlag()) {
				showStatus (Status.WAITING);
				try {
					worker.getParent().wait();
				} catch (InterruptedException e) {}
			}			
			worker.setBusyFlag(true);
			worker.setJob(this);
		}
		
		SwingWorker<Integer, Integer> swingWorker = new SwingWorker<Integer, Integer>() {
			long time = System.currentTimeMillis();
			long startTime = time;
			long stopTime = time + 1000 * jobTime;
			double duration = stopTime - time;
			
			@Override
			protected Integer doInBackground() throws Exception {
				while (time < stopTime && noKillFlag) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
					if (goFlag) {
						showStatus (Status.RUNNING);
						time += 100;
						timeWorked = (int)(((time - startTime) / duration) * 100);
						setProgress(timeWorked);
					} else {
						showStatus (Status.SUSPENDED);
					}
				}
				
				return timeWorked;
			}
			
			@Override
			protected void done() {
				pm.setValue(100);
				worker.setJob(null);
				worker.setUserObject(0);
				showStatus (Status.DONE);
				synchronized (worker.getParent()) {
					worker.setBusyFlag(false);
					worker.getParent().notifyAll();

				}
				model.nodeChanged(worker);
			}
		};
		swingWorker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				
				if (e.getPropertyName().equals("progress")) {
					int i = (int) e.getNewValue();
					pm.setValue(i);
					worker.setUserObject(i);
					model.nodeChanged(worker);
				}
			}
		});
		
		swingWorker.execute();
		
	}
	
	@Override
	public String toString() {
		return "Job: " + getIndex() + ", " + getName() + ", " + jobTime;
	}
}
