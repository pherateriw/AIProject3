package classification;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class RunModels {
	private static ArrayList<String[]> data = new ArrayList<String[]>();
	private static ArrayList<String[]> test1 = new ArrayList<String[]>();	
	private static ArrayList<String[]> train1 = new ArrayList<String[]>();	
	private static ArrayList<String[]> test2 = new ArrayList<String[]>();	
	private static ArrayList<String[]> train2 = new ArrayList<String[]>();	
	private static ArrayList<String[]> test3 = new ArrayList<String[]>();	
	private static ArrayList<String[]> train3 = new ArrayList<String[]>();
	private static ArrayList<String[]> test4 = new ArrayList<String[]>();	
	private static ArrayList<String[]> train4 = new ArrayList<String[]>();	
	private static ArrayList<String[]> test5 = new ArrayList<String[]>();	
	private static ArrayList<String[]> train5 = new ArrayList<String[]>();	
	
	public static void main(String[] args) throws IOException {
		// Scanner for reading in the user's choice
		Scanner in = new Scanner(System.in);
    	String choice = "";

		// get file location for training/test data    	
    	String dataFileLocation = getData();
    	
    	// gets short name of dataset (from file location), for use in printing information
    	String shortName = "";
    	shortName = dataFileLocation.substring(dataFileLocation.lastIndexOf(File.separator) + 1, dataFileLocation.indexOf('.'));

    	// an array list to hold the necessary parser arguments
    	ArrayList<String> parserArgs = new ArrayList<String>();
    	    	
    	// get the appropriate parser arguments
    	getParserArgs(shortName, parserArgs);
    	
    	// converts the variables back from Strings to the appropriate type
    	int classVariableLoc = Integer.parseInt(parserArgs.get(0)); 
    	boolean dataImputation = Boolean.parseBoolean(parserArgs.get(1));
    	boolean dataDiscretization = Boolean.parseBoolean(parserArgs.get(2));
    	int idNumLoc = Integer.parseInt(parserArgs.get(3)); 
    	boolean dataRemoval = Boolean.parseBoolean(parserArgs.get(4));
    	
    	// parse file, doing data imputation and discretization as necessary 
    	Parser p = new Parser(dataFileLocation, classVariableLoc, dataImputation, dataDiscretization, idNumLoc, dataRemoval);    	
    	data = p.getData();
    	
    	// splits the data into 5 repetitions of 2 folds, for 5 x 2 cross validation
    	DataSplitter ds = new DataSplitter(data);
    	ds.splitData(1);
    	train1 = ds.getTrainingData();
    	test1 = ds.getTestingData();
    	
    	ds.splitData(2);     	
    	train2 = ds.getTrainingData();
    	test2 = ds.getTestingData();
    	
    	ds.splitData(3);
    	train3 = ds.getTrainingData();
    	test3 = ds.getTestingData();
    	
    	ds.splitData(4);
    	train4 = ds.getTrainingData();
    	test4 = ds.getTestingData();    	
    	
    	ds.splitData(5);
    	train5 = ds.getTrainingData();
    	test5 = ds.getTestingData();
    	
    	    		


    
    	//gives the user a series of choices
    	System.out.println("Please pick from one of the following options");
    	System.out.println("To classify " + shortName + " data using k-Nearest Neighbor type 'knn'");
    	System.out.println("To classify " + shortName + " data using naive Bayes type 'nb'");
    	System.out.println("To classify " + shortName + " data using tree-augmented naive Bayes type 'tan'");
    	System.out.println("To classify " + shortName + " data using Iterative Dichotomiser 3 type 'id3'");
    	System.out.println("Type 'x' to exit");

    	// holds the user's choice of algorithm
    	choice = in.nextLine();

    	
    	//TODO: return measures and average over all folds 
    	
    	if (choice.equals("knn")) {
    		System.out.println("Classifying data using k-Nearest neighbor"); 
    		// Fold 1
    		// train on S1, test on S2
    		// test on S1, train on S2
    		// repeat 5 times, averaging over all folds
    		//TODO: tune k
    		int k = 7; 
    		
    		Algorithm knn = new KNearestNeighbor(shortName, train1, test1, k);
    		
    		// split data, train on B and test on A
    		
    	} else if (choice.equals("nb")) {	
    		System.out.println("Classifying data using naive Bayes"); 
    		//Algorithm nb = new NaiveBayes();                        
    	} else if (choice.equals("tan")){
    		System.out.println("Classifying data using tree-augmented naive Bayes"); 
    		Algorithm tan = new TreeAugNB(); 
    	} else if (choice.equals("id3")) {	
    		System.out.println("Classifying data using the Iterative Dichotomiser 3"); 
    		Algorithm id3 = new DecisionTree();    		
    	} else {
    		// user chose to exit the program or typed their choice incorrectly
    		System.out.println("Exiting program.");
    		in.close();
    		System.exit(0);
    	}
		
		// closes the scanner 
		in.close();
    }


	// will need to add param for bc, where we remove instances with '?'
	public static void getParserArgs(String shortName, ArrayList<String> parserArgs) {
    	// variables for parser, because particulars of dataset vary
		System.out.println(shortName);
    	int classVariableLoc = Integer.MAX_VALUE;
    	boolean dataImputation = false;
    	boolean dataDiscretization = false;
    	int idNumLoc = Integer.MAX_VALUE;
    	boolean removeMissingVals = false;
    	
    	// the following series of if statements picks the appropriate parser arguments, based on the dataset
    	// note, a value of -1 for idNumLoc means that there is no id value in that dataset
    	if (shortName.equals("breast-cancer-wisconsin")){
    		classVariableLoc = 10;
    		dataImputation = true;
    		// NOTE: will need to also do one without discretization, where we just remove missing vals (complete
    		// case analysis) - when we do that, leave data imputation as true but set removeMissingVals to true
    		dataDiscretization = false;
    		idNumLoc = 0;
    		removeMissingVals = false;
    	} else if (shortName.equals("glass")) {
    		classVariableLoc = 10;
    		dataImputation = false;
    		dataDiscretization = true;
    		idNumLoc = 0;
    		removeMissingVals = false;
    	} else if (shortName.equals("house-votes-84")) {
    		classVariableLoc = 0;
    		dataImputation = true;
    		// NOTE: will need to also do one without discretization, where we just treat '?' as a third value
    		dataDiscretization = false;
    		removeMissingVals = false;
    		idNumLoc = Integer.MAX_VALUE;
    	} else if (shortName.equals("iris")) {
    		classVariableLoc = 4;
    		dataImputation = false;
    		dataDiscretization = true;    
    		idNumLoc = Integer.MAX_VALUE;
    		removeMissingVals = false;
    	} else if (shortName.equals("soybean-small")) {
    		classVariableLoc = 35;
    		dataImputation = false;
    		dataDiscretization = false;    
    		idNumLoc = Integer.MAX_VALUE;
    		removeMissingVals = false;
    	} else {
    		System.out.println("You have not picked a valid dataset. Please re-pick or add the appropriate dataset.");
    	}
    	    	
    	// add these to the array list (as strings), will cast back accordingly before calling parser
    	parserArgs.add(Integer.toString(classVariableLoc));
    	parserArgs.add(String.valueOf(dataImputation));
    	parserArgs.add(String.valueOf(dataDiscretization));
    	parserArgs.add(Integer.toString(idNumLoc));
    	parserArgs.add(String.valueOf(removeMissingVals));
    }
	
	
    // gets the location of the data for classification (training and test sets are built in the parser)
    public static String getData() {
    	// gets the os for the computer this program is run on
        String os = System.getProperty("os.name").toLowerCase();
        
        File file = new File(".");
        
        // get the location where the data files are stored
        String filePathData = file.getAbsolutePath();
        
        // uses file separator so is operating system agnostic
        if (os.startsWith("windows")) { // Windows
            filePathData += File.separator + "data" + File.separator;
        } else if (os.startsWith("mac")) { // Mac
            filePathData += File.separator + "data" + File.separator;
        } else {
            // everything else
            filePathData += File.separator + "data" + File.separator;
        }

        // calls the file chooser, returns the updated file path
        System.out.println("Select Classification Data Location");
        filePathData = callFileChooser(filePathData);
        System.out.println("Data Location: " + filePathData);
        System.out.println();
        return filePathData;
    }
	
	
    //calls a window with a pop up box that lets the user choose their exact
    //file location (with input from file string that gives user's home directory.
    public static String callFileChooser(String filePath) {
        // builds a JFrame
        JFrame frame = new JFrame("Folder Selection Pane");
        // string to score the path
        String thisPath = "";

        // JFrame look and feel
        frame.setPreferredSize(new Dimension(400, 200));
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton button = new JButton("Select Folder");

        // sets up the file chooser
        JFileChooser fileChooser = new JFileChooser();
        // uses file path as a starting point for file browsing
        fileChooser.setCurrentDirectory(new File(filePath));
        // choose only from directories
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int fileChosen = fileChooser.showOpenDialog(null);

        // returns either the file path, or nothing (based on user choice)
        if (fileChosen == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            thisPath = selectedFile.getAbsolutePath();
            return thisPath;
        } else {
            return null;
        }
    }
}



