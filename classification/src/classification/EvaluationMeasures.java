package classification;

public class EvaluationMeasures {

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

	// accuracy
	// takes the value of 0 if the predicted classification equals that of the
	// true class or a 1 if the predicted classification does not match the true
	// class.

	// precision
	// recall
	// f-measure

}
