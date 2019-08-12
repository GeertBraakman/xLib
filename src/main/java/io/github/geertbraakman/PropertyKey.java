package io.github.geertbraakman;

public enum PropertyKey {

    SUB_COMMAND_CHECK("subCommandCheck");

    private String path;

    PropertyKey(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
