package classification;

public class Edge {
	public TreeNode x;
	public TreeNode y;
	public double weight;
	boolean directed = false;

	public Edge(TreeNode x, TreeNode y) {
		this.x = x;
		this.y = y;
	}

	public TreeNode traverseEdge(TreeNode left) {
		if (!directed) {
			if (left.equals(x)) {
				return y;
			} else if (left.equals(y)) {
				return x;
			} else {
				return left;
			}
		}else{
			if (left.equals(x)) {
				return y;
			} else if (left.equals(y)) {
				return y;
			} else {
				return left;
			}
		}
	}
}
