package classification;

import java.util.ArrayList;
import java.util.logging.Level;

public class KNearestNeighbor extends Algorithm {
	ArrayList<String[]> trainData;
	ArrayList<String[]> testData;
	String shortName;

	int numAttributes;
	int numInstancesTrain; 
	int classLoc;

	int k;

	public KNearestNeighbor(String shortName, ArrayList<String[]> trainData,
			ArrayList<String[]> testData, int k) {
		this.shortName = shortName;
		this.trainData = trainData;
		this.testData = testData;
		this.k = k;
		super.get_logger().log(
				Level.INFO,
				"Running k-Nearest Neighbor (k = " + k + ") to classify "
						+ shortName + " data.");
		train(trainData);
		test(testData);
	}

	/*
	 * For k-NN, the training step of the algorithm equates to storing the vectors of 
	 * attributes and their associated class labels. 
	 * 
	 */
	
	void train(ArrayList trainData) {

		// determines the number of instances in the training set
		numInstancesTrain = trainData.size();
		
		// determines the number of attributes in the data set (- 1 for the class value)
		String[] o = (String[]) trainData.get(0);
		numAttributes = o.length - 1;
		
		super.get_logger().log(Level.INFO, "Starting training");
		super.get_logger().log(Level.INFO, "Size of training set: " + numInstancesTrain + " instances, " + numAttributes + " attributes");
	}

	void test(ArrayList data) {
		// compute the distance from the query point to all other points in the training set
		
		// select the k nearest neighbors to the query point
		
		// take the majority vote (with respect to class) of k-nearest neighbors, breaking ties randomly
		
		
		// store the class prediction
		
		
		
		// after all 
		
		
		
		
		
		super.get_logger().log(Level.INFO, "Starting testing:");

	}

	
	public void evaluate() {
		// after all test set instances have been classified, evaluate the performance of classifier
	}
	
	
}
