/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * A node in a tree structure.
 * 
 * Nodes will fire change events to listeners which can be other nodes, or
 * JTreeModel objects.
 * 
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 */
public class Node implements MutableTreeNode, ActionListener {

	/** class logger */
	private static final Logger LOGGER = Logger.getLogger(Node.class.getName());

	/** synchronisation lock */
	private final Object objLock = new Object();
	// Read more:
	// http://javarevisited.blogspot.com/2011/04/synchronization-in-java-synchronized.html#ixzz2wy76gzSj

	/** The human identifiable name of this node. */
	private String name;

	/** Our parent node. */
	private MutableTreeNode parent;

	/** The child nodes of this node. */
	private List<Node> children;

	/**
	 * Those that listen for changes to the model. E.g. other nodes or
	 * JTreeModel. Using weak references for listener set. It's very easy to
	 * forget removing listeners when the actual instance isn't in use any more
	 * and thats a source of memory leak.
	 */
	private List<ActionListener> listeners;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the human identifiable name of this node.
	 */
	public Node(String name) {
		this.name = name;
		children = new ArrayList<>();
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
	public void setParent(MutableTreeNode newParent) {
		parent = newParent;
	}

	// MutableTreeNode
	@Override
	public MutableTreeNode getParent() {
		return parent;
	}

	// MutableTreeNode
	@Override
	public boolean isLeaf() {

		// This will generate the 'file' icon for the leafs,
		// and the folder icons for the non-leafs.Also, you will probably need
		// the implementation later.
		return children.isEmpty();
	}

	// MutableTreeNode
	@Override
	public Enumeration<Node> children() {
		return Collections.enumeration(children);
	}

	// MutableTreeNode
	@Override
	public int getChildCount() {
		synchronized (objLock) {
			return children.size();
		}
	}

	// MutableTreeNode
	@Override
	public Node getChildAt(int childIndex) {
		synchronized (objLock) {
			Node child = children.get(childIndex);
			LOGGER.info("At index=" + childIndex + " found child named '"
					+ child.name + "'");
			return child;
		}
	}

	// MutableTreeNode
	@Override
	public int getIndex(TreeNode child) {
		Node childNode = (Node) child;
		synchronized (objLock) {
			int index = children.indexOf(child);
			LOGGER.info("getIndexOfChild: In node '" + name + "' found child "
					+ childNode.name + " at index " + index);
			return index;
		}
	}

	// MutableTreeNode
	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	/**
	 * Add the child node to the end of a list of children nodes.
	 * 
	 * @param child
	 *            new child node.
	 */
	public void add(MutableTreeNode child) {
		insert(child, children.size());
	}

	// MutableTreeNode
	@Override
	public void insert(MutableTreeNode child, int index) {
		LOGGER.info("Parent='" + name + "', child='" + child + "' at index="
				+ index);
		Node childNode = (Node) child;
		synchronized (objLock) {
			// child node from current parent, if any.
			MutableTreeNode currentContainer = childNode.getParent();
			if (null != currentContainer) {
				currentContainer.remove(child);
			}
			// add child to children.
			children.add(index, childNode);

			childNode.setParent((MutableTreeNode) this);

			// We listen for changes on nodes we hold.
			childNode.addActionListener(this);
			// We inform listeners that we have changed because we have a new
			// node.
			fireNodeChanged(new ActionEvent(child, index,
					NodeChangeType.NODE_INSERTED.toString()));
		}
	}

	// MutableTreeNode
	@Override
	public void remove(MutableTreeNode node) {
		Node child = (Node) node;
		LOGGER.info("remove node=" + this);
		synchronized (objLock) {
			children.remove(child);
			child.setParent(null);

			// Stop listening to child node.
			child.removeActionListener(this);
		}
	}

	// MutableTreeNode
	@Override
	public void remove(int index) {
		MutableTreeNode node = (MutableTreeNode) children.get(index);
		remove(node);
	}

	// MutableTreeNode
	@Override
	public void setUserObject(Object object) {
		// TODO Auto-generated method stub

	}

	// MutableTreeNode
	@Override
	public void removeFromParent() {
		((Node) parent).remove((MutableTreeNode) this);

	}

	/**
	 * @return a path of nodes leading from root. Last node is this node.
	 */

	public TreePath getPathFromRoot() {
		TreeNode node = (TreeNode) this;
		ArrayList<TreeNode> nodeArrayList = new ArrayList<>();
		synchronized (objLock) {
			while ((null != node)) {
				nodeArrayList.add(0, node);
				node = node.getParent();
			}
			return new TreePath(nodeArrayList.toArray(new Node[nodeArrayList
					.size()]));
		}
	}

	/**
	 * Notify Listeners that this node has changed in some way. e.g. this node
	 * is about to be die.
	 * 
	 * @param e
	 *            event.
	 */
	private void fireNodeChanged(ActionEvent e) {
		LOGGER.info("fireTreeNodeChanged node='" + this + "'");
		ActionListener[] tmpListeners = null;
		// Don't leak the lock.
		synchronized (objLock) {
			tmpListeners = listeners.toArray(new ActionListener[listeners
					.size()]);
		}
		for (ActionListener listener : tmpListeners) {
			listener.actionPerformed(e);
		}
	}

	/**
	 * Remove a listener from the list that wish to listen to events involving
	 * this node.
	 * 
	 * @param listener
	 *            listener to add.
	 */
	public void addActionListener(ActionListener listener) {
		LOGGER.info("addActionListener for '" + this + "', listener='"
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
	public void removeActionListener(ActionListener listener) {
		LOGGER.info("removeActionListener for '" + this + "', listener="
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
		ActionEvent event;
		LOGGER.info("destroy node=" + this);

		// Notify listeners this node is being destroyed.
		event = new ActionEvent(this, 0, NodeChangeType.NODE_REMOVED.toString());
		fireNodeChanged(event);
		// If parent still set, remove this node from parent.
		synchronized (objLock) {
			if (null != parent) {
				parent.remove((MutableTreeNode) this);
			}
		}
		// TODO free resources of this node at this subtype.
		// TODO call parent class's destroy.
	}

	/**
	 * Perform actions when we are notified about an event. e.g. the death of
	 * one of our child nodes.
	 * 
	 * @param event
	 *            the event we have been informed about.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		LOGGER.info("actionPerformed, event=" + event.toString());
		String command = event.getActionCommand();
		Object source = event.getSource();
		if (NodeChangeType.NODE_REMOVED.toString().equals(command)) {
			LOGGER.info(command + " event");
			if (source instanceof Node) {
				Node child = (Node) source;
				LOGGER.info("actionPerformed,  Source is node=" + child);
				synchronized (objLock) {
					if (children.contains(child)) {
						LOGGER.info("actionPerformed,     '" + this
								+ "' removing child node called='" + child
								+ "'");
						children.remove(child);
					}
				}
			}
		}
	}
}
