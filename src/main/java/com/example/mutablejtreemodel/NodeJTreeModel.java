/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Objects of this class form an adapter between a JTree and the model, that
 * being a tree of Node objects.
 * 
 * The methods in this class allow the JTree component to traverse the tree.
 * 
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 **/
public class NodeJTreeModel implements TreeModel, ActionListener {

	/** class logger */
	private static final Logger LOGGER = Logger.getLogger(NodeJTreeModel.class
			.getName());

	/**
	 * synchronisation lock.
	 * http://javarevisited.blogspot.com/2011/04/synchronization
	 * -in-java-synchronized.html#ixzz2wy76gzSj
	 */
	private final Object objLock = new Object();

	/** We specify the root directory when we create the model. */
	private Node root;

	/**
	 * Those that listen for changes to the model. Using weak references for
	 * listener set. It's very easy to forget removing listeners when the actual
	 * instance isn't in use any more and thats a source of memory leak.
	 */
	private List<TreeModelListener> listeners;

	/**
	 * Constructor.
	 */
	public NodeJTreeModel() {
		listeners = new ArrayList<>();
	}

	// Getters and Setters

	/**
	 * The model knows how to return the root object of the tree.
	 * 
	 * @return Object
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	@Override
	public Object getRoot() {
		return root;
	}

	/**
	 * Set the root node.
	 * 
	 * @param root
	 *            set the root node that this TreeModel is listening to.
	 */
	public void setRoot(Node root) {
		this.root = root;
		root.addActionListener(this);
	}

	// Misc methods
	/**
	 * Notifies the listener that the structure below a given node has been
	 * completely changed.
	 * 
	 * @param path
	 *            the sequence of nodes that lead up the tree to the root node.
	 */
	private void fireStructureChanged(TreePath path) {
		TreeModelEvent event = new TreeModelEvent(this, path);
		TreeModelListener[] tmpListeners = null;
		// Don't leak the lock.
		synchronized (objLock) {
			tmpListeners = listeners.toArray(new TreeModelListener[listeners
					.size()]);
		}
		for (TreeModelListener lis : tmpListeners) {
			lis.treeStructureChanged(event);
		}
	}

	/**
	 * Notifies the listener that some nodes have been removed below a node.
	 * 
	 * @param parentPath
	 *            the sequence of nodes from the parent node to the root node.
	 * @param indices
	 * @param nodes
	 */
	private void fireNodesRemoved(TreePath parentPath, int[] indices,
			Object[] nodes) {
		TreeModelEvent event = new TreeModelEvent(this, parentPath, indices,
				nodes);

		TreeModelListener[] tmpListeners = null;
		// Don't leak the lock.
		synchronized (objLock) {
			tmpListeners = listeners.toArray(new TreeModelListener[listeners
					.size()]);
		}
		for (TreeModelListener lis : tmpListeners) {
			lis.treeNodesRemoved(event);
		}
	}

	/**
	 * Notifies the listener that a particular node has been removed.
	 * 
	 * @param path
	 * @param index
	 * @param node
	 */
	private void fireNodeRemoved(TreePath path, int index, Object node) {
		fireNodesRemoved(path, new int[] { index }, new Object[] { node });
	}

	/**
	 * Notifies the listener that the appearance of some sub-nodes a node has
	 * changed.
	 * 
	 * @param parentPath
	 * @param indices
	 * @param nodes
	 */
	private void fireNodesChanged(TreePath parentPath, int[] indices,
			Object[] nodes) {
		TreeModelEvent event = new TreeModelEvent(this, parentPath, indices,
				nodes);
		TreeModelListener[] tmpListeners = null;
		// Don't leak the lock.
		synchronized (objLock) {
			tmpListeners = listeners.toArray(new TreeModelListener[listeners
					.size()]);
		}
		for (TreeModelListener lis : tmpListeners) {
			lis.treeNodesChanged(event);
		}
	}

	/**
	 * Notifies the listener that the appearance of a node has changed.
	 * 
	 * @param parentPath
	 *            the path of the parent node of the relevant node.
	 * @param index
	 *            the index of the node under the parent node. If <0, the
	 *            listener will not be notified.
	 * @param node
	 *            the subnode.
	 */
	private void fireNodeChanged(TreePath parentPath, int index, Object node) {
		if (index >= 0) {
			fireNodesChanged(parentPath, new int[] { index },
					new Object[] { node });
		}
	}

	/**
	 * Notifies listeners that below a node, some nodes were inserted.
	 * 
	 * @param parentPath
	 *            TreePath
	 * @param indices
	 *            int[]
	 * @param subNodes
	 *            Object[]
	 */
	private void fireNodesInserted(TreePath parentPath, int[] indices,
			Object[] subNodes) {
		TreeModelEvent event = new TreeModelEvent(this, parentPath, indices,
				subNodes);
		TreeModelListener[] tmpListeners = null;
		// Don't leak the lock.
		synchronized (objLock) {
			tmpListeners = listeners.toArray(new TreeModelListener[listeners
					.size()]);
		}
		for (TreeModelListener lis : tmpListeners) {
			lis.treeNodesInserted(event);
		}
	}

