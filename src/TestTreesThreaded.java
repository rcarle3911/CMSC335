import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.IconUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

/**
 * This is a POC for creating a JTree with JProgressBar's as its nodes,
 * each node has an Integer as its user object, the value of this Integer
 * represents the progress bars value. Each tree cell uses the ProgressBarRenderer
 * this renderer grabs the Integer value from the nodes user object and returns
 * a JProgressBar as the component to render
 * 
 * Some credit goes to http://stackoverflow.com/questions/7036036/adding-multiple-jprogressbar-to-tablecolumn-of-jtable/7036206#7036206
 * 
 * @author pstatham
 *
 */
public class TestTreesThreaded {

  private DefaultMutableTreeNode root = new DefaultMutableTreeNode(0);
  private DefaultTreeModel model = new DefaultTreeModel(root);

  private JTree tree = new JTree(model);

  private Component makeUI() {    
    tree.setCellRenderer(new ProgressBarRenderer());

    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        //Start adding nodes to the root node and process
        //the values in the progress bar
        startTask(root, false);
        startTask(new DefaultMutableTreeNode(0), true);
        startTask(new DefaultMutableTreeNode(10), true);
        startTask(new DefaultMutableTreeNode(110), true);
      }
    });
    JPanel p = new JPanel(new BorderLayout());    
    p.add(tree);
    return p;
  }  

  private void startTask(final DefaultMutableTreeNode treeNode, boolean isChild) {
    SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>() {

      private int sleepDummy = new Random().nextInt(100) + 1;
      private int lengthOfTask = 120;

      /**
       * Overrides the SwingWorker doInBackground, this version, increments 
       * the value of the % complete and publishes it, the process method will
       * pick up the published value so that the ProgressBarRenderer can
       * deal with it. It also triggers a nodeChanged event on the DefaultTreeModel
       * so that the JTree updates
       */
      @Override
      protected Integer doInBackground() throws Exception {
        int current = 0;
        while(current < lengthOfTask && !isCancelled()) {
          current++;
          try {
            Thread.sleep(sleepDummy);
          } catch (InterruptedException ie) {
            break;
          }
          publish(100 * current / lengthOfTask);
          model.nodeChanged(treeNode);
        }
        return sleepDummy * lengthOfTask;
      }

      /**
       * Attach a user object to the node, in this case
       * it is an Integer with the latest value triggered by publish
       * process will fire getTreeCellRendererComponent of the
       * ProgressBarRenderer
       * 
       * @param c - a list of Integer to process, only process the last value set
       */
      @Override
      protected void process(List<Integer> c) {
        treeNode.setUserObject(c.get(c.size() - 1));
      }

      /**
       * Overrides the swingworkers done method,
       * once complete print out the ms this thread took to
       * complete and trigger another nodeChanged event
       */
      @Override
      protected void done() {
        int i = -1;
        try {
          i = get();
        } catch (Exception e) {
          e.printStackTrace();
        }
        System.out.println("Value: " +i);
        model.nodeChanged(treeNode);
      }

    };
    if(isChild) {
      root.add(treeNode);
    }
    worker.execute();    
  }

  public static void main(String... args) {
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        createAndShowGUI();
      }
    });
  }

  public static void createAndShowGUI() {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.getContentPane().add(new TestTreesThreaded().makeUI());
    frame.setSize(320, 240);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}

/**
 * This class extends the DefaultTreeCellRenderer and returns a JProgressBar
 * as its renderer component
 * @author pstatham
 *
 */
@SuppressWarnings("serial")
class ProgressBarRenderer extends DefaultTreeCellRenderer {

  private final JProgressBar b = new JProgressBar(0, 100);

  public ProgressBarRenderer() {
    super();    
    setOpaque(true);
    setClosedIcon(new IconUIResource(new NodeIcon('+')));
    setOpenIcon(new IconUIResource(new NodeIcon('-')));
    b.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
  }

  @Override
  public Component getTreeCellRendererComponent(JTree tree, final Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    Integer i = (Integer) ((DefaultMutableTreeNode) value).getUserObject();
    b.setValue(i);
    b.setStringPainted(true);    
    return b;    
  }

}

/**
 * Pinched from http://stackoverflow.com/a/7984734/564045
 * @author pstatham
 *
 */
class NodeIcon implements Icon {

  private static final int SIZE = 9;

  private char type;

  public NodeIcon(char type) {
    this.type = type;
  }

  public void paintIcon(Component c, Graphics g, int x, int y) {
    g.setColor(UIManager.getColor("Tree.background"));
    g.fillRect(x, y, SIZE - 1, SIZE - 1);

    g.setColor(UIManager.getColor("Tree.hash").darker());
    g.drawRect(x, y, SIZE - 1, SIZE - 1);

    g.setColor(UIManager.getColor("Tree.foreground"));
    g.drawLine(x + 2, y + SIZE / 2, x + SIZE - 3, y + SIZE / 2);
    if (type == '+') {
      g.drawLine(x + SIZE / 2, y + 2, x + SIZE / 2, y + SIZE - 3);
    }
  }

  public int getIconWidth() {
    return SIZE;
  }

  public int getIconHeight() {
    return SIZE;
  }
}