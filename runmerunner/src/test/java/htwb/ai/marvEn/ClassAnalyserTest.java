package htwb.ai.marvEn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import htwb.ai.marvEn.myRunMeClasses.MyExampleClass;

import java.io.ByteArrayOutputStream;
import java.io.Flushable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClassAnalyserTest {

    private ClassAnalyser stringClassAnalyzer;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private final PrintStream errorOut = System.err;
    private final ByteArrayOutputStream errOutputStream = new ByteArrayOutputStream();

    @BeforeEach
    private void init() throws ClassNotFoundException {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(errOutputStream));

        stringClassAnalyzer = new ClassAnalyser("java.lang.String");
    }

    @Test
    public void createInstanceOfClassAnalyserBadClassNotExisting() {
        assertThrows(ClassNotFoundException.class, () -> new ClassAnalyser("java.lang.MyString"));
    }

    @Test
    public void createInstanceOfClassAnalyserEdgeClassNameEmpty() {
        assertThrows(ClassNotFoundException.class, () -> new ClassAnalyser(""));
    }

    @Test
    public void createInstanceOfClassAnalyserBadNull() {
        assertThrows(IllegalArgumentException.class, () -> new ClassAnalyser(null));
    }

    @Test
    public void startAnalysisClassToAnalyzeDisplayed() {
        try (MockedStatic<CLIProcessor> cliProcessorMocked = mockStatic(CLIProcessor.class)) {
            ArgumentCaptor captor = ArgumentCaptor.forClass(CLIProcessor.class);
            this.stringClassAnalyzer.startAnalysis();
            cliProcessorMocked.verify(times(1), () ->
                    CLIProcessor.displayAnalyzedClass((String) captor.capture()));
            assertEquals("java.lang.String", (String) captor.getValue());
        }
    }

    @Test
    public void startAnalysisMethodsWithoutAnnotationsHeadingPrinted() {
        try (MockedStatic<CLIProcessor> cliProcessorMocked = mockStatic(CLIProcessor.class)) {
            ArgumentCaptor stringCaptor = ArgumentCaptor.forClass(CLIProcessor.class);

            this.stringClassAnalyzer.startAnalysis();
            cliProcessorMocked.verify(atLeastOnce(), () ->
                    CLIProcessor.printMethodArray(any(), (String) stringCaptor.capture()));
            assertEquals("Methods with @RunMe:", (String) stringCaptor.getValue());
        }
    }

    @Test
    public void startAnalysisMethodsWithoutAnnotationsListFilled() {
        try (MockedStatic<CLIProcessor> cliProcessorMocked = mockStatic(CLIProcessor.class)) {
            ArgumentCaptor captor = ArgumentCaptor.forClass(CLIProcessor.class);

            this.stringClassAnalyzer.startAnalysis();
            cliProcessorMocked.verify(atLeastOnce(), () ->
                    CLIProcessor.printMethodArray(((List) captor.capture()), any()));
            assertFalse(((List) captor.getAllValues().get(0)).isEmpty());
        }
    }

    @Test
    public void startAnalysisBadNotInvocableMethods() throws ClassNotFoundException {
        try (MockedStatic<CLIProcessor> cliProcessorMocked = mockStatic(CLIProcessor.class)) {
            ArgumentCaptor captor = ArgumentCaptor.forClass(CLIProcessor.class);
            ClassAnalyser classAnalyser = new ClassAnalyser("htwb.ai.marvEn.myRunMeClasses.MyExampleClass");
            classAnalyser.startAnalysis();
            cliProcessorMocked.verify(atLeastOnce(), () ->
                    CLIProcessor.printNotInvocableMethods(((HashMap) captor.capture())));
            assertEquals(1, ((HashMap) captor.getValue()).size());
        }
    }

    @Test
    public void startAnalysisMethodsWithAnnotationsListFilled() throws ClassNotFoundException {
        try (MockedStatic<CLIProcessor> cliProcessorMocked = mockStatic(CLIProcessor.class)) {
            ArgumentCaptor captor = ArgumentCaptor.forClass(CLIProcessor.class);
            ClassAnalyser classAnalyser = new ClassAnalyser("htwb.ai.marvEn.myRunMeClasses.MyExampleClass");
            classAnalyser.startAnalysis();
            cliProcessorMocked.verify(atLeastOnce(), () ->
                    CLIProcessor.printMethodArray(((List) captor.capture()), any()));
            assertFalse(((List) captor.getAllValues().get(1)).isEmpty());
        }
    }

    @Test
    public void startAnalysisBadInterfacePassed() throws ClassNotFoundException {
        try (MockedStatic<CLIProcessor> cliProcessorMocked = mockStatic(CLIProcessor.class)) {
            ArgumentCaptor captor = ArgumentCaptor.forClass(CLIProcessor.class);
            ClassAnalyser classAnalyser = new ClassAnalyser("java.io.Closeable");
            classAnalyser.startAnalysis();
            cliProcessorMocked.verify(atLeastOnce(), () ->
                    CLIProcessor.printErrorMessage(((String) captor.capture())));
            assertEquals("Could not instantiate class java.io.Closeable", ((String) captor.getValue()));
        }
    }

    @Test
    public void startAnalysisBadAbstractClassPassed() throws ClassNotFoundException {
        try (MockedStatic<CLIProcessor> cliProcessorMocked = mockStatic(CLIProcessor.class)) {
            ArgumentCaptor captor = ArgumentCaptor.forClass(CLIProcessor.class);
            ClassAnalyser classAnalyser = new ClassAnalyser("java.io.OutputStream");
            classAnalyser.startAnalysis();
            cliProcessorMocked.verify(atLeastOnce(), () ->
                    CLIProcessor.printErrorMessage(((String) captor.capture())));
            assertEquals("Could not instantiate class java.io.OutputStream", ((String) captor.getValue()));
        }
    }

    @Test
    public void startAnalysisBadClassWithInvocationTargetException() throws ClassNotFoundException {
        try (MockedStatic<CLIProcessor> cliProcessorMocked = mockStatic(CLIProcessor.class)) {
            ArgumentCaptor captor = ArgumentCaptor.forClass(CLIProcessor.class);
            ClassAnalyser classAnalyser = new ClassAnalyser("htwb.ai.marvEn.myRunMeClasses." +
                    "ClassToTriggerInvocationTargetException");
            classAnalyser.startAnalysis();
            cliProcessorMocked.verify(atLeastOnce(), () ->
                    CLIProcessor.printErrorMessage(((String) captor.capture())));
            assertEquals("htwb.ai.marvEn.myRunMeClasses.ClassToTriggerInvocationTargetException:" +
                    " InvocationTargetException", ((String) captor.getValue()));
        }
    }


}