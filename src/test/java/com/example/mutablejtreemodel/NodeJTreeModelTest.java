package com.example.mutablejtreemodel;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.junit.Test;

public class NodeJTreeModelTest {

	@Test
	public void testNodeJTreeModel() {
		AbstractTreeModel model = new NodeJTreeModel();
	}

	@Test
	public void testRoot() {
		NodeJTreeModel model = new NodeJTreeModel();
		Node root = new Node();
		model.setRoot(root);
		assertEquals(root, model.getRoot());
	}

	@Test
	public void testAddRemoveTreeModelListener() {
		AbstractTreeModel model = new NodeJTreeModel();
		TreeModelListener listener = new NodeJTreeModel();
		model.addTreeModelListener(listener);
		model.removeTreeModelListener(listener);
	}

	@Test
	public void testIsLeaf() {
		AbstractTreeModel model = new NodeJTreeModel();
		DefaultMutableTreeNode child = new Node();
		assertTrue(model.isLeaf(child));
		DefaultMutableTreeNode parent = new Node();
		parent.add(child);
		assertFalse(model.isLeaf(parent));
	}

	@Test
	public void testGetChildCount() {
		AbstractTreeModel model = new NodeJTreeModel();
		DefaultMutableTreeNode child = new Node();
		DefaultMutableTreeNode parent = new Node();
		assertEquals(0, model.getChildCount(parent));
		parent.add(child);
		assertEquals(1, model.getChildCount(parent));
	}

	@Test
	public void testGetChild() {
		AbstractTreeModel model = new NodeJTreeModel();
		DefaultMutableTreeNode child = new Node("child");
		DefaultMutableTreeNode child1 = new Node("child1");
		DefaultMutableTreeNode parent = new Node();
		parent.add(child);
		parent.add(child1);
		assertEquals(child, model.getChild(parent, 0));
		assertEquals(child1, model.getChild(parent, 1));
	}

	@Test
	public void testGetIndexOfChild() {
		AbstractTreeModel model = new NodeJTreeModel();
		DefaultMutableTreeNode child = new Node("child");
		DefaultMutableTreeNode child1 = new Node("child1");
		DefaultMutableTreeNode parent = new Node();
		parent.add(child);
		parent.add(child1);
		assertEquals(0, model.getIndexOfChild(parent, child));
		assertEquals(1, model.getIndexOfChild(parent, child1));
	}

	@Test
	public void testSetNameFireTreeNodesChanged() {
		NodeJTreeModel model = new NodeJTreeModel();
		Node root = new Node();
		TestTreeModelListener listener = new TestTreeModelListener();
		model.addTreeModelListener(listener);
		model.setRoot(root);
		assertEquals(0, listener.getNodesChanged().size());
		root.setName("root");
		// https://docs.oracle.com/javase/7/docs/api/javax/swing/tree/DefaultTreeModel.html#fireTreeNodesChanged(java.lang.Object,%20java.lang.Object[],%20int[],%20java.lang.Object[])
		var got = listener.getNodesChanged();
		assertEquals(1, got.size());
		assertEquals(root, got.get(0).getSource());
		assertNull(got.get(0).getPath());
		assertEquals(0, got.get(0).getChildIndices().length);
		assertEquals(1, got.get(0).getChildren().length);
		assertEquals(root , got.get(0).getChildren()[0]);
	}

	@Test
	public void testValueForPathChanged() {
		NodeJTreeModel model = new NodeJTreeModel();
		Node root = new Node();
		Node child1 = new Node();
		TestTreeModelListener listener = new TestTreeModelListener();
		model.addTreeModelListener(listener);
		model.setRoot(root);
		List<TreeModelEvent> expected = new ArrayList<>();
		assertEquals(expected, listener.getNodesChanged());
		root.setName("root");
		root.add(child1);
		expected.add(new TreeModelEvent(this, root.getPathFromRoot(), null, null));
		assertEquals(expected, listener.getNodesChanged());
		child1.setName("child1");
		expected.add(new TreeModelEvent(this, child1.getPathFromRoot(), null, null));
		assertEquals(expected, listener.getNodesChanged());
	}

	@Test
	public void testToString() {
		AbstractTreeModel model = new NodeJTreeModel();
		assertEquals("NodeJTreeModel", model.toString());
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
		assertEquals("Changed count", 0, listener.getNodesChanged().size());
		assertEquals("Insert count", 1, listener.getNodesInserted().size());
		assertEquals("Removed count", 0, listener.getNodesRemoved().size());
		assertEquals("StructureChanged count", 0, listener.getStructureChanged()
				.size());
		// Check inserted event
		TreeModelEvent e = listener.getNodesInserted().get(0);
		// assertTrue("e source", treeModel.equals(e.getSource()));
		// TODO more unit tests
		assertNotNull("e path", e.getPath());
		// TODO more unit tests
		assertEquals("e childIndex length", 1, e.getChildIndices().length);
		assertEquals("e children length",1, e.getChildren().length);
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
