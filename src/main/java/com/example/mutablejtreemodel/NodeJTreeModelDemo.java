/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import java.awt.BorderLayout;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

/**
 * This version automatically add nodes, which removes the button logic
 * as a possible cause of the bugs.
 * 
 * Also this version tries to separate the UI thread from the thread that 
 * changes the Model.
 * 
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 */
public class NodeJTreeModelDemo {
	/** class logger */
	private static final Logger LOGGER = Logger.getLogger(Node.class.getName());

	/** the root node of the tree. */
	private static final Node ROOT = new Node("Root Node");

	/**
	 * Method startSwing.
	 * @param root the root Node.
	 */
	private void startSwing(Node root) {

		/** The link between our nodes and the UI JTree */
		NodeJTreeModel treeModel = new NodeJTreeModel();
		treeModel.setRoot(root);

		// Create a JTree and tell it to display our model
		JTree jTree = new JTree();
		jTree.setModel(treeModel);
		jTree.setEditable(true);
		jTree.setSelectionRow(0);

		// The JTree can get big, so allow it to scroll.
		JScrollPane scrollpane = new JScrollPane(jTree);

		// Setup frame.
		JFrame frame = new JFrame("MyNode Creator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(scrollpane, BorderLayout.CENTER);
		frame.setSize(200, 100);
		frame.setVisible(true);
	}

	/**
	 * Method main.
	 * 
	 * @param argv
	 *            command line arguments. Not used.
	 * 
	 * @throws InterruptedException */
	public static void main(String[] argv) throws InterruptedException {
		Node parent = ROOT;
		Node child = null;

		// Trying to put the UI on a different thread to the one that changes the Model.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				NodeJTreeModelDemo demo = new NodeJTreeModelDemo();
				demo.startSwing(ROOT);
			}
		});

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 1; j++) {
				child = new Node("CHILD_NODE_" + i + "_" + j);
				LOGGER.info("********************************************");
				Thread.sleep(5000);
				parent.add(child);
			}
			parent = child;
		}
	}

}
