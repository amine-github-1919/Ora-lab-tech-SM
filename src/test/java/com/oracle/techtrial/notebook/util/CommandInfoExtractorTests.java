package com.oracle.techtrial.notebook.util;


import com.oracle.techtrial.notebook.exception.UnprocessableCommandException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class CommandInfoExtractorTests {

    public static final String PYTHON_INTERPRETER = "python";
    public static final String PERCENT = "%";
    public static final String SOURCE_CODE_TEXT = "print x + y";

    public static final String VALID_COMMAD = PERCENT + PYTHON_INTERPRETER + " " + SOURCE_CODE_TEXT;
    public static final String NON_VALID_COMMAD_1 = PYTHON_INTERPRETER + " " + SOURCE_CODE_TEXT;
    public static final String NON_VALID_COMMAD_2 = PERCENT + PYTHON_INTERPRETER;


    @Test
    public void getInterpreterName_should_return_interpreter_for_valid_command() {
        CommandInfoExtractor extractor = new CommandInfoExtractor();
        String interpreter = extractor.getInterpreterName(VALID_COMMAD);
        assertThat(interpreter).isEqualTo(PYTHON_INTERPRETER);
    }

    @Test(expected = UnprocessableCommandException.class)
    public void getInterpreterName_should_throw_UnprocessableCommandException() {
        CommandInfoExtractor extractor = new CommandInfoExtractor();
        extractor.getInterpreterName(NON_VALID_COMMAD_1);
    }

    @Test
    public void getSourceCode_should_return_code_for_valid_command() {
        CommandInfoExtractor extractor = new CommandInfoExtractor();
        String interpreter = extractor.getSourceCode(VALID_COMMAD);
        assertThat(interpreter).isEqualTo(SOURCE_CODE_TEXT);
    }

    @Test(expected = UnprocessableCommandException.class)
    public void getSourceCode_should_throw_UnprocessableCommandException() {
        CommandInfoExtractor extractor = new CommandInfoExtractor();
        extractor.getSourceCode(NON_VALID_COMMAD_2);
    }
}
