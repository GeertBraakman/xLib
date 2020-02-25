package io.github.geertbraakman.v0_3_4.exceptions;

public class ConfigLoadException extends Exception {

    private final String file;

    public ConfigLoadException(String file, String message) {
        super(message);
        this.file = file;
    }

    @Override
    public String getMessage(){
        return "Error while creating '" + file + "', message: " + super.getMessage();
    }
}
