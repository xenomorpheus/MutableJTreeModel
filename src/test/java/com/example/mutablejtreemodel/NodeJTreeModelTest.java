package com.example.mutablejtreemodel;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.junit.Test;

class MyTreeModelListener implements TreeModelListener{

	private int treeNodesChanged = 0;

	public MyTreeModelListener(){
		super();
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		System.out.print("treeNodesChanged");
		treeNodesChanged++;
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		System.out.print("treeNodesInserted");
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		System.out.print("treeNodesRemoved");
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		System.out.print("treeStructureChanged");
	}

	public int getTreeNodesChanged() {
		return treeNodesChanged;
	}

};

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
	public void testValueForPathChanged() {
		NodeJTreeModel model = new NodeJTreeModel();
		Node root = new Node();
		Node child1 = new Node();
		MyTreeModelListener listener = new MyTreeModelListener();
		model.addTreeModelListener(listener);
		model.setRoot(root);
		assertEquals(0, listener.getTreeNodesChanged());
		root.setName("root");
		root.add(child1);
		assertEquals(1, listener.getTreeNodesChanged());
		child1.setName("child1");
		assertEquals(2, listener.getTreeNodesChanged());
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
