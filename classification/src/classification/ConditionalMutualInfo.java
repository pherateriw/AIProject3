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

	private int locOfAttI;
	private int locOfAttJ;
	private ArrayList<String[]> trainData;

	public ConditionalMutualInfo(int locOfAttI, int locOfAttJ, ArrayList<String[]> trainData) {
		this.locOfAttI = locOfAttI;
		this.locOfAttJ = locOfAttJ;
		this.trainData = trainData;		
	}
	
	public double calculate() {
//		int classLoc = trainData.get(0)
//		
		
		double cmi = 0.0;
		

		
		
		
		
		
		
		return cmi;
	}
	
	

	
	
	
}
