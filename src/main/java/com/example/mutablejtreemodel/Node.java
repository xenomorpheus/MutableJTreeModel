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
 * <p>
 * 
 * Nodes will fire change events to listeners when the tree structure changes.<br>
 * Nodes will also listen for tree change events in their neighbours.
 * <p>
 * 
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
		fireNodeChanged();
	}

	private void fireNodeChanged() {
		TreePath path = null;
		Node parent = (Node) getParent();
		int[] childIndices = new int[] {};
		if (parent != null) {
			path = parent.getPathFromRoot();
			int index = parent.getIndex(this);
			childIndices = new int[] { index };
		}
		TreeModelEvent e = new TreeModelEvent(this, path, childIndices,
				new TreeNode[] { this });
		fireTreeNodesChanged(e);

	}

	// MutableTreeNode
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(MutableTreeNode child, int index) {
		LOGGER.info("This='" + this + "', child='" + child + "' at index="
				+ index);

		// If child has existing parent then remove it.
		Node oldParent = (Node) child.getParent();
		if (oldParent != null) {
			oldParent.remove(child);
		}
		super.insert(child, index);

		// Inform our listeners that we have inserted node(s).
		// https://docs.oracle.com/javase/7/docs/api/javax/swing/tree/DefaultTreeModel.html#fireTreeNodesInserted(java.lang.Object,%20java.lang.Object[],%20int[],%20java.lang.Object[])
		TreeModelEvent e = new TreeModelEvent(this, getPathFromRoot(),
				new int[] { index }, new TreeNode[] { child });
		fireTreeNodesInserted(e);
	}

	// MutableTreeNode
	/**
	 * {@inheritDoc}
	 */
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
		LOGGER.info("remove node=" + this + " fire event " + e);

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

	/**
	 * Request the destruction of this node. Notify the listeners of this node
	 * of the death.
	 */
	public void destroy() {
		LOGGER.info("destroy node=" + this);

		// If parent still set, remove this node from parent.
		if (null != parent) {
			parent.remove(this);
		}
		// TODO free resources of this node at this subtype.
		// TODO call parent class's destroy.
	}

	// Start of section where we notify others of changes.

	// https://docs.oracle.com/javase/7/docs/api/javax/swing/tree/DefaultTreeModel.html
	/**
	 * Notify listeners that node(s) have changed.
	 * 
	 * @param e
	 *            event
	 * @see https 
	 *      ://docs.oracle.com/javase/7/docs/api/javax/swing/tree/DefaultTreeModel
	 *      .html#fireTreeNodesChanged(java.lang.Object,%20java.lang.Object[],%
	 *      20int[],%20java.lang.Object[])
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

	// Start of section where we are notified of changes.
	// http://docs.oracle.com/javase/8/docs/api/javax/swing/event/TreeModelListener.html

	// TreeModelListener
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		LOGGER.info("treeNodesChanged node=" + this + ", event=" + e);
		// TODO Currently we don't care if nodes we listen to change.
		// fireTreeNodesChanged(e);
	}

	// TreeModelListener
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		LOGGER.info("treeNodesInserted node=" + this + ", event=" + e);
		// TODO Currently we don't care if nodes we listen to change.
		// fireTreeNodesInserted(e);

	}

	// TreeModelListener
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		LOGGER.info("treeNodesRemoved node=" + this + ", event=" + e);
		// TODO Currently we don't care if nodes we listen to change.
		// fireTreeNodesRemoved(e);
	}

	// TreeModelListener
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		LOGGER.info("treeStructureChanged node=" + this + ", event=" + e);
		// TODO Currently we don't care if nodes we listen to change.
		// fireTreeStructureChanged(e);
	}

	// Misc. methods.

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + " " + name + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
