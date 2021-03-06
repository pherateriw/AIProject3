package classification;

import java.lang.reflect.Array;
import java.util.*;

//import java.util.HashSet;

import java.util.logging.Level;

public class TreeAugNB extends Algorithm {

	private String shortName;
	private ArrayList<String[]> trainData;
	private ArrayList<String[]> testData;
	private Tree tree;
	private NaiveBayes nb;
	private HashMap<String, Double> classPriors;
	private ArrayList<HashMap> predictorPriors;
	private ArrayList<HashMap> likelihoods;
	private HashMap<String, Double> valOccurences;
	private HashMap<String, Double> togetherness;
	private HashMap<String, Double> featureLikelihoods;
	private ArrayList<String> predictedClasses;
	private HashMap<String, Double> posteriors;
	int classNums;
	Set<String> valNames = new HashSet<String>();

	public TreeAugNB(String shortName, ArrayList<String[]> trainData, ArrayList<String[]> testData) {

		this.shortName = shortName;
		this.trainData = trainData;
		this.testData = testData;
		super.get_logger().log(Level.INFO, "Running TAN to classify " + shortName + " data.");
		super.get_logger().log(Level.INFO, "");
		train(trainData);
		test(testData);
		results = evaluate();

	}

	// TODO: update these comments, or discard them if not needed
	// TAN learning algo strategy, from ML notes starting at p 112:
	/*
	 * 1. Create a complete undirected graph among the attributes. In other
	 * words, assume every attribute variable Ai is connected to every other
	 * attribute variable Aj by a simple link. (NOTE: assume i != j)
	 * 
	 * 2. For each edge (Ai, Aj) in the complete graph, associate a weight,
	 * determined by calculating CMI between attributes Ai and Aj.
	 * 
	 * 3. Next find a MAXIMUM spanning tree among the vertices in the complete
	 * graph. Note that the class node is not used in this calculation. The
	 * result will be a tree-based graph.
	 * 
	 * 4. Pick any of the attributes arbitrarily. The attribute will become the
	 * root of the tree.
	 * 
	 * 5. Orient all of the edges downward, starting with the root node. These
	 * edges will define the dependencies between attributes.
	 * 
	 * 6. Associate conditional probability tables with each attribute based on
	 * the data. Note that these probabilities are also frequencies as described
	 * with naive Bayes, except that two conditions must be applied instead of
	 * just one. This is likely to increase the possibility of zero
	 * probabilities, so the m-estimate (or similar) is important to use.
	 */

	void train(ArrayList<String[]> trainData) {
		createFullGraph(trainData);
		associateWeights();
		createCondProbTables();
		maxSpanTree();
		addTreeEdgesToNode();
		tree = directEdges((BayesTree) tree, (BayesTreeNode) tree.getRoot());

	}

	void test(ArrayList<String[]> testData) {
		super.get_logger().log(Level.INFO, "Starting testing:");
		predictedClasses = new ArrayList<>();
		for (Object obj : testData) {
			String[] oldArray = (String[]) obj;
			String[] newArray = Arrays.copyOfRange(oldArray, 0, oldArray.length - 1);
			String clas = predictSingle(newArray);
			predictedClasses.add(clas);
			super.get_logger().log(Level.INFO,
					String.format("Given features %s: predicted class is %s", Arrays.toString(newArray), clas));
		}

		super.get_logger().log(Level.INFO, "Done testing");

	}

	private String predictSingle(String[] features) {
		posteriors = new HashMap<>();

		for (String classKey : this.classPriors.keySet()) {
			String posteriorKey = classKey;
			double posterior = 1.0;
			posterior *= classPriors.get(classKey); // p(c)
			String firstLikely = "f" + "0:" + features[0] + "|" + classKey;
			posterior *= (Double) likelihoods.get(0).get(firstLikely); // p(root|c)
			ArrayList<Edge> edges = tree.getEdges();
			for (int i = 0; i < edges.size(); i++) {
				Edge e = edges.get(i);
				String f2 = "f" + e.x.featureIndex + ":" + features[e.x.featureIndex];
				String f1 = "f" + e.y.featureIndex + ":" + features[e.y.featureIndex];
				// p(x|clas, y)
				posterior *= probOfXGivenYandZ(f1, e.y.featureIndex, classKey, f2, e.x.featureIndex);
			}

			posteriors.put(posteriorKey, posterior);
		}

		// Take the max
		String maxKey = "NoClassValueHigherThan0.0";
		double maxVal = 0.0;

		try {
			for (String classKey : this.posteriors.keySet()) {
				if (this.posteriors.get(classKey) > maxVal) {
					maxKey = classKey;
					maxVal = this.posteriors.get(classKey);
				}
			}
		} catch (Exception e) {

		}
		return maxKey;

	}

