/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.tree.TreePath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
		final String listenerCountKey = "listenerCountKey";
		AbstractAction childAction = new AbstractAction() {
			/** serial id. */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent event) {
				Integer listenerCount = (Integer) getValue(listenerCountKey);
				putValue(listenerCountKey, listenerCount + 1);
			}
		};
		AbstractAction parentAction = new AbstractAction() {
			/** serial id. */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent event) {
				Integer listenerCount = (Integer) getValue(listenerCountKey);
				putValue(listenerCountKey, listenerCount + 1);
			}
		};
		parentAction.putValue(listenerCountKey, 0);
		Node parent = new Node("Parent");
		parent.addActionListener(parentAction);
		Node child = new Node("Child");
		childAction.putValue(listenerCountKey, 0);
		child.addActionListener(childAction);
		parent.add(child);
		assertTrue("parent count",
				(Integer) parentAction.getValue(listenerCountKey) > 0);
		// assertTrue("child count",(Integer)
		// childAction.getValue(listenerCountKey) > 0);
	}

	/** test add informs the listener */
	@Test
	public void testRemmoveFiresAction() {
		final String listenerCountKey = "listenerCountKey";
		AbstractAction childAction = new AbstractAction() {
			/** serial id. */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent event) {
				Integer listenerCount = (Integer) getValue(listenerCountKey);
				putValue(listenerCountKey, listenerCount + 1);
			}
		};
		AbstractAction parentAction = new AbstractAction() {
			/** serial id. */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent event) {
				Integer listenerCount = (Integer) getValue(listenerCountKey);
				putValue(listenerCountKey, listenerCount + 1);
			}
		};
		Node parent = new Node("Parent");
		Node child = new Node("Child");
		parent.add(child);
		// listeners added now so that counters are at zero
		parentAction.putValue(listenerCountKey, 0);
		childAction.putValue(listenerCountKey, 0);
		parent.addActionListener(parentAction);
		child.addActionListener(childAction);
		// perform action
		parent.remove(child);
		// test result
		assertTrue("parent count",
				(Integer) parentAction.getValue(listenerCountKey) > 0);
		assertTrue("child count",
				(Integer) childAction.getValue(listenerCountKey) > 0);
	}

}
