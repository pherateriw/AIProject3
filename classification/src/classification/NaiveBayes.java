package classification;

import java.util.logging.Level;

public class NaiveBayes extends Algorithm {

    public NaiveBayes(){

        super.get_logger().log(Level.INFO, "Naive Bayes Algorithm created.");
        train();
    }

    void train(){
        super.get_logger().log(Level.INFO, "Starting training:");
    }
    void test(){
        super.get_logger().log(Level.INFO, "Starting testing:");
    }
}
