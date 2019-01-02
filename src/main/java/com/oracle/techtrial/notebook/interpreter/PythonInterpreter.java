package com.oracle.techtrial.notebook.interpreter;

import com.oracle.techtrial.notebook.config.Constants;
import com.oracle.techtrial.notebook.interpreter.connector.PythonConnector;
import com.oracle.techtrial.notebook.session.ExecutionSandbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PythonInterpreter implements LanguageInterpreter {

    private PythonConnector pythonConnector;

    @Autowired
    public PythonInterpreter(final PythonConnector pythonConnector) {
        this.pythonConnector = pythonConnector;
    }

    @Override
    public String getLanguageName() {
        return Constants.PYTHON;
    }

    @Override
    public String execute(final String code, final ExecutionSandbox sandbox) {
        return pythonConnector.execute(code, sandbox);
    }
}
