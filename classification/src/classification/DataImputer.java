package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * Some of the datasets have missing attribute values (indicated with a '?'), and these values 
 * are sampled according to the conditional probability of the values occurring, given the underlying
 * class for that example.
 */

public class DataImputer {
	public DataImputer(ArrayList<String[]> data) {
		// number of instances in the entire dataset
		int numInstances = data.size();

		// store the number of missing values for each attribute in an array
		int numFeatures = data.get(0).length;
		int[] missingFeatureVals = new int[numFeatures];

		// used to store location of missing values, for quick retrieval
		Map<Integer, Integer> missingValLocs = new HashMap<Integer, Integer>();
		int arrayListLoc = 0;

		// used to store attribute/class/count information
		Map<String, Map<String, Integer>> attributeMap = new HashMap<String, Map<String, Integer>>();

		// loops through each of the instances in the dataset, looking for
		// missing values
		for (String[] arr : data) {

			// loops through the features for this particular instance
			for (int i = 0; i < arr.length; i++) {
				// checks for a '?', which indicates a missing value
				if (arr[i].equalsIgnoreCase("?")) {
					// adds to the number of missing values at that location
					missingFeatureVals[i] += 1;
					missingValLocs.put(arrayListLoc, i);
				}
			} // end for: finished with array corresponding to this instance
			arrayListLoc++;
		} // end for: looping through each instance in dataset

		// for the features we know that we have missing data, determine the
		// data distribution (given the class)
		for (int i = 0; i < missingFeatureVals.length; i++) {
			// stores the feature/class combinations
			// key is the class value, value is the value of the associated
			// attribute, integer represents the count
			attributeMap = new HashMap<String, Map<String, Integer>>();

			if (missingFeatureVals[i] != 0) {
				// there are some attributes here with missing values
				// look through all instances
				for (int j = 0; j < numInstances; j++) {
					String key = data.get(j)[data.get(j).length - 1];
					String value = data.get(j)[i];

					// builds a map of class/value combinations, as well as the
					// count
					if (!attributeMap.containsKey(key)) {
						// if key is not in attributeMap, neither is the value,
						// and the count should be 0
						// checks that the value is not '?', bc we already
						// counted those
						if (!value.equalsIgnoreCase("?")) {
							int count = 1;
							attributeMap.put(key,
									new HashMap<String, Integer>());
							attributeMap.get(key).put(value, count);
						}

					} else {
						// if key is in attributeMap, but value is not
						if (!attributeMap.get(key).containsKey(value)) {
							if (!value.equalsIgnoreCase("?")) {
								int count = 1;
								attributeMap.get(key).put(value, count);
							}

						} else {
							// if both key and value are in attribute map,
							// update count
							if (!value.equalsIgnoreCase("?")) {
								int count = attributeMap.get(key).get(value) + 1;
								attributeMap.get(key).put(value, count);
							}
						}
					}

				} // end for: for this particular attribute, all missing value occurrences
					// counted

				// gets all possible keys (class values) for these attributes
				Object[] keySet = attributeMap.keySet().toArray();
				Object[] valueSet = null;
				int countSet;
				
				// used to help determine the conditional probability of each feature
				int totalCount; 
				Map<String, Double> condProb = new HashMap<String, Double>();
				
				for (int j = 0; j < keySet.length; j++) {
					condProb.clear();
					countSet = 0; 
					totalCount = 0; 
					System.out.println("Key: " + keySet[j]);
					valueSet = attributeMap.get(keySet[j]).keySet().toArray();
					for (int k = 0; k < valueSet.length; k++) {
						System.out.print(" " + valueSet[k]);
						countSet = attributeMap.get(keySet[j]).get(valueSet[k]);
						totalCount += countSet;
						System.out.print(", " + countSet + "; ");
					} // end for: have gone through all values for key
					
					// have calculated the total count, now figure out conditional probability of each feature
					for (int k = 0; k < valueSet.length; k++) {
						double featCount = attributeMap.get(keySet[j]).get(valueSet[k]).doubleValue();
						Double prob = featCount/totalCount;
						System.out.println(prob);
						condProb.put(valueSet[k].toString(),prob);
					} 
					
					
					System.out.println(totalCount);
					
					
					
				} // end for : building key, value and object set
			
				// built keySet, valueSet, countSet
				


				//} // end for : building key, value and object set
				
				
				
			} // end if

			
			
			
			
			
			
			// check class associated with missing data, calc percent of each
			// feature, assign features accordingly
			
		}

		// loops through each of the instances in the dataset, looking for
		// missing values
		for (String[] arr : data) {

			// loops through the features for this particular instance
			for (int i = 0; i < arr.length; i++) {
				// checks for a '?', which indicates a missing value
				if (arr[i].equalsIgnoreCase("?")) {

					// adds to the number of missing values at that location
					// missingFeatureVals[i] += 1;
				}
			} // end for: finished with array corresponding to this instance
		} // end for: looping through each instance in dataset

	}

	// System.out.println(missingFeatureVals[i]);
	// }
	//
	//
	// for (int j = 0; j < featureMap.size(); j++) {
	// System.out.println(featureMap.get(j));
	// }

	// when values are missing, look at the distribution of remaining examples
	// for each class

	// }

	// where we have missing values, record the associated class
	// get feature distribution, given the class (how many examples for the
	// determine fraction of each feature (numInstance - missingFeatureVals[i])

	// get frequency of other data at that loc
	// add in data according to distribution (considering ties, and what to if
	// calculations don't work out exactly
	// as integers and )
	// consider the class as well
	// book 260
	// cite paper handling missing attribute values starting page 5

	// }
}
