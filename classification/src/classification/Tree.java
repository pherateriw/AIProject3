package classification;

import java.util.ArrayList;

public class Tree {
	private TreeNode root;
	private ArrayList<TreeNode> tree;
	public Tree(TreeNode root){
		this.root = root;
		tree = new ArrayList<TreeNode>();
		tree.add(root);
	}
	public Tree(ArrayList<TreeNode>tree){
		this.tree = new ArrayList<TreeNode>(tree);
		this.root = this.tree.get(0);
	}
	public ArrayList<TreeNode> findLeaves(TreeNode root){
		//TODO finish this
		return tree;
	}
	public ArrayList<TreeNode> findBranches(TreeNode root){
		//TODO finish this
		return tree;
	}
	public Tree removeNode(int index){
		if(tree.size() > index){
			tree.remove(index);
		}else{
			System.out.println("Tree removal FAILURE!!!");
		}
		return this;
	}
	public TreeNode getNode(int index){
		return tree.get(index);
	}
	public TreeNode getRoot() {
		return root;
	}
	
	public ArrayList<TreeNode> getTree() {
		return tree;
	}
	
	public int getTreeSize(){
		return tree.size();
	}
	public void addNode(TreeNode child){
		tree.add(child);
	}
	
}
