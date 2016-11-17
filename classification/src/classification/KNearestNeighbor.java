package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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

		//for (int qp = 0; qp < numInstancesTest; qp++) {
		
		for (int qp = 0; qp < 1; qp++) {
			// gets the first unlabeled item in the set of test data 
			queryPoint = Arrays.copyOfRange(testData.get(qp), 0, numAttributes);
			
			// neighbors of the query point, unsorted (key = index, value = distance)
			Map<Integer, Double> qpNeighbors = new HashMap<Integer, Double>();
			
			// returns unsorted list of neighbors for the query point
			qpNeighbors = getNeighbors(queryPoint);		
			
			// select the k nearest neighbors of the query point
			// NOTE: distance between points measured with the Value Difference Metric (VDM)
			int [] indicesOfkNearest = findKClosest(qpNeighbors);
			
			// take the majority vote (with respect to class) of k-nearest neighbors, breaking ties randomly			
			String classPrediction = majorityVote(indicesOfKNearest);
			
//			// for testing
//			for (int i = 0; i < queryPoint.length; i++) {
//				//System.out.println(queryPoint[i]);
//			}
			
			

			
		
		
		
		}
		


		
		

		// grab from train array list
		
		// store the class prediction for this query point
		
		
		

//		for (Map.Entry<Integer,Double> entry : qpNeighbors.entrySet()) {
//		  int key = entry.getKey();
//		  double value = entry.getValue();
//		  System.out.println(key + " : " + value );
//		} 
		
		
		
		


	}

	
	public void evaluate() {
		// after all test set instances have been classified, evaluate the performance of classifier
	}
	
	// calculate the Value Difference Metric (VDM) to compare the query point with all points in training data
	public Map<Integer, Double> getNeighbors(String[] qp) {
		// Stores the indices and distances for all neighboring points
		Map<Integer, Double> qpNeighbors = new HashMap<Integer, Double>();
				
		// calculate distance from qp to all points in training set
		ValueDiff vd = new ValueDiff(qp, trainData);
		qpNeighbors = vd.qpNeighbors; 
		
		return qpNeighbors;
	}
	
	// find the k closest neighbors of the query point (where closest is defined as lowest VDM values
	public int[] findKClosest(Map<Integer, Double> qpNeighbors) {
		// leave in just for testing now
		double[] kSmallestDist = new double[k];
		
		int[] kClosestIndices = new int[k];
		
		ArrayList<Double> distances = new ArrayList<Double>();
				
		// add all distances to an array list (to sort)
		for (Map.Entry<Integer,Double> entry : qpNeighbors.entrySet()) {
		  distances.add(entry.getValue());
		} 
		
		// sort the arraylist based on distance
		Collections.sort(distances);
		
		// gets the key associated with the smallest k distance values
		for (int i = 0; i < k; i++) {
			kSmallestDist[i] = distances.get(i);

	        for (Entry<Integer, Double> entry : qpNeighbors.entrySet()) {			
	        	if (entry.getValue().equals(kSmallestDist[i])) {
	        		kClosestIndices[i] = entry.getKey();
	        	}
	        }

		}
		
		return kClosestIndices;
	}
	
	
}
