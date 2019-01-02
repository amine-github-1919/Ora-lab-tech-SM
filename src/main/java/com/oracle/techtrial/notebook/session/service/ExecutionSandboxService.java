package com.oracle.techtrial.notebook.session.service;

import com.oracle.techtrial.notebook.exception.SessionNotFoundException;
import com.oracle.techtrial.notebook.session.ExecutionSandbox;
import com.oracle.techtrial.notebook.session.repository.ExecutionSandboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class ExecutionSandboxService {

    private final Logger logger = LoggerFactory.getLogger(ExecutionSandboxService.class);

    private final ExecutionSandboxRepository sessionRepository;

    public ExecutionSandboxService(ExecutionSandboxRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public ExecutionSandbox createIfEmpty(String sessionId) {
        logger.info("Looking for Session with sessionId : {}", sessionId);

        if (!StringUtils.isEmpty(sessionId)) {
            ExecutionSandbox session = sessionRepository.findOne(sessionId);
            if (session != null) {
                logger.info("Found Session with sessionId : {}", sessionId);
                return session;
            } else {
                throw new SessionNotFoundException(sessionId);
            }
        }
        logger.info("No sessionId provided creating a new Session");
        ExecutionSandbox newSession = new ExecutionSandbox(UUID.randomUUID().toString().replace("-", ""));
        return sessionRepository.save(newSession);
    }


    public ExecutionSandbox updateSession(ExecutionSandbox session) {
        logger.info("Updating Session :{} ", session);
        return sessionRepository.save(session);
    }
}
