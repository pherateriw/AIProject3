package classification;

import java.util.ArrayList;

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
 * 4. Fscore with macro averaging 
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
	// order -- 0: tpi, 1: fpi, 2: fni, 3: tni  
	private ArrayList<Integer[]> results = new ArrayList<Integer[]>();
	
	// returns average accuracy, precision with macro averaging, recall with macro averaging 
	// and fscore with macro averaging
	private ArrayList<Double> evalResults = new ArrayList<Double>();		
	
	public EvaluationMeasures(int numClasses, ArrayList<String> classLabels, ArrayList<String[]> testData) {
		this.numClasses = numClasses;
		this.predictedCLs = classLabels;
		this.testData = testData;
	}

	public ArrayList<Double> evaluateData() {
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
						//System.out.println("tp " + classPredict + " = " + thisClass + " = " + trueClass);
					} else {
						// number of false positive examples for class i (fpi)
						// predicted class i, true class is not i
						fpi++;
						//System.out.println("fp " + classPredict + " = " + thisClass + " != " + thisClass);						
					}
				} // end if: checked for tpi and fpi cases
				
				// check if the predicted class is not i (to determine fni and tni values)
				if (!classPredict.equalsIgnoreCase(thisClass)) {
					// check if true class is i
					if (trueClass.equalsIgnoreCase(thisClass)) {
						// number of false negative examples for class i (fni)
						// did not predict i, true class is i
						fni++;
						//System.out.println("fn " + classPredict + " != " + thisClass + " = " + trueClass);
					} else {
						// number of true negative examples (tni)
						// did not predict i, class is not i
						tni++;
						//System.out.println("tn " + classPredict + " != " + thisClass + " != " + thisClass);						
					}
				} // end if: checked for fni and tni cases				
				
			} // end for: all values calculated for this class
			
			// put calculated values (for this class) in array
//			System.out.println(tpi + "," + fpi + "," + fni + "," + tni);
			Integer[] resultsArray = {tpi, fpi, fni, tni};
			
			results.add(resultsArray);
		}
		
		double accuracy = avgAccuracy();
		
		double precision = macroPrecision(); 
		
		double recall = macroRecall(); 
		
		// following convention (mentioned in class and in article), we set beta = 1
		int beta = 1; 
		double fScore = macroFscore(beta, precision, recall); 
		
		evalResults.add(accuracy);
		evalResults.add(precision);		
		evalResults.add(recall);
		evalResults.add(fScore);	
		
		//System.out.println(accuracy + "," + precision + "," + recall + "," + fScore);
		
		// return 
		return evalResults;
	}
	
	// calculates the average per-class effectiveness of a classifier
	// got equation from Sokolova and Lapalme Table 3, pg 430
	// closer number is to number of classes, more effective classifier is
	public double avgAccuracy() {
		double accuracy;
		double intermedSum = 0.0;
		
		// sum over all classes in data set
		for (int i = 0; i < numClasses; i++) {
			int tpi = results.get(i)[0];
			int fpi = results.get(i)[1];
			int fni = results.get(i)[2];
			int tni = results.get(i)[3];
			
			//System.out.println(tpi + "," + fpi + "," + fni + "," + tni);	
			
			int numer = tpi + tni;
			int denom = tpi + fni + fpi + tni;
			
//			System.out.println(numer);
//			System.out.println(denom);			
			
			double ratio = (double) numer / denom;
			
//			System.out.println(acc);
			intermedSum += ratio;
		}
		
		accuracy = intermedSum;
		//System.out.println(accuracy);
		
		return accuracy;
	}
	
	// calculates an average per-class agreement of the data class labels with those of a classifier 
	// got equation from Sokolova and Lapalme Table 3, pg 430
	// a value closer to one indicates a higher per-class agreement between labels and classifier
	public double macroPrecision() {
		double precision; 
		double itermedSum = 0.0; 
		
		// sum over all classes in data set
		for (int i = 0; i < numClasses; i++) {
			int tpi = results.get(i)[0];
			int fpi = results.get(i)[1];
			
//			System.out.println(tpi + "," + fpi);		
			
			int numer = tpi;
			double denom = tpi + fpi;
			
			// no true positives or false positives (only true negatives or false negatives)
			if (denom == 0) {
				// small non-zero value to prevent NaN
				denom = 0.00001;
			}
			
			double ratio = numer / denom;
			
			itermedSum += ratio;
		}
		
		precision = itermedSum/numClasses;
		//System.out.println(precision);
		
		return precision;
	}

	// calculates an average per-class effectiveness of a classifier to identify class labels
	// got equation from Sokolova and Lapalme Table 3, pg 430
	public double macroRecall() {
		double recall; 
		double itermedSum = 0.0; 
		
		// sum over all classes in data set
		for (int i = 0; i < numClasses; i++) {
			int tpi = results.get(i)[0];
			int fni = results.get(i)[2];
			
			//System.out.println(tpi + "," + fni);		
			
			int numer = tpi;
			double denom = tpi + fni;
			
			// no true positives or false negatives (only true negatives or false positives)
			if (denom == 0) {
				// small non-zero value to prevent NaN
				denom = 0.00001;
			}
			
			double ratio = numer / denom;
			
			itermedSum += ratio;
		}
		
		recall = itermedSum/numClasses;
		//System.out.println(recall);
		
		return recall;
	}	
	
	// calculates the relations between data’s positive labels and those given by a classifier 
	// based on a per-class average
	// got equation from Sokolova and Lapalme Table 3, pg 430
	public double macroFscore(int beta, double precision, double recall) {
		double fScore;
		
//		System.out.println(precision);
//		System.out.println(recall);
		
		double betaPower = Math.pow(beta, 2);
		
		double numer = (betaPower + 1) * precision * recall;
		double denom = (betaPower) * precision + recall;
		
		fScore = numer/denom;
		
//		System.out.println(fScore);
		
		return fScore;
	}	
	
}
