package com.oracle.techtrial.notebook.interpreter;


import com.oracle.techtrial.notebook.config.Constants;
import com.oracle.techtrial.notebook.exception.UnsupportedInterpreterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LanguageInterpreterFactoryTests {

    @Autowired
    LanguageInterpreterFactory factory;


    @Test
    public void get_should_return_existing_python_interpreter_when_provided_with_python_param() {
        assertThat(factory.get(Constants.PYTHON)).isNotNull();
        assertThat(factory.get(Constants.PYTHON)).isInstanceOf(PythonInterpreter.class);

    }

    @Test
    public void get_should_throw_an_UnsupportedInterpreterException_when_provided_with_non_existing_interpreter_param() {
        assertThatThrownBy(() -> {
            factory.get("");
        }).isInstanceOf(UnsupportedInterpreterException.class);

        assertThatThrownBy(() -> {
            factory.get(null);
        }).isInstanceOf(UnsupportedInterpreterException.class);

    }

}
