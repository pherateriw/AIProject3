package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
 * Some of the datasets have missing attribute values (indicated with a '?'), and these values 
 * are sampled according to the conditional probability of the values occurring, given the underlying
 * class for that example. Note that in the following, attribute and feature are used interchangeably. 
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

		/*
		 * The following for loop moves through each of the instances in the
		 * dataset, looking for missing attributes and recording the number of
		 * occurrences. This information is stored in an array, which tells us
		 * the number of missing attributes at a given location.
		 */

		for (String[] arr : data) {

			// loops through the features for this particular instance
			for (int featI = 0; featI < arr.length; featI++) {
				// checks for a '?', which indicates a missing value
				if (arr[featI].equalsIgnoreCase("?")) {
					// adds to the number of missing values at that location
					missingFeatureVals[featI] += 1;
					missingValLocs.put(arrayListLoc, featI);
				}
			} // end for: finished with array corresponding to this instance
			arrayListLoc++;
		} // end for: looping through each instance in dataset

		/*
		 * Number of missing attribute values at each location in the dataset
		 * has been determined. Now the conditional probability of the values
		 * occurring (given the underlying class for that example) can be
		 * calculated, and the values can be imputed according to the
		 * conditional probabilities.
		 */

		// for the features we know that we have missing data, determine the
		// data distribution (given the class)
		for (int m = 0; m < missingFeatureVals.length; m++) {
			// stores the feature/class combinations
			// class value is key, secondary key is the value of the associated
			// attribute, and the integer value represents the count
			attributeMap = new HashMap<String, Map<String, Integer>>();

			if (missingFeatureVals[m] != 0) {
				// there are some attributes here with missing values
				// look through all instances to determine distribution of attribute values
				for (int n = 0; n < numInstances; n++) {
					String classVal = data.get(n)[data.get(n).length - 1];
					String attVal = data.get(n)[m];

					// builds a map of class/value combinations, also counts occurrences
					if (!attributeMap.containsKey(classVal)) {
						// if key (class) is not in attributeMap, neither is the value (attribute),
						// and the count should be 0
						if (!attVal.equalsIgnoreCase("?")) {
							// NOTE: checks that the value is not '?', bc we already counted those
							int count = 1;
							attributeMap.put(classVal,
									new HashMap<String, Integer>());
							attributeMap.get(classVal).put(attVal, count);
						}

					} else {
						// if key is in attributeMap, but value is not
						if (!attributeMap.get(classVal).containsKey(attVal)) {
							if (!attVal.equalsIgnoreCase("?")) {
								int count = 1;
								attributeMap.get(classVal).put(attVal, count);
							}

						} else {
							// if both key and value are in attribute map,
							// update count
							if (!attVal.equalsIgnoreCase("?")) {
								int count = attributeMap.get(classVal).get(attVal) + 1;
								attributeMap.get(classVal).put(attVal, count);
							}
						}
					}

				} // end for: for this particular attribute, all missing value
					// occurrences have been counted

				// gets all possible keys (class values) for these attributes
				Object[] classSet = attributeMap.keySet().toArray();
				Object[] valueByClassSet = null;
				int countSet;

				// the following are used to help determine the conditional probability of each
				// feature (given the class)
				int totalCount;
				Map<String, Double> condProb = new HashMap<String, Double>();
				
				// stores the number of attributes to be imputed for each value
				Map<String, Integer> imputedAttributes = new HashMap<String, Integer>();
				
				// iterate through class values
				for (int c = 0; c < classSet.length; c++) {
					// for calculating on a per class value
					condProb.clear();
					countSet = 0;
					totalCount = 0;
					
					// gets all attribute values for the specified class
					valueByClassSet = attributeMap.get(classSet[c]).keySet().toArray();
					
					// iterates through the attributes for the specified class
					for (int a = 0; a < valueByClassSet.length; a++) {
						countSet = attributeMap.get(classSet[c]).get(valueByClassSet[a]);
						totalCount += countSet;
					} // end for: have gone through all attributes for key to calculate the total count
					
					// NOTE: we are intentionally not counting missing attributes, so adding all of the 
					// attributes does not (necessarily) = the number of instances in te data set

					// having calculated the total count, calculate the 
					// conditional probability of each feature (given the class)
					for (int a = 0; a < valueByClassSet.length; a++) {
						double featCount = attributeMap.get(classSet[c])
								.get(valueByClassSet[a]).doubleValue();
						Double prob = featCount / totalCount;
						condProb.put(valueByClassSet[a].toString(), prob);
					}

					
					int numMissingVals = missingFeatureVals[m];
					int numSoFar = 0;
					
					// go through the conditional probabilities, multiply by number of 
					// missing values to determine the number that should be imputed for
					// each attribute value
					for (Map.Entry<String, Double> entry : condProb.entrySet()) {
						String attVal = entry.getKey();
						double value = entry.getValue().doubleValue();
						
						// compute the number to impute for each attribute value
						int numToImpute = (int) (value * numMissingVals);
											
						imputedAttributes.put(attVal, numToImpute);
						numSoFar += numToImpute;						
					}
					
					
					/*
					 * NOTE: at the end of this process (because of rounding) we might have a 
					 * mismatch between numToImpute and the actual number of values we need to impute.
					 * To handle this, we will assign the remaining (in practice, has been less 
					 * than 5 values) at random.
					 */
					
					// generates a random number for the purpose of assigning remaining values
					Random rand = new Random();
					int randomNumber = 0;
					int breakWhen = 0;
					String randomKey = "";
					
					// while there are still unassigned values, pick a key at random
					while(numSoFar < numMissingVals) {
						randomNumber = rand.nextInt((imputedAttributes.size() - 1) + 1);
						breakWhen = 0; 
						randomKey = "";
						
						// get a key at random
						for (String key : imputedAttributes.keySet()) {
														
							if (breakWhen < randomNumber) {
								// keep going
								breakWhen++;
							} else {
								randomKey = key;
								break;
							}
						} // end for: have random key
						
																	
						// given the random key, add to the number of entries to be  imputed
						for (Map.Entry<String, Integer> entry : imputedAttributes.entrySet()) {
							String attVal = entry.getKey();
	
							
							if (randomKey.equalsIgnoreCase(attVal)) {
								int numToImpute = entry.getValue() + 1;
								imputedAttributes.put(randomKey, numToImpute);
								numSoFar++;								
							}
													
						} // end for: have dealt with one imputed value						
					} // end while
					
					
					
									
					// check with printing
					for (Map.Entry<String, Integer> entry : imputedAttributes.entrySet()) {
						String attVal = entry.getKey();
						double value = entry.getValue();
												
						System.out.println(attVal);
						System.out.println(value);
					}					
//					
//					
//					
//					
					
					
					// System.out.println(totalCount);

				} // end for : building key, value and object set

				// built keySet, valueSet, countSet

				// } // end for : building key, value and object set

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
