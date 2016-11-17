package classification;

import java.util.ArrayList;

public class DecisionTreeNode extends TreeNode {
	public ArrayList<String[]> examples;//examples associated with this node
	public String label; //
	public int attribute; //if is not leaf node, attribute will correspond to a data column.
	//The data will be split into the values for the attribute
	public ArrayList<String> splitvalues;//for example, if parent node has attribute 2 and this node has split values [true]
	//then the examples for this node will only contain the value 'true' for attribute 2
	public int rootindex; //the index in the tree for the parent node
	public int locindex; //the index for the current node
	public DecisionTreeNode(ArrayList<String[]> examples, int rootindex, int locindex){
		this.examples = examples;
		this.rootindex = rootindex;
		this.locindex = locindex;
	}
	
	public int getLocindex() {
		return locindex;
	}

	public void setLocindex(int locindex) {
		this.locindex = locindex;
	}

	public int getRootindex() {
		return rootindex;
	}

	public void setRootindex(int rootindex) {
		this.rootindex = rootindex;
	}

	public ArrayList<String[]> getExamples() {
		return examples;
	}
	public void setExamples(ArrayList<String[]> examples) {
		this.examples = examples;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getAttribute() {
		return attribute;
	}
	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}
	public ArrayList<String> getSplitvalues() {
		return splitvalues;
	}
	public void setSplitvalues(ArrayList<String> splitvalues) {
		this.splitvalues = splitvalues;
	}
	
}
