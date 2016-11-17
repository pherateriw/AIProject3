package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public class KNearestNeighbor extends Algorithm {
	// the test and train data sets
	ArrayList<String[]> trainData;
	ArrayList<String[]> testData;
	
	// stores the shortened name of the data set (for printing)
	String shortName;

	// stores the output of the k-NN algorithm, a class label given the feature data 
	ArrayList<String> knnClass; 
	
	// used to navigate the data sets and print information
	int numAttributes;
	int numInstancesTrain; 
	int numInstancesTest; 
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
		evaluate();
	}

	/*
	 * For k-NN, the training step of the algorithm equates to storing the vectors of 
	 * attributes and their associated class labels. 
	 * 
	 */
	
	void train(ArrayList<String[]> trainData) {
		super.get_logger().log(Level.INFO, "Starting training");
		
		// determines the number of instances in the training set
		numInstancesTrain = trainData.size();
		
		// determines the number of attributes in the data set (- 1 for the class value)
		numAttributes = trainData.get(0).length - 1;
		
		// stores the position of the class variable in the array
		classLoc = numAttributes;
				
		super.get_logger().log(Level.INFO, "Size of training set: " + numInstancesTrain + " instances, " + numAttributes + " attributes");
	}

	
	
	
	
	void test(ArrayList<String[]> testData) {
		super.get_logger().log(Level.INFO, "Starting testing:");		
		
		numInstancesTest = testData.size();
		String[] queryPoint;
		
		super.get_logger().log(Level.INFO, "Size of testing set: " + numInstancesTest + " instances, " + numAttributes + " attributes");
		
		// compute the distance from the query point to all other points in the training set
		// commented out for testing get neighbors
		queryPoint = Arrays.copyOfRange(testData.get(0), 0, numAttributes);
		int[] closestIndices = getNeighbors(queryPoint);
		
//		for (int qp = 0; qp < numInstancesTest; qp++) {
//			// gets the first unlabeled item in the set of test data 
//			queryPoint = Arrays.copyOfRange(testData.get(qp), 0, numAttributes);
//			
//			// for testing
//			for (int i = 0; i < queryPoint.length; i++) {
//				//System.out.println(queryPoint[i]);
//			}
//			
//			
//			// find the neighbors of the query point
//			int[] closestIndices = getNeighbors(queryPoint);
//			
//		}
		
		// select the k nearest neighbors to the query point
		// take top k of closestIndices
		
		
		// take the majority vote (with respect to class) of k-nearest neighbors, breaking ties randomly
		// grab from train array list
		
		// store the class prediction for this query point
		
		
		

		
		
		
		
		


	}

	
	public void evaluate() {
		// after all test set instances have been classified, evaluate the performance of classifier
	}
	
	public int[] getNeighbors(String[] qp) {
		// Stores the indices of all neighboring points
		int[] neighboringPoints = new int[numInstancesTrain];
		
		// calculate distance from qp to all points in training set
		ValueDiff vd = new ValueDiff(qp, trainData);
		
		
		// 
		
		// store in map? k = index, v = dist
		// sort all distances
		// populate index array, going smallest distance to largest
		
		
		
		return neighboringPoints;
	}
	
	
	
}
