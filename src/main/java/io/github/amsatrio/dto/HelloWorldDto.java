package io.github.amsatrio.dto;

import java.io.Serializable;

public class HelloWorldDto implements Serializable {
    private String message = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
