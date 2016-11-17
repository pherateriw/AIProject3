package classification;
import java.util.ArrayList;
public class DecisionTree extends Algorithm {
	ArrayList<String[]> trainset;
	ArrayList<String[]> valset;
	Tree tree;
    void test(ArrayList data){

    }
    void train(ArrayList data){
    	trainset = (ArrayList<String[]>)(ArrayList<?>)(data);
    	//create validation set
    	valset = createValSet(trainset, 0);
    	//create root node
    	DecisionTreeNode root = new DecisionTreeNode(trainset, -1, 0);
    	
    	
    	//generate attribute array
    	int[] attributes =  new int[trainset.get(0).length - 1];
    	for(int i = 0; i < trainset.get(0).length-1; i++){
    		attributes[i] = i;
    	}
    	//call treeBuilder on data, attribute array, root
    	treeBuilder(trainset, attributes, root);
    	
    	//call pruning
    	
    }
    public void treeBuilder(ArrayList<String[]> subset, int[] attributes, DecisionTreeNode parent){
    	//count the numbers of examples for each class
    	//if all examples have the same class
    		//parent.label = class label
    		//remove all children for parent
    	//else if attributes is empty
    		//count the numbers of examples for each class
    		//parent.label = class label with the highest count
    		//remove all children for parent
    	//else if subset.size == 1
    		//parent.attribute = class label of example 
    		//remove all children for parent
    	//else if subset is empty and parent is not root
    		//remove current node from tree and parent.children
    	//else
    		// calculate Gain Ratio for each attribute 
    		// select attribute with highest Gain Ratio
    		// parent.attribute = selected attribute
    		// remove attribute from attributes
    		// identify values for attribute
    		// parent.splitvalues = possible values for attribute
    		// divide subset by attribute values
    		// create children using subsubsets, rootindex and curindex
    		// add children to children list and to tree
    		// for each child
    			//call treeBuilder on child
    	
    	
    }
    public void pruneTree(){
    	
    }
    public ArrayList<String[]> createValSet(ArrayList<String[]> data, double ratio){
    	//TODO finish this
    	return data;
    }
    
    protected double calcGainRatio(){
    	return 1;
    }
    private double calcI(){
    	return 1;
    }
    private double calcE(){
    	return 1;
    }
    private double calcIV(){
    	return 1;
    }
}
