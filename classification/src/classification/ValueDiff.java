package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * Calculates the value difference metric (VDM), as described by Stanfill and Walz in "Toward Memory
 * Based Reasoning" (1986). VDM was designed as a distance function specifically for nominal attributes, 
 * and the advantage of VDM is that it was developed to measure the difference between predictive features 
 * by taking the similarity (or dissimilarity) of values into account. 
 * 
 * Using VDM, two values are "closer" if they have more similar classifications. Note: this does not depend
 * on order in which values were given. 
 */
public class ValueDiff {

	private String[] queryPoint;
	private ArrayList<String[]> trainData;
	Map<Integer, Double> qpNeighbors = new HashMap<Integer, Double>();
	
	
	// hold the classes, as gathered from the training data
	ArrayList<String> classVals = new ArrayList<String>();

	public ValueDiff(String[] queryPoint, ArrayList<String[]> trainData) {
		this.queryPoint = queryPoint;
		this.trainData = trainData;
		calcVD();
	}

	public Map<Integer, Double> calcVD() {
		// get the number of attributes in the data
		int numAttributes = trainData.get(0).length - 1;

		// get number of classes in problem domain
		for (int inst = 0; inst < trainData.size(); inst++) {
			if (!classVals.contains(trainData.get(inst)[numAttributes])) {
				classVals.add(trainData.get(inst)[numAttributes]);
			}
		}

		// key is index of neighbor val, value is the distances between the
		// neighbor and the query point


		// go through each training example in the training data
		for (int te = 0; te < trainData.size(); te++) {

			// gives us the neighbor to compare the query point against
			String[] neighbor = Arrays.copyOfRange(trainData.get(te), 0,
					numAttributes);

			// calculate the VDM between query and neighbor for each attribute
			double totalForNeighbor = 0.0;

			// compare each of the attributes in the query point and the
			// neighbor
			for (int attribute = 0; attribute < neighbor.length; attribute++) {
				String trainAtt = neighbor[attribute];
				String queryAtt = queryPoint[attribute];

				double distThisAtt = calcThisAtt(trainAtt, queryAtt,
						numAttributes, classVals, attribute);
				totalForNeighbor += distThisAtt;
			}

			qpNeighbors.put(te, totalForNeighbor);

		}

		return qpNeighbors;
	}

	public double calcThisAtt(String x, String y, int numAttributes,
			ArrayList classVals, int attLoc) {
		double distance = 0.0;

		// number of instances in the data set
		int numDSInst = trainData.size();

		int classValLoc = trainData.get(0).length - 1;

		// get the number of classes in the data
		int numClasses = classVals.size();

		/*
		 * vdmCompByClass holds the intermediate values used to calculate VDM,
		 * where each int[] corresponds to vals for that class size. order of
		 * array is as follows: [0]: xinDSCount, [1]:yinDSCount, [2]:
		 * xByClassCount, [3]: yByClassCount
		 */
		ArrayList<int[]> vdmCompByClass = new ArrayList<int[]>();

		// records the number of entries in the training data that have an
		// attribute value that matches x
		int xinDSCount = 0;
		// records the number of entries in the training data that have
		// attribute value x and class c
		int xByClassCount = 0;

		// records the number of entries in the training data that have an
		// attribute value that matches y
		int yinDSCount = 0;
		// records the number of entries in the training data that have
		// attribute value y and class c
		int yByClassCount = 0;

		// goes through each of the classes in the training data
		for (int c = 0; c < numClasses; c++) {
			xByClassCount = 0;
			yByClassCount = 0;

			for (int i = 0; i < numDSInst; i++) {
				// calculate the number of times attribute x occurs
				if (x.equalsIgnoreCase(trainData.get(i)[attLoc])) {
					xinDSCount++;
					// calculate number of times x occurs with the given class
					if (trainData.get(i)[classValLoc]
							.equalsIgnoreCase(classVals.get(c).toString())) {
						xByClassCount++;
					}

				} // end if: x val counting

				// calculate the number of times attribute y occurs
				if (y.equalsIgnoreCase(trainData.get(i)[attLoc])) {
					yinDSCount++;
					// calculate number of times y occurs with the given class
					if (trainData.get(i)[classValLoc]
							.equalsIgnoreCase(classVals.get(c).toString())) {
						yByClassCount++;
					}
				} // end if: y val counting

			} // end for: have looped through entire training data

			// add to array, reset everything
			int[] calcsForClass = { xinDSCount, yinDSCount, xByClassCount,
					yByClassCount };
			vdmCompByClass.add(calcsForClass);
			xinDSCount = 0;
			yinDSCount = 0;
		} // end for: have looped through all classes

		// now calculate the value difference metric
		// per Wilson
		// "Value Difference Metrics for Continuously Valued Attributes" 1996,
		// this is usually 1 or 2
		int q = 1;
		double vdm = 0.0;

		// goes through each class
		for (int thisClass = 0; thisClass < vdmCompByClass.size(); thisClass++) {
			// gets values, as counted earlier
			int countX = vdmCompByClass.get(thisClass)[0];
			int countY = vdmCompByClass.get(thisClass)[1];
			int countXClass = vdmCompByClass.get(thisClass)[2];
			int countYClass = vdmCompByClass.get(thisClass)[3];

			// calculates N_a,x,c/N_a,x
			double xRatio = (double) countXClass / countX;
			// calculates N_a,y,c/N_a,y
			double yRatio = (double) countYClass / countY;

			// takes the absolute value of the difference between x and y for
			// this attribute
			double diffXandY = Math.abs(xRatio - yRatio);

			diffXandY = Math.pow(diffXandY, q);
			vdm += diffXandY;
		}

		// returns the vdm for this pair of attributes
		return vdm;
	}

}