	ArrayList<Double> evaluate() {

		// determine classification accuracy, required information - the number
		// of classes for this
		// dataset, the list of class labels (ArrayList String) as determined by
		// the classifier, and the
		// testData set (ArrayList String[]) that includes the true class
		// labels.
		super.get_logger().log(Level.INFO, "");
		super.get_logger().log(Level.INFO, "Starting evaluation.");

		// after all test set instances have been classified, evaluate the
		// performance of classifier
		EvaluationMeasures em = new EvaluationMeasures(classNums, predictedClasses, testData);
		ArrayList<Double> evaluationResults = em.evaluateData();

		double accuracy = evaluationResults.get(0);
		double precision = evaluationResults.get(1);
		double recall = evaluationResults.get(2);
		double fScore = evaluationResults.get(3);

		super.get_logger().log(Level.INFO, "######################################");
		super.get_logger().log(Level.INFO, "RESULTS");
		super.get_logger().log(Level.INFO, classNums + " class classification problem");
		super.get_logger().log(Level.INFO, "Results for this fold:");
		super.get_logger().log(Level.INFO, "Average Accuracy: " + accuracy);
		super.get_logger().log(Level.INFO, "Macro Precision: " + precision);
		super.get_logger().log(Level.INFO, "Macro Recall: " + recall);
		super.get_logger().log(Level.INFO, "Macro Score: " + fScore);
		super.get_logger().log(Level.INFO, "######################################");

		return evaluationResults;
	}

	private void createFullGraph(ArrayList<String[]> data) {
		super.get_logger().log(Level.INFO, "Creating full graph");

		TreeNode root = new BayesTreeNode(); // class node
		tree = new BayesTree(root);
		for (int i = 0; i < data.get(0).length - 1; i++) { // -1 because
															// root/class
															// already created
			BayesTreeNode newNode = new BayesTreeNode();
			newNode.setFeatureIndex(i);
			tree.addNode(newNode);
			for (TreeNode node : tree.getNodes()) {
				if (node != newNode && node != root) {
					tree.addEdge(new Edge(newNode, (BayesTreeNode) node));
				}
			}
		}
	}

	private void associateWeights() {
		super.get_logger().log(Level.INFO, "Associating weights between features");
		ConditionalMutualInfo cm = new ConditionalMutualInfo(trainData);
		// This depends on class node not being connected in graph!!
		for (Edge e : tree.getEdges()) {
			double newWeight = cm.calculate(e.x.getFeatureIndex(), e.y.getFeatureIndex());
			e.weight = newWeight;
		}
	}

