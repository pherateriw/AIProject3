package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DecisionTree extends Algorithm {
	ArrayList<String[]> trainset;
	ArrayList<String[]> valset;
	Tree tree;
	ArrayList<String> classlabels;

	void test(ArrayList data) {

	}

	void train(ArrayList data) {
		trainset = (ArrayList<String[]>) (ArrayList<?>) (data);
		classlabels = new ArrayList<String>();
		// generate class label array
		for (int i = 0; i < trainset.size(); i++) {
			if (!classlabels.contains(trainset.get(i)[trainset.get(i).length - 1])) {
				classlabels.add(trainset.get(i)[trainset.get(i).length - 1]);
			}
		}
		// create validation set
		valset = createValSet(trainset, 0);
		// create root node
		DecisionTreeNode root = new DecisionTreeNode(trainset, -1, 0);
		// generate attribute array
		ArrayList<Integer> attributes = new ArrayList<Integer>();
		for (int i = 0; i < trainset.get(0).length - 1; i++) {
			attributes.add(i);
		}

		// call treeBuilder on data, attribute array, root
		treeBuilder(trainset, attributes, root);

		// call pruning

	}

	public void treeBuilder(ArrayList<String[]> subset, ArrayList<Integer> attributes, DecisionTreeNode parent) {
		// count the numbers of examples for each class
		HashMap<String, Integer> countmap = countAllClasses(subset);
		// if all examples have the same class
		if (countmap.size() == 1) {
			// System.out.println(team1.keySet().toArray()[0]);
			String key = "";
			for (Map.Entry<String, Integer> entry : countmap.entrySet()) {
				key = entry.getKey();
			}
			// parent.label = class label
			// remove all children for parent
			parent.label = key;
			parent.children = new ArrayList<TreeNode>();
		} else if (attributes.size() < 1) {
			// else if attributes is empty
			HashMap.Entry<String, Integer> maxEntry = null;

			for (HashMap.Entry<String, Integer> entry : countmap.entrySet()) {
				if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
					maxEntry = entry;
				}
			}
			parent.label = maxEntry.getKey();
			// parent.label = class label with the highest count
			// remove all children for parent
			parent.children = new ArrayList<TreeNode>();
		}
		// else if subset.size == 1
		else if (subset.size() == 1) {
			// parent.label = class label of example
			parent.label = subset.get(0)[subset.get(0).length - 1];
			// remove all children for parent
			parent.children = new ArrayList<TreeNode>();
		}
		// else if subset is empty and parent is not root
		// else if(subset.isEmpty()){ //trying to handle this without this if
		// statement at creation time for children
		// int rindex = parent.rootindex;
		//
		// //remove current node from tree and parent.children
		// TreeNode r;
		// if(rindex != -1){
		// r = tree.getNode(rindex);
		// }
		// else{
		// return;
		// }
		//
		// tree.removeNode(parent.locindex);
		// r.children.remove(parent);
		// }
		// else
		else {
			HashMap<Integer, Double> gainmap = new HashMap<Integer, Double>();
			for (int a : attributes) {
				gainmap.put(a, calcGainRatio());
			}
			// calculate Gain Ratio for each attribute
			// select attribute with highest Gain Ratio
			HashMap.Entry<Integer, Double> maxEntry = null;

			for (HashMap.Entry<Integer, Double> entry : gainmap.entrySet()) {
				if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
					maxEntry = entry;
				}
			}
			parent.attribute = maxEntry.getKey();
			// parent.attribute = selected attribute
			// remove attribute from attributes
			int i = attributes.indexOf(maxEntry.getKey());
			attributes.remove(i);
			// identify values for attribute
			for (int j = 0; j < subset.size(); j++) {
				if (!parent.splitvalues.contains(subset.get(j)[maxEntry.getKey()])) {
					parent.splitvalues.add(subset.get(j)[maxEntry.getKey()]);
				}

				// parent.splitvalues = possible values for attribute
			}
			for (int j = 0; j < parent.splitvalues.size(); j++) {
				// divide subset by attribute values
				ArrayList<String[]> subsubset = new ArrayList<String[]>();
				for (int k = 0; k < subset.size(); k++) {
					if (parent.splitvalues.get(j) == subset.get(k)[parent.attribute]) {
						subsubset.add(subset.get(k));
					}
				}
				if (subsubset.size() > 0) {
					TreeNode child = new DecisionTreeNode(subsubset, parent.locindex, tree.getTreeSize());
					tree.addNode(child);
					// create children using subsubsets, rootindex and curindex
					// add children to children list and to tree
					parent.children.add(child);
					treeBuilder(subsubset, attributes, (DecisionTreeNode) (child));
					// for each child
					// call treeBuilder on child
				}
			}

		}

	}

	public void pruneTree() {

	}

	public ArrayList<String[]> createValSet(ArrayList<String[]> data, double ratio) {
		// TODO finish this
		return data;
	}

	protected double calcGainRatio() {
		return 1;
	}

	private double calcI() {
		return 1;
	}

	private double calcE() {
		return 1;
	}

	private double calcIV() {
		return 1;
	}

	private int countclass(String classlabel, ArrayList<String[]> data) {
		int classloc = data.get(0).length - 1;
		int count = 0;
		for (String[] i : data) {
			if (classlabel == i[classloc]) {
				count++;
			}
		}
		return count;
	}

	private HashMap<String, Integer> countAllClasses(ArrayList<String[]> subset) {
		HashMap<String, Integer> countmap = new HashMap<String, Integer>();
		for (int i = 0; i < classlabels.size(); i++) {
			if (!countmap.containsKey(classlabels.get(i))) {
				countmap.put(classlabels.get(i), countclass(classlabels.get(i), subset));
			} else {
				countmap.remove(classlabels.get(i));
				countmap.put(classlabels.get(i), countclass(classlabels.get(i), subset));
			}
		}
		return countmap;
	}
}
