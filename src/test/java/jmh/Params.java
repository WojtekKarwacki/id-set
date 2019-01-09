package jmh;

public class Params {
    private String function;
    private int numberOfElements;
    private String testObjectType;

    public Params(String function, int numberOfElements, String testObjectType) {
        this.function = function;
        this.numberOfElements = numberOfElements;
        this.testObjectType = testObjectType;
    }

    public String getTestObjectType() {
        return testObjectType;
    }

    public String getFunction() {
        return function;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }
}
