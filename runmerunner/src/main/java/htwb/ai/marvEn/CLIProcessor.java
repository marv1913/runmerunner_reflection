package htwb.ai.marvEn;

import htwb.ai.marvEn.exceptions.UserInputException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class CLIProcessor {

    /**
     * Helper method to print expected usage of arguments
     */
    private static void printUsageText() {
        System.out.println("Usage: java -jar runmerunner-marven.jar classname");
    }

    /**
     * Helper method to print passed arguments
     *
     * @param args arguments
     */
    private static void printArguments(String[] args) {
        System.out.println("Argument(s): " + String.join(" ", args));
    }

    /**
     * Returns the first argument only if exactly 1 argument was passed
     *
     * @param args arguments
     * @return argument
     * @throws UserInputException If more or less then 1 argument was passed
     */
    public static String getUserInput(String[] args) throws UserInputException {
        if (args.length != 1) {
            printArguments(args);
            throw new UserInputException("exactly one argument excepted.");
        }
        if (String.join("", args).isBlank()) {
            printArguments(args);
            throw new UserInputException("empty argument not allowed");
        }
        return args[0];
    }

    /**
     * Print array of methods
     *
     * @param methods list of methods to print
     * @param heading string to print before methods
     */
    public static void printMethodArray(List<Method> methods, String heading) {
        System.out.println(heading);
        if (!methods.isEmpty()) {
            for (Method m : methods) {
                System.out.println(m.getName());
            }
        }
    }

    /**
     * Print error
     *
     * @param message message to print
     */
    public static void printErrorMessage(String message) {
        System.err.println("Error: " + message);
        printUsageText();
    }

    /**
     * prints the name of analyzed class on console
     *
     * @param className className
     */
    public static void displayAnalyzedClass(String className) {
        if (null != className) {
            System.out.println("Analyzed class ´" + className + "´:");
        }
    }

    /**
     * prints all methods, where the execution failed
     *
     * @param methodsAndErrorMessages HashMap which contains the method name as key and the name of the exception as
     *                                value
     */
    public static void printNotInvocableMethods(HashMap<String, String> methodsAndErrorMessages) {
        if (!methodsAndErrorMessages.isEmpty()) {
            System.out.println("not invocable:");
            for (String key : methodsAndErrorMessages.keySet()) {
                System.out.println(key + ": " + methodsAndErrorMessages.get(key));
            }
        }
    }
}
