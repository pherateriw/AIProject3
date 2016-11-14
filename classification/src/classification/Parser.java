package classification;

import java.io.*;
import java.util.*;

/*
 * Parses the data, given the parameters from   
 */

public class Parser {
	
	private String dataFileLocation;
	private int classVariableLoc;
	private boolean dataImputation;
	private boolean dataDiscretization;
	private int idNumLoc;
	private ArrayList<String[]> data = new ArrayList<String[]>();
	
	public Parser(String dataFileLocation, int classVariableLoc, boolean dataImputation, boolean dataDiscretization, int idNumLoc) throws IOException {
		this.dataFileLocation = dataFileLocation;	
		this.classVariableLoc = classVariableLoc;
		this.dataImputation = dataImputation;
		this.dataDiscretization = dataDiscretization;
		this.idNumLoc = idNumLoc;
		
	// read in data
	String currentLine = null;
	
	try {
		BufferedReader br = new BufferedReader(new FileReader(new File (dataFileLocation)));
		
		while ((currentLine = br.readLine()) != null) {
			// if there is an idNumber, ignore it
			// NOTE: for these datasets, all id numbers are in the first position
			if (idNumLoc < Integer.MAX_VALUE) {
				currentLine = currentLine.substring(currentLine.indexOf(',') + 1,currentLine.length());		
			} 
							
			// if the classVariable is not at the end of the line, put it there
			// NOTE: for these datasets, the class variable is either in the first or last position
			if (classVariableLoc == 0) {
				String classV = currentLine.substring(0, currentLine.indexOf(','));
				String features = currentLine.substring(currentLine.indexOf(',') + 1, currentLine.length()); 
				
				// Frankenstein the line back together with the class variable in the last location
				currentLine = features + "," + classV;
			}
							
			// build data array
			String[] splitArray = currentLine.split(",");
			data.add(splitArray);
		}
				
	} catch(Exception e) {
		e.printStackTrace();
	}
		
	// do data imputation (if necessary)
	if (dataImputation == true) {
		// TODO: Data Imputer
		DataImputer di = new DataImputer();
	}
	
	
	// do data discretization (if necessary)
	if (dataDiscretization == true) {
		// TODO: DataDiscretizer
		DataDiscretizer dd = new DataDiscretizer();
	}	
	
	
    for (String[] arr : data) {
        System.out.println(Arrays.toString(arr));
    }
	
	
	
	
	// split data into train/test set trying to keep distributions equal
	
	// 5 x 2 cross validation - what do we need to do here?
	
	// send data back to Run Models so that it can be used by the algo
	
	
	}
	
	
	
	//store data in 2d array, where last item in array corresponds to the class
	
	

	

	
}