package classification;

/*
 *  Measure used in tree-augmented naive Bayes (TAN) algorithm. Conditional mutual information (CMI) helps 
 *  to determines which dependencies to add back into the network (by estimating the amount entropy can be
 *  reduced through the addition of a dependency relationship. 
 *  
 *  Our calculation of CMI comes from equation provided in Sheppard "A Graduate Course in Machine Learning"
 *  course notes (2016).
 */

public class ConditionalMutualInfo {

	private String attI;
	private String attJ;
	private String classVal;

	public ConditionalMutualInfo(String attI, String attJ, String classVal) {
		this.attI = attI;
		this.attJ = attJ;
		this.classVal = classVal;		
	}
	
	

	
	
	
}
