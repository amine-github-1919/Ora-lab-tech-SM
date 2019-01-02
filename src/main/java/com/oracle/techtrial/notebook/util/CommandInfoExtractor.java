package com.oracle.techtrial.notebook.util;

import com.oracle.techtrial.notebook.config.Constants;
import com.oracle.techtrial.notebook.exception.UnprocessableCommandException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommandInfoExtractor {

    private final Pattern pattern = Pattern.compile(Constants.INTERPRETER_COMMAND_REGEX);


    public String getInterpreterName(String command) {
        return extractCommandInfo(command, 2);
    }


    public String getSourceCode(String command) {
        return extractCommandInfo(command, 3);
    }

    private String extractCommandInfo(String command, int infoIndex) {
        Matcher m = pattern.matcher(command);
        if (m.find()) {
            return m.group(infoIndex);
        }
        throw new UnprocessableCommandException("Unprocessable command: [" + command + "]");
    }
}
