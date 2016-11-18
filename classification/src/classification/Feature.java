package classification;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by lisapeters on 11/15/16.
 * Example: Outlook feature with values Sunny:(yes,3)(no,2), Overcast:(yes,4)(no,0), Rainy:(yes,2)(no,3)
 */
public class Feature {
    HashMap<String, HashMap> vars; //All vars across all classes
    HashMap<String, Integer> varTotals; //individual total for a var across all classes
    HashMap<String, Double> likelihoods; //p(x|c) or p(sunny|yes)
    HashMap<String, Double> predictorPriors; // p(x)
    int classestotal;  //Total of all instances in training set

    public Feature(int classestotal) {
        vars = new HashMap<String, HashMap>();
        varTotals = new HashMap<>();
        this.classestotal = classestotal;
    }

    //Add an instance of a feature and update the count of its value across classes as well as it's total count
    public void addInstance(String name, String clas) {
        HashMap<String, Integer> value = vars.get(name);
        if (value != null) {
            Integer num = value.get(clas);
            if (num != null) {
                value.put(clas, num + 1);
            } else {
                value.put(clas, 1);
            }
        } else {
            HashMap clasFreqsForVal = new HashMap<String, Integer>();
            clasFreqsForVal.put(clas, 1);
            vars.put(name, clasFreqsForVal);
        }
    }

    // Calculate likelihoods and predictorPriors for all values of feature
    public ArrayList calculateProbabilities(HashMap classFrequencies) {
        ArrayList allProbs = new ArrayList();
        calculateValTotals();

        calculateLikelihoods(classFrequencies);
        allProbs.add(this.likelihoods);
        calculatePredictorPriors();
        allProbs.add(this.predictorPriors);

        return allProbs;
    }

    //p(x)
    public void calculatePredictorPriors() {
        predictorPriors = new HashMap<>();
        for (String varKey : this.varTotals.keySet()) {
            double value = (double) this.varTotals.get(varKey) / this.classestotal;
            predictorPriors.put(varKey, value);
        }
    }

    //p(x|c)
    public void calculateLikelihoods(HashMap classFrequencies) {
        this.likelihoods = new HashMap();
        for (String varKey : this.vars.keySet()) {
            HashMap varFreqsInClasses = this.vars.get(varKey);
            for (Object classKey : varFreqsInClasses.keySet()) {
                String likelihoodKey = varKey + "|" + classKey;
                double likelihood = (double) (Integer) varFreqsInClasses.get(classKey) / (Integer) classFrequencies.get(classKey);
                this.likelihoods.put(likelihoodKey, likelihood);
            }
        }
    }

    // return total number of all vals across all classes
    // and init individuals total for a val across all classes
    public void calculateValTotals() {
        int total = 0;
        for (String key : this.vars.keySet()) {
            int temp = 0;
            for (Object value : this.vars.get(key).values()) {
                temp += (Integer) value;
            }
            this.varTotals.put(key, temp);
            total += temp;
        }
    }

}

