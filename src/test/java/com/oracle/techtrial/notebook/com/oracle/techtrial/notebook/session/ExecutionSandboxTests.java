package com.oracle.techtrial.notebook.com.oracle.techtrial.notebook.session;

import com.oracle.techtrial.notebook.config.Constants;
import com.oracle.techtrial.notebook.exception.UnsupportedInterpreterException;
import com.oracle.techtrial.notebook.session.ExecutionSandbox;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ExecutionSandboxTests {

    private static final String VAR_A = "a";
    private static final String VAR_B = "b";
    private static final String VAL = "1";


    @Test
    public void setValueForVariables_should_set_the_value_for_all_variables() {
        ExecutionSandbox sandbox = new ExecutionSandbox();
        Map<String, String> variables = new HashMap<>();
        variables.put(VAR_A, VAL);
        variables.put(VAR_B, VAL);
        sandbox.setValueForVariables(VAL, Arrays.asList(VAR_A, VAR_B));
        assertThat(sandbox.getVariablesValues()).isEqualTo(variables);
    }

    @Test
    public void getInitExpressionForVariableAndLanguage_should_return_a_valid_expression_for_an_existing_var() {
        ExecutionSandbox sandbox = new ExecutionSandbox();
        sandbox.setValueForVariables(VAL, Arrays.asList(VAR_A));
        assertThat(sandbox.getInitExpressionForVariableAndLanguage(VAR_A, Constants.PYTHON)).isEqualTo(VAR_A + ExecutionSandbox.ASSIGN + VAL);
    }

    @Test(expected = UnsupportedInterpreterException.class)
    public void getInitExpressionForVariableAndLanguage_should_throw_UnsupportedInterpreterException_when_not_python_language() {
        ExecutionSandbox sandbox = new ExecutionSandbox();
        sandbox.setValueForVariables(VAL, Arrays.asList(VAR_A));
        sandbox.getInitExpressionForVariableAndLanguage(VAR_A, null);
    }
}
