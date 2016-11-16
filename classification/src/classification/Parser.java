package classification;

import java.io.*;
import java.util.*;

/*
 * Parses the data, given the parameters appropriate for the selected dataset. 
 * If necessary, the data undergoes the following preprocessing steps:
 * 1. the class variable is moved to the last position (for the sake of consistency), 
 * 2. idNumbers are stripped from the data (because they do not tell us anything as a feature), 
 * 3. missing values are imputed
 * 4. continuous values are discretized
 * 
 * After preprocessing, the data is sent back to RunModels where it is split into train and test sets 
 * (where the split is random, but we try to keep as close as possible to the underlying class distribution).  
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
	// TODO: REMINDER, for bc remember we need to also remove missing attribute values
	if (dataImputation == true) {
		// TODO: Data Imputer
		// TODO: return dataset di.getImputed data or something?
		DataImputer di = new DataImputer(data);
	}
	
	
	// do data discretization (if necessary)
	if (dataDiscretization == true) {
		// TODO: DataDiscretizer
		int v = 1;
		int r = 12;
		DataDiscretizer dd = new DataDiscretizer(data, v, r);
	}	
	
    //for (String[] arr : data) {
    //    System.out.println(Arrays.toString(arr));
    //}
	
	
	// send data back to Run Models so that it can be used by the algo
	
	
	}

	public ArrayList getData(){
		return this.data;
	}
	
	
	
	//store data in 2d array, where last item in array corresponds to the class
	
	

	

	
}