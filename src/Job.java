import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Random;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

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
		//Color defColor = panel.getBackground();
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
		
		jbGo.addMouseListener(mouseHover);
		jbKill.addMouseListener(mouseHover);
		pm.addMouseListener(mouseHover);
	}
	
	public void startJob() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		int height = 15;

		panel.setLayout(new GridBagLayout());
		
		JLabel lbl = new JLabel(getParent().getParent().getName() + ": " + getName(), SwingConstants.LEFT);
		lbl.setPreferredSize(new Dimension(180, height));
		
		panel.add(lbl, c);		
		
		jbGo.setToolTipText("Click to pause");
		jbGo.setPreferredSize(new Dimension(80, height));
		jbKill.setPreferredSize(new Dimension(80, height));

		panel.add(jbGo, c);
		panel.add(jbKill, c);
		
		pm.setPreferredSize(new Dimension(200, height));
		
		
		c.weightx = 1.0;	
		
		panel.add(pm, c);
		
		jbGo.addActionListener(e -> toggleGoFlag());		
		jbKill.addActionListener(e -> setKillFlag());
		
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
		if (noKillFlag) {
			if(!goFlag) showStatus (Status.SUSPENDED);
			else if((this.equals(((Creature) getParent().getParent()).getJob())) && ((Creature) getParent().getParent()).getBusyFlag()) showStatus(Status.RUNNING);
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
		Creature worker = (Creature) getParent().getParent();	
		
		

		synchronized (worker.getParent()) {
			while (worker.getBusyFlag()) {
				if (goFlag) showStatus (Status.WAITING);
				else showStatus(Status.SUSPENDED);
				try {
					worker.getParent().wait();
				} catch (InterruptedException e) {}
			}			
			worker.setBusyFlag(true);
			worker.setJob(this);
		}
		
		//I used SwingWorker because I wanted to update a progress bar in the JTree. Code structure is a mix of Java tutorials and the professor's example code.
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
				synchronized (worker.getParent()) {
					worker.setBusyFlag(false);
					worker.getParent().notifyAll();

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
		
		swingWorker.execute();
		
	}
	
	@Override
	public String toString() {
		return getName() + ", I: " + getIndex() + ", T: " + jobTime;
	}
}
