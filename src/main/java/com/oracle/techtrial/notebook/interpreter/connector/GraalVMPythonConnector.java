package com.oracle.techtrial.notebook.interpreter.connector;

import com.oracle.techtrial.notebook.config.Constants;
import com.oracle.techtrial.notebook.exception.CodeExecutionException;
import com.oracle.techtrial.notebook.interpreter.parser.SimplePythonVariableDeclarationParser;
import com.oracle.techtrial.notebook.session.ExecutionSandbox;
import com.oracle.techtrial.notebook.session.service.ExecutionSandboxService;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Primary
@Component
public class GraalVMPythonConnector implements PythonConnector {

    private final Logger logger = LoggerFactory.getLogger(GraalVMPythonConnector.class);
    private final ExecutionSandboxService sessionService;


    public GraalVMPythonConnector(ExecutionSandboxService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String execute(String pythonCode, ExecutionSandbox sandbox) {
        logger.debug("Starting GraalVM context for code: {} and Session: {}", pythonCode, sandbox);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        String result = "";
        Context context = Context.newBuilder(Constants.PYTHON).allowIO(true).out(out).err(err).allowAllAccess(true).build();
        initContextWithExistingVariables(sandbox, context);
        List<String> assignedVariables = new ArrayList<>();
        boolean isAnAssignement = addVariablesToSandbox(context, assignedVariables, sandbox, pythonCode);
        if (isAnAssignement) {
            sessionService.updateSession(sandbox);
        } else {
            result = eval(context, pythonCode, out);
        }
        logger.debug("GraalVM result: {} for code: {} and Session: {}", result, pythonCode, sandbox);

        return result;
    }


    private void initContextWithExistingVariables(ExecutionSandbox sandbox, Context context) {
        for (String varName : sandbox.getVariablesValues().keySet()) {
            String expression = sandbox.getInitExpressionForVariableAndLanguage(varName, Constants.PYTHON);
            eval(context, expression);
        }
    }

    private boolean addVariablesToSandbox(Context context, List<String> assignedVariables, ExecutionSandbox sandbox, String pythonCode) {
        SimplePythonVariableDeclarationParser parser = new SimplePythonVariableDeclarationParser(pythonCode);
        String expression;
        if (parser.hasDeclaredVariables()) {
            expression = parser.getExpression();
            String expressionValue = eval(context, expression);
            assignedVariables.addAll(parser.getVariableIdentifiers());
            sandbox.setValueForVariables(expressionValue, assignedVariables);
            logger.debug("Adding new variables {} to Session: {}", assignedVariables, sandbox);
            return true;
        }
        return false;
    }


    private String eval(Context context, String code) {
        logger.debug("Evaluating code: {}", code);

        Value result = null;
        try {
            result = context.eval(Constants.PYTHON, code);
        } catch (PolyglotException pe) {
            throw new CodeExecutionException(Constants.PYTHON, code, pe.getMessage(), pe);
        }
        logger.debug("Result: {} for code: {}", result, code);
        return result.toString();
    }

    private String eval(Context context, String code, ByteArrayOutputStream out) {
        logger.debug("Evaluating code: {} with output capture", code);
        try {
            context.eval(Constants.PYTHON, code);
        } catch (PolyglotException pe) {
            throw new CodeExecutionException(Constants.PYTHON, code, pe.getMessage(), pe);
        }
        String result = new String(out.toByteArray()).replace("\n", "").replace("\r", "");
        logger.debug("Result: {} for code: {}", result, code);
        return result;

    }


}
