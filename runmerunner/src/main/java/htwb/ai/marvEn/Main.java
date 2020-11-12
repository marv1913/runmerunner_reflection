package htwb.ai.marvEn;

import htwb.ai.marvEn.exceptions.UserInputException;

class Main {

    public static void main(String[] args) {
        try {
            // Get Class name from args
            String className = CLIProcessor.getUserInput(args);

            // Analyse Class
            ClassAnalyser classAnalyser = new ClassAnalyser(className);
            classAnalyser.startAnalysis();

        } catch (IllegalArgumentException e) {
            CLIProcessor.printErrorMessage("could not call annotated function, because parameters needed. message: " +
                    e.getMessage());
        } catch (ClassNotFoundException e) {
            CLIProcessor.printErrorMessage("could not find class '" + e.getMessage() + "'");
        } catch (UserInputException e) {
            CLIProcessor.printErrorMessage(e.getMessage());
        }
    }
}