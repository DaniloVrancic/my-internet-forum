package org.etf.unibl.SecureForum.exceptions.servlet_exceptions;

import jakarta.servlet.ServletException;

public class XSSAttackException extends ServletException {
    public XSSAttackException(String message) {
        super(message);
    }
}
