package com.oracle.techtrial.notebook.com.oracle.techtrial.notebook.session.service;

import com.oracle.techtrial.notebook.session.ExecutionSandbox;
import com.oracle.techtrial.notebook.session.repository.ExecutionSandboxRepository;
import com.oracle.techtrial.notebook.session.service.ExecutionSandboxService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ExecutionSandboxServiceTests {

    @Mock
    private ExecutionSandboxRepository sandboxRepository;

    @InjectMocks
    private ExecutionSandboxService sandboxService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createIfAbsent_should_return_a_new_sandbox_if_it_does_not_exist() {
        String idSession = "1";
        ExecutionSandbox existingSandbox = new ExecutionSandbox(idSession);

        when(sandboxRepository.findOne(idSession)).thenReturn(existingSandbox);

        assertThat(sandboxService.createIfEmpty(idSession)).isEqualTo(existingSandbox);

    }

    @Test
    public void createIfAbsent_should_bad_request_for_non_valid_command_is_null() {

        String idSession = "10";
        when(sandboxRepository.findOne(any(String.class))).thenReturn(null);
        when(sandboxRepository.save(any(ExecutionSandbox.class))).thenReturn(new ExecutionSandbox(idSession));


        ExecutionSandbox result = sandboxService.createIfEmpty(null);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(idSession);

    }


}
