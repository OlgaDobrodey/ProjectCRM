package com.itrex.java.lab.crm.exceptions;

public class CRMProjectServiceException extends Exception{

    public CRMProjectServiceException(String message) {
        super(message);
    }

    public CRMProjectServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
