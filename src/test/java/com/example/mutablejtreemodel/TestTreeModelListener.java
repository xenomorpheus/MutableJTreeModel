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

	public List<TreeModelEvent> getNodesInserted() {
		return nodesInserted;
	}

	public List<TreeModelEvent> getNodesRemoved() {
		return nodesRemoved;
	}

	public List<TreeModelEvent> getStructureChanged() {
		return structureChanged;
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
}