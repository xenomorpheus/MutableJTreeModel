/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

import javax.swing.SwingUtilities;

/**
 * Main program. Create a root node and start a tree editor.
 * 
 * @author xenomorpheus
 * @version $Revision: 1.0 $
 */
public class Main {
	/**
	 * Method main.
	 * 
	 * @param args
	 *            Command line arguments. Not used.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new NodeJTreeEditor(new Node("Root Node"));
			}
		});
	}
}
