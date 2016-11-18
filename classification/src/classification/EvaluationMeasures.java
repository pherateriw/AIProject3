package classification;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Measures used to evaluate the classification performance of each
 * algorithm. As the majority of the datasets used in this project are not
 * binary (glass has 7 classes, iris has 3 classes and soybean has four
 * classes), we used the following multi-class classification measures: 
 * 
 * 1. Average Accuracy  
 * 
 * 2. Precision with macro averaging 
 * 
 * 3. Recall with macro averaging 
 * 
 * 4.Fscore with macro averaging 
 * 
 * Note: Macro-averaging treats all classes equally while micro-averaging 
 * favors bigger classes.
 * 
 * These measures are drawn from Sokolova and Lapalme's "A systematic analysis of 
 * performance measures for classification tasks" 2009
 */


public class EvaluationMeasures {
	// the number and value of classes in the dataset
	private int numClasses;
	private ArrayList<String> classVals = new ArrayList<String>();	
	
	// the test data for the given algorithm/fold/repetition
	private ArrayList<String[]> testData;
	
	// the class labels as predicted by the algorithm
	private ArrayList<String> predictedCLs;
	// the actual class labels
	private ArrayList<String> trueCLs = new ArrayList<String>();
	
	// holds the number of true pos, false pos, false neg and true neg examples for each of the classes
	private ArrayList<Integer> results = new ArrayList<Integer>();
	
	public EvaluationMeasures(int numClasses, ArrayList<String> classLabels, ArrayList<String[]> testData) {
		this.numClasses = numClasses;
		this.predictedCLs = classLabels;
		this.testData = testData;
		evaluate();
	}

	private void evaluate() {
		// the location of the class variable
		int classValLoc = testData.get(0).length - 1; 
		
		// the number of instances
		int numInstances = testData.size();
				
		// gets the true classes (from the original, labeled data) of the 
		// test instances
		for (String[] c : testData) {
			trueCLs.add(c[classValLoc]);
			if (!classVals.contains(c[classValLoc])) {
				classVals.add(c[classValLoc]);
			}
		}
				
		// moves through classes, calculating true positive, false positive, 
		// false negative and true negative values for each class
		for (int i = 0; i < classVals.size(); i++) {
			int tpi = 0; 
			int fpi = 0; 
			int fni = 0; 
			int tni = 0; 

			String thisClass = classVals.get(i);
			
			for (int v = 0; v < numInstances; v++) {
				String classPredict = predictedCLs.get(v);
				String trueClass = trueCLs.get(v);	
				
				// check if the predicted class is i (to determine tpi and fpi values)
				if (classPredict.equalsIgnoreCase(thisClass)) {
					// check if true class is i
					if (trueClass.equalsIgnoreCase(thisClass)) {
						// number of true positive examples for class i (tpi)
						// predicted class i, true class is i
						tpi++;
						System.out.println("tp " + classPredict + " = " + thisClass + " = " + trueClass);
					} else {
						// number of false positive examples for class i (fpi)
						// predicted class i, true class is not i
						fpi++;
						System.out.println("fp " + classPredict + " = " + thisClass + " != " + thisClass);						
					}
				} // end if: checked for tpi and fpi cases
				
				// check if the predicted class is not i (to determine fni and tni values)
				if (!classPredict.equalsIgnoreCase(thisClass)) {
					// check if true class is i
					if (trueClass.equalsIgnoreCase(thisClass)) {
						// number of false negative examples for class i (fni)
						// did not predict i, true class is i
						fni++;
						System.out.println("fn " + classPredict + " != " + thisClass + " = " + trueClass);
					} else {
						// number of true negative examples (tni)
						// did not predict i, class is not i
						tni++;
						System.out.println("tn " + classPredict + " != " + thisClass + " != " + thisClass);						
					}
				} // end if: checked for fni and tni cases				
				
			} // end for: all values calculated for this class
			
			// put in array
		}
		
		

		

		
		
		avgAccuracy();
		
		macroPrecision(); 
		
		macroRecall(); 
		
		macroFmeasure(); 
	}
	
	// calculates the average per-class effectiveness of a classifier
	public double avgAccuracy() {
		
		return 0.0;
	}
	
	// calculates an average per-class agreement of the data class labels with those of a classifier 
	public double macroPrecision() {
		
		return 0.0;
	}

	// calculates an average per-class effectiveness of a classifier to identify class labels
	public double macroRecall() {
		
		return 0.0;
	}	
	
	// calculates the relations between data’s positive labels and those given by a classifier 
	// based on a per-class average
	public double macroFmeasure() {
		
		return 0.0;
	}	
	
}
