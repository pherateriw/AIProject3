package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;

public class KNearestNeighbor extends Algorithm {
	// the test and train data sets
	ArrayList<String[]> trainData;
	ArrayList<String[]> testData;
	
	// stores the shortened name of the data set (for printing)
	String shortName;

	// stores the output of the k-NN algorithm, a class label given the feature data 
	ArrayList<String> knnClass = new ArrayList<String>(); 
	
	// used to navigate the data sets and print information
	int numAttributes;
	int numInstancesTrain; 
	int numInstancesTest; 
	int classLoc;
	int numClasses;

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
		super.get_logger().log(Level.INFO, "Training set stored.");
	}

	
	
	
	void test(ArrayList<String[]> testData) {
		super.get_logger().log(Level.INFO, "Starting testing:");		
		
		numInstancesTest = testData.size();
		String[] queryPoint;
		
		super.get_logger().log(Level.INFO, "Size of testing set: " + numInstancesTest + " instances, " + numAttributes + " attributes");
		
		// compute the distance from the query point to all other points in the training set
		for (int qp = 0; qp < numInstancesTest; qp++) {
			// gets the first unlabeled item in the set of test data 
			queryPoint = Arrays.copyOfRange(testData.get(qp), 0, numAttributes);
			
			super.get_logger().log(Level.INFO, "Query point: " +  Arrays.toString(queryPoint));
			
			// neighbors of the query point, unsorted (key = index, value = distance)
			Map<Integer, Double> qpNeighbors = new HashMap<Integer, Double>();

			// returns unsorted list of neighbors for the query point
			qpNeighbors = getNeighbors(queryPoint);		
			
			// select the k nearest neighbors of the query point
			// NOTE: distance between points measured with the Value Difference Metric (VDM)
			ArrayList<Integer> indicesOfkNearest = findKClosest(qpNeighbors);
			
			// take the majority vote (with respect to class) of k-nearest neighbors, breaking ties randomly			
			String classPrediction = majorityVote(indicesOfkNearest);
			
			//knnClass.add(classPrediction);
			System.out.println(classPrediction);
		}
		

		
		
		


		
		
		

//		for (Map.Entry<Integer,Double> entry : qpNeighbors.entrySet()) {
//		  int key = entry.getKey();
//		  double value = entry.getValue();
//		  System.out.println(key + " : " + value );
//		} 
		
		
		
		


	}

	
	public void evaluate() {
		for (String s : knnClass) {
			System.out.println(s);
		}
		
		
		// after all test set instances have been classified, evaluate the performance of classifier
	}
	
	// calculate the Value Difference Metric (VDM) to compare the query point with all points in training data
	public Map<Integer, Double> getNeighbors(String[] qp) {
		// Stores the indices and distances for all neighboring points
		Map<Integer, Double> qpNeighbors = new HashMap<Integer, Double>();
				
		// calculate distance from qp to all points in training set
		ValueDiff vd = new ValueDiff(qp, trainData);
		qpNeighbors = vd.qpNeighbors; 
		numClasses = vd.numClasses;
		
		return qpNeighbors;
	}
	
	// find the k closest neighbors of the query point (where closest is defined as lowest VDM values
	public ArrayList<Integer> findKClosest(Map<Integer, Double> qpNeighbors) {
		
		// array to hold the k smallest distances
		ArrayList<Double> kSmallestDist = new ArrayList <Double>();
		
		// array to hold the indices of the instances with the k smallest distances
		ArrayList<Integer> kClosestIndices = new ArrayList <Integer>();
		
		// because hash maps are not sortable
		// NOTE: this is not efficient, think about changing if time allows
		ArrayList<Double> distances = new ArrayList<Double>();
				
		// add all distances to an array list (to sort)
		for (Map.Entry<Integer,Double> entry : qpNeighbors.entrySet()) {
		  distances.add(entry.getValue());
		} 
		
		// sort the arraylist based on distance
		Collections.sort(distances);
		
		// gets the key associated with the smallest k distance values
		for (int i = 0; i < k; i++) {
			kSmallestDist.add(distances.get(i));
		}
	    
		
		for (int i = 0; i < kSmallestDist.size(); k++) {
			for (Entry<Integer, Double> entry : qpNeighbors.entrySet()) {			
		        if (entry.getValue().equals(kSmallestDist.get(i))) {
		        	if(!kClosestIndices.contains(entry.getKey()))
		        		kClosestIndices.add(entry.getKey());
		        		break;
		        }
		    }			
		}
			
		super.get_logger().log(Level.INFO, "k-Nearest Neighbors are " +  Arrays.toString(kClosestIndices.toArray()));
		super.get_logger().log(Level.INFO, "k closest distances are " +  Arrays.toString(kSmallestDist.toArray()));
		
		return kClosestIndices;
	}
	
	// take a majority vote among the k nearest neighbors (breaking ties randomly) to determine 
	// the most popular class assignment
	public String majorityVote(ArrayList<Integer> kNearestIndices) {
		String mostPop = "";
		
		Random rand = new Random(System.currentTimeMillis());
		int tieBreaker = rand.nextInt();
		
		// holds the associated class value for each of the k nearest neighbors
		ArrayList<String> ballotBox = new ArrayList<String>();
		
		// gets the classes associated with the k nearest neighbors
		for (int i = 0; i < kNearestIndices.size(); i++) {
			ballotBox.add(trainData.get(i)[classLoc]);
		}
			
		// determine the most popular class, breaking ties randomly
		int mostPopFreq = Integer.MIN_VALUE;
		
		for (int i = 0; i < ballotBox.size() - 1; i++) {
			int thisFreq = Collections.frequency(ballotBox, ballotBox.get(i));
			
			if (thisFreq > mostPopFreq) {
				mostPopFreq = thisFreq;
				mostPop = ballotBox.get(i);
			} else if (thisFreq == mostPopFreq) {
				if (tieBreaker % 2 == 0) {
					mostPopFreq = thisFreq;
					mostPop = ballotBox.get(i);
				} 
			}
			
		} // end for: have determined the most popular class
		
		return mostPop;
	}
	
	
	
}
