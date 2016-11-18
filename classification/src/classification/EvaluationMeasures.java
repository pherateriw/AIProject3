package classification;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Measures used to evaluate the classification performance of each
 * algorithm. As the majority of the datasets used in this project are not
 * binary (glass has 7 classes, iris has 3 classes and soybean has four
 * classes), we used the following multi-class classification measures: 
 * 
 * 1. Average Accuracy (The average per-class effectiveness of a classifier) 
 * 
 * 2. Precision with macro averaging (An average per-class agreement of the
 * data class labels with those of a classifiers) 
 * 
 * 3. Recall with macro averaging (An average per-class effectiveness of a 
 * classifier to identify class labels)
 * 
 * 4.Fscore with macro averaging (Relations between data’s positive labels 
 * and those given by a classifier based on a per-class average)
 * 
 * Note: Macro-averaging treats all classes equally while micro-averaging 
 * favors bigger classes.
 * 
 * These measures are drawn from Sokolova and Lapalme's "A systematic analysis of 
 * performance measures for classification tasks" 2009
 */


public class EvaluationMeasures {
	// the number of classes in the dataset
	private int numClasses;
	
	// the test data for the given algorithm/fold/repetition
	private ArrayList<String[]> testData;
	
	// the class labels as predicted by the algorithm
	private ArrayList<String> predictedCLs;
	// the actual class labels
	private ArrayList<String> trueCLs = new ArrayList<String>();
	


	public EvaluationMeasures(int numClasses, ArrayList<String> classLabels, ArrayList<String[]> testData) {
		this.numClasses = numClasses;
		this.predictedCLs = classLabels;
		this.testData = testData;
		evaluate();
	}

	
	private void evaluate() {
		// gets the location of the class variable
		int classValLoc = testData.get(0).length - 1; 
				
		// gets the true classes (from the original, labeled data) of the 
		// test instances
		for (String[] c : testData) {
			trueCLs.add(c[classValLoc]);		
		}
		
		
		for (String c: trueCLs) {
			System.out.println(c);
		}
		
		
		
//		System.out.println("numClasses: " + numClasses);
//		System.out.println();
//		System.out.println("class labels");
//		for (String c : predictedCLs) {
//			System.out.println(c);
//		}
//		System.out.println();	
//
//		System.out.println("true class val");	
//		for (String[] c : testData) {
//			System.out.println(Arrays.toString(c));
//
//		}
		
		
		
		
	}
	
	
	
	// accuracy
	// takes the value of 0 if the predicted classification equals that of the
	// true class or a 1 if the predicted classification does not match the true
	// class.

	// precision
	// recall
	// f-measure

}
