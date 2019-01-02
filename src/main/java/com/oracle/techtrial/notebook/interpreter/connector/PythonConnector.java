package com.oracle.techtrial.notebook.interpreter.connector;

import com.oracle.techtrial.notebook.session.ExecutionSandbox;

public interface PythonConnector {

    String execute(String pythonCode, ExecutionSandbox sandbox);
}
