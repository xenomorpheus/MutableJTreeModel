/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import java.util.ArrayDeque;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main program. Create a root node and start a tree editor.
 *
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 */
public class Main {
	/** class logger */
	private static final Logger logger = LogManager.getLogger(Main.class);

	/**
	 * Method main.
	 *
	 * @param argv
	 *            Command line arguments. Not used.
	 */
	public static void main(String[] argv) throws InterruptedException {
		final Node root = new Node("Root Node");
		Node parent = root;
		Node child = null;

		// The UI is in a different thread to the one that changes
		// the Model.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new NodeJTreeEditor(root);
			}
		});

		// Keep creating child nodes within the last created child node
		// and within the root node.
		ArrayDeque<Node> nodes = new ArrayDeque<>();
		for (int i = 0; i < 10; i++) {
			// TODO fix - root.setName("Root Node Name "+i);
			for (int j = 0; j < 2; j++) {
				var name = "CHILD_NODE_" + i + "_" + j;
				logger.info("Adding node: "+name);
				Thread.sleep(1000);
				child = new Node(name);
				parent.add(child);
				nodes.addLast(child);
			}
			parent = child;

			// Also add children to root. Just for fun.
			var child_of_root = new Node("ROOT_CHILD_NODE_" + i);
			root.add(child_of_root);
			nodes.addLast(child_of_root);
		}

		while(!nodes.isEmpty()){
			var node = nodes.removeLast();
			logger.info("Removing node: "+node);
			node.destroy();
			Thread.sleep(1000);
		}
	}
}
