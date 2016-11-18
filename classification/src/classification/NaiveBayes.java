package classification;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NaiveBayes extends Algorithm {

    ArrayList trainData;  //Examples used to train the algorithm
    ArrayList<Feature> features = new ArrayList();  //Features used to classify. Each feature has different values.
    HashMap<String, Double> classPriors;    //All P(Class)
    ArrayList<HashMap> likelihoods;         //All P(Features|Class)
    ArrayList<HashMap> predictorPriors;     //All P(Features)
    HashMap<String, Integer> classFrequencies;  // Number of occurrences for each class
    HashMap<String, Double> posteriors = new HashMap<>();  // All Features and their value's posteriors
    int classesTotal;  //Total number of occurrences of all classes

    public NaiveBayes(ArrayList<String[]> data) {
        trainData = data;
        super.get_logger().log(Level.INFO, "Naive Bayes Algorithm created.");
        this.classesTotal = trainData.size();
        this.classFrequencies = new HashMap<>();
        train(data);
        //TODO: test data!! Other than manually I mean.
    }

    /*
    Counts frequencies of all instances, calculate probabilities of all elements of Bayes Theorem, p(x)=predictorPrior
     likelihoods=P(x|c) and classPriors=p(c)
     */
    void train(ArrayList trainData) {
        super.get_logger().log(Level.INFO, "Starting training:");

        // get number of features from first data instance
        Object o = trainData.get(0);
        int numFeatures = ((String[]) o).length - 1;
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
                features.get(i).addInstance(stringArray[i], stringArray[stringArray.length - 1]);
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
        super.get_logger().log(Level.INFO, "Training Results.");
        s = String.format("Total occurrences:%d", this.classesTotal);
        super.get_logger().log(Level.INFO, s);
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
        }

        for (String classKey : this.classFrequencies.keySet()) {
            s = String.format("ClassPrior of %s: %.2f", classKey, this.classPriors.get(classKey));
            super.get_logger().log(Level.INFO, s);
        }

    }

    void test(ArrayList testData) {
        super.get_logger().log(Level.INFO, "Starting testing:");
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
        NaiveBayes b = new NaiveBayes(testData);

    }
}


