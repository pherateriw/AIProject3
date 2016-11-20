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

		
		int vCount = 0; 
		double vProb = 0.0;
		double cmi = 0.0;
		
		// goes through each class value
		for (int v = 0; v < classVals.size(); v++) {
			
			// get number of times class V occurs in the data
			vCount = countPofV(classLoc, v);						
			vProb = (double) vCount / trainData.size();
			
			int aiVCount = 0; 
			double aiVProb = 0.0;
			
				
			for (int i = 0; i < aiVals.size(); i++) {								
				aiVCount = countPofAIV(classLoc, aiLoc, v , i);
				aiVProb = (double) aiVCount / trainData.size();	
				
				int ajVCount = 0; 
				double ajVProb = 0.0;
				double ajCim = 0.0;
				
				for (int j = 0; j < ajVals.size(); j++) {
					
					ajVCount = countPofAJV(classLoc, aiLoc, v , j);
					ajVProb = (double) ajVCount / trainData.size();
					//System.out.println("P(aj | v) = " +  ajVProb);
					
					int aiajVCount = 0; 
					double aiajVProb = 0.0;
					
					aiajVCount = countPofAIAJV(classLoc, ajLoc, aiLoc, v, j, i);
					aiajVProb = (double) aiajVCount / trainData.size();
					//System.out.println("P(ai | aj, v) = " +  aiajVProb);
					
					double numer = aiajVProb;
					// adds a small number, in case either is 0
					double denom = aiVProb * ajVProb + 0.00001;
					
					// adds a small number, in case the ratio is 0
					double ratio = (numer / denom) + 0.00001;
					
					double log = Math.log(ratio);
					//System.out.println(log);
					
					double product = aiajVProb * ajVProb * vProb * log;
					
					//System.out.println(product);
					cmi += product;
				}
				
				cmi += cmi;
				
			} 
			
			cmi += cmi;
			
		} // end for: calculated all necessary probabilities
				
		return cmi;
	}
	
	// gets the number of times p is found in the training data
	public int countPofV(int classLoc, int vIt) {
		int numV = 0; 
		
		for (String[] arr : trainData) {
			// checks for number of times we find class v in the
			// training data
			if (arr[classLoc].equalsIgnoreCase(classVals.get(vIt))) {
				numV++;

			} // end if: checking for this class
		} // end for

		
		return numV;
	}
	
	// gets the number of times is found in the training data
	public int countPofAIV(int classLoc, int aiLoc, int vIt, int aiIt) {
		int numAIandV = 0; 
		
		for (String[] arr : trainData) {

			// checks for number of times we find class v in the
			// training data
			if (arr[classLoc].equalsIgnoreCase(classVals.get(vIt))) {

				// checks for number of times we find ai given v in
				// the
				// training data
				if (arr[aiLoc].equals(aiVals.get(aiIt))) {
					numAIandV++;
					
					
				} // end ifs: checking for ai
						
			} // end if: checking for this class


		} // end for

		
		return numAIandV;
	}
	
	// gets the number of times is found in the training data
	public int countPofAJV(int classLoc, int ajLoc, int vIt, int ajIt) {
		int numAJandV = 0; 
		
		for (String[] arr : trainData) {

			// checks for number of times we find class v in the
			// training data
			if (arr[classLoc].equalsIgnoreCase(classVals.get(vIt))) {

				// checks for number of times we find ai given v in
				// the
				// training data
				if (arr[ajLoc].equals(ajVals.get(ajIt))) {
					numAJandV++;
					
			
				} // end ifs: checking for ai
						
			} // end if: checking for this class


		} // end for

		
		return numAJandV;
	}
	
	// gets the number of times is found in the training data
	public int countPofAIAJV(int classLoc, int ajLoc, int aiLoc, int vIt, int ajIt, int aiIt) {
		int numAIAJandV = 0; 
		
		for (String[] arr : trainData) {

			// checks for number of times we find class v in the
			// training data
			if (arr[classLoc].equalsIgnoreCase(classVals.get(vIt))) {

				// checks for number of times we find ai given v in
				// the
				// training data
				if (arr[aiLoc].equals(aiVals.get(aiIt))) {
					
					// checks for number of times we find ai given aj and 
					// v in the training data
					if (arr[ajLoc].equals(ajVals.get(ajIt))) {
						numAIAJandV++;
					}
					
				} // end ifs: checking for ai
				
				
			} // end if: checking for this class


		} // end for: have gone through all training data getting counts

		
		return numAIAJandV;
	}
	
}
	
