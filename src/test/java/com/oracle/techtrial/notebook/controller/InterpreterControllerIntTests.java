package com.oracle.techtrial.notebook.controller;


import com.oracle.techtrial.notebook.dto.InterpreterCommandDTO;
import com.oracle.techtrial.notebook.exception.GlobalExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static com.oracle.techtrial.notebook.controller.InterpreterController.SESSION_ID_HEADER;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureJsonTesters
@EnableSpringDataWebSupport
@TestPropertySource(locations= "classpath:application.yml")
@RunWith(SpringRunner.class)
public class InterpreterControllerIntTests {


    public static final MediaType APPLICATION_JSON_UTF_8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private MockMvc mockMvc;

    @Autowired
    private InterpreterController interpreterController;

    @Autowired
    private JacksonTester json;

    @Before
    public void setup() {

        this.mockMvc = standaloneSetup(this.interpreterController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void execute_should_return_result_written_in_std_out_when_no_variables_assigned() throws Exception {
        InterpreterCommandDTO commandDTO = new InterpreterCommandDTO();
        commandDTO.setCommand("%python print (1 + 1)");
        String commandJson = json.write(commandDTO).getJson();
        mockMvc.perform(post("/api/interpreters/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commandJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(header().string(SESSION_ID_HEADER, is(notNullValue())))
                .andExpect(jsonPath("result", is("2")));
    }

    @Test
    public void execute_should_return_empty_result_when_code_contain_variable_assignement() throws Exception {
        InterpreterCommandDTO commandDTO = new InterpreterCommandDTO();
        commandDTO.setCommand("%python a,b = 5");
        String commandJson = json.write(commandDTO).getJson();
        mockMvc.perform(post("/api/interpreters/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commandJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(header().string(SESSION_ID_HEADER, is(notNullValue())))
                .andExpect(jsonPath("result", is("")));
    }

    @Test
    public void execute_should_reuse_already_existing_variables() throws Exception {
        InterpreterCommandDTO commandDTO = new InterpreterCommandDTO();
        commandDTO.setCommand("%python a = 5");
        String commandJson = json.write(commandDTO).getJson();
        MvcResult result = mockMvc.perform(post("/api/interpreters/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commandJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String sessionId = result.getResponse().getHeader(SESSION_ID_HEADER);
        commandDTO = new InterpreterCommandDTO();
        commandDTO.setCommand("%python print ( 5 + a )");
        commandJson = json.write(commandDTO).getJson();

        mockMvc.perform(post("/api/interpreters/execute?sessionId=" + sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(commandJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(header().string(SESSION_ID_HEADER, is(notNullValue())))
                .andExpect(jsonPath("result", is("10")));

    }


    @Test
    public void execute_should_return_404_when_provided_sessionId_does_not_exist() throws Exception {
        InterpreterCommandDTO commandDTO = new InterpreterCommandDTO();
        commandDTO.setCommand("%python print (1 + 1)");
        String commandJson = json.write(commandDTO).getJson();
        mockMvc.perform(post("/api/interpreters/execute?sessionId=SIDE")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commandJson))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(header().string(SESSION_ID_HEADER, is(nullValue())))
                .andExpect(jsonPath("message", equalTo("Session Id [SIDE] not found")));
    }

    @Test
    public void execute_should_return_bad_request_when_non_valid_command() throws Exception {
        InterpreterCommandDTO commandDTO = new InterpreterCommandDTO();
        commandDTO.setCommand("%pythonprint(1 + 1)");
        String commandJson = json.write(commandDTO).getJson();
        mockMvc.perform(post("/api/interpreters/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commandJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(jsonPath("message", equalTo("Invalid command format")));
    }

    @Test
    public void execute_should_return_unprocessable_entity_when_non_valid_interpreter() throws Exception {
        InterpreterCommandDTO commandDTO = new InterpreterCommandDTO();
        commandDTO.setCommand("%js print(1 + 1)");
        String commandJson = json.write(commandDTO).getJson();
        mockMvc.perform(post("/api/interpreters/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commandJson))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(jsonPath("message", equalTo("Unsupported interpreter [js]")));
    }

// 4. CodeExecution exception

    @Test
    public void execute_should_return_bad_request_when_code_produce_an_interpreter_runtime_error() throws Exception {
        InterpreterCommandDTO commandDTO = new InterpreterCommandDTO();
        commandDTO.setCommand("%python 1/0");
        String commandJson = json.write(commandDTO).getJson();
        mockMvc.perform(post("/api/interpreters/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commandJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(content().string(containsString("[python] Failed to execute:[1/0] with message:")));

    }
}
