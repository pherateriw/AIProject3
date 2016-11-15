package classification;
import java.util.HashMap;
/**
 * Created by lisapeters on 11/15/16.
 * Example: Outlook feature with values Sunny:5, Overcast:4, Rainy:5
 */
public class Feature {
    HashMap<String, Integer> types;
    public Feature(){
         types = new HashMap<String, Integer>();
    }

    public void addInstance(String name){

        Integer value = types.get(name);
        if (value != null) {
            types.put(name,  value + 1);
        } else {
            types.put(name, 1);
        }
    }
}
