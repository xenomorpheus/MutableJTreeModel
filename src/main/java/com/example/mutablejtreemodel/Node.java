/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * A node in a tree structure.
 * 
 * Nodes will fire change events to listeners e.g. other nodes or JTreeModel
 * objects.
 * 
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 */
public class Node extends DefaultMutableTreeNode implements TreeModelListener {

	/** serial id. */
	private static final long serialVersionUID = 1L;

	/** class logger */
	private static final Logger LOGGER = Logger.getLogger(Node.class.getName());

	/** synchronisation lock */
	private final Object objLock = new Object();
	// Read more:
	// http://javarevisited.blogspot.com/2011/04/synchronization-in-java-synchronized.html#ixzz2wy76gzSj

	/** The human identifiable name of this node. */
	private String name;

	/**
	 * Those that listen for changes to this node. E.g. other nodes or
	 * JTreeModel. <br>
	 * TODO Using weak references for listener set. It's very easy to forget
	 * removing listeners when the actual instance isn't in use any more and
	 * thats a source of memory leak.
	 */
	private List<TreeModelListener> listeners;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the human identifiable name of this node.
	 */
	public Node(String name) {
		super();
		this.name = name;
		listeners = new ArrayList<>();
	}

	/** Constructor. */
	public Node() {
		this("No Name");
	}

	// Getters and Setters
	/**
	 * Set the name.
	 * 
	 * @param name
	 *            the new name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	// MutableTreeNode
	@Override
	public void insert(MutableTreeNode child, int index) {
		LOGGER.info("This='" + this + "', child='" + child + "' at index="
				+ index);

		// TODO remove from existing parent, if any.
		super.insert(child, index);

		// Inform our listeners that we have inserted node(s).
		TreeModelEvent e = new TreeModelEvent(this, getPathFromRoot(),
				new int[] { index }, new TreeNode[] { child });
		fireTreeNodesInserted(e);
	}

	// MutableTreeNode
	@Override
	public void remove(MutableTreeNode child) {
		LOGGER.info("remove node=" + this);
		if (!(child instanceof Node)) {
			throw new IllegalArgumentException("Expecting node to be of class "
					+ Node.class.getCanonicalName() + ", but I got "
					+ child.getClass().getCanonicalName());
		}

		int index = getIndex(child);
		super.remove(child);
		// Inform listeners that we have removed node(s).
		TreeModelEvent e = new TreeModelEvent(this, getPathFromRoot(),
				new int[] { index }, new TreeNode[] { child });
		LOGGER.info("remove node=" + this+ " fire event "+e);
		

		fireTreeNodesRemoved(e);
	}

	/**
	 * @return a path of nodes leading from root. Last node is this node.
	 */

	public TreePath getPathFromRoot() {
		TreeNode nodePtr = this;
		ArrayList<TreeNode> nodeArrayList = new ArrayList<>();
		synchronized (objLock) {
			while ((null != nodePtr)) {
				nodeArrayList.add(0, nodePtr);
				nodePtr = nodePtr.getParent();
			}
			return new TreePath(nodeArrayList.toArray(new Node[nodeArrayList
					.size()]));
		}
	}

	/**
	 * Add a listener from the list that wish to listen to events involving this
	 * node.
	 * 
	 * @param listener
	 *            listener to add.
	 */
	public void addListener(TreeModelListener listener) {
		LOGGER.info("addActionListener for this='" + this + "', listener='"
				+ listener + "'");
		synchronized (objLock) {
			listeners.add(listener);
		}
	}

	/**
	 * Add a listener to the list that wish to listen to events involving this
	 * node.
	 * 
	 * @param listener
	 *            listener to remove.
	 */
	public void removeListener(TreeModelListener listener) {
		LOGGER.info("removeActionListener for this='" + this + "', listener="
				+ listener);
		synchronized (objLock) {
			listeners.remove(listener);
		}
	}

	// Misc. methods.

	/**
	 * 
	 * @return String representation of object.
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Request the destruction of this node. Notify the listeners of this node
	 * of the death.
	 */
	public void destroy() {
		LOGGER.info("destroy node=" + this);

		// If parent still set, remove this node from parent.
		synchronized (objLock) {
			if (null != parent) {
				parent.remove(this);
			}
		}
		// TODO free resources of this node at this subtype.
		// TODO call parent class's destroy.
	}

	// http://docs.oracle.com/javase/8/docs/api/javax/swing/event/TreeModelListener.html#treeNodesRemoved-javax.swing.event.TreeModelEvent-
	/**
	 * Notify listeners that node(s) have changed.
	 * 
	 * @param e
	 *            event
	 */

	private void fireTreeNodesChanged(TreeModelEvent e) {

		TreeModelListener[] tmpListeners = null;
		// Don't leak the lock.
		synchronized (objLock) {
			tmpListeners = listeners.toArray(new TreeModelListener[listeners
					.size()]);
		}
		for (TreeModelListener listener : tmpListeners) {
			listener.treeNodesChanged(e);
		}
	}

	/**
	 * Notify listeners that node(s) have been inserted.
	 * 
	 * @param parent
	 *            the parent node.
	 * @param childIndexes
	 *            indexes of children be inserted, ascending order.
	 * @param children
	 *            array of the inserted children.
	 */

	private void fireTreeNodesInserted(TreeModelEvent e) {

		TreeModelListener[] tmpListeners = null;
		// Don't leak the lock.
		synchronized (objLock) {
			tmpListeners = listeners.toArray(new TreeModelListener[listeners
					.size()]);
		}
		for (TreeModelListener listener : tmpListeners) {
			listener.treeNodesInserted(e);
		}
	}

	/**
	 * Notify listeners that node(s) have been removed.
	 * 
	 * @param e
	 *            event
	 */

	private void fireTreeNodesRemoved(TreeModelEvent e) {

		TreeModelListener[] tmpListeners = null;
		// Don't leak the lock.
		synchronized (objLock) {
			tmpListeners = listeners.toArray(new TreeModelListener[listeners
					.size()]);
		}
		for (TreeModelListener listener : tmpListeners) {
			listener.treeNodesRemoved(e);
		}
	}

	/**
	 * Notify listeners that node(s) have changed structure.
	 * 
	 * @param e
	 *            event
	 */
	private void fireTreeStructureChanged(TreeModelEvent e) {

		TreeModelListener[] tmpListeners = null;
		// Don't leak the lock.
		synchronized (objLock) {
			tmpListeners = listeners.toArray(new TreeModelListener[listeners
					.size()]);
		}
		for (TreeModelListener listener : tmpListeners) {
			listener.treeStructureChanged(e);
		}
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		fireTreeNodesChanged(e);

	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		fireTreeNodesInserted(e);

	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		fireTreeNodesRemoved(e);
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		fireTreeStructureChanged(e);
	}
}
