/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * A node in a tree structure.
 * 
 * Nodes will fire change events to listeners e.g. other nodes or 
 * JTreeModel objects.
 * 
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 */
public class Node extends DefaultMutableTreeNode implements ActionListener {

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
	 * TODO Using weak references for listener set. It's very easy to
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

		// old parent, if any.
		super.insert(child, index);

		// We inform listeners that we have changed because we have a new
		// node.
		// TODO should this be child, parent or both?
		fireNodeChanged(new ActionEvent(child, index,
				NodeChangeType.NODE_INSERTED.toString()));
	}

	// MutableTreeNode
	@Override
	public void remove(MutableTreeNode aChild) {
		LOGGER.info("remove node=" + this);
		if (!(aChild instanceof Node)) {
			throw new IllegalArgumentException("Expecting node to be of class "
					+ Node.class.getCanonicalName() + ", but I got "
					+ aChild.getClass().getCanonicalName());
		}

		super.remove(aChild);

		Node child = (Node) aChild;

		// We inform listeners that we have changed nodes.
		// TODO should this be child, parent or both?
		// TODO should we be passing the node as a parameter?
		child.fireNodeChanged(new ActionEvent(child, 0,
				NodeChangeType.NODE_CHANGED.toString()));
		fireNodeChanged(new ActionEvent(this, 0,
				NodeChangeType.NODE_CHANGED.toString()));
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
	 * Notify Listeners that this node has changed in some way. e.g. this node
	 * is about to be die.
	 * 
	 * @param e
	 *            event.
	 */
	private void fireNodeChanged(ActionEvent e) {
		LOGGER.info("fireTreeNodeChanged this='" + this + "'");
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
	 * Add a listener from the list that wish to listen to events involving this
	 * node.
	 * 
	 * @param listener
	 *            listener to add.
	 */
	public void addActionListener(ActionListener listener) {
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
	public void removeActionListener(ActionListener listener) {
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
		ActionEvent event;
		LOGGER.info("destroy node=" + this);

		// Notify listeners this node is being destroyed.
		event = new ActionEvent(this, 0, NodeChangeType.NODE_REMOVED.toString());
		fireNodeChanged(event);
		// If parent still set, remove this node from parent.
		synchronized (objLock) {
			if (null != parent) {
				parent.remove(this);
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
