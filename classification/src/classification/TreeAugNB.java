package classification;
import java.util.ArrayList;
import java.util.logging.Level;
public class TreeAugNB extends Algorithm {
	
	private String shortName;
	private ArrayList<String[]> trainData;
	private ArrayList<String[]> testData;



	public TreeAugNB(String shortName, ArrayList<String[]> trainData,
			ArrayList<String[]> testData){
		
		this.shortName = shortName;
		this.trainData = trainData;
		this.testData = testData;
		super.get_logger().log(
				Level.INFO,
				"Running TAN to classify "
						+ shortName + " data.");
		super.get_logger().log(Level.INFO,"");
		train(trainData);
		test(testData);
		evaluate();
	
	}
	
	
	// TODO: update these comments, or discard them if not needed
	// TAN learning algo strategy, from ML notes starting at p 112:
	/*
	 * 1. Create a complete undirected graph among the attributes. In other words, assume every attribute
	 * variable Ai is connected to every other attribute variable Aj by a simple link. (NOTE: assume i != j)
	 * 
	 * 2. For each edge (Ai, Aj) in the complete graph, associate a weight, determined by calculating 
	 * CMI between attributes Ai and Aj.
	 * 
	 * 3. Next find a MAXIMUM spanning tree among the vertices in the complete graph. Note that the class node 
	 * is not used in this calculation. The result will be a tree-based graph.
	 * 
	 * 4. Pick any of the attributes arbitrarily. The attribute will become the root of the tree.
	 * 
	 * 5. Orient all of the edges downward, starting with the root node. These edges will define the dependencies 
	 * between attributes.
	 * 
	 * 6. Associate conditional probability tables with each attribute based on the data. Note that these probabilities
	 * are also frequencies as described with naive Bayes, except that two conditions must be applied instead of just 
	 * one. This is likely to increase the possibility of zero probabilities, so the m-estimate (or similar)
	 * is important to use.
	 */
	
	
	
	
    void test(ArrayList<String[]> trainData){
    	
    	ConditionalMutualInfo cm = new ConditionalMutualInfo(trainData);
    	
    	// TODO: these values just for testing, change to loop through all edges in graph
    	int aiLoc = 0;
    	int ajLoc = 1;
    	
    	cm.calculate(aiLoc, ajLoc);
    	
    	
    }

    
    void train(ArrayList<String[]> testData){
    	
    	
    	
    	
    	
    }
    
    void evaluate(){
    	
    	
    	
    	
    	
    }
    
    

    private Tree createFullGraph(ArrayList<String[]> data){
    	TreeNode root = new BayesTreeNode();
    	Tree tree = new Tree(root);
    	//TODo finish this
    	return tree;
    }
    
}
