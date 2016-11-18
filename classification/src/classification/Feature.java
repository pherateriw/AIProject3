package classification;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by lisapeters on 11/15/16.
 * Example: Outlook feature with values Sunny:(yes,3)(no,2), Overcast:(yes,4)(no,0), Rainy:(yes,2)(no,3)
 */
public class Feature {
    HashMap<String, HashMap> vars; //All vars across all classes
    HashMap<String, Integer> varTotals; //individual total for a var across all classes
    HashMap<String, Double> likelihoods; //p(x|c) or p(sunny|yes)
    HashMap<String, Double> predictorPriors; // p(x)
    HashMap<String, Double> posteriors; // p(c|x)
    int classestotal;  //Total of all instances

    public Feature(int classestotal){
        vars = new HashMap<String, HashMap>();
        varTotals = new HashMap<>();
        this.classestotal = classestotal;
    }

    public void addInstance(String name, String clas){

        HashMap<String, Integer> value = vars.get(name);
        if (value != null) {
            System.out.println();
            Integer num = value.get(clas);
            if (num != null){
                value.put(clas, num + 1);
            }
            else{
                value.put(clas, 1);
            }
        } else {
            HashMap m = new HashMap<String, Integer>();
            m.put(clas, 1);
            vars.put(name, m);

        }
    }

    public ArrayList calculateProbabilities(HashMap classFrequencies){
        ArrayList allProbs = new ArrayList();
        getVarTotal();

        calculateLikelihoods(classFrequencies); //p(x|c)
        allProbs.add(this.likelihoods);
        calculatePredictorPriors(); //p(x)
        allProbs.add(this.predictorPriors);

        return allProbs;
        }



    public  void calculatePredictorPriors(){
        predictorPriors = new HashMap<>();
        for (String varKey : this.varTotals.keySet()){
            double value = (double) this.varTotals.get(varKey) / this.classestotal;
            predictorPriors.put(varKey, value);
        }
    }

    public void calculateLikelihoods(HashMap classFrequencies){
        this.likelihoods = new HashMap();
        for (String varKey : this.vars.keySet()){
            HashMap varFreqsInClasses = this.vars.get(varKey);
            for (Object classKey :varFreqsInClasses.keySet()){
                String likelihoodKey = varKey + "|" + classKey;
                double likelihood = (double)  (Integer) varFreqsInClasses.get(classKey) /  (Integer) classFrequencies.get(classKey);
                this.likelihoods.put(likelihoodKey, likelihood);
            }
        }
    }

    //return total number of all vars across all classes
    // and init individuals total for a var across all classes
    public void getVarTotal(){
        int total = 0;
        for (String key : this.vars.keySet()){
            int temp = 0;
            for (Object value : this.vars.get(key).values()){
                temp += (Integer) value;
            }
            this.varTotals.put(key, temp);
            total += temp;
        }
    }


    public double BayesAlgo (double likelihood,double classPrior,double predictorPrior){
        double posterior = 0.0;
        return posterior;
    }

}

