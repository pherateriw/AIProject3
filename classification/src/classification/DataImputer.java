package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
		
		// loops through each of the instances in the dataset, looking for missing values
		for (String[] arr : data) {
			
			// loops through the features for this particular instance
			for (int i = 0; i < arr.length; i++) {
				// checks for a '?', which indicates a missing value
				if (arr[i].equalsIgnoreCase("?")) {
					  // adds to the number of missing values at that location
					  missingFeatureVals[i] += 1;
				}
			} // end for: finished with array corresponding to this instance
		} // end for: looping through each instance in dataset
		
		// for the features we know that we have missing data, determine the data distribution (given the class)
		for (int i = 0; i < missingFeatureVals.length; i++) {
			// stores the feature/class combinations
			// key is the class value, value is the value of the associated attribute, integer represents the count
			Map<String, Map<String, Integer>> attributeMap = new HashMap<String, Map<String,Integer>>();

			if (missingFeatureVals[i] != 0) {
				// there are some attributes here with missing values
				// look through all instances
				for (int j = 0; j < numInstances; j++) {
					String key = data.get(j)[data.get(j).length - 1];
					String value = data.get(j)[i];
				
					// builds a map of class/value combinations, as well as the count
					if (!attributeMap.containsKey(key)) {
						// if key is not in attributeMap, neither is the value, and the count should be 0 
						// checks that the value is not '?', bc we already counted those
						if (!value.equalsIgnoreCase("?")) {
							int count = 1;
							attributeMap.put(key, new HashMap<String, Integer>());
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
							// if both key and value are in attribute map, update count
							if (!value.equalsIgnoreCase("?")) {							
								int count = attributeMap.get(key).get(value) + 1;
								attributeMap.get(key).put(value, count);
							}
						}
						

						
						
						


						}
						
						// has key, not value
					} // end for

				// gets the keys
				Object[] keySet = attributeMap.keySet().toArray();
				
				for (int j = 0; j < keySet.length; j++) {
					System.out.println("Key: " + keySet[j]);
					System.out.print("Values:");
					Object[] valueSet = attributeMap.get(keySet[j]).keySet().toArray();					
					for (int k = 0; k < valueSet.length; k++) {
						//System.out.println("Values: " + valueSet[k]);						
						System.out.print(" " + valueSet[k]);
						int countSet = attributeMap.get(keySet[j]).get(valueSet[k]);
						System.out.print(", " + countSet + "; ");
					}
					
					System.out.println();
					

				}
				

				


				
				// gets the value

//				System.out.println(attributeMap.get(key));						
//				// gets the count
//				System.out.println(attributeMap.get(key).get(value));
				
				
					//System.out.println("Key: " + attributeMap.get(key) + ", Value: " + attributeMap.get(key).get(value));
					//System.out.println(attributeMap.get(key).get(value));					
				} // end for: for this particular missing value, all conditional probabilities calculated 
			
		
			
			
		
		} //end if: makes sure we are only looking at those attributes with missing values
			
			
		
		
		
			 
					 

		//} // end for: have determined all fractions for missing variables
				 
				 
				
				// go through all instances, collecting info to determine distribution				
//				for(String[] arr : data) {
//					System.out.println(featureMap.containsValue(arr[i]));
					
					// check key
					
					// check value
					
					// key = class, value = feature 
//					if (!featureMap.containsValue(arr[i])) {
//						featureMap.put(arr[arr.length - 1], arr[i]);							
//					}				
				} // end for: counted all feature/class combinations
				
				
			
			
			
		
			
			
					
				
				//System.out.println(missingFeatureVals[i]);	
//			}
//
//			
//			for (int j = 0; j < featureMap.size(); j++) {
//				System.out.println(featureMap.get(j));
//			}	
			
			// when values are missing, look at the distribution of remaining examples for each class
			
		
		//}
		
		

		
		
		
		// where we have missing values, record the associated class
		// get feature distribution, given the class (how many examples for the 
		// determine fraction of each feature (numInstance - missingFeatureVals[i])
		
		
		
		
		
		
		
		
		// get frequency of other data at that loc
		// add in data according to distribution (considering ties, and what to if calculations don't work out exactly 
		// as integers and )
		// consider the class as well
		// book 260
		// cite paper handling missing attribute values starting page 5
		

	//}
}
