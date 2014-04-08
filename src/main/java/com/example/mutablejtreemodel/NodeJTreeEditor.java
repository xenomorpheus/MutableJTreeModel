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

	/** In this demo we use a counter to give a unique name for each node. */
	private static int NodeId = 0;

	/** the interface between a JTree and our tree of nodes. */
	private NodeJTreeModel treeModel = null;

	/** UI for Tree */
	private JTree jTree = null;

	/** the add button */
	private final JButton addButton = new JButton(new AbstractAction("Add") {

		/**
		 * serial id.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent event) {
			Object selObject = jTree.getLastSelectedPathComponent();
			if ((null != selObject) && (selObject instanceof Node)) {
				Node location = (Node) jTree.getLastSelectedPathComponent();
				Node newNode = new Node("node" + ++NodeId);
				location.add(newNode);

				// Expand the new added node
				jTree.expandPath(location.getPathFromRoot());

				// TODO MVC BUG - Update the tree view
				jTree.updateUI();
			}
		}
	});

	/** the remove button */
	private final JButton removeButton = new JButton(new AbstractAction(
			"Remove") {

		/**
		 * serial id.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent event) {
			Object selObject = jTree.getLastSelectedPathComponent();
			if ((null != selObject) && (selObject instanceof Node)) {

				Node node = (Node) jTree.getLastSelectedPathComponent();
				node.destroy();

				// TODO MVC BUG - Update the tree view
				jTree.updateUI();
			}
		}
	});

	/**
	 * Constructor.
	 * 
	 * @param rootNode
	 *            the root node in the tree.
	 */
	public NodeJTreeEditor(Node rootNode) {
		// Create a TreeModel as the interface between a JTree and our tree of
		// nodes.
		treeModel = new NodeJTreeModel();
		treeModel.setRoot(rootNode);

		// Create a JTree and tell it to display our model
		jTree = new JTree();
		jTree.setModel(treeModel);
		jTree.setEditable(true);
		jTree.setSelectionRow(0);

		// The JTree can get big, so allow it to scroll.
		JScrollPane scrollpane = new JScrollPane(jTree);

		// Include an "Add" button to add new nodes to our tree.
		JPanel addPanel = new JPanel();

		// Add buttons
		addPanel.add(addButton);
		addPanel.add(removeButton);

		// Setup frame.
		JFrame frame = new JFrame("MyNode Creator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(scrollpane, BorderLayout.CENTER);
		frame.getContentPane().add(addPanel, BorderLayout.SOUTH);
		frame.setPreferredSize(new Dimension(400, 600));
		frame.setVisible(true);

		// Always pack the frame after adding components.
		// (Recommended after every change that the components may have)
		frame.pack();

	}
}