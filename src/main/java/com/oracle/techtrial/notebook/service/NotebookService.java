package com.oracle.techtrial.notebook.service;

import com.oracle.techtrial.notebook.dto.InterpreterCommandDTO;
import com.oracle.techtrial.notebook.interpreter.LanguageInterpreter;
import com.oracle.techtrial.notebook.interpreter.LanguageInterpreterFactory;
import com.oracle.techtrial.notebook.session.ExecutionSandbox;
import com.oracle.techtrial.notebook.util.CommandInfoExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotebookService {

    private final Logger logger = LoggerFactory.getLogger(NotebookService.class);

    private final CommandInfoExtractor infoExtractor;

    private final LanguageInterpreterFactory interpreterFactory;


    public NotebookService(CommandInfoExtractor infoExtractor,
                           LanguageInterpreterFactory interpreterFactory) {
        this.infoExtractor = infoExtractor;
        this.interpreterFactory = interpreterFactory;
    }

    public String execute(final InterpreterCommandDTO commandDTO,
                          final ExecutionSandbox sandbox) {
        logger.info("Command: {} / session: {}", commandDTO.getCommand(), sandbox);

        String interpreterName = infoExtractor.getInterpreterName(commandDTO.getCommand());
        String commandSourceCode = infoExtractor.getSourceCode(commandDTO.getCommand());
        LanguageInterpreter interpreter = interpreterFactory.get(interpreterName);
        logger.debug("Executing interpreter {} with source: {} and session: {}",
                interpreter.getLanguageName(), commandSourceCode, sandbox);
        return interpreter.execute(commandSourceCode, sandbox);
    }
}
