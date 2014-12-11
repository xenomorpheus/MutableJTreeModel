/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

/**
 * Objects of this class form an adapter between a JTree and the model, that
 * being a tree of Node objects.
 * 
 * The methods in this class allow the JTree component to traverse the tree.
 * 
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 **/
public class NodeJTreeModel implements TreeModel, TreeModelListener {

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
		root.addListener(this);
	}

	// Misc methods

	/**
	 * Method addTreeModelListener.
	 * 
	 * @param listener
	 *            TreeModelListener
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(TreeModelListener)
	 */
	@Override
	public void addTreeModelListener(TreeModelListener listener) {
		LOGGER.debug("listener: " + listener);
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
		LOGGER.debug("listener: " + listener);
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
		if (!(node instanceof Node)) {
			throw new IllegalArgumentException("Expecting node to be of class "
					+ Node.class.getCanonicalName() + ", but I got "
					+ node.getClass().getCanonicalName());
		}
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
		if (!(node instanceof Node)) {
			throw new IllegalArgumentException("Expecting node to be of class "
					+ Node.class.getCanonicalName() + ", but I got "
					+ node.getClass().getCanonicalName());
		}
		int count = ((Node) node).getChildCount();
		LOGGER.debug("node='" + node + "', count=" + count);
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
		if (!(parent instanceof DefaultMutableTreeNode)) {
			throw new IllegalArgumentException(
					"Expecting parent to be of class "
							+ DefaultMutableTreeNode.class.getCanonicalName()
							+ ", but I got "
							+ parent.getClass().getCanonicalName());
		}
		Object child = ((DefaultMutableTreeNode) parent).getChildAt(index);
		LOGGER.debug("parent=" + parent + ", index=" + index
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
		if (!(parent instanceof Node)) {
			throw new IllegalArgumentException(
					"Expecting parent to be of class "
							+ Node.class.getCanonicalName() + ", but I got "
							+ parent.getClass().getCanonicalName());
		}
		if (!(child instanceof Node)) {
			throw new IllegalArgumentException(
					"Expecting child to be of class "
							+ Node.class.getCanonicalName() + ", but I got "
							+ child.getClass().getCanonicalName());
		}
		int index = ((Node) parent).getIndex((Node) child);
		LOGGER.debug("parent=" + parent + ", child=" + child
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
		LOGGER.debug("path=" + path + ", newValue="
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

	// http://docs.oracle.com/javase/8/docs/api/javax/swing/event/TreeModelListener.html#treeNodesRemoved-javax.swing.event.TreeModelEvent-
	/**
	 * Notify listeners that node(s) have changed.
	 * 
	 * @param e
	 *            event
	 */

	private void fireTreeNodesChanged(TreeModelEvent e) {
		LOGGER.debug("TreeModelEvent=" + e);

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
		LOGGER.debug("TreeModelEvent=" + e);

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
		LOGGER.debug("TreeModelEvent=" + e);

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
		LOGGER.debug("TreeModelEvent=" + e);

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
		LOGGER.debug("TreeModelEvent=" + e);
		fireTreeNodesChanged(e);
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		LOGGER.debug("TreeModelEvent=" + e);
		fireTreeNodesInserted(e);
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		LOGGER.debug("TreeModelEvent=" + e);
		fireTreeNodesRemoved(e);
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		LOGGER.debug("TreeModelEvent=" + e);
		fireTreeStructureChanged(e);
	}

}
