package classification;

import java.util.ArrayList;
import java.util.logging.Level;

public class TreeAugNB extends Algorithm {

	private String shortName;
	private ArrayList<String[]> trainData;
	private ArrayList<String[]> testData;
	private Tree tree;

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

    	
    	
    }

    
    void test(ArrayList<String[]> testData){
    	
    	
    	
    	
    	
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
		super.get_logger().log(Level.INFO, "Associating weights");
		ConditionalMutualInfo cm = new ConditionalMutualInfo(trainData);
		// This depends on class node not being connected in graph!!
		for (Edge e : tree.getEdges()){
			double newWeight = cm.calculate(e.x.getFeatureIndex(), e.y.getFeatureIndex());
			e.weight = newWeight;
		}
	}

	private Tree maxSpanTree(BayesTree tree) {
		//new arraylist of edges edges
		//sort edges in tree
		//for each edge in tree
		return tree;
	}

	private Tree directEdges(BayesTree tree, BayesTreeNode root) {
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
				tree = (BayesTree) directEdges(tree, (BayesTreeNode)e.traverseEdge(e.x));
			}
		}
		return tree;
	}

	private void createCondProbTables(Tree tree) {

	}
}
