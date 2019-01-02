package com.oracle.techtrial.notebook.controller;


import com.oracle.techtrial.notebook.dto.CommandResultDTO;
import com.oracle.techtrial.notebook.dto.InterpreterCommandDTO;
import com.oracle.techtrial.notebook.service.NotebookService;
import com.oracle.techtrial.notebook.session.ExecutionSandbox;
import com.oracle.techtrial.notebook.session.service.ExecutionSandboxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/api/interpreters")
public class InterpreterController {

    public static final String SESSION_ID_HEADER = "SESSION-ID";
    private final Logger logger = LoggerFactory.getLogger(InterpreterController.class);

    private final NotebookService notebookService;

    private final ExecutionSandboxService executionSandboxService;

    public InterpreterController(NotebookService notebookService, ExecutionSandboxService executionSandboxService) {
        this.notebookService = notebookService;
        this.executionSandboxService = executionSandboxService;
    }

    @PostMapping(path = "/execute", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandResultDTO> execute(@Valid @RequestBody InterpreterCommandDTO commandDTO,
                                                    @RequestParam(value = "sessionId", required = false) String sessionId) {
        logger.info("sessionId: {} / command: {}", sessionId, commandDTO.getCommand());
        ExecutionSandbox sandbox = executionSandboxService.createIfEmpty(sessionId);
        String result = notebookService.execute(commandDTO, sandbox);
        logger.info("sessionId: {} / command: {}", sessionId, result);
        return createResponseEntity(result, sandbox.getId());
    }

    private ResponseEntity<CommandResultDTO> createResponseEntity(String result, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SESSION_ID_HEADER, sessionId);
        return ResponseEntity.ok()
                .headers(headers)
                .body(new CommandResultDTO(result));
    }


}
