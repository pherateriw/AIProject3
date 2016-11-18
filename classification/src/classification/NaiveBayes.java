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
    ArrayList<HashMap> likelihoods;
    ArrayList<HashMap> predictorPriors;
    HashMap<String, Double> posteriors = new HashMap<>();
    int classesTotal;

    public NaiveBayes(ArrayList<String[]> data){
        trainData = data;
        clas = new Feature(data.size());
        super.get_logger().log(Level.INFO, "Naive Bayes Algorithm created.");
        this.classesTotal = trainData.size();
        train(data);
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

        likelihoods = new ArrayList();
        predictorPriors = new ArrayList();
        for (Feature f : features) {
            ArrayList<HashMap> featureProbs = f.calculateProbabilities(classFrequencies);
            likelihoods.add(featureProbs.get(0));
            predictorPriors.add(featureProbs.get(1));
        }
        calculateClassPriors(classFrequencies);  //p(c)

        String[] test = new String[] {"Sunny", "Cool", "High", "Strong"};
        predict(test);
    }

    public void predict(String[] testEx){
        calculatePosteriors(testEx);
    }

    private void calculatePosteriors(String[] testEx){
        // bayes thoerem
        // p(c|X) = p(x1|c)*p(x2|c)...p(xn|c)*p(c) / p(x1)...p(xn)
        this.posteriors = new HashMap<>();


        double allPredPrior = 1.0;
        for (int i = 0; i  < testEx.length; i++){
            String featureKey = testEx[i];
            HashMap<String,Double> featureLikelihood = this.likelihoods.get(i);
            HashMap<String, Double> predictorPrior = this.predictorPriors.get(i);

            // multiple all likelihoods
            for (String classKey : this.classPriors.keySet()){
                String likelihoodKey = featureKey + "|" + classKey;
                String postKey = classKey;
                if (this.posteriors.get(postKey) == null){
                    this.posteriors.put(postKey, featureLikelihood.get(likelihoodKey));
                }
                else{
                    try{
                    double likelihood = featureLikelihood.get(likelihoodKey);
                        this.posteriors.put(postKey, likelihood * this.posteriors.get(postKey));

                    }
                    catch (Exception e){
                        //zero frequency problem
                    }
                }
            }

            //multiple all predictors
            allPredPrior *= predictorPrior.get(featureKey);
        }
        System.out.println();

        //multiply by classPriors then divide by all predictor priors
        for (String classKey : this.classPriors.keySet()) {
            this.posteriors.put(classKey, this.posteriors.get(classKey) * this.classPriors.get(classKey));
            this.posteriors.put(classKey, this.posteriors.get(classKey)/allPredPrior);
        }



    }

    public void calculateClassPriors(HashMap classFrequencies){
        this.classPriors = new HashMap<>();

        for (Object classKey : classFrequencies.keySet()) {
            double value = (double) (Integer) classFrequencies.get(classKey) /  this.classesTotal;
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
        testData.add(new String[]{"Rainy", "Mild", "High", "Weak", "Yes"});
        testData.add(new String[]{"Rainy", "Cool", "Normal", "Weak", "Yes"});
        testData.add(new String[]{"Overcast", "Hot", "High", "Weak", "Yes"});
        testData.add(new String[]{"Sunny", "Hot", "High", "Weak", "No"});
        testData.add(new String[]{"Sunny", "Hot", "High", "Strong", "No"});
        testData.add(new String[]{"Sunny", "Mild", "High", "Weak", "No"});
        testData.add(new String[]{"Overcast", "Cool", "Normal", "Strong", "Yes"});
        testData.add(new String[]{"Rainy", "Cool", "Normal", "Strong", "No"});
        testData.add(new String[]{"Rainy", "Mild", "Normal", "Weak", "Yes"});
        testData.add(new String[]{"Sunny", "Cool", "Normal", "Weak", "Yes"});
        testData.add(new String[]{"Rainy", "Mild", "High", "Strong", "No"});
        testData.add(new String[]{"Overcast", "Mild", "High", "Strong", "Yes"});
        testData.add(new String[]{"Overcast", "Hot", "Normal", "Weak", "Yes"});
        testData.add(new String[]{"Sunny", "Mild", "Normal", "Strong", "Yes"});
        NaiveBayes b = new NaiveBayes(testData);

    }
}


