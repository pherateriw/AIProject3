package classification;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Some of the datasets have missing attribute values (indicated with a '?'), and these values are 
 * replaced via a “fractioning” of the examples based on the prior probability of each of the values 
 * as determined by the remaining examples in the data set.
 */


public class DataImputer {
	public DataImputer(ArrayList<String[]> data) {

		// loops through each of the instances
		for (String[] arr : data) {
			for (int i = 0; i < arr.length; i++) {
				System.out.println(arr[i]);
			}
		}
		
		
		
		
		

	}
}
