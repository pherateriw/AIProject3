package classification;

import java.util.ArrayList;

/*
 *  Measure used in tree-augmented naive Bayes (TAN) algorithm. Conditional mutual information (CMI) helps 
 *  to determines which dependencies to add back into the network (by estimating the amount entropy can be
 *  reduced through the addition of a dependency relationship. 
 *  
 *  Our calculation of CMI comes from equation provided in Sheppard "A Graduate Course in Machine Learning"
 *  course notes (2016).
 */

public class ConditionalMutualInfo {

	private ArrayList<String[]> trainData;

	// hold the classes, as gathered from the training data
	ArrayList<String> classVals = new ArrayList<String>();

	// hold all possible values of attribute ai
	ArrayList<String> aiVals = new ArrayList<String>();

	// hold all possible values of attribute ai
	ArrayList<String> ajVals = new ArrayList<String>();

	public ConditionalMutualInfo(ArrayList<String[]> trainData) {
		this.trainData = trainData;
	}

	public double calculate(int aiLoc, int ajLoc) {
		// store location of class for easy retrieval
		int classLoc = trainData.get(0).length - 1;

		// get all possible class values in problem domain
		for (int inst = 0; inst < trainData.size(); inst++) {
			if (!classVals.contains(trainData.get(inst)[classLoc])) {
				classVals.add(trainData.get(inst)[classLoc]);
			}
		}

		// get all possible values of attribute ai in problem domain
		for (int inst = 0; inst < trainData.size(); inst++) {
			if (!aiVals.contains(trainData.get(inst)[aiLoc])) {
				aiVals.add(trainData.get(inst)[aiLoc]);
			}
		}

		// get all possible values of attribute aj in problem domain
		for (int inst = 0; inst < trainData.size(); inst++) {
			if (!ajVals.contains(trainData.get(inst)[ajLoc])) {
				ajVals.add(trainData.get(inst)[ajLoc]);
			}
		}

		// iterate through all class values
		for (int v = 0; v < classVals.size(); v++) {

			// holds P(v)
			double probV = 0.0;

			// number of times the class v appears in the training data
			int numV = 0;

			for (int ai = 0; ai < aiVals.size(); ai++) {
				// holds P(ai | v)
				double probAIandV = 0.0;

				// number of times ai and v appear together in the training data
				int numAIandV = 0;

				for (int aj = 0; aj < ajVals.size(); aj++) {
					// holds P( aj | v )
					double probAJandV = 0.0;

					// holds P( ai | aj, v )
					double probAIAJandV = 0.0;

					// number of times aj and v appear together in the training
					// data
					int numAJandV = 0;

					// number of times ai, aj and v appear together in the
					// training data
					int numAIAJandV = 0;

					for (String[] arr : trainData) {

						// checks for number of times we find class v in the
						// training data
						if (arr[classLoc].equalsIgnoreCase(classVals.get(v))) {
							numV++;

							// checks for number of times we find ai given v in
							// the
							// training data
							if (arr[aiLoc].equals(aiVals.get(ai))) {
								numAIandV++;
								
								// checks for number of times we find ai given aj and 
								// v in the training data
								if (arr[ajLoc].equals(ajVals.get(aj))) {
									numAIAJandV++;
								}
								
							}
							
							if (arr[ajLoc].equals(ajVals)) {
								numAJandV++;
							}
							
						} // end if: v in training data

						probAIAJandV = (double) numAIAJandV / ajVals.size();
						//System.out.println(probAIAJandV);
						
						probAJandV = (double) numAJandV / ajVals.size();
						//System.out.println(probAIandV);						

					}

					probAIandV = (double) numAIandV / aiVals.size();
					//System.out.println(probAIandV);

				}

				probV = (double) numV / trainData.size();
				//System.out.println(probV);
				
			} // end for: have counted all occurrences of class

		}

		// sum over all classes v in the data set

		// iterate over all aj in data set
		// sum over all values of

		// iterate over all ai in data set

		// get P(Ai,Aj,V) with chain rule?

		double cmi = 0.0;

		return cmi;
	}

}
