package com.example.mutablejtreemodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

class TestTreeModelListener implements TreeModelListener {

	private List<TreeModelEvent> nodesChanged = new ArrayList<>();
	private List<TreeModelEvent> nodesInserted = new ArrayList<>();
	private List<TreeModelEvent> nodesRemoved = new ArrayList<>();
	private List<TreeModelEvent> structureChanged = new ArrayList<>();

	public List<TreeModelEvent> getNodesChanged() {
		return nodesChanged;
	}

	public void setChanged(List<TreeModelEvent> list) {
		nodesChanged = list;
	}

	public List<TreeModelEvent> getNodesInserted() {
		return nodesInserted;
	}

	public void setNodeInserted(List<TreeModelEvent> list) {
		nodesInserted = list;
	}

	public List<TreeModelEvent> getNodesRemoved() {
		return nodesRemoved;
	}

	public void setNodesRemoved(List<TreeModelEvent> list) {
		nodesRemoved = list;
	}

	public List<TreeModelEvent> getStructureChanged() {
		return structureChanged;
	}

	public void setStructureChanged(List<TreeModelEvent> list) {
		structureChanged = list;
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		nodesChanged.add(e);
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		nodesInserted.add(e);
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		nodesRemoved.add(e);
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		structureChanged.add(e);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}