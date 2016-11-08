package classification;
// Logging examples obtained from https://examples.javacodegeeks.com/core-java/util/logging/java-util-logging-example/
import java.io.IOException;
import java.util.logging.*;

public abstract class Algorithm {

    private static final Logger LOGGER = Logger.getLogger(Algorithm.class.getName());

    public Algorithm(){

        try {

            Handler consoleHandler = null;
            Handler fileHandler = null;
            //Creating consoleHandler and fileHandler
            consoleHandler = new ConsoleHandler();
            fileHandler = new FileHandler("./output.txt");
            System.out.println("NOTE THAT THE LOGGER OVERWRITES WHATEVER IS ALREADY IN OUTPUT!!!!!");
            System.out.println("Used log level info for writing to file and fine for degbugging.");

            Formatter customFormater = new CustomFormatter();

            //Assigning handlers to LOGGER object
            LOGGER.addHandler(consoleHandler);
            consoleHandler.setFormatter(customFormater);

            LOGGER.addHandler(fileHandler);
            fileHandler.setFormatter(customFormater);


            //Setting levels to handlers and LOGGER
            consoleHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.INFO);
            LOGGER.setLevel(Level.ALL);
        }
        catch(IOException exception){

        }

    }

    public Logger get_logger(){
        return LOGGER;
    }

    abstract void train();
    abstract void test();

}

class CustomFormatter extends Formatter{
    public String format(LogRecord record){
        StringBuilder builder = new StringBuilder(1000);
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }
}