/** This document is AS-IS. No claims are made for suitability for any purpose. */
package com.example.mutablejtreemodel;

/**
 * When listening to Node change events, these are the types of changes.
 * 
 * @author xenomorpheus
 *
 */

public enum NodeChangeType {

	NODE_CHANGED, NODES_CHANGED, NODE_INSERTED, NODES_INSERTED, NODE_REMOVED, NODES_REMOVED, STRUCTURE_CHANGED;

	/**
	 * Return the NodeChangeType that has this string representation.
	 * @param value the String representation of the enum we are trying to find.
	 * @return null or the enum that matches the param value.
	 */
	public static NodeChangeType get(String value) {
		NodeChangeType result = null;
		for (NodeChangeType property : NodeChangeType.values()) {
			if (property.toString().equals(value)) {
				result = property;
				break;
			}
		}
		return result;
	}
}
