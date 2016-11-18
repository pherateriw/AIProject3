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
	Set<String> valNames = new HashSet<String>();



	public TreeAugNB(String shortName, ArrayList<String[]> trainData, ArrayList<String[]> testData) {

		this.shortName = shortName;
		this.trainData = trainData;
		this.testData = testData;
		super.get_logger().log(Level.INFO, "Running TAN to classify " + shortName + " data.");
		super.get_logger().log(Level.INFO, "");
		train(trainData);
		test(testData);
		evaluate();

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
	
    void train(ArrayList<String[]> trainData){
		createFullGraph(trainData);
		associateWeights();
		createCondProbTables();
//		maxSpanTree();
//		tree = directEdges((BayesTree) tree, (BayesTreeNode) tree.getRoot());
		BayesTreeNode clas = new BayesTreeNode();
		Tree testTree = new BayesTree(clas);
		BayesTreeNode f1 = new BayesTreeNode();
		f1.setFeatureIndex(0);
		testTree.addNode(f1);
		BayesTreeNode f2 = new BayesTreeNode();
		f1.setFeatureIndex(1);
		testTree.addNode(f2);
		BayesTreeNode f3 = new BayesTreeNode();
		f1.setFeatureIndex(2);
		testTree.addNode(f3);
		BayesTreeNode f4 = new BayesTreeNode();
		f1.setFeatureIndex(3);
		testTree.addNode(f4);

		testTree.addEdge(new Edge(f1, f2));
		testTree.addEdge(new Edge(f2, f3));
		testTree.addEdge(new Edge(f3, f4));

		tree = testTree;

    	
    	
    }

    
    void test(ArrayList<String[]> testData){
		super.get_logger().log(Level.INFO, "Starting testing:");
		predictedClasses = new ArrayList<>();
		for (Object obj : testData) {
			String[] oldArray = (String[]) obj;
			String[] newArray = Arrays.copyOfRange(oldArray, 0, oldArray.length - 1);
			String clas = predictSingle(newArray);
			predictedClasses.add(clas);
			super.get_logger().log(Level.INFO, String.format("Given features %s: predicted class is %s", Arrays.toString(newArray), clas));
		}

		super.get_logger().log(Level.INFO, "Done testing");

	}

	private String predictSingle(String[] features){
		String clas = "";
		for (String classKey : this.classPriors.keySet()){
			String posteriorKey = classKey + "|";
			double posterior = 1.0;
			for (Edge e : tree.getEdges()){
				System.out.println();
				String f1 = features[e.x.featureIndex];
				posteriorKey += f1 + " ";
				String f2 = features[e.y.featureIndex];
				posteriorKey += f2 + " ";
				posterior *= probOfXGivenYandZ(f1, e.x.featureIndex, classKey, f2, e.y.featureIndex);
			}
		}

		return clas;

	}
    
    void evaluate(){
    	
    	
    	
    	
    	
    }

    private void createFullGraph(ArrayList<String[]> data){
		super.get_logger().log(Level.INFO, "Creating full graph");

		TreeNode root = new BayesTreeNode();  //class node
    	tree = new BayesTree(root);
		for (int i = 0; i < data.get(0).length -2; i++){  //-2 because root is already created
			BayesTreeNode newNode = new BayesTreeNode();
			newNode.setFeatureIndex(i);
			tree.addNode(newNode);
			for (TreeNode node : tree.getNodes()){
				if (node != newNode && node != root){
					tree.addEdge(new Edge(newNode, (BayesTreeNode) node));
				}
			}
		}
    }
    
    private void associateWeights() {
		super.get_logger().log(Level.INFO, "Associating weights between features");
		ConditionalMutualInfo cm = new ConditionalMutualInfo(trainData);
		// This depends on class node not being connected in graph!!
		for (Edge e : tree.getEdges()){
			double newWeight = cm.calculate(e.x.getFeatureIndex(), e.y.getFeatureIndex());
			e.weight = newWeight;
		}
	}

	private void maxSpanTree() {
		super.get_logger().log(Level.INFO, "Creating Max Spanning Tree");
		//new arraylist of edges 
		ArrayList<Edge> newedges = new ArrayList<Edge>();
		//new arraylist of sets of nodes
		ArrayList<TANTreeSet<BayesTreeNode>> sets = new ArrayList<TANTreeSet<BayesTreeNode>>();
		//each node is own set in setlist
		for(TreeNode n : tree.getNodes()){
			TANTreeSet<BayesTreeNode> tempset = new TANTreeSet<BayesTreeNode>();
			tempset.add((BayesTreeNode)n);
		}
		//sort edges in tree
		
		//TODO write sorter for edges!
		//for each edge in tree until edges.size = |nodes|-1
		int edgecounter = 0;
		int index = 0;
		for(Edge e : tree.getEdges()){
			//if x and y are not in the same set
			BayesTreeNode xi = (BayesTreeNode)e.x;
			BayesTreeNode yi = (BayesTreeNode)e.y;
			boolean both = false;
			for(TANTreeSet<BayesTreeNode> s : sets){
				if(s.contains(xi)&& s.contains(yi)){
					both = true;
				}
			}
			if(!both){
				//add edge to edges
				newedges.add(e);
			}
			//find sets with x and y and union them
			for(TANTreeSet<BayesTreeNode> s : sets){
				if(s.contains(xi)){
					for(TANTreeSet<BayesTreeNode> s2 : sets){
						if(s2.contains(yi) && !s.equals(s2)){
							s = (TANTreeSet)s.union(s, s2);
							sets.remove(s2);
						}
					}
				}
			}
			
		}
		//tree.edges = edges
		tree.addEdges(newedges);
		//for edge in edges
		for(Edge e : tree.getEdges()){
			//add edge to x.edges and y.edges
			e.x.edges.add(e);
			e.y.edges.add(e);
		}
	}

	private Tree directEdges(BayesTree tree, BayesTreeNode root) {
		super.get_logger().log(Level.INFO, "Directing edges in tree");
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
				
				((BayesTreeNode)e.y).edges.remove(e);
				tree = (BayesTree) directEdges(tree, (BayesTreeNode)e.traverseEdge(e.x));
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
		calculateFeatureLikelihoods();
	}

	private void calculateFeatureLikelihoods(){
		super.get_logger().log(Level.INFO, "Calculating FeatureLikelihoods");
		featureLikelihoods = new HashMap<>();
		for (String f1 : valNames){
			for (String f2 : valNames){
				if (f1 == f2){
					continue;
				}
				String fKey = f1 + "|" + f2;
				if (!featureLikelihoods.containsKey(fKey)){
					double possibleNull;
					try {
						possibleNull = togetherness.get(f1 + "," + f2);
					}
					catch(Exception e){
						possibleNull = .0001;
					}
					double likelihood = possibleNull / valOccurences.get(f2);
					featureLikelihoods.put(fKey, likelihood);
				}
			}
		}
	}

	private double probOfXGivenYandZ(String x, int xfeatureIndex, String clas, String y, int yfeatureIndex){
		//p(y|z)p(y)p(x|z)  / p(y|z)
		return 0.0;
	}


	public static void main(String[] args) {

		ArrayList testData = new ArrayList<Arrays>();
		testData.add(new String[]{"Rainy", "Mild", "High", "Weak", "Yes"});
		testData.add(new String[]{"Rainy", "Cool", "Normal", "Weak", "Yes"});
		testData.add(new String[]{"Overcast", "Hot", "High", "Weak", "Yes"});
		testData.add(new String[]{"Sunny", "Hot", "High", "Weak", "No"});
		testData.add(new String[]{"Sunny", "Hot", "High", "Strong", "No"});
		testData.add(new String[]{"Sunny", "Mild", "High", "Weak", "No"});
		testData.add(new String[]{"Overcast", "Cool", "Normal", "Strong", "Yes"});
		testData.add(new String[]{"Rainy", "Cool", "Normal", "Strong", "No"});
		testData.add(new String[]{"Rainy", "Mild", "Normal", "Weak", "Yes"});
		testData.add(new String[]{"Sunny", "Cool", "Normal", "Weak", "Yes"});
		testData.add(new String[]{"Rainy", "Mild", "High", "Strong", "No"});
		testData.add(new String[]{"Overcast", "Mild", "High", "Strong", "Yes"});
		testData.add(new String[]{"Overcast", "Hot", "Normal", "Weak", "Yes"});
		testData.add(new String[]{"Sunny", "Mild", "Normal", "Strong", "Yes"});

		String[] test = new String[]{"Sunny", "Mild", "Normal", "Strong", "Yes"};
		ArrayList<String[]> testit = new ArrayList<>();
		testit.add(test);
		TreeAugNB tan = new TreeAugNB("Dummy data", testData, testit);
	}
}
