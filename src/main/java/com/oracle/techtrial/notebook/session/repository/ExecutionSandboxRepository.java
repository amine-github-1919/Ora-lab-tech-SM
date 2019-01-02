package com.oracle.techtrial.notebook.session.repository;

import com.oracle.techtrial.notebook.session.ExecutionSandbox;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionSandboxRepository extends CrudRepository<ExecutionSandbox, String> {
}
