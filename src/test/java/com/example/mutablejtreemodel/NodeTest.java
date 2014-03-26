/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import javax.swing.tree.TreePath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.example.mutablejtreemodel.Node;

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
		assertFalse("Setup - bagStart contains cookie",
				bagStart.isOurChild(cookie));
		assertFalse("Setup - bagFinal contains cookie",
				bagStart.isOurChild(cookie));
		// add cookie to one bag
		bagStart.add(cookie);
		assertEquals("Start - bagStart count setup ", 1,
				bagStart.getChildCount());
		assertEquals("Start - bagFinal count setup ", 0,
				bagFinal.getChildCount());
		assertEquals("Start - cookie's container", bagStart,
				cookie.getParent());
		assertTrue("Start - bagStart contains cookie",
				bagStart.isOurChild(cookie));
		assertFalse("Start - bagFinal contains cookie",
				bagFinal.isOurChild(cookie));
		// transfer cookie to other bag
		bagFinal.add(cookie);
		assertEquals("Final - bagStart count setup ", 0,
				bagStart.getChildCount());
		assertEquals("Final - bagFinal count setup ", 1,
				bagFinal.getChildCount());
		assertEquals("Final - cookie's container", bagFinal,
				cookie.getParent());
		assertFalse("Final - bagStart contains cookie",
				bagStart.isOurChild(cookie));
		assertTrue("Final - bagFinal contains cookie",
				bagFinal.isOurChild(cookie));
	}

	/** test the destroy method */
	@Test
	public void testDestroy() {
		Node parent = new Node("Parent");
		Node child = new Node("Child");
		parent.add(child);
		assertEquals("before - location", parent, child.getParent());
		assertTrue("before - location contains child",
				parent.isOurChild(child));

		// call destroy
		child.destroy();
		assertEquals("after - location", null, child.getParent());
		assertFalse("after - location contains child",
				parent.isOurChild(child));
	}

	/** test the pathToRoot method */
	@Test
	public void testPathToRoot() {
		Node parent = new Node("Parent");
		Node child = new Node("Child");
		parent.add(child);
		TreePath got = child.getPathToRoot();
		TreePath expected = new TreePath(new Node[] { child, parent });
		assertEquals("path", expected, got);
	}

}
