package classification;

import java.util.logging.Level;
import java.util.ArrayList;

public class NaiveBayes extends Algorithm {

    public NaiveBayes(ArrayList data){

        super.get_logger().log(Level.INFO, "Naive Bayes Algorithm created.");
        train(data);
    }

    void train(ArrayList trainData){
        super.get_logger().log(Level.INFO, "Starting training:");
    }
    void test(ArrayList testData){
        super.get_logger().log(Level.INFO, "Starting testing:");
    }





    public static void main(String[] args){

//        1. Class Name: 2 (democrat, republican)
//        2. handicapped-infants: 2 (y,n)
//        3. water-project-cost-sharing: 2 (y,n)
//        4. adoption-of-the-budget-resolution: 2 (y,n)
//        5. physician-fee-freeze: 2 (y,n)
//        6. el-salvador-aid: 2 (y,n)
//        7. religious-groups-in-schools: 2 (y,n)
//        8. anti-satellite-test-ban: 2 (y,n)
//        9. aid-to-nicaraguan-contras: 2 (y,n)
//        10. mx-missile: 2 (y,n)
//        11. immigration: 2 (y,n)
//        12. synfuels-corporation-cutback: 2 (y,n)
//        13. education-spending: 2 (y,n)
//        14. superfund-right-to-sue: 2 (y,n)
//        15. crime: 2 (y,n)
//        16. duty-free-exports: 2 (y,n)
//        17. export-administration-act-south-africa: 2 (y,n)


    }
}


