package com.oracle.techtrial.notebook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oracle.techtrial.notebook.config.Constants;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class InterpreterCommandDTO {

    @NotNull
    @NotEmpty
    @Pattern(regexp = Constants.INTERPRETER_COMMAND_REGEX, message = "Invalid command format")
    @JsonProperty("code")
    private String command;


    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
