package com.itrex.java.lab.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ResultExecutionRepositoryMethodsStartNameSelect {

    private static final String RESULT_REPOSITORY_METHOD_NAME_START_SELECT =
            "\n\n\nREPOSITORY'S METHOD:\n %s\n result: %s\n";

    @Pointcut("execution(* com.itrex.java.lab.repository.*.select*(..))")
    public void repositoryMethodsStartNameSelect() {
    }

    @AfterReturning(value = "repositoryMethodsStartNameSelect()", returning = "entity")
    public void methodLoggingResult(JoinPoint jp, Object entity) throws Throwable {
        log.info(String.format(RESULT_REPOSITORY_METHOD_NAME_START_SELECT, jp.getSignature().toLongString(), entity));
    }

    @AfterThrowing(value = "repositoryMethodsStartNameSelect()", throwing = "ex")
    public void methodLoggingException(Exception ex) throws Throwable {
        log.error(ex.getStackTrace().toString());
    }
}