	private void maxSpanTree() {
		super.get_logger().log(Level.INFO, "Creating Max Spanning Tree");
		// get arraylist of edges
		ArrayList<Edge> oldedges = tree.getEdges();
		// new arraylist of edges
		ArrayList<Edge> newedges = new ArrayList<Edge>();
		// new arraylist of sets of nodes
		ArrayList<TANTreeSet<BayesTreeNode>> sets = new ArrayList<TANTreeSet<BayesTreeNode>>();
		// each node is own set in setlist
		for (TreeNode n : tree.getNodes()) {
			TANTreeSet<BayesTreeNode> tempset = new TANTreeSet<BayesTreeNode>();
			tempset.add((BayesTreeNode) n);
		}
		// sort edges in tree
		Collections.sort(oldedges, new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				// Parse values for to sort by

				if (e1.weight > e2.weight)
					return 1; // tells Arrays.sort() that e1 comes after e2
				else if (e1.weight < e2.weight)
					return -1; // tells Arrays.sort() that e1 comes before e2
				else {
					// e1 and e2 are equal. Arrays.sort() is stable, so thesetwo
					// rows will appear in their original order.
					return 0;
				}
			}
		});

		// for each edge in tree until edges.size = |nodes|-1
		int edgecounter = 0;
		int index = 0;
		for (Edge e : oldedges) {
			// if x and y are not in the same set
			BayesTreeNode xi = (BayesTreeNode) e.x;
			BayesTreeNode yi = (BayesTreeNode) e.y;
			boolean both = false;
			for (TANTreeSet<BayesTreeNode> s : sets) {
				if (s.contains(xi) && s.contains(yi)) {
					both = true;
				}
			}
			if (!both && edgecounter < tree.getTreeSize()) {
				// add edge to edges
				newedges.add(e);
				edgecounter++;
			}
			// find sets with x and y and union them
			for (TANTreeSet<BayesTreeNode> s : sets) {
				if (s.contains(xi)) {
					for (TANTreeSet<BayesTreeNode> s2 : sets) {
						if (s2.contains(yi) && !s.equals(s2)) {
							s = (TANTreeSet) s.union(s, s2);
							sets.remove(s2);
						}
					}
				}
			}

		}
		// tree.edges = edges
		tree.addEdges(newedges);
	}

	public void addTreeEdgesToNode() {
		ArrayList<Edge> edges = tree.getEdges();
		// ArrayList<BayesTreeNode> nodes =
		// (ArrayList<BayesTreeNode>)(ArrayList<?>) tree.getNodes();
		for (int i = 0; i < tree.getTreeSize(); i++) {
			((BayesTreeNode) tree.getNode(i)).edges = new ArrayList<Edge>();
			for (Edge e : edges) {
				// System.out.println(e.x.featureIndex + " " + e.y.featureIndex
				// + " " + ((BayesTreeNode)tree.getNode(i)).featureIndex);
				if (e.x.featureIndex == ((BayesTreeNode) tree.getNode(i)).featureIndex
						|| e.y.featureIndex == ((BayesTreeNode) tree.getNode(i)).featureIndex) {
					// System.out.println("Adding edge to node");
					((BayesTreeNode) tree.getNode(i)).edges.add(e);

				}
			}
		}
	}

	private Tree directEdges(BayesTree tree, BayesTreeNode root) {
		super.get_logger().log(Level.INFO, "Directing edges in tree");
		if (!root.edges.isEmpty()) {
			for (Edge e : root.edges) {
				if (e.directed == false) {
					e.directed = true;
					BayesTreeNode z = e.y;
					BayesTreeNode w = e.x;
					e.x = root;
					if (e.x.equals(z)) {
						e.y = w;
					} else {
						e.y = z;
					}

					((BayesTreeNode) e.y).edges.remove(e);
					if(!(root.featureIndex == e.y.featureIndex)){
						tree = (BayesTree) directEdges(tree, (BayesTreeNode) e.y);
					}
				}
			}
		}
		return tree;
	}

	private void createCondProbTables() {
		nb = new NaiveBayes(trainData);
		this.classPriors = nb.classPriors;
		this.likelihoods = nb.likelihoods;
		this.predictorPriors = nb.predictorPriors;
		this.togetherness = nb.togetherness;
		this.valOccurences = nb.valOccurances;
		this.valNames = nb.valNames;
		classNums = nb.classFrequencies.size();
		calculateFeatureLikelihoods();
	}

	private void calculateFeatureLikelihoods() {
		super.get_logger().log(Level.INFO, "Calculating FeatureLikelihoods");
		featureLikelihoods = new HashMap<>();
		for (String f1 : valNames) {
			for (String f2 : valNames) {
				if (f1 == f2) {
					continue;
				}
				String fKey = f1 + "|" + f2;
				if (!featureLikelihoods.containsKey(fKey)) {
					double possibleNull;
					try {
						possibleNull = togetherness.get(f1 + "," + f2);
					} catch (Exception e) {
						possibleNull = .0001;
					}
					double likelihood = possibleNull / valOccurences.get(f2);
					featureLikelihoods.put(fKey, likelihood);
				}
			}
		}

		for (String f : featureLikelihoods.keySet()) {
			super.get_logger().log(Level.INFO, String.format("Likelihood of %s is %s", f, featureLikelihoods.get(f)));
		}
	}

	private double probOfXGivenYandZ(String x, int xfeatureIndex, String clas, String y, int yfeatureIndex) {
		// p(x|clas, y)
		try {
			HashMap<String, Double> xlikelihoods = likelihoods.get(xfeatureIndex);
			Double classprior = classPriors.get(clas);
			String xgivenclass = x + "|" + clas;
			String ygivenx = y + "|" + x;
			double mult = xlikelihoods.get(xgivenclass);
			mult = mult * featureLikelihoods.get(ygivenx);
			mult = mult * classprior;
			return mult;
		} catch (Exception e) {
			return 1.0;
		}
	}

	public static void main(String[] args) {

		ArrayList testData = new ArrayList<Arrays>();
		testData.add(new String[] { "Rainy", "Mild", "High", "Weak", "Yes" });
		testData.add(new String[] { "Rainy", "Cool", "Normal", "Weak", "Yes" });
		testData.add(new String[] { "Overcast", "Hot", "High", "Weak", "Yes" });
		testData.add(new String[] { "Sunny", "Hot", "High", "Weak", "No" });
		testData.add(new String[] { "Sunny", "Hot", "High", "Strong", "No" });
		testData.add(new String[] { "Sunny", "Mild", "High", "Weak", "No" });
		testData.add(new String[] { "Overcast", "Cool", "Normal", "Strong", "Yes" });
		testData.add(new String[] { "Rainy", "Cool", "Normal", "Strong", "No" });
		testData.add(new String[] { "Rainy", "Mild", "Normal", "Weak", "Yes" });
		testData.add(new String[] { "Sunny", "Cool", "Normal", "Weak", "Yes" });
		testData.add(new String[] { "Rainy", "Mild", "High", "Strong", "No" });
		testData.add(new String[] { "Overcast", "Mild", "High", "Strong", "Yes" });
		testData.add(new String[] { "Overcast", "Hot", "Normal", "Weak", "Yes" });
		testData.add(new String[] { "Sunny", "Mild", "Normal", "Strong", "Yes" });

		String[] test = new String[] { "Sunny", "Mild", "Normal", "Strong", "Yes" };
		ArrayList<String[]> testit = new ArrayList<>();
		testit.add(test);
		TreeAugNB tan = new TreeAugNB("Dummy data", testData, testit);
	}
}
