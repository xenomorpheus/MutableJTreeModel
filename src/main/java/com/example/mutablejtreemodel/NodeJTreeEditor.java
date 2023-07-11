/** This document is AS-IS. No claims are made for suitability for any purpose. */

package com.example.mutablejtreemodel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 * UI that allows the user to alter the tree structure of nodes.
 *
 * Note the UI JTree is updated by listening to changes in a TreeModel.
 *
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 */
public class NodeJTreeEditor {

	static final String KEY_JTREE = "jTree";
	static final String KEY_NODE_ID = "Node Id";

	/** The add button's action. */
	AbstractAction addButtonAction = new AbstractAction("Add") {

		/**
		 * serial id.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent event) {
			/** The JTree we are working on. */
			JTree jTree = (JTree) getValue(KEY_JTREE);

			/**
			 * In this demo we use a counter to give a unique name for each
			 * node.
			 */
			Integer NodeId = (Integer) getValue(KEY_NODE_ID);
			if (NodeId == null) {
				NodeId = 0;
			}

			Object selObject = jTree.getLastSelectedPathComponent();
			if ((null != selObject) && (selObject instanceof Node)) {
				Node location = (Node) jTree.getLastSelectedPathComponent();
				Node newNode = new Node("node" + ++NodeId);
				putValue(KEY_NODE_ID, NodeId);
				location.add(newNode);

				// Expand the new added node
				jTree.expandPath(location.getPathFromRoot());
			}
		}
	};

	/** The add button. */
	private final JButton addButton = new JButton(addButtonAction);

	/** The remove button's action. */
	AbstractAction removeButtonAction = new AbstractAction("Remove") {

		/**
		 * serial id.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent event) {
			JTree jTree = (JTree) getValue(KEY_JTREE);
			Object selObject = jTree.getLastSelectedPathComponent();
			if ((null != selObject) && (selObject instanceof Node)) {
				Node node = (Node) jTree.getLastSelectedPathComponent();
				node.destroy();
			}
		}
	};

	/** The remove button. */
	private final JButton removeButton = new JButton(removeButtonAction);

	/**
	 * Constructor.
	 *
	 * @param rootNode
	 *            the root node in the tree.
	 */
	public NodeJTreeEditor(Node rootNode) {
		// Create a TreeModel as the interface between a JTree and our tree of
		// nodes.
		NodeJTreeModel treeModel = new NodeJTreeModel();
		treeModel.setRoot(rootNode);

		// UI for Tree. Create a JTree and tell it to display our model
		JTree jTree = new JTree();
		jTree.setModel(treeModel);
		jTree.setEditable(true);
		jTree.setSelectionRow(0);

		// The JTree can get big, so allow it to scroll.
		JScrollPane scrollpane = new JScrollPane(jTree);

		// A control panel for buttons to add and remove nodes.
		JPanel controlPanel = new JPanel();
		addButtonAction.putValue(KEY_JTREE, jTree);
		controlPanel.add(addButton);
		removeButtonAction.putValue(KEY_JTREE, jTree);
		controlPanel.add(removeButton);

		// Setup frame.
		JFrame frame = new JFrame("MyNode Creator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(scrollpane, BorderLayout.CENTER);
		frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);
		frame.setPreferredSize(new Dimension(400, 600));
		frame.setLocationRelativeTo(null); // This will center your app

		// Always pack the frame after adding components.
		// (Recommended after every change that the components may have)
		frame.pack();

		frame.setVisible(true);
	}
}