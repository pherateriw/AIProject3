package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

/*
 * When running our four algorithms (k-NN, NB, TAN, ID3), we are using 5 x 2 cross validation, as described by 
 * Dietterich in "Approximate Statistical Tests for Comparing Supervised Classification Learning 
 * Algorithms" (1998). Although the majority of the 5 x 2 cross validation process (switching train and test sets, 
 * repeating the process 5 times) will take place in RunModels (where we call the algorithms), this will randomly
 * split the data into train and test sets. The data will be randomly split into two sets that are 
 * (roughly) equivalent in size, with a class distribution that reflects (again, roughly) to the original data.  
 */

public class DataSplitter {
	// array lists to keep track of the entire dataset, as well as the
	// test/train sets
	ArrayList<String[]> data;
	ArrayList<String[]> train;
	ArrayList<String[]> test;

	public DataSplitter(ArrayList<String[]> data) {
		this.data = data;
	}

	// splits the data into train and test sets
	public void splitData(int seed) {
		ArrayList<String> classVals = new ArrayList<String>();
		
		// Random number generator, seeded for consistency across algos
		Random rand = new Random(seed);
		int randomNumber;

		// holds the train and test set
		train = new ArrayList<String[]>();
		test = new ArrayList<String[]>();

		for (String[] arr : data) {
			String thisClass = arr[arr.length - 1];

			// classVal is not yet in the list, add it
			if (!classVals.contains(thisClass)) {
				classVals.add(thisClass);
			} 
		} // have all classes in the data

		int numClasses = classVals.size();
		
		// move through all classes
		for (int c = 0; c < numClasses; c++) {
			// move through all data, checking for this class
			for (String[] arr : data) {
				// if the correct class, randomly assign to either test or train
				if (arr[arr.length - 1].equalsIgnoreCase(classVals.get(c))) {
					randomNumber = rand.nextInt((numClasses - 1) + 1);
					
					if (randomNumber % 2 == 1) {
						train.add(arr);
					} else {
						test.add(arr);
					} // end if: assigned to train or test
				} // end if : checked class value
				
			} // end for: move through all data
		} // end for: finished splitting data
	}

	// return the test set
	public ArrayList<String[]> getTrainingData() {
		// shuffles array because we imposed an ordering when splitting
		long seed = System.nanoTime();
		Collections.shuffle(train, new Random(seed));
		return train;
	}

	// return the training set
	public ArrayList<String[]> getTestingData() {
		// shuffles array because we imposed an ordering when splitting
		long seed = System.nanoTime();
		Collections.shuffle(test, new Random(seed));
		return test;
	}

}
