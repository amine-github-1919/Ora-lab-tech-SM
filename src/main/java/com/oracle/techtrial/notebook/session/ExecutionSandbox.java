package com.oracle.techtrial.notebook.session;

import com.oracle.techtrial.notebook.config.Constants;
import com.oracle.techtrial.notebook.exception.UnsupportedInterpreterException;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RedisHash("ExecutionSandboxes")
public class ExecutionSandbox implements Serializable {

    public static final String ASSIGN = "=";
    private String id;

    private Map<String, String> variablesValues = new HashMap<>();

    public ExecutionSandbox() {
    }

    public ExecutionSandbox(String id) {
        this.id = id;
    }


    public Map<String, String> getVariablesValues() {
        return variablesValues;
    }

    public void setVariablesValues(Map<String, String> variablesValues) {
        this.variablesValues = variablesValues;
    }

    public String getVariableValue(String variableName) {
        return variablesValues.get(variableName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValueForVariables(String varValue, List<String> varNames) {
        for (String varName : varNames) {
            variablesValues.put(varName, varValue);
        }
    }

    public String getInitExpressionForVariableAndLanguage(String varName, String language) {
        if (!StringUtils.isEmpty(language) && language.equals(Constants.PYTHON)) {
            StringBuilder expression = new StringBuilder(varName);
            expression.append(ASSIGN);
            expression.append(getVariableValue(varName));
            return expression.toString();
        } else {
            throw new UnsupportedInterpreterException("Unsupported Interperter [" + language + "]");
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionSandbox sandbox = (ExecutionSandbox) o;
        return Objects.equals(id, sandbox.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ExecutionSandbox{" +
                "id='" + id + '\'' +
                ", variablesValues=" + variablesValues +
                '}';
    }
}
