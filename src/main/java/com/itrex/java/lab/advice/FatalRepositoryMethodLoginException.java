package com.itrex.java.lab.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class FatalRepositoryMethodLoginException {

    private static final String MESSAGE_LOG_FATAL_REPOSITORY_METHOD = "\n\nERROR: %s\n\n";

    @AfterThrowing(value = "execution(* com.itrex.java.lab.repository.*.*(..))", throwing = "ex")
    public void methodLoggingException(JoinPoint jp, Exception ex) throws Throwable {
        log.error(String.format(MESSAGE_LOG_FATAL_REPOSITORY_METHOD,
                jp.getSignature().toLongString()), ex);
    }
}
