package htwb.ai.marvEn.exceptions;

public class UserInputException extends Exception {

    String s;

    public UserInputException() {
        this(null);
    }

    public UserInputException(String s) {
        super(s);
        this.s = s;
    }

}
