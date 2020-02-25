package io.github.geertbraakman.v0_3_4.exceptions;

public class InvalidSizeException extends Exception {

    private final int input;
    private final int minimum;

    public InvalidSizeException(int input, int minimum) {
        this.input = input;
        this.minimum = minimum;
    }

    public int getInput() {
        return input;
    }

    public int getMinimum() {
        return minimum;
    }
}
