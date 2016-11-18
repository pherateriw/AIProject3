package classification;

import java.util.ArrayList;

public abstract class TreeNode {
	public ArrayList<TreeNode> children;

	public ArrayList<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<TreeNode> children) {
		this.children = children;
	}
	
}
