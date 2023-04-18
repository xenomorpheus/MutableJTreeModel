/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This version automatically add nodes. This is a simplified version that
 * removes the button logic, and hence button events as a possible cause of
 * bugs.
 *
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 */
public class NodeJTreeModelDemo {
	/** class logger */
	private static final Logger logger = LogManager.getLogger(Node.class);

	/** the root node of the tree. */
	private static final Node ROOT = new Node("Root Node");

	/**
	 * JTree UI to show tree. We only create this so we programmatically show
	 * how to select a node. No other operations are done on the raw JTree.
	 */
	private static JTree JTREE = new JTree();

	/** Constructor */
	NodeJTreeModelDemo() {
		super();
	}

	/**
	 * Method startSwing.
	 *
	 * @param root
	 *            the root Node.
	 */
	private void startSwing(Node root) {

		/** The link between our nodes and the UI JTree */
		NodeJTreeModel treeModel = new NodeJTreeModel();
		treeModel.setRoot(root);

		// Tell JTree to display our model
		JTREE.setModel(treeModel);
		JTREE.setEditable(true);

		// The JTree can get big, so allow it to scroll.
		JScrollPane scrollpane = new JScrollPane(JTREE);

		// Setup frame.
		JFrame frame = new JFrame("MyNode Creator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(scrollpane, BorderLayout.CENTER);
		frame.setPreferredSize(new Dimension(400, 600));
		// Always pack the frame after adding components.
		// (Recommended after every change that the components may have)
		frame.pack();
		frame.setLocationRelativeTo(null); // This will center your app
		frame.setVisible(true);
	}

	/**
	 * Method main.
	 *
	 * @param argv
	 *            command line arguments. Not used.
	 *
	 * @throws InterruptedException
	 */
	public static void main(String[] argv) throws InterruptedException {
		Node parent = ROOT;
		Node child = null;

		// The UI is on a different thread to the one that changes
		// the Model.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				NodeJTreeModelDemo demo = new NodeJTreeModelDemo();
				demo.startSwing(ROOT);
			}
		});
		// Keep creating child nodes within the last created child node.
		for (int i = 0; i < 10; i++) {
			// TODO - fix - ROOT.setName("Root Node Name "+i);
			for (int j = 0; j < 2; j++) {
				logger.info("********************************************");
				Thread.sleep(1000);
				child = new Node("CHILD_NODE_" + i + "_" + j);
				parent.add(child);

				// Select the newly created node.
				TreeSelectionModel treeSelectionModel = JTREE
						.getSelectionModel();
				TreePath path = child.getPathFromRoot();
				treeSelectionModel.setSelectionPath(path);

			}
			parent = child;
			// Also add children to root. Just for fun.
			ROOT.add(new Node("CHILD_NODE_" + i));

		}
	}
}
