package classification;

public class Edge {
	public TreeNode x;
	public TreeNode y;
	public double weight;
	public Edge(TreeNode x, TreeNode y){
		this.x = x;
		this.y = y;
	}
	public TreeNode traverseEdge(TreeNode left){
		if(left.equals(x)){
			return y;
		}else if(left.equals(y)){
			return x;
		}else{
			return left;
		}
	}
}
