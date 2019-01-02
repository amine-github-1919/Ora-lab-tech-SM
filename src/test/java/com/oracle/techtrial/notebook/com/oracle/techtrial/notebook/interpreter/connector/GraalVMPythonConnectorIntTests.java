package com.oracle.techtrial.notebook.com.oracle.techtrial.notebook.interpreter.connector;

import com.oracle.techtrial.notebook.interpreter.connector.GraalVMPythonConnector;
import com.oracle.techtrial.notebook.session.ExecutionSandbox;
import com.oracle.techtrial.notebook.session.service.ExecutionSandboxService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class GraalVMPythonConnectorIntTests {

    @Mock
    ExecutionSandboxService sandboxService;

    @InjectMocks
    private GraalVMPythonConnector graalVMPythonConnector;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute_should_return_output_for_non_assignement_python_code() {
        ExecutionSandbox sandbox = new ExecutionSandbox("1");
        String result = graalVMPythonConnector.execute("print (1 + 1)", sandbox);
        assertThat(result).isEqualTo("2");
        verify(sandboxService, never()).updateSession(any(ExecutionSandbox.class));
    }

    @Test
    public void execute_should_return_no_output_for_assignement_of_a_variable() {
        ExecutionSandbox sandbox = new ExecutionSandbox("1");
        sandbox.setValueForVariables("5", Arrays.asList("a"));
        String result = graalVMPythonConnector.execute("a = 5", sandbox);
        assertThat(result).isEqualTo("");
        verify(sandboxService, times(1)).updateSession(sandbox);

    }

    @Test
    public void execute_should_execute_command_using_an_already_declared_variable() {
        ExecutionSandbox inputSandbox = new ExecutionSandbox("1");
        inputSandbox.setValueForVariables("5", Arrays.asList("a"));
        String result = graalVMPythonConnector.execute("b = a + 5", inputSandbox);
        assertThat(result).isEqualTo("");
        ExecutionSandbox outputSandbox = new ExecutionSandbox("1");
        outputSandbox.setValueForVariables("5", Arrays.asList("a"));
        outputSandbox.setValueForVariables("10", Arrays.asList("b"));
        verify(sandboxService, times(1)).updateSession(outputSandbox);

    }
}
