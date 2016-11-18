package classification;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NaiveBayes extends Algorithm {

    ArrayList trainData;
    //create class into feature
    Feature clas;
    //create arraylist of features
    ArrayList<Feature> features = new ArrayList();
    HashMap<String, Double> classPriors;
    int classesTotal;

    public NaiveBayes(ArrayList data){
        trainData = data;
        clas = new Feature(data.size());
        super.get_logger().log(Level.INFO, "Naive Bayes Algorithm created.");
        train(data);
        classesTotal = trainData.size();
    }

    void train(ArrayList trainData){
        super.get_logger().log(Level.INFO, "Starting training:");

        // get number of features from first data instance
        Object o = trainData.get(0);
        int numFeatures = ((String[]) o).length - 1;
        for (int i = 0; i < numFeatures; i++){
            features.add(new Feature(trainData.size()));
        }

        count(numFeatures);
        HashMap classFrequencies = new HashMap();

        //TODO
        classFrequencies.put("No", 5);
        classFrequencies.put("Yes", 9);

        ArrayList featureProbs = new ArrayList();
        for (Feature f : features) {
            featureProbs.add(f.calculateProbabilities(classFrequencies));
        }
        calculateClassPriors(classFrequencies);  //p(c)

        String[] test = new String[] {"Sunny", "Cool", "High", "Strong"};
        predict(test);
    }

    public void predict(String[] x){

    }
    public void calculateClassPriors(HashMap classFrequencies){
        this.classPriors = new HashMap<>();

        for (Object classKey : classFrequencies.keySet()) {
            double value = (double) (Integer) classFrequencies.get(classKey) /  classesTotal;
            this.classPriors.put((String)classKey, value);
        }

    }

    //iterate through all instances and increment feature counts accordingly
    public void count(int numFeatures){
        for(Object obj: trainData){
            String[] stringArray = (String[]) obj;
            for (int i = 0; i < numFeatures; i++){
                features.get(i).addInstance(stringArray[i], stringArray[stringArray.length - 1]);
            }
            clas.addInstance(stringArray[stringArray.length - 1], "None");
        }
    }


    void test(ArrayList testData){

        super.get_logger().log(Level.INFO, "Starting testing:");

    }





    public static void main(String[] args){

        ArrayList testData = new ArrayList<Arrays>();
        testData.add(new String[]{"Rainy", "Hot", "High", "False", "No"});
        testData.add(new String[]{"Rainy", "Hot", "High", "True", "No"});
        testData.add(new String[]{"Overcast", "Hot", "High", "False", "Yes"});
        testData.add(new String[]{"Sunny", "Mild", "High", "False", "Yes"});
        testData.add(new String[]{"Sunny", "Cool", "Normal", "True", "Yes"});
        testData.add(new String[]{"Sunny", "Cool", "Normal", "True", "No"});
        testData.add(new String[]{"Overcast", "Cool", "Normal", "True", "Yes"});
        testData.add(new String[]{"Rainy", "Mild", "High", "False", "No"});
        testData.add(new String[]{"Rainy", "Cool", "Normal", "False", "Yes"});
        testData.add(new String[]{"Sunny", "Mild", "Normal", "False", "Yes"});
        testData.add(new String[]{"Rainy", "Mild", "Normal", "True", "Yes"});
        testData.add(new String[]{"Overcast", "Mild", "High", "True", "Yes"});
        testData.add(new String[]{"Overcast", "Hot", "Normal", "False", "Yes"});
        testData.add(new String[]{"Sunny", "Mild", "High", "True", "Yes"});
        NaiveBayes b = new NaiveBayes(testData);

    }
}


