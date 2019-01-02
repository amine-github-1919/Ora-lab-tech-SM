package com.oracle.techtrial.notebook.interpreter.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimplePythonVariableDeclarationParser {

    private static final String COMMA = ",";
    private static final String ASSIGN = "=";
    private static final String DEF_FUNC = "def";
    private final Logger logger =
            LoggerFactory.getLogger(SimplePythonVariableDeclarationParser.class);
    private boolean declaredVariables;
    private List<String> variableIdentifiers = new ArrayList<>();
    private String expression;

    public SimplePythonVariableDeclarationParser(String pythonStatement) {
        parse(pythonStatement);
    }

    private void parse(final String pythonStatement) {
        logger.info("Starting parsing code : {}", pythonStatement);
        if (StringUtils.isEmpty(pythonStatement)) {
            throw new IllegalArgumentException("Empty source code");
        }
        // do nothing when the code does not contain assignement i.e there are no new variables
        // if the statement is a function declaration
        if (!pythonStatement.contains(ASSIGN) || pythonStatement.contains(DEF_FUNC)) {
            logger.info("No variables found do nothing");
            return;
        } else {
            declaredVariables = true;
            // get the declared variables
            String leftOperand = pythonStatement
                    .substring(0, pythonStatement.indexOf(ASSIGN) - 1).trim();
            parseLeftOperand(leftOperand);
            logger.info("Variables found : {}", variableIdentifiers);

            expression = pythonStatement
                    .substring(pythonStatement.indexOf(ASSIGN) + 1).trim();

            logger.info("Expression of the assignment  : {}", expression);

        }
    }

    private void parseLeftOperand(final String leftOperand) {
        if (!leftOperand.contains(COMMA)) {
            variableIdentifiers.add(leftOperand.trim());
        } else { // multiple variables declaration
            variableIdentifiers.addAll(Arrays.stream(leftOperand.split(COMMA))
                    .map(String::trim).collect(Collectors.toList()));
        }
    }

    public boolean hasDeclaredVariables() {
        return declaredVariables;
    }

    public List<String> getVariableIdentifiers() {
        checkState();
        return variableIdentifiers;
    }

    public String getExpression() {
        checkState();
        return expression;
    }

    private void checkState() {
        if (!declaredVariables) {
            throw new IllegalStateException("There are no declared variables. " +
                    "Nothing to return");
        }
    }
}
