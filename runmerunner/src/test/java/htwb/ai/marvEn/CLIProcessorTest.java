package htwb.ai.marvEn;

import htwb.ai.marvEn.exceptions.UserInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class CLIProcessorTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private final PrintStream errorOut = System.err;
    private final ByteArrayOutputStream errOutputStream = new ByteArrayOutputStream();

    @BeforeEach
    private void init() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(errOutputStream));
    }

    /**
     * Test Good case of one argument
     */
    @Test
    void testGetUserInput() {
        try {
            Assertions.assertEquals(CLIProcessor.getUserInput(new String[]{"MyExampleClass"}), "MyExampleClass");
        } catch (UserInputException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test Bad case of empty string of arguments
     */
    @Test
    void testGetUserInputEmptyArgs() {
        Assertions.assertThrows(UserInputException.class, () -> CLIProcessor.getUserInput(new String[]{""}),
                "Empty String of arguments should not be allowed");
    }

    /**
     * Test Bad case of bad String (IndexOutOfBounds)
     */
    @Test
    void testGetUserInputNoArgs() {
        Assertions.assertThrows(UserInputException.class, () -> CLIProcessor.getUserInput(new String[]{}),
                "UserInputException should have been thrown when passing no args");
    }

    /**
     * Test Bad case of String with multiple arguments
     */
    @Test
    void testGetUserInputManyArgs() {
        Assertions.assertThrows(UserInputException.class, () -> CLIProcessor.getUserInput(new String[]{"MyExampleClass", "MyExampleClass2", "MyExampleClass3"}),
                "UserInputException should have been thrown when passing more then 1 arg");
    }

    /**
     * Test Good case of passed list containing two methods
     */
    @Test
    void testPrintMethodArray() throws IllegalAccessException {
        List<Method> methodList = new ArrayList<>();
        String expectedSysOutput = "Test" + System.lineSeparator() +
                "testMethod1" + System.lineSeparator() +
                "testMethod2" + System.lineSeparator();

        Method mockedMethod = Mockito.mock(Method.class);
        Method mockedMethod2 = Mockito.mock(Method.class);

        Mockito.when(mockedMethod.getName()).thenReturn("testMethod1");
        Mockito.when(mockedMethod2.getName()).thenReturn("testMethod2");

        methodList.add(mockedMethod);
        methodList.add(mockedMethod2);

        Assertions.assertDoesNotThrow(() -> {
            CLIProcessor.printMethodArray(methodList, "Test");
        });

        Assertions.assertEquals(expectedSysOutput, outputStreamCaptor.toString());
    }

    /**
     * Test Bad case of empty list of Methods passed
     */
    @Test
    void testPrintMethodArrayEmpty() {
        List<Method> methodList = new ArrayList<>();

        CLIProcessor.printMethodArray(methodList, "Test");
        Assertions.assertEquals("Test" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    /**
     * Test Bad case methods prints correct error message
     */
    @Test
    void testPrintErrorMessageEmpty() {
        CLIProcessor.printErrorMessage("");
        Assertions.assertEquals("Error: " + System.lineSeparator(), errOutputStream.toString());
    }

    /**
     * Test Good case methods prints correct error message
     */
    @Test
    void testPrintErrorMessage() {
        CLIProcessor.printErrorMessage("The cake is a lie");
        Assertions.assertEquals("Error: The cake is a lie" + System.lineSeparator(),
                errOutputStream.toString());
    }

    /**
     * Test Good case print not invocable methods
     */
    @Test
    void printNotInvocableMethodsGood() {
        HashMap<String, String> testDict = new HashMap<>();
        testDict.put("testMethod1", "IllegalArgumentException");
        testDict.put("testMethod2", "InvocationTargetException");
        CLIProcessor.printNotInvocableMethods(testDict);

        Assertions.assertEquals("not invocable:" + System.lineSeparator() +
                "testMethod2: InvocationTargetException" + System.lineSeparator() +
                "testMethod1: IllegalArgumentException" + System.lineSeparator()
                , outputStreamCaptor.toString());
    }

    /**
     * Test Bad case empty list of Methods
     */
    @Test
    void printNotInvocableMethodsEmptyHashMap() {
        CLIProcessor.printNotInvocableMethods(new HashMap<>());

        Assertions.assertTrue(outputStreamCaptor.toString().isEmpty());
    }

    /**
     * Test Bad case null String
     */
    @Test
    void displayAnalyzedClassNullString() {
        CLIProcessor.displayAnalyzedClass(null);

        Assertions.assertTrue(outputStreamCaptor.toString().isEmpty());
    }

    /**
     * Test Good case class name passed
     */
    @Test
    void displayAnalyzedClassTest() {
        CLIProcessor.displayAnalyzedClass("TestClassName");

        Assertions.assertEquals("Analyzed class ´TestClassName´:" + System.lineSeparator(), outputStreamCaptor.toString());
    }
}