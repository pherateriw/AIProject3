package classification;

import java.util.*;
import java.util.logging.Level;

public class DecisionTree extends Algorithm {
	ArrayList<String[]> trainset;
	ArrayList<String[]> testset;
	ArrayList<String[]> valset;
	double valratio;
	Tree tree;
	ArrayList<String> classlabels;
	EvaluationMeasures error;

	public DecisionTree(String dataName, ArrayList<String[]> trainset, ArrayList<String[]> testset, double valratio) {
		super.get_logger().log(Level.INFO, "Decision Tree Algorithm created.");
		super.get_logger().log(Level.INFO, String.format("Working with %s dataset", dataName));
		this.trainset = trainset;
		this.testset = testset;
		this.valratio = valratio;
		train(trainset);
		test(testset);
	}
	
	void test(ArrayList<String[]> data) {
		super.get_logger().log(Level.INFO, "Starting testing:");
		// testset = data;
		// create list of predicted class labels
		ArrayList<String> predictedClass = new ArrayList<String>();

		// for element in data
		for (String[] instance : data) {
			// run the element through the tree:
			DecisionTreeNode root = (DecisionTreeNode) tree.getRoot();
			// start at root:
			// while root.children is not empty
			int nodeCount = 0;
			String label = null;
			while (!root.children.isEmpty() && nodeCount < 2*tree.getTreeSize()) {
				// find root.attribute in element
				int att = root.attribute;
				// for each root.child

				for (TreeNode c : root.children) {
					nodeCount++;
					DecisionTreeNode child = (DecisionTreeNode) c;
					// if featValue = attribute value for element
					if (child.featValue.equals(instance[att])) {
						// root = child
						root = child;
						//label = root.label;
						break;
					}
				}
			}
			// store root.label in predicted class labels

			if(root.label == null){
				HashMap<String, Integer> countmap = countAllClasses(root.examples);
				HashMap.Entry<String, Integer> maxEntry = null;

				for (HashMap.Entry<String, Integer> entry : countmap.entrySet()) {
					if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
						maxEntry = entry;
					}
					
				}
				label = maxEntry.getKey();
				predictedClass.add(maxEntry.getKey());
			}else{
				label = root.label;
				predictedClass.add(root.label);
			}

			String[] newArray = Arrays.copyOfRange(instance, 0, instance.length -1);
			super.get_logger().log(Level.INFO, String.format("Given features %s: predicted class is %s", Arrays.toString(newArray), label));

		}
		//System.out.println(data);
		//HashMap<String, Integer> map = countAllClasses(data);
		//System.out.println(map.toString());
		System.out.println(predictedClass);
		error = new EvaluationMeasures(classlabels.size(), predictedClass, data);
		ArrayList<Double> results = error.evaluateData();
		System.out.println("F-measure: " + results.get(3));
	}

	void train(ArrayList<String[]> data) {
		super.get_logger().log(Level.INFO, "Training started:");
		//trainset = data;
		classlabels = new ArrayList<String>();
		// generate class label array
		for (int i = 0; i < trainset.size(); i++) {
			if (!classlabels.contains(trainset.get(i)[trainset.get(i).length - 1])) {
				classlabels.add(trainset.get(i)[trainset.get(i).length - 1]);
			}
		}
		super.get_logger().log(Level.INFO, "Class labels set created");
		// create validation set
		//System.out.print(data);
		valset = createValSet(data, valratio);
		super.get_logger().log(Level.INFO, "Validation set created");
		// create root node
		DecisionTreeNode root = new DecisionTreeNode(trainset, -1, 0);
		tree = new Tree(root);
		//super.get_logger().log(Level.INFO, "Tree created");
		// generate attribute array
		ArrayList<Integer> attributes = new ArrayList<Integer>();
		for (int i = 0; i < trainset.get(0).length - 2; i++) {
			attributes.add(i);
		}
		super.get_logger().log(Level.INFO, "Attribute set created");
		// call treeBuilder on data, attribute array, root
		treeBuilder(trainset, attributes, root);
		super.get_logger().log(Level.INFO, "Tree Built");
		ArrayList<TreeNode> t = tree.getTree();

		for (TreeNode i : t) {
			DecisionTreeNode j = (DecisionTreeNode) i;
			super.get_logger().log(Level.INFO, String.format("Node %s has parent %s and splits on attribute %s", j.getLocindex(), j.getRootindex(), j.attribute));
//			System.out.println("Node " + j.getLocindex() + " has parent " + j.getRootindex()
//					+ " and splits on attribute " + j.attribute);
			if (j.getRootindex() > -1) {
				super.get_logger().log(Level.INFO, String.format("\t and parents splits on %s and this node has feature value %s", ((DecisionTreeNode) t.get(j.getRootindex())).attribute, j.featValue));
//				System.out.println("\t and parent splits on " + ((DecisionTreeNode) t.get(j.getRootindex())).attribute
//						+ " and this node has feature value " + j.featValue);
			}

		}

		// call pruning
		super.get_logger().log(Level.INFO, "Pruning:");
		pruneTree(tree);
		super.get_logger().log(Level.INFO, "Done pruning");

		for (TreeNode i : t) {
			DecisionTreeNode j = (DecisionTreeNode) i;
			super.get_logger().log(Level.INFO, String.format("Node %s has parent %s and splits on attribute %s",
					j.getLocindex(), j.getRootindex(), j.attribute));
//			System.out.println("Node " + j.getLocindex() + " has parent " + j.getRootindex()
//					+ " and splits on attribute " + j.attribute);
			if (j.getRootindex() > -1) {
				super.get_logger().log(Level.INFO, String.format("\t and parents splits on %s and this node has feature value %s",
						((DecisionTreeNode) t.get(j.getRootindex())).attribute, j.featValue));
//				System.out.println("\t and parent splits on " + ((DecisionTreeNode) t.get(j.getRootindex())).attribute
//						+ " and this node has feature value " + j.featValue);
			}
			
		}
		super.get_logger().log(Level.INFO, "Done training");
	}

	public void treeBuilder(ArrayList<String[]> subset, ArrayList<Integer> attributes, DecisionTreeNode parent) {
		super.get_logger().log(Level.INFO, "Selecting attribute/label for Node number " + tree.getTreeSize());
		// count the numbers of examples for each class
		HashMap<String, Integer> countmap = countAllClasses(subset);
		super.get_logger().log(Level.INFO, "Hashmap of class numbers created");
		// if all examples have the same class
		System.out.println(subset.size());
		System.out.println(countmap.toString());
		if (countmap.size() == 1) {
			super.get_logger().log(Level.INFO,
					"All examples of Node Number " + tree.getTreeSize() + " are the same class");
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
			super.get_logger().log(Level.INFO, "Attributes for Node Number " + tree.getTreeSize() + " are empty");

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
		// else if (subset.size() == 1) {//unnecessary
		// super.get_logger().log(Level.INFO, "Only one example for Node Number
		// " + tree.getTreeSize());
		//
		// // parent.label = class label of example
		// parent.label = subset.get(0)[subset.get(0).length - 1];
		// // remove all children for parent
		// parent.children = new ArrayList<TreeNode>();
		// }
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
			super.get_logger().log(Level.INFO, "Expanding tree at " + tree.getTreeSize() + "");

			// calc Gain ratio:
			HashMap<Integer, HashMap<Double, HashMap<String, ArrayList<String[]>>>> outer = new HashMap<Integer, HashMap<Double, HashMap<String, ArrayList<String[]>>>>();
			// Nested Hashmap!
			// map of attribute -> map of attribute values -> map of subset of
			// data -> gain ratio
			for (Integer i : attributes) {// for each attribute
				HashMap<Double, HashMap<String, ArrayList<String[]>>> inner = new HashMap<Double, HashMap<String, ArrayList<String[]>>>();
				HashMap<String, ArrayList<String[]>> russiandoll = new HashMap<String, ArrayList<String[]>>();
				for (String[] ex : subset) {// for each example in the dataset
					if (!inner.containsKey(ex[i])) {

						// create subsubset of data with value ex[i]
						ArrayList<String[]> subsub = new ArrayList<String[]>();
						for (String[] ex2 : subset) {
							if (ex2[i].equals(ex[i])) {
								subsub.add(ex2);
							}
						}

						// calculate gain ratio for subsubset
						russiandoll.put(ex[i], subsub);

					}
				}
				double g = calcGainRatio(subset, i);
				// System.out.println(tree.getTreeSize() + " : Attribute " + i +
				// " : Gain Ratio " + g);
				// super.get_logger().log(Level.INFO, tree.getTreeSize() + " :
				// Attribute " + i + " : Gain Ratio " + g);
				inner.put(g, russiandoll);
				outer.put(i, inner);
			}
			//super.get_logger().log(Level.INFO, "Really cool HashMap created!");

			// find max gain
			Map.Entry<Integer, HashMap<Double, HashMap<String, ArrayList<String[]>>>> maxEntry = null;

			for (Map.Entry<Integer, HashMap<Double, HashMap<String, ArrayList<String[]>>>> entry : outer.entrySet()) {
				double e = (double) entry.getValue().keySet().toArray()[0];

				if (maxEntry == null) {
					maxEntry = entry;
				} else {
					double m = (double) entry.getValue().keySet().toArray()[0];
					if (e > m)

					{
						maxEntry = entry;
					}
				}
			}
			//super.get_logger().log(Level.INFO, "Max Gain Ratio is : " + maxEntry.getValue().keySet().toArray()[0]);
			// set as attribute
			// if(maxEntry.getValue().keySet().toArray()[0])
			parent.attribute = maxEntry.getKey();
			attributes.remove(attributes.indexOf(maxEntry.getKey()));
			// take keys for russiandoll as split values
			HashMap<String, ArrayList<String[]>> russiandoll = (HashMap<String, ArrayList<String[]>>) maxEntry
					.getValue().values().toArray()[0];
			parent.splitvalues = new ArrayList<String>(russiandoll.keySet());
			// create children
			for (String v : parent.splitvalues) {
				if (!russiandoll.get(v).isEmpty()) {
					DecisionTreeNode child = new DecisionTreeNode(russiandoll.get(v), parent.locindex,
							tree.getTreeSize());
					child.featValue = v;
					// add children to tree and parent's children set
					parent.children.add(child);
					tree.addNode(child);
					treeBuilder(russiandoll.get(v), attributes, child);
				}
			}

		}

	}

	public void pruneTree(Tree subtree) {
		// create a copy of the list
		Tree realtree = new Tree(tree.getTree());
		Tree temptree = new Tree(subtree.getTree());
		ArrayList<TreeNode> tlist = temptree.getTree();
		DecisionTreeNode root = (DecisionTreeNode) temptree.getRoot();
		ArrayList<DecisionTreeNode> internalnodes = new ArrayList<DecisionTreeNode>();
		// find internal nodes
		internalnodes = findInternalNodes(internalnodes, root);
		// ensure that nodes closest to the branches are test before root
		Collections.reverse(internalnodes);
		tree = clip(subtree, internalnodes);
	}

	private EvaluationMeasures validate(ArrayList<String[]> data, Tree minitree) {
		// testset = data;
		// create list of predicted class labels
		ArrayList<String> predictedClass = new ArrayList<String>();

		// for element in data
		for (String[] instance : data) {
			// run the element through the tree:
			DecisionTreeNode root = (DecisionTreeNode) minitree.getRoot();
			// start at root:
			// while root.children is not empty
			while (!root.children.isEmpty()) {
				// find root.attribute in element
				int att = root.attribute;
				// for each root.child

				for (TreeNode c : root.children) {
					DecisionTreeNode child = (DecisionTreeNode) c;
					// if featValue = attribute value for element
					if (child.featValue.equals(instance[att])) {
						// root = child
						root = child;
						break;
					}
				}
			}
			// store root.label in predicted class labels
			predictedClass.add(root.label);
		}
		HashMap<String, Integer> map = countAllClasses(data);
		System.out.println(map.toString());
		EvaluationMeasures err = new EvaluationMeasures(classlabels.size(), predictedClass, data);
		return err;
	}

	public Tree clip(Tree subtree, ArrayList<DecisionTreeNode> internalnodes) {
		// create a copy of the list
		if (internalnodes.isEmpty()) {
			return subtree;
		}
		//tree = subtree;
		HashMap<String, Integer> map = countAllClasses(valset);
		//System.out.println(map.toString());
		EvaluationMeasures  err = validate(valset, subtree);
		ArrayList<Double> results= err.evaluateData();
		double besterror = results.get(0);
		//super.get_logger().log(Level.INFO, "Validation Error before node removed: " + besterror);
		
		Tree temptree = new Tree(subtree.getTree());
		ArrayList<TreeNode> tlist = temptree.getTree();
		DecisionTreeNode node = internalnodes.get(0);
		internalnodes.remove(0);
		for (TreeNode c : node.children) {
			DecisionTreeNode child = (DecisionTreeNode) c;
			temptree.removeNode(child.locindex);
		}
		node.children.clear();
		HashMap<String, Integer> countmap = countAllClasses(node.examples);
		HashMap.Entry<String, Integer> maxEntry = null;

		for (HashMap.Entry<String, Integer> entry : countmap.entrySet()) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}
		node.label = maxEntry.getKey();
		// parent.label = class label with the highest count
		
		EvaluationMeasures err2 = validate(valset, temptree);
		ArrayList<Double> results2= err2.evaluateData();
		double newerror = results2.get(0);
		super.get_logger().log(Level.INFO, "Validation Error before node removed: " + besterror + ", validation error after node removed: " + newerror);
		if(besterror > newerror){
			super.get_logger().log(Level.INFO, "Node removed");
			return temptree;
		}
		super.get_logger().log(Level.INFO, "Node not removed");
		return subtree;
	}

	public ArrayList<DecisionTreeNode> findInternalNodes(ArrayList<DecisionTreeNode> internalnodes,
			DecisionTreeNode root) {
		
		if (root.children.isEmpty()) {
			return internalnodes;
		} else {
			internalnodes.add(root);
			for (TreeNode child : root.children) {
				ArrayList<DecisionTreeNode> list = findInternalNodes(internalnodes, (DecisionTreeNode) child);
				for (DecisionTreeNode l : list) {
					if (!internalnodes.contains(l)) {
						internalnodes.add(l);
					}
				}
			}
		}
		return internalnodes;
	}

	public ArrayList<String[]> createValSet(ArrayList<String[]> data, double ratio) {
		System.out.println(data);
		ArrayList<String[]> tempdata = new ArrayList<String[]>();
		int classindex = data.get(0).length -1;
		System.out.println(data.get(0)[classindex]);
		HashMap<String, ArrayList<String[]>> pool = subdivideData(data, classindex);
		System.out.println(pool.toString());
		Random rando = new Random();
		for(ArrayList<String[]> val : pool.values()){
			tempdata.add(val.get(rando.nextInt(val.size())));
			System.out.println("Validation set size: " + tempdata.size());
			data.remove(val);
		}
		int maxcount = 0;
		int maxsize = (int)Math.floor(ratio*data.size());
		while(tempdata.size() < maxsize && maxcount < 5){
			for(ArrayList<String[]> val : pool.values()){
				tempdata.add(val.get(rando.nextInt(val.size())));
				data.remove(val);
			}
			maxcount = 5;
		}
		//System.out.println("Validation set size: " + tempdata.size());
		trainset = data;
		return tempdata;
	}

	protected double calcGainRatio(ArrayList<String[]> subset, int attribute) {
		double g = calcGain(subset, attribute);
		double iv = calcIV(subset, attribute);
		super.get_logger().log(Level.INFO, "Calculating gain ratio for attribute " + attribute + " : " + g / iv);
		// for(String[] ex : subset){
		// System.out.print("example number " + subset.indexOf(ex) + " : ");
		// for(String feat : ex){
		// System.out.print(feat + ", ");
		// }
		// System.out.println();
		// }

		return g / iv;
	}

	private double calcEntropy(ArrayList<String[]> subsubset) {// probability of
																// class
		double citok = subsubset.size();
		// System.out.println(subsubset.size());
		HashMap<String, Integer> cis = countAllClasses(subsubset);

		double entropy = 0;
		for (Integer i : cis.values()) {
			if (i != 0) {
				double e = i / citok;
				entropy += ((e) * (Math.log(e) / Math.log(2)));
				// System.out.println("incremental entropy: " +i + " : " +
				// entropy);
			}
		}
		// System.out.println();
		return -1 * entropy;
	}

	private double calcGain(ArrayList<String[]> subsubset, int attribute) {
		double citok = subsubset.size();
		double gain = 0;
		double entropy = calcEntropy(subsubset);
		HashMap<String, Integer> countmap = countInstances(subsubset, attribute);
		HashMap<String, ArrayList<String[]>> subsetmap = subdivideData(subsubset, attribute);
		// for(String val : subsetmap.keySet()){
		// for(String[] example : subsetmap.get(val)){
		// for(String feat : example){
		// System.out.print(feat + ",");
		// }
		// System.out.println();
		// }
		// }
		for (String val : countmap.keySet()) {

			double subentropy = calcEntropy(subsetmap.get(val));
			// System.out.println("subentropy : " + subentropy);
			gain += (countmap.get(val) / citok);// *subentropy;
			// System.out.println("gain for " + val + " : " +gain);
		}
		// System.out.println("entropy : " + entropy);
		return entropy - gain;
	}

	private double calcIV(ArrayList<String[]> subsubset, int attribute) {
		HashMap<String, Integer> countmap = countInstances(subsubset, attribute);
		double citok = subsubset.size();

		double intrinsicVal = 0;
		for (Integer i : countmap.values()) {
			if (i != 0) {
				// double blergh = i/citok;
				// System.out.println(i + " : " + citok + " : " + (1/citok) + "
				// : " + Math.log(i/citok) + " : " +Math.log(blergh));
				intrinsicVal += ((i / citok) * (Math.log(i / citok) / Math.log(2)));
				// System.out.println("iv: " + intrinsicVal);
			}
		}
		if (intrinsicVal == 0) {
			return intrinsicVal;
		}
		return -1 * intrinsicVal;
	}

	private int countclass(String classlabel, ArrayList<String[]> data) {
		//System.out.println("data size: " + data.size());
		int classloc = data.get(0).length - 1;
		
		int count = 0;
		for (String[] i : data) {
			if (classlabel.equals(i[classloc])) {
				count++;
			}
		}
		return count;
	}

	private HashMap<String, Integer> countInstances(ArrayList<String[]> subset, int attribute) {
		HashMap<String, Integer> countmap = new HashMap<String, Integer>();
		for (String[] ex : subset) {
			if (!countmap.containsKey(ex[attribute])) {
				int count = 0;
				for (String[] ex2 : subset) {
					if (ex[attribute].equals(ex2[attribute])) {
						count++;
					}
				}
				countmap.put(ex[attribute], count);
			}
		}
		return countmap;
	}

	private HashMap<String, Integer> countAllClasses(ArrayList<String[]> subset) {

		HashMap<String, Integer> countmap = new HashMap<String, Integer>();
		for (int i = 0; i < classlabels.size(); i++) {
			if (!countmap.containsKey(classlabels.get(i))) {
				int c = countclass(classlabels.get(i), subset);
				if (c != 0) {
					countmap.put(classlabels.get(i), c);
				}
			}
			// else {
			// countmap.remove(classlabels.get(i));
			// countmap.put(classlabels.get(i), countclass(classlabels.get(i),
			// subset));
			// }
		}
		return countmap;
	}

	private HashMap<String, ArrayList<String[]>> subdivideData(ArrayList<String[]> data, int attribute) {
		
		HashMap<String, ArrayList<String[]>> countmap = new HashMap<String, ArrayList<String[]>>();
		for (String[] ex : data) {
			if (!countmap.containsKey(ex[attribute])) {
				ArrayList<String[]> subset = new ArrayList<String[]>();
				for (String[] ex2 : data) {
					if (ex[attribute].equals(ex2[attribute])) {
						subset.add(ex2);
					}
					//System.out.println("subdivided set size : " + subset.size() + " at " + data.indexOf(ex) + " : " + ex[attribute] + " and " + data.indexOf(ex2) + " : " +ex2[attribute] + " and attribute " + attribute);
				}
				//System.out.println("subdivided set size : " + subset.size() + " at " + data.indexOf(ex) + " and attribute " + attribute);
				countmap.put(ex[attribute], subset);
			}
		}
		//System.out.print(countmap.toString());
		return countmap;
	}

	private void printTree(boolean log) {
		if (log) {

		} else {
			TreeNode root = tree.getRoot();

		}
	}
}
