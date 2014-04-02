/** This document is AS-IS. No claims are made for suitability for any purpose. */

package com.example.mutablejtreemodel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

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

	/** logger */
	private static final Logger LOGGER = Logger.getLogger(NodeJTreeEditor.class
			.getName());

	/** In this demo we use a counter to give a unique name for each node. */
	private static int NodeId = 0;

	/** the interface between a JTree and our tree of nodes. */
	private NodeJTreeModel treeModel = null;

	/** UI for Tree */
	private JTree jTree = null;

	/** UI helper toolkit. */
	private final Toolkit toolkit = Toolkit.getDefaultToolkit();

	/** the add button */
	private final JButton addButton = new JButton("Add");

	/** the remove button */
	private final JButton removeButton = new JButton("Remove");

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

		// Add a listener to respond to events from the Buttons.
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				Object selObject = jTree.getLastSelectedPathComponent();
				if ((null != selObject) && (selObject instanceof Node)) {

					Object source = event.getSource();
					if (addButton == source) {
						Node location = (Node) jTree
								.getLastSelectedPathComponent();
						Node newNode = new Node("node" + ++NodeId);
						location.add(newNode);

						// Expand the new added node
						jTree.expandPath(location.getPathToRoot());

					} else if (removeButton == source) {
						Node node = (Node) jTree.getLastSelectedPathComponent();
						node.destroy();
					} else {
						LOGGER.warning("Event source not known:" + source);
						toolkit.beep();
					}

					// Update the tree view
					jTree.updateUI();

				} else {
					LOGGER.warning("Event selected Object should be a Node:" + selObject);
					toolkit.beep();
				}

			}
		};
		// Add button
		addButton.addActionListener(actionListener);
		addPanel.add(addButton);

		// Remove button
		removeButton.addActionListener(actionListener);
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