package com.example.mutablejtreemodel;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;

/**
 * JTree nodes may be listened to for changes. This class provides support for
 * adding and removing listeners, also for sending events to those listeners.
 *
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 **/
public abstract class AbstractTreeModel implements TreeModel {

	/** class logger */
	private static final Logger logger = LogManager.getLogger(AbstractTreeModel.class);

	/**
	 * Those that listen for changes to the model.<br>
	 * TODO: Using weak references for listener set. It's very easy to forget
	 * removing listeners when the actual instance isn't in use any more and
	 * thats a source of memory leak.
	 */
	private List<TreeModelListener> listeners;

	/**
	 * synchronisation lock.
	 * http://javarevisited.blogspot.com/2011/04/synchronization
	 * -in-java-synchronized.html#ixzz2wy76gzSj
	 */
	private final Object objLock = new Object();

	/**
	 * Constructor
	 */
	public AbstractTreeModel() {
		listeners = new ArrayList<TreeModelListener>();
	}

	/**
	 * Add TreeModelListener.
	 *
	 * @param listener
	 *            TreeModelListener
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(TreeModelListener)
	 */
	@Override
	public void addTreeModelListener(TreeModelListener listener) {
		logger.debug("listener: " + listener);
		synchronized (objLock) {
			if (listener != null && !listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	/**
	 * Remove TreeModelListener.
	 *
	 * @param listener
	 *            TreeModelListener
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(TreeModelListener)
	 */
	@Override
	public void removeTreeModelListener(TreeModelListener listener) {
		logger.debug("listener: " + listener);
		synchronized (objLock) {
			if (listener != null) {
				listeners.remove(listener);
			}
		}
	}

	// http://docs.oracle.com/javase/8/docs/api/javax/swing/event/TreeModelListener.html#treeNodesRemoved-javax.swing.event.TreeModelEvent-
	/**
	 * Notify listeners that node(s) have changed.
	 *
	 * @param e
	 *            event
	 */

	public void fireTreeNodesChanged(TreeModelEvent e) {
		logger.debug("TreeModelEvent=" + e);

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
	 * @param e
	 *            event
	 */

	public void fireTreeNodesInserted(TreeModelEvent e) {
		logger.debug("TreeModelEvent=" + e);

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

	public void fireTreeNodesRemoved(TreeModelEvent e) {
		logger.debug("TreeModelEvent=" + e);

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
	public void fireTreeStructureChanged(TreeModelEvent e) {
		logger.debug("TreeModelEvent=" + e);

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

}
