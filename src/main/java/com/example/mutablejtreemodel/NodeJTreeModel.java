/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Objects of this class form an adapter between a JTree and the model, that
 * being a tree of Node objects.
 *
 * The methods in this class allow the JTree component to traverse the tree.
 *
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 **/
public class NodeJTreeModel extends AbstractTreeModel implements TreeModelListener {

	/** class logger */
	private static final Logger logger = LogManager.getLogger(NodeJTreeModel.class
			.getName());

	/** We specify the root directory when we create the model. */
	private Node root;

	/**
	 * Constructor.
	 */
	public NodeJTreeModel() {
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
	 *            set the root node. This TreeModel will start listening to root
	 *            for Tree events.
	 */
	public void setRoot(Node root) {
		this.root = root;
		root.addListener(this);
	}

	// Misc methods

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
		logger.debug("node='" + node + "', count=" + count);
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
		logger.debug("parent=" + parent + ", index=" + index
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
		logger.debug("parent=" + parent + ", child=" + child
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
		logger.debug("path=" + path + ", newValue=" + newValue);
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

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		logger.debug("TreeModelEvent=" + e);
		fireTreeNodesChanged(e);
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		logger.debug("TreeModelEvent=" + e);
		// TODO thread safe?
		Object children[] = e.getChildren();
		for (Object child : children) {
			if (child instanceof Node) {
				Node node = (Node) child;
				node.addListener(this);
			}
		}
		fireTreeNodesInserted(e);
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		logger.debug("TreeModelEvent=" + e);
		// TODO thread safe?
		Object children[] = e.getChildren();
		for (Object child : children) {
			if (child instanceof Node) {
				Node node = (Node) child;
				node.removeListener(this);
			}
		}
		fireTreeNodesRemoved(e);
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		logger.debug("TreeModelEvent=" + e);
		fireTreeStructureChanged(e);
	}

}
