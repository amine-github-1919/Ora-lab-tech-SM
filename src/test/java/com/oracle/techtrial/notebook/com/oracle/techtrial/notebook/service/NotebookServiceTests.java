package com.oracle.techtrial.notebook.com.oracle.techtrial.notebook.service;

import com.oracle.techtrial.notebook.dto.InterpreterCommandDTO;
import com.oracle.techtrial.notebook.interpreter.LanguageInterpreter;
import com.oracle.techtrial.notebook.interpreter.LanguageInterpreterFactory;
import com.oracle.techtrial.notebook.service.NotebookService;
import com.oracle.techtrial.notebook.session.ExecutionSandbox;
import com.oracle.techtrial.notebook.util.CommandInfoExtractor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.oracle.techtrial.notebook.util.CommandInfoExtractorTests.*;
import static org.mockito.Mockito.*;

public class NotebookServiceTests {

    @Mock
    LanguageInterpreterFactory interpreterFactory;

    @Mock
    CommandInfoExtractor commandInfoExtractor;

    @InjectMocks
    private NotebookService notebookService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute_should_call_an_interpreter_execute() {
        InterpreterCommandDTO commandDTO = new InterpreterCommandDTO();
        commandDTO.setCommand(VALID_COMMAD);
        ExecutionSandbox sandbox = new ExecutionSandbox("1");
        when(commandInfoExtractor.getInterpreterName(commandDTO.getCommand())).thenReturn(PYTHON_INTERPRETER);
        when(commandInfoExtractor.getSourceCode(commandDTO.getCommand())).thenReturn(SOURCE_CODE_TEXT);

        LanguageInterpreter interpreter = mock(LanguageInterpreter.class);
        when(interpreterFactory.get(PYTHON_INTERPRETER)).thenReturn(interpreter);

        notebookService.execute(commandDTO, sandbox);
        verify(interpreter).execute(SOURCE_CODE_TEXT, sandbox);

    }

}
