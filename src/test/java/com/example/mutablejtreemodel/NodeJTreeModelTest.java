package com.example.mutablejtreemodel;

import static org.junit.Assert.*;

import javax.swing.event.TreeModelEvent;

import org.junit.Test;

public class NodeJTreeModelTest {

	@Test
	public void testNodeJTreeModel() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRoot() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetRoot() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddTreeModelListener() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveTreeModelListener() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsLeaf() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetChildCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetChild() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIndexOfChild() {
		fail("Not yet implemented");
	}

	@Test
	public void testValueForPathChanged() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testTreeNodesChanged() {
		fail("Not yet implemented");
	}

	@Test
	public void testTreeNodesInserted() {
		// Setup
		NodeJTreeModel treeModel = new NodeJTreeModel();
		Node root = new Node("Root");
		treeModel.setRoot(root);
		TestTreeModelListener listener = new TestTreeModelListener();
		// Add a listener, then add a new Node.
		treeModel.addTreeModelListener(listener);
		Node child = new Node("Child");
		root.insert(child, 0);
		// Test that we heard the event.
		assertEquals("Changed count", listener.getNodesChanged().size(), 0);
		assertEquals("Insert count", listener.getNodesInserted().size(), 1);
		assertEquals("Removed count", listener.getNodesRemoved().size(), 0);
		assertEquals("StructureChanged count", listener.getStructureChanged()
				.size(), 0);
		// Check inserted event
		TreeModelEvent e = listener.getNodesInserted().get(0);
		// assertTrue("e source", treeModel.equals(e.getSource()));
		// TODO more unit tests
		assertNotNull("e path", e.getPath());
		// TODO more unit tests
		assertEquals("e childIndex length", e.getChildIndices().length, 1); 
		assertEquals("e children length", e.getChildren().length, 1);
		assertTrue("e children 0", e.getChildren()[0].equals(child));
		fail("Not yet completed");
	}

	@Test
	public void testTreeNodesRemoved() {
		fail("Not yet implemented");
	}

	@Test
	public void testTreeStructureChanged() {
		fail("Not yet implemented");
	}

}
