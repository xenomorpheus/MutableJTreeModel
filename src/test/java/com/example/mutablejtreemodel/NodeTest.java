/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 */
public class NodeTest {

	/**
	 * Add function should be smart enough to cause the removal from the losing
	 * container.
	 * 
	 */
	@Test
	public void testAddDoesRemove() {
		Node bagStart = new Node("Bag Start");
		Node bagFinal = new Node("Bag Final");
		Node cookie = new Node("Cookie");
		assertEquals("Setup - bagStart count setup ", 0,
				bagStart.getChildCount());
		assertEquals("Setup - bagFinal count setup ", 0,
				bagFinal.getChildCount());
		assertNull("Setup - cookie's container", cookie.getParent());
		// add cookie to one bag
		bagStart.add(cookie);
		assertEquals("Start - bagStart count setup ", 1,
				bagStart.getChildCount());
		assertEquals("Start - bagFinal count setup ", 0,
				bagFinal.getChildCount());
		assertEquals("Start - cookie's container", bagStart, cookie.getParent());
		// transfer cookie to other bag
		bagFinal.add(cookie);
		assertEquals("Final - bagStart count setup ", 0,
				bagStart.getChildCount());
		assertEquals("Final - bagFinal count setup ", 1,
				bagFinal.getChildCount());
		assertEquals("Final - cookie's container", bagFinal, cookie.getParent());
	}

	/** test the destroy method */
	@Test
	public void testDestroy() {
		Node parent = new Node("Parent");
		Node child = new Node("Child");
		parent.add(child);
		assertEquals("before - location", parent, child.getParent());
		assertEquals("before - location contains child", 1,
				parent.getChildCount());

		// call destroy
		child.destroy();
		assertEquals("after - location", null, child.getParent());
		assertEquals("after - location contains child", 0,
				parent.getChildCount());
	}

	/** test the pathFromRoot method */
	@Test
	public void testPathFromRoot() {
		Node root = new Node("Root");
		Node parent = new Node("Parent");
		Node child = new Node("Child");
		root.add(parent);
		parent.add(child);
		TreePath got = child.getPathFromRoot();
		TreePath expected = new TreePath(new Node[] { root, parent, child });
		assertEquals("path", expected, got);
	}

	/** test add informs the listener */
	@Test
	public void testAddFiresAction() {
		MyTreeModelListener listener = new MyTreeModelListener();
		Node parent = new Node("Parent");
		parent.addListener(listener);
		Node child = new Node("Child");
		parent.add(child);
		assertEquals("parent Insert count", listener.getInsertCount() , 1);
		assertEquals("parent Removed count", listener.getRemovedCount(), 0);
	}

	/** test add informs the listener */
	@Test
	public void testRemoveFiresAction() {

		MyTreeModelListener listener = new MyTreeModelListener();
		Node parent = new Node("Parent");
		Node child = new Node("Child");
		parent.add(child);
		parent.addListener(listener);
		// perform action
		parent.remove(child);
		// test result
		assertEquals("parent Insert count", listener.getInsertCount() , 0);
		assertEquals("parent Removed count", listener.getRemovedCount(), 1);
	}

	class MyTreeModelListener implements TreeModelListener {

		private int changedCount = 0;
		private int insertCount = 0;
		private int removedCount = 0;
		private int structureChangedCount = 0;

		public int getChangedCount() {
			return changedCount;
		}

		public void setChangedCount(int changedCount) {
			this.changedCount = changedCount;
		}

		public int getInsertCount() {
			return insertCount;
		}

		public void setInsertCount(int insertCount) {
			this.insertCount = insertCount;
		}

		public int getRemovedCount() {
			return removedCount;
		}

		public void setRemovedCount(int removedCount) {
			this.removedCount = removedCount;
		}

		public int getStructureChangedCount() {
			return structureChangedCount;
		}

		public void setStructureChangedCount(int structureChangedCount) {
			this.structureChangedCount = structureChangedCount;
		}

		@Override
		public void treeNodesChanged(TreeModelEvent e) {
			changedCount++;
		}

		@Override
		public void treeNodesInserted(TreeModelEvent e) {
			insertCount++;
		}

		@Override
		public void treeNodesRemoved(TreeModelEvent e) {
			removedCount++;
		}

		@Override
		public void treeStructureChanged(TreeModelEvent e) {
			structureChangedCount++;
		}
	};

}
