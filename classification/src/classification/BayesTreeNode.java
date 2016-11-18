package classification;

import java.util.ArrayList;
import java.util.HashMap;

public class BayesTreeNode extends TreeNode {
	ArrayList<Edge> edges;
	HashMap<ArrayList<String[]>, Double> condProbTable;
	int featureIndex; // index of feature in string array of data instance

	public void setFeatureIndex(int i){
		this.featureIndex = i;
	}

	public int getFeatureIndex(){
		return this.featureIndex;
	}
}
