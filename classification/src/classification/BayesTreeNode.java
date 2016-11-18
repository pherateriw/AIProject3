package classification;

import java.util.ArrayList;
import java.util.HashMap;

public class BayesTreeNode extends TreeNode {
	ArrayList<Edge> edges;
	HashMap<ArrayList<String[]>, Double> condProbTable;
}
