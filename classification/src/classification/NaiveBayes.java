package classification;

import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class NaiveBayes extends Algorithm {

    ArrayList trainData;  //Examples used to train the algorithm
    ArrayList<String[]> testData;  //Examples used to test the algorithm
    ArrayList<String> predictedClasses;
    ArrayList<Feature> features = new ArrayList();  //Features used to classify. Each feature has different values.
    HashMap<String, Double> classPriors;    //All P(Class)
    ArrayList<HashMap> likelihoods;         //All P(Features|Class)
    ArrayList<HashMap> predictorPriors;     //All P(Features)
    HashMap<String, Integer> classFrequencies;  // Number of occurrences for each class
    HashMap<String, Double> posteriors = new HashMap<>();  // All Features and their value's posteriors
    HashMap<String, Double> featureProbs = new HashMap<>(); // p(x1|x2)
    HashMap<String, Double> togetherness = new HashMap<>(); // occurrences of x1 and x2
    HashMap<String, Double> valOccurances = new HashMap<>();
    Set<String> valNames = new HashSet<String>();
    int classesTotal;  // Total number of occurrences of all classes
    int numFeatures; // Total number of features

    public NaiveBayes(String dataName, ArrayList<String[]> trainData, ArrayList<String[]> testData) {
        super.get_logger().log(Level.INFO, "Naive Bayes Algorithm created.");
        super.get_logger().log(Level.INFO, String.format("Working with %s dataset", dataName));
        this.classesTotal = trainData.size();
        this.classFrequencies = new HashMap<>();
        this.trainData = trainData;
        this.testData = testData;
        train(trainData);
        test(testData);
        results = evaluate();
    }

    public NaiveBayes(ArrayList trainData){
        this.classesTotal = trainData.size();
        this.classFrequencies = new HashMap<>();
        this.trainData = trainData;
        train(trainData);
    }

    /*
    Counts frequencies of all instances, calculate probabilities of all elements of Bayes Theorem, p(x)=predictorPrior
     likelihoods=P(x|c) and classPriors=p(c)
     */
    void train(ArrayList trainData) {
        super.get_logger().log(Level.INFO, "Starting training:");

        // get number of features from first data instance
        Object o = trainData.get(0);
        numFeatures = ((String[]) o).length - 1;
        for (int i = 0; i < numFeatures; i++) {
            features.add(new Feature(trainData.size()));
        }

        // count up all frequencies
        super.get_logger().log(Level.INFO, "Counting all frequencies.");
        count(numFeatures);

        // get likelihoods and predictorPriors for all features
        super.get_logger().log(Level.INFO, "Calculating likelihoods and predictorPriors.");
        likelihoods = new ArrayList();
        predictorPriors = new ArrayList();
        for (Feature f : features) {
            ArrayList<HashMap> featureProbs = f.calculateProbabilities(classFrequencies);
            likelihoods.add(featureProbs.get(0));
            predictorPriors.add(featureProbs.get(1));
        }

        super.get_logger().log(Level.INFO, "Calculating classPriors.");
        //get classPriors
        calculateClassPriors(classFrequencies);  //p(c)
        //log results
        super.get_logger().log(Level.INFO, "Done training");
        printTrainResults();

    }


    // Predict a class given single data instance using Baye's Theorem for each class and choosing the highest
    // posterior
    public String predictSingle(String[] testEx) {
        calculatePosteriors(testEx);

        // Take the max
        String maxKey = "";
        double maxVal = 0.0;
        for (String classKey : this.posteriors.keySet()) {
            if (this.posteriors.get(classKey) > maxVal) {
                maxKey = classKey;
                maxVal = this.posteriors.get(classKey);
            }
        }
        return maxKey;
    }


    // Calculate according to Bayes Theorem
    // p(c|X) = p(x1|c)*p(x2|c)...p(xn|c)*p(c) / p(x1)...p(xn)
    private void calculatePosteriors(String[] testEx) {

        this.posteriors = new HashMap<>();
        double allPredPrior = 1.0; //p(X) = p(x1)*p(x2)...*p(xn)

        for (int i = 0; i < testEx.length; i++) {
            String featureKey = testEx[i];
            HashMap<String, Double> thisFeatureLikelihood = this.likelihoods.get(i);
            HashMap<String, Double> thisPredictorPrior = this.predictorPriors.get(i);

            // multiple all likelihoods, p(x1|c)*p(x2|c)...p(xn|c)
            for (String classKey : this.classPriors.keySet()) {
                String likelihoodKey = featureKey + "|" + classKey;
                String postKey = classKey;
                if (this.posteriors.get(postKey) == null) {
                    this.posteriors.put(postKey, thisFeatureLikelihood.get(likelihoodKey));
                } else {
                    try {
                        double likelihood = thisFeatureLikelihood.get(likelihoodKey);
                        this.posteriors.put(postKey, likelihood * this.posteriors.get(postKey));

                    } catch (Exception e) {
                        //No likelihood for this class
                    }
                }
            }

            //multiple all predictors
            allPredPrior *= thisPredictorPrior.get(featureKey);
        }

        //multiply by classPriors then divide by all predictorPriors
        for (String classKey : this.classPriors.keySet()) {
            this.posteriors.put(classKey, this.posteriors.get(classKey) * this.classPriors.get(classKey));
            this.posteriors.put(classKey, this.posteriors.get(classKey) / allPredPrior);
        }


    }

    // Use frequencies to calculate classPrior p(c)
    public void calculateClassPriors(HashMap classFrequencies) {
        this.classPriors = new HashMap<>();

        for (Object classKey : classFrequencies.keySet()) {
            double value = (double) (Integer) classFrequencies.get(classKey) / this.classesTotal;
            this.classPriors.put((String) classKey, value);
        }

    }

    //iterate through all instances and increment feature and class counts accordingly
    public void count(int numFeatures) {
        for (Object obj : trainData) {
            String[] stringArray = (String[]) obj;
            for (int i = 0; i < numFeatures; i++) {
                String val = stringArray[i];
                valNames.add(val);
                features.get(i).addInstance(val, stringArray[stringArray.length - 1]);

                //All occurances of this value
                if (valOccurances.containsKey(val)) {
                    valOccurances.put(val, valOccurances.get(val) + 1.0);
                } else {
                    valOccurances.put(val, 1.0);
                }
                // all occurences of these two features appearing together
                for (int j = i +1; j < numFeatures; j++){
                    String secondFeature = stringArray[j];
                    String featureKey = val + "," +secondFeature;
                    if (togetherness.containsKey(featureKey)){
                        togetherness.put(featureKey, togetherness.get(featureKey) + 1.0);
                    }
                    else{
                        togetherness.put(featureKey, 1.0);
                    }
                }
            }
            try {
                int value = this.classFrequencies.get(stringArray[stringArray.length - 1]);
                this.classFrequencies.put(stringArray[stringArray.length - 1], value + 1);
            } catch (Exception e) {
                this.classFrequencies.put(stringArray[stringArray.length - 1], 1);
            }
        }
    }

    //Log results of calculations and countings
    public void printTrainResults() {
        String s;
        super.get_logger().log(Level.INFO, "");
        super.get_logger().log(Level.INFO, "Training Results.");
        s = String.format("Size of testing set: %d instances, %d attributes", this.classesTotal, this.numFeatures);
        super.get_logger().log(Level.INFO, s);
        super.get_logger().log(Level.INFO, "");

        int i = 0;
        for (Feature f : this.features) {
            s = String.format("Feature %s with values of: ", i);
            ArrayList<String> vals = new ArrayList();
            for (String varKey : f.vars.keySet()) {
                s += varKey + " ";
                vals.add(varKey);
            }
            super.get_logger().log(Level.INFO, s);

            for (String valKey : vals) {
                s = String.format("PredictorPrior of %s: %.2f", valKey, this.predictorPriors.get(i).get(valKey));
                super.get_logger().log(Level.INFO, s);

                for (String classKey : this.classFrequencies.keySet()) {
                    s = String.format("Likelihood of %s given %s: %.2f", valKey, classKey, this.likelihoods.get(i).get(valKey + "|" + classKey));
                    super.get_logger().log(Level.INFO, s);
                }
            }
            i++;
            super.get_logger().log(Level.INFO, "");
        }

        super.get_logger().log(Level.INFO, "");
        for (String classKey : this.classFrequencies.keySet()) {
            s = String.format("ClassPrior of %s: %.2f", classKey, this.classPriors.get(classKey));
            super.get_logger().log(Level.INFO, s);
        }

    }

    void test(ArrayList<String[]> testData) {
        super.get_logger().log(Level.INFO, "");
        super.get_logger().log(Level.INFO, "Starting testing:");
        predictedClasses = new ArrayList<>();
        for (Object obj : testData){
            String[] oldArray = (String[]) obj;
            String[] newArray = Arrays.copyOfRange(oldArray, 0, oldArray.length -1);
            String clas = predictSingle(newArray);
            predictedClasses.add(clas);
            super.get_logger().log(Level.INFO, String.format("Given features %s: predicted class is %s", Arrays.toString(newArray), clas));
        }
        super.get_logger().log(Level.INFO, "Done testing");
    }

    public ArrayList<Double> evaluate() {

        // determine classification accuracy, required information - the number of classes for this
        // dataset, the list of class labels (ArrayList String) as determined by the classifier, and the
        // testData set (ArrayList String[]) that includes the true class labels.
        super.get_logger().log(Level.INFO, "");
        super.get_logger().log(Level.INFO, "Starting evaluation.");

        // after all test set instances have been classified, evaluate the performance of classifier
        EvaluationMeasures em = new EvaluationMeasures(this.classFrequencies.size(), predictedClasses, testData);
        ArrayList<Double> evaluationResults = em.evaluateData();

        double accuracy = evaluationResults.get(0);
        double precision = evaluationResults.get(1);
        double recall = evaluationResults.get(2);
        double fScore = evaluationResults.get(3);


        super.get_logger().log(Level.INFO, "######################################");
        super.get_logger().log(Level.INFO, "RESULTS");
        super.get_logger().log(Level.INFO, this.classFrequencies.size() + " class classification problem");
        super.get_logger().log(Level.INFO, "Results for this fold:");
        super.get_logger().log(Level.INFO, "Average Accuracy: " + accuracy);
        super.get_logger().log(Level.INFO, "Macro Precision: " + precision);
        super.get_logger().log(Level.INFO, "Macro Precision: " + recall);
        super.get_logger().log(Level.INFO, "Macro Score: " + recall);
        super.get_logger().log(Level.INFO, "######################################");

        return evaluationResults;
    }


    public static void main(String[] args) {

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
        NaiveBayes b = new NaiveBayes("DummyData", testData, null);

    }
}


