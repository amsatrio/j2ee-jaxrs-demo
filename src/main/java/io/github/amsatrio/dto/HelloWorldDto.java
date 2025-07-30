package io.github.amsatrio.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelloWorldDto implements Serializable {
    @NotNull(message = "Message cannot be null")
    @Size(min = 3, max = 50, message = "Message must be between 3 and 50 characters")
    private String message;
}
