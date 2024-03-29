/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreePath;

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
		assertEquals("path", new TreePath(new Node[] { root, parent, child }), child.getPathFromRoot());
	}

	/** test setName on root node informs listener */
	@Test
	public void testSetNameRootNodeFiresAction() {
		TestTreeModelListener listener = new TestTreeModelListener();
		Node root = new Node("Parent");
		root.addListener(listener);
		root.setName("newName");

		assertEquals("Changed count", 1, listener.getNodesChanged().size());
		assertEquals("Insert count", 0, listener.getNodesInserted().size());
		assertEquals("Removed count", 0, listener.getNodesRemoved().size());
		assertEquals("StructureChanged count", 0, listener.getStructureChanged()
				.size());
		// Check inserted event
		TreeModelEvent e = listener.getNodesChanged().get(0);

		assertTrue("e source", root.equals(e.getSource()));
		assertNull("e path", e.getPath());
		assertEquals("e children length", 1, e.getChildren().length);
		assertTrue("e children 1", e.getChildren()[0].equals(root));
	}

	/** test setName on child node informs listener */
	@Test
	public void testSetNameChildNodeFiresAction() {
		TestTreeModelListener listener = new TestTreeModelListener();
		Node root = new Node("Root");
		Node child = new Node("Child");
		child.setParent(root);
		child.addListener(listener);
		child.setName("newName");

		assertEquals("Changed count", 1, listener.getNodesChanged().size());
		assertEquals("Insert count", 0, listener.getNodesInserted().size());
		assertEquals("Removed count", 0, listener.getNodesRemoved().size());
		assertEquals("StructureChanged count", 0, listener.getStructureChanged()
				.size());
		// Check inserted event
		TreeModelEvent e = listener.getNodesChanged().get(0);

		assertTrue("e source", child.equals(e.getSource()));
		// TODO more unit tests
		assertNotNull("e path", e.getPath());
		// TODO more unit tests
		assertEquals("e childIndex length", 1, e.getChildIndices().length);
		assertEquals("e children length", 1, e.getChildren().length);
		assertTrue("e children 0", e.getChildren()[0].equals(child));
	}

	/** test add informs the listener */
	@Test
	public void testInsertFiresAction() {
		TestTreeModelListener listener = new TestTreeModelListener();
		Node parent = new Node("Parent");
		parent.addListener(listener);
		Node child = new Node("Child");
		parent.insert(child, 0);
		assertEquals("Changed count", 0, listener.getNodesChanged().size());
		assertEquals("Insert count", 1, listener.getNodesInserted().size());
		assertEquals("Removed count", 0, listener.getNodesRemoved().size());
		assertEquals("StructureChanged count", 0, listener.getStructureChanged()
				.size());
		// TODO check inserted event
		// TreeModelEvent e = listener.getNodesInserted().get(0);
	}

	/** test remove informs the listener */
	@Test
	public void testRemoveFiresAction() {

		TestTreeModelListener listener = new TestTreeModelListener();
		Node parent = new Node("Parent");
		Node child = new Node("Child");
		parent.add(child);
		parent.addListener(listener);
		// perform action
		parent.remove(child);
		// test result
		assertEquals("Changed count", 0, listener.getNodesChanged().size());
		assertEquals("Insert count", 0, listener.getNodesInserted().size());
		assertEquals("Removed count", 1, listener.getNodesRemoved().size());
		assertEquals("StructureChanged count", 0, listener.getStructureChanged()
				.size());
		// TODO check removed event
		// TreeModelEvent e = listener.getNodesInserted().get(0);
	};

}
