package classification;

public class Edge {
	public BayesTreeNode x;
	public BayesTreeNode y;
	public double weight;
	boolean directed = false;

	public Edge(BayesTreeNode x, BayesTreeNode y) {
		this.x = x;
		this.y = y;
	}

	public TreeNode traverseEdge(BayesTreeNode left) {
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