	/**
	 * Notifies the listener that a node has been inserted.
	 * 
	 * @param parentPath
	 * @param index
	 * @param node
	 */
	private void fireNodeInserted(TreePath parentPath, int index, Object node) {
		fireNodesInserted(parentPath, new int[] { index },
				new Object[] { node });
	}

	/**
	 * Method actionPerformed.
	 * 
	 * @param e
	 *            ActionEvent
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		NodeChangeType command = NodeChangeType.get(e.getActionCommand());
		int id = e.getID();
		LOGGER.info("actionPerformed was called: " + e);
		LOGGER.info("           source =" + source);
		LOGGER.info("           command =" + command);
		LOGGER.info("           ID =" + id);

		if (!(source instanceof Node)) {
			throw new IllegalArgumentException("Bad Source");
		}
		Node node = (Node) source;
		Node parent = (Node) node.getParent();

		LOGGER.info("command type: " + command);
		LOGGER.info("fire events: Node path from root" + node.getPathFromRoot());

		switch (command) {

		case STRUCTURE_CHANGED:
			fireStructureChanged(node.getPathFromRoot());
			break;

		// case NODES_REMOVED:
		// fireNodesRemoved(node.getParent().getPathToRoot(), indices,
		// nodes);
		// break;

		case NODE_REMOVED:
			// Handle root deletion attempt
			if (node.getParent() != null) {
				fireNodeRemoved(parent.getPathFromRoot(), id, node);
			} else {
				LOGGER.info("Cannot delete Root node!");
			}
			break;

		// case NODES_CHANGED:
		// fireNodesChanged(node.getParent().getPathToRoot(), indices,
		// nodes);
		// break;

		case NODE_CHANGED:
			fireNodeChanged(parent.getPathFromRoot(), id, node);
			break;

		// case NODES_INSERTED:
		// fireNodesInserted(node.getParent().getPathToRoot(), indices,
		// subNodes);
		// break;

		case NODE_INSERTED:
			fireNodeInserted(parent.getPathFromRoot(), id, node);
			break;

		default:
			LOGGER.info("Unsupported command type: " + command);
		}
	}

	/**
	 * Method addTreeModelListener.
	 * 
	 * @param listener
	 *            TreeModelListener
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(TreeModelListener)
	 */
	@Override
	public void addTreeModelListener(TreeModelListener listener) {
		LOGGER.info("Adding Listener: " + listener);
		synchronized (objLock) {
			listeners.add(listener);
		}
	}

	/**
	 * Method removeTreeModelListener.
	 * 
	 * @param listener
	 *            TreeModelListener
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(TreeModelListener)
	 */
	@Override
	public void removeTreeModelListener(TreeModelListener listener) {
		LOGGER.info("Remove Listener: " + listener);
		synchronized (objLock) {
			listeners.remove(listener);
		}
	}

	/**
	 * Tell JTree whether an object in the tree is a leaf or not.
	 * 
	 * 
	 * @param node
	 *            Object
	 * @return tree if node is a leaf. * @see
	 *         javax.swing.tree.TreeModel#isLeaf(Object)
	 */
	@Override
	public boolean isLeaf(Object node) {
		return ((Node) node).isLeaf();
	}

	/**
	 * Tell JTree how many children a node has.
	 * 
	 * 
	 * @param node
	 *            Object
	 * @return how many children. * @see
	 *         javax.swing.tree.TreeModel#getChildCount(Object)
	 */
	@Override
	public int getChildCount(Object node) {
		int count = ((Node) node).getChildCount();
		LOGGER.info("node='" + node + "', count=" + count);
		return count;
	}

	/**
	 * Fetch any numbered child of a node for the JTree. Our model returns
	 * MyNode objects for all nodes in the tree. The JTree displays these by
	 * calling the MyNode.toString() method.
	 * 
	 * @param parent
	 * @param index
	 * 
	 * @return child at the requested index. * @see
	 *         javax.swing.tree.TreeModel#getChild(Object, int)
	 */
	@Override
	public Object getChild(Object parent, int index) {
		Node child = ((Node) parent).getChildAt(index);
		LOGGER.info("getChild - parent=" + parent + ", index=" + index
				+ ", RETURN child=" + child);
		return child;
	}

	/**
	 * Figure out a child's position in its parent node.
	 * 
	 * @param parent
	 *            the parent node
	 * @param child
	 *            the child node to find index of.
	 * @return int
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(Object, Object)
	 */
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		int index = ((Node) parent).getIndex((Node) child);
		LOGGER.info("getIndexOfChild - parent=" + parent + ", child=" + child
				+ ", RETURN index=" + index);
		return index;
	}

	/**
	 * This method is only invoked by the JTree for editable trees.
	 * 
	 * @param path
	 *            TreePath
	 * @param newValue
	 *            Object
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(TreePath, Object)
	 */
	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		LOGGER.info("valueForPathChanged path=" + path + ", newValue="
				+ newValue);
		Node node = (Node) path.getLastPathComponent();
		node.setName((String) newValue);
	}

	/**
	 * 
	 * @return string representation of this object.
	 */
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
