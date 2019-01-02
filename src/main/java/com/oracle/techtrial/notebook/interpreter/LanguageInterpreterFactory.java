package com.oracle.techtrial.notebook.interpreter;

import com.oracle.techtrial.notebook.exception.UnsupportedInterpreterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class LanguageInterpreterFactory {
    private final Logger logger = LoggerFactory.getLogger(LanguageInterpreterFactory.class);


    private final List<LanguageInterpreter> interpreters;

    @Autowired
    public LanguageInterpreterFactory(List<LanguageInterpreter> interpreters) {
        this.interpreters = interpreters;
    }


    public LanguageInterpreter get(String interpreterName) {
        logger.debug("interpreterName: {}", interpreterName);
        if (StringUtils.isEmpty(interpreterName)) {
            throw new UnsupportedInterpreterException(interpreterName);
        }
        return interpreters
                .stream()
                .filter(interpreter -> interpreter.getLanguageName().equals(interpreterName))
                .findFirst()
                .orElseThrow(() -> new UnsupportedInterpreterException(interpreterName));
    }
}
