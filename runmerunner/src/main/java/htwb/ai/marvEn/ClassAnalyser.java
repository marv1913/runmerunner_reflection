package htwb.ai.marvEn;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassAnalyser {

    private final Object classToAnalyse;
    private List<Method> runMeAnnotatedMethods;
    private final List<Method> notInvokableMethods;
    private final HashMap<String, String> notInvokableMethodsWithErrorMessage;

    public ClassAnalyser(String className) throws ClassNotFoundException, IllegalArgumentException {
        if (null == className) {
            throw new IllegalArgumentException("class name should not be null");
        }

        this.classToAnalyse = this.getInstanceOfClass(Class.forName(className));

        this.notInvokableMethods = new ArrayList<>();
        this.notInvokableMethodsWithErrorMessage = new HashMap<>();
    }

    /**
     * Analyse Class
     */
    public void startAnalysis() {
        if (this.classToAnalyse != null) {
            CLIProcessor.displayAnalyzedClass(this.classToAnalyse.getClass().getName());
            this.printAllDeclaredMethodsWithoutRunMeAnnotation();
            this.runMeAnnotatedMethods = this.getMethods(true);
            String methodOutput = this.runMethods();
            for (Method m : this.notInvokableMethods) {
                this.runMeAnnotatedMethods.remove(m);
            }
            this.printAllDeclaredMethodsWithRunMeAnnotation();
            CLIProcessor.printNotInvocableMethods(this.notInvokableMethodsWithErrorMessage);
            System.out.println(System.lineSeparator() + methodOutput);
        }
    }

    /**
     * Returns all declared methods of class 'classToAnalyze'
     *
     * @param withRunMeAnnotation if true only methods with @RunMe annotation are returned; else only methods
     *                            without this annotation
     * @return matching methods as class List
     */
    private List<Method> getMethods(boolean withRunMeAnnotation) {
        List<Method> methodsWithoutAnnotations = new ArrayList<>();
        for (Method m : classToAnalyse.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(RunMe.class) == withRunMeAnnotation) {
                methodsWithoutAnnotations.add(m);
            }
        }
        return methodsWithoutAnnotations;
    }

    /**
     * Prints all declared methods of given class which have no RunMe annotation
     */
    private void printAllDeclaredMethodsWithoutRunMeAnnotation() {
        CLIProcessor.printMethodArray(this.getMethods(false), "Methods without @RunMe:");

    }

    /**
     * Prints all declared methods of given class which are annotated with RunMe
     */
    private void printAllDeclaredMethodsWithRunMeAnnotation() {
        CLIProcessor.printMethodArray(this.runMeAnnotatedMethods, "Methods with @RunMe:");

    }

    /**
     * Returns instance of a Class
     *
     * @param classToAnalyse Class to analyse
     * @return Object of class
     */
    private Object getInstanceOfClass(Class classToAnalyse) {
        Object classObject = null;
        try {
            classObject = classToAnalyse.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            CLIProcessor.printErrorMessage("Could not instantiate class " + classToAnalyse.getName());
        } catch (IllegalAccessException e) {
            CLIProcessor.printErrorMessage(classToAnalyse.getName() + ": " + e.getClass().getSimpleName());
        } catch (InvocationTargetException e) {
            CLIProcessor.printErrorMessage(classToAnalyse.getName() + ": " + e.getClass().getSimpleName());
        } catch (NoSuchMethodException e) {
            CLIProcessor.printErrorMessage("Could not instantiate class " + classToAnalyse.getName());
        }
        return classObject;

    }

    /**
     * runs methods on new instance of given class
     */
    private String runMethods() {
        PrintStream standardOut = System.out;
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        for (Method m : this.runMeAnnotatedMethods) {
            try {
                m.invoke(this.classToAnalyse);  // throws IllegalArgumentException when a method from methodList is not a method of classToAnalyze
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                this.notInvokableMethodsWithErrorMessage.put(m.getName(), e.getClass().getSimpleName());
                this.notInvokableMethods.add(m);
            }
        }
        System.setOut(standardOut);
        return outputStreamCaptor.toString();
    }

}
