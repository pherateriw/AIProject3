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

    public void calculateProbabilities(HashMap classInfo){
        this.likelihoods = new HashMap();
        int allVarTotal = getVarTotal();


        //probability for each class ie P(Var|Class) or P(Sunny|Yes) and P(Sunny|No)
            for (String key : this.vars.keySet()){  //for each var in feature, ie Sunny in OutLook
                HashMap classFrequencies = this.vars.get(key);
                for (Object keytwo : classFrequencies.keySet()){
                    String keythree = key + "|" + keytwo;
                    double likelihood = (double)  (Integer) classFrequencies.get(keytwo) /  (Integer) classInfo.get(keytwo); //p(x|c) = # of sunny in Yes
                    this.likelihoods.put(keythree, likelihood);
                }
            }
        }



    //return total number of all vars across all classes
    // and init individuals total for a var across all classes
    public int getVarTotal(){
        int total = 0;
        for (String key : this.vars.keySet()){
            int temp = 0;
            for (Object value : this.vars.get(key).values()){
                temp += (Integer) value;
            }
            this.varTotals.put(key, temp);
            total += temp;
        }
        return total;
    }


}

