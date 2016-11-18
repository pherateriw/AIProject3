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
	
	ArrayList<String> aiVals = new ArrayList<String>();
	
	public ConditionalMutualInfo(ArrayList<String[]> trainData) {
		this.trainData = trainData;		
	}
	
	public double calculate(int aiLoc, int ajLoc) {
		// store location of class for easy retrieval
		int classLoc = trainData.get(0).length- 1;
				
		// get all possible class values in problem domain
		for (int inst = 0; inst < trainData.size(); inst++) {
			if (!classVals.contains(trainData.get(inst)[classLoc])) {
				classVals.add(trainData.get(inst)[classLoc]);
			}
		}
		
		// get all possible values of ai in problem domain
		for (int inst = 0; inst < trainData.size(); inst++) {
			if (!aiVals.contains(trainData.get(inst)[aiLoc])) {
				aiVals.add(trainData.get(inst)[aiLoc]);
			}
		}		
		
		
		System.out.println("classes");
		for(String c : classVals) {
			System.out.println(c.toCharArray());
		}
		
		System.out.println("aiVals");
		for(String a : aiVals) {
			System.out.println(a.toCharArray());
		}		
		
		
		
		// sum over all classes v in the data set
		
		
		
		double cmi = 0.0;
		

		
		
		
		
		
		
		return cmi;
	}
	
	

	
	
	
}
