package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class DecisionTree extends Algorithm {
	ArrayList<String[]> trainset;
	ArrayList<String[]> valset;
	Tree tree;
	ArrayList<String> classlabels;
	public DecisionTree(ArrayList<String[]> data){
		
	}
	void test(ArrayList data) {

	}
	
	void train(ArrayList data) {
		super.get_logger().log(Level.INFO, "ID3 Training started");
		trainset = (ArrayList<String[]>) (ArrayList<?>) (data);
		classlabels = new ArrayList<String>();
		// generate class label array
		for (int i = 0; i < trainset.size(); i++) {
			if (!classlabels.contains(trainset.get(i)[trainset.get(i).length - 1])) {
				classlabels.add(trainset.get(i)[trainset.get(i).length - 1]);
			}
		}
		super.get_logger().log(Level.INFO, "Class labels set created");
		// create validation set
		valset = createValSet(trainset, 0);
		super.get_logger().log(Level.INFO, "Validation set created");
		// create root node
		DecisionTreeNode root = new DecisionTreeNode(trainset, -1, 0);
		tree = new Tree(root);
		super.get_logger().log(Level.INFO, "Tree created");
		// generate attribute array
		ArrayList<Integer> attributes = new ArrayList<Integer>();
		for (int i = 0; i < trainset.get(0).length - 1; i++) {
			attributes.add(i);
		}
		super.get_logger().log(Level.INFO, "Attribute set created");
		// call treeBuilder on data, attribute array, root
		treeBuilder(trainset, attributes, root);
		super.get_logger().log(Level.INFO, "Tree Built");
		// call pruning

	}

	public void treeBuilder(ArrayList<String[]> subset, ArrayList<Integer> attributes, DecisionTreeNode parent) {
		super.get_logger().log(Level.INFO, "Selecting attribute/label for Node number " + tree.getTreeSize());
		// count the numbers of examples for each class
		HashMap<String, Integer> countmap = countAllClasses(subset);
		super.get_logger().log(Level.INFO, "Hashmap of class numbers created");
		// if all examples have the same class
		if (countmap.size() == 1) {
			super.get_logger().log(Level.INFO, "All examples of Node Number " +  tree.getTreeSize() + " are the same class");
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
			super.get_logger().log(Level.INFO, "Attributes for Node Number " +  tree.getTreeSize() + " are empty");
			
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
			super.get_logger().log(Level.INFO, "Only one example for Node Number " +  tree.getTreeSize());
			
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
			super.get_logger().log(Level.INFO, "Expanding tree at " +  tree.getTreeSize() + "");
			
			//calc Gain ratio:
			HashMap<Integer, HashMap<Double, HashMap<String, ArrayList<String[]>>>> outer = new HashMap<Integer, HashMap<Double, HashMap<String, ArrayList<String[]>>>>();
			//Nested Hashmap!
			//map of attribute -> map of attribute values -> map of subset of data -> gain ratio
			for(Integer i: attributes){//for each attribute
				HashMap<Double, HashMap<String, ArrayList<String[]>>> inner = new HashMap<Double, HashMap<String, ArrayList<String[]>>>();
				HashMap<String, ArrayList<String[]>> russiandoll= new HashMap<String, ArrayList<String[]>>();
				for(String[] ex : subset){//for each example in the dataset
					if(!inner.containsKey(ex[i])){
						
						
						//create subsubset of data with value ex[i]
						ArrayList<String[]> subsub = new ArrayList<String[]>();
						for(String[] ex2: subset){
							if(ex2[i] == ex[i]){
								subsub.add(ex2);
							}
						}
						
						//calculate gain ratio for subsubset
						russiandoll.put(ex[i], subsub);
						
					}
				}
				double g = calcGainRatio(russiandoll, i);
				super.get_logger().log(Level.INFO, tree.getTreeSize() + " : Attribute " + i + " : Gain Ratio " + g);
				inner.put(g, russiandoll);
				outer.put(i, inner);
			}
			super.get_logger().log(Level.INFO, "Really cool HashMap created!");
			
			//find max gain
			Map.Entry<Integer, HashMap<Double, HashMap<String, ArrayList<String[]>>>> maxEntry = null;

			for (Map.Entry<Integer, HashMap<Double, HashMap<String,ArrayList<String[]>>>> entry : outer.entrySet())
			{
				double e = (double)entry.getValue().keySet().toArray()[0];
				double m = (double)maxEntry.getValue().keySet().toArray()[0];
			    if (maxEntry == null || e > m)
			    {
			        maxEntry = entry;
			    }
			}
			super.get_logger().log(Level.INFO, "Max Gain Ratio is : " + maxEntry.getValue().keySet().toArray()[0]);
			//set as attribute
			parent.attribute = maxEntry.getKey();
			attributes.remove(attributes.indexOf(maxEntry.getKey()));
			//take keys for russiandoll as split values
			HashMap<String,ArrayList<String[]>> russiandoll = (HashMap<String,ArrayList<String[]>>)maxEntry.getValue().values().toArray()[0];
			parent.splitvalues = new ArrayList<String>(russiandoll.keySet());
			//create children
			for(String v : parent.splitvalues){
				if(!russiandoll.get(v).isEmpty()){
					TreeNode child = new DecisionTreeNode(russiandoll.get(v), parent.locindex, tree.getTreeSize());
					//add children to tree and parent's children set
					parent.children.add(child);
					tree.addNode(child);
					treeBuilder(russiandoll.get(v), attributes, (DecisionTreeNode)child);
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

	protected double calcGainRatio(HashMap<String, ArrayList<String[]>> subsubsets, int attribute) {
		
		return 1;
	}

	private double calcEntropy(ArrayList<String[]> subsubset, int attribute) {//probability of class
		
		return 1;
	}

	private double calcGain(ArrayList<String[]> subsubset, int attribute) {
		return 1;
	}

	private double calcIV(ArrayList<String[]> subsubset, int attribute) {//probability of attribute value
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
