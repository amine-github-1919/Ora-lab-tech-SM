package com.oracle.techtrial.notebook.interpreter;

import com.oracle.techtrial.notebook.session.ExecutionSandbox;

public interface LanguageInterpreter {

    String getLanguageName();

    String execute(String code, ExecutionSandbox sandbox);
}
