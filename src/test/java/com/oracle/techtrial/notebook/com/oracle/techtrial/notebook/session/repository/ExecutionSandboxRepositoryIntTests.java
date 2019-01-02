package com.oracle.techtrial.notebook.com.oracle.techtrial.notebook.session.repository;

import com.oracle.techtrial.notebook.session.ExecutionSandbox;
import com.oracle.techtrial.notebook.session.repository.ExecutionSandboxRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExecutionSandboxRepositoryIntTests {

    @Autowired
    ExecutionSandboxRepository repository;


    @Test
    public void saveExecutionSandbox_should_return_a_saved_Sandbox() {
        String id = "1";

        ExecutionSandbox sandbox = createSandbox(id);
        ExecutionSandbox savedSandbox = repository.save(sandbox);
        assertThat(savedSandbox).isNotNull();
        assertThat(savedSandbox).isEqualTo(sandbox);
        assertThat(savedSandbox.getVariablesValues()).isEqualTo(sandbox.getVariablesValues());

    }


    @Test
    public void findOne_should_return_an_existing_Sandbox() {
        String id = "2";
        ExecutionSandbox sandbox = createSandbox(id);
        repository.save(sandbox);
        ExecutionSandbox existingSandbox = repository.findOne(id);
        assertThat(existingSandbox).isNotNull();
        assertThat(existingSandbox).isEqualTo(sandbox);
        assertThat(existingSandbox.getVariablesValues()).isEqualTo(sandbox.getVariablesValues());
    }

    @Test
    public void findOne_should_return_null_for_non_existing_id() {
        ExecutionSandbox existingSandbox = repository.findOne("NAID");
        assertThat(existingSandbox).isNull();

    }

    private ExecutionSandbox createSandbox(String id) {
        ExecutionSandbox sandbox = new ExecutionSandbox();
        sandbox.setId(id);
        sandbox.setValueForVariables("A_VALUE", Arrays.asList("A", "B"));
        return sandbox;
    }

}
