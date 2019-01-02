package com.oracle.techtrial.notebook.com.oracle.techtrial.notebook.interpreter.parser;

import com.oracle.techtrial.notebook.interpreter.parser.SimplePythonVariableDeclarationParser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SimplePythonVariableDeclarationParserTests {


    @Test
    public void parse_should_parse_valid_declared_varibales_when_python_code_contains_assignement() {
        String pythonCode = "a,b = 5";
        SimplePythonVariableDeclarationParser parser = new SimplePythonVariableDeclarationParser(pythonCode);
        assertThat(parser.hasDeclaredVariables()).isTrue();
        assertThat(parser.getExpression()).isEqualTo("5");
        assertThat(parser.getVariableIdentifiers()).hasSize(2);
        assertThat(parser.getVariableIdentifiers()).contains("a", "b");
    }

    @Test
    public void parse_should_have_no_declared_varibales_when_python_code_does_not_contain_assignement() {
        String pythonCode = "print (1 + 1)";
        SimplePythonVariableDeclarationParser parser = new SimplePythonVariableDeclarationParser(pythonCode);
        assertThat(parser.hasDeclaredVariables()).isFalse();
        assertThatThrownBy(() -> {
            parser.getExpression();
        }).isInstanceOf(IllegalStateException.class);

        assertThatThrownBy(() -> {
            parser.getVariableIdentifiers();
        }).isInstanceOf(IllegalStateException.class);
    }
}
