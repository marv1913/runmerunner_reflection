package htwb.ai.marvEn.myRunMeClasses;

import htwb.ai.marvEn.RunMe;

public class MyExampleClass {
    private String text;

    public MyExampleClass(String text) {
        this.text = text;
    }


    public MyExampleClass() {
        this.text = "default constructor was used";
    }

    public void printTextUpperCase() {
        System.out.println(this.text.toUpperCase());
    }

    public void printTextLowerCase() {
        System.out.println(this.text.toLowerCase());
    }

    private void myPrivateMethod() {
        System.out.println(this.text.toLowerCase());
    }


    public void printConcatenatedText(String additionalText){
        System.out.println(this.text + additionalText);
    }

    @RunMe()
    public void printTextAnnotated(){
        System.out.println("i am an annotated method");
    }

    @RunMe()
    public void printTextAnnotatedWithArguments(String additionalText){
        System.out.println("i am an annotated method");
    }

    public static void printHello(){
        System.out.println("Hello world!");
    }

}

