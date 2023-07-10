package com.example.mutablejtreemodel;

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
	public void testToString() {
		AbstractTreeModel model = new NodeJTreeModel();
		assertEquals("NodeJTreeModel", model.toString());
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
		// https://docs.oracle.com/javase/7/docs/api/javax/swing/tree/DefaultTreeModel.html#fireTreeNodesChanged(java.lang.Object,%20java.lang.Object[],%20int[],%20java.lang.Object[])
		NodeJTreeModel model = new NodeJTreeModel();
		Node root = new Node();
		TestTreeModelListener listener = new TestTreeModelListener();
		model.addTreeModelListener(listener);
		model.setRoot(root);
		assertEquals(0, listener.getNodesChanged().size());
		root.setName("root");
		assertEquals("Insert count", 0, listener.getNodesInserted().size());
		assertEquals("Removed count", 0, listener.getNodesRemoved().size());
		assertEquals("StructureChanged count", 0, listener.getStructureChanged()
				.size());
		var got = listener.getNodesChanged();
		assertEquals(1, got.size());
		assertEquals(root, got.get(0).getSource());
		assertNull(got.get(0).getPath());
		assertEquals(0, got.get(0).getChildIndices().length);
		assertEquals(1, got.get(0).getChildren().length);
		assertEquals(root, got.get(0).getChildren()[0]);
	}

	@Test
	public void testAddFireTreeNodesInserted() {
		// https://docs.oracle.com/javase/7/docs/api/javax/swing/tree/DefaultTreeModel.html#fireTreeNodesInserted(java.lang.Object,%20java.lang.Object[],%20int[],%20java.lang.Object[])
		NodeJTreeModel model = new NodeJTreeModel();
		Node root = new Node("root");
		TestTreeModelListener listener = new TestTreeModelListener();
		model.addTreeModelListener(listener);
		model.setRoot(root);
		Node child1 = new Node("child");
		root.add(child1);
		assertEquals("Changed count", 0, listener.getNodesChanged().size());
		assertEquals("Removed count", 0, listener.getNodesRemoved().size());
		assertEquals("StructureChanged count", 0, listener.getStructureChanged()
				.size());
		var got = listener.getNodesInserted();
		assertEquals(1, got.size());
		assertEquals(root, got.get(0).getSource());
		assertEquals(1, got.get(0).getPath().length);
		assertEquals(root, got.get(0).getPath()[0]);
		assertEquals(1, got.get(0).getChildren().length);
		assertEquals(0, got.get(0).getChildIndices()[0]);
		assertEquals(child1, got.get(0).getChildren()[0]);
	}


	@Test
	public void testTreeNodesChanged() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertFireTreeNodesInserted() {
		// https://docs.oracle.com/javase/7/docs/api/javax/swing/tree/DefaultTreeModel.html#fireTreeNodesInserted(java.lang.Object,%20java.lang.Object[],%20int[],%20java.lang.Object[])
		NodeJTreeModel model = new NodeJTreeModel();
		Node root = new Node("root");
		TestTreeModelListener listener = new TestTreeModelListener();
		model.setRoot(root);
		root.insert(new Node("child0"), 0);
		root.insert(new Node("child2"), 1);
		Node child1 = new Node("child1");
		model.addTreeModelListener(listener);
		int position = 1;
		root.insert(child1, position);
		assertEquals("Changed count", 0, listener.getNodesChanged().size());
		assertEquals("Removed count", 0, listener.getNodesRemoved().size());
		assertEquals("StructureChanged count", 0, listener.getStructureChanged()
				.size());
		var got = listener.getNodesInserted();
		assertEquals(1, got.size());
		assertEquals(root, got.get(0).getSource());
		assertEquals(position, got.get(0).getPath().length);
		assertEquals(root, got.get(0).getPath()[0]);
		assertEquals(1, got.get(0).getChildren().length);
		assertEquals(1, got.get(0).getChildIndices()[0]);
		assertEquals(child1, got.get(0).getChildren()[0]);
	}

	@Test
	public void testTreeNodesRemoved() {
		// https://docs.oracle.com/javase/7/docs/api/javax/swing/tree/DefaultTreeModel.html#fireTreeNodesRemoved(java.lang.Object,%20java.lang.Object[],%20int[],%20java.lang.Object[])
				NodeJTreeModel model = new NodeJTreeModel();
		Node root = new Node("root");
		TestTreeModelListener listener = new TestTreeModelListener();
		model.setRoot(root);
		root.add(new Node("child0"));
		Node child1 = new Node("child1");
		root.add(child1);
		root.add(new Node("child2"));
		model.addTreeModelListener(listener);
		var postition = root.getIndex(child1);
		root.remove(child1);
		assertEquals("Insert count", 0, listener.getNodesInserted().size());
		assertEquals("Changed count", 0, listener.getNodesChanged().size());
		assertEquals("StructureChanged count", 0, listener.getStructureChanged().size());
		var got = listener.getNodesRemoved();
		assertEquals(1, got.size());
		assertEquals(root, got.get(0).getSource());
		assertEquals(1, got.get(0).getPath().length);
		assertEquals(root, got.get(0).getPath()[0]);
		assertEquals(1, got.get(0).getChildren().length);
		assertEquals(child1, got.get(0).getChildren()[0]);
		assertEquals(postition, got.get(0).getChildIndices()[0]);
		assertNull("child1's parent", child1.getParent());
	}

	@Test
	public void testTreeStructureChanged() {
		fail("Not yet implemented");
	}

}
