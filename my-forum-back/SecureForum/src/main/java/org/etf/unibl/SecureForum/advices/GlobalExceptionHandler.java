package org.etf.unibl.SecureForum.advices;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.etf.unibl.SecureForum.exceptions.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.etf.unibl.SecureForum.additional.logging.LoggingUtil;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HandlerMethod handlerMethod){
        LoggingUtil.logException(e, getLog(handlerMethod));
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpException.class)
    public final ResponseEntity<Object> handleHttpException(HttpException e, HandlerMethod handlerMethod){
        Log log = getLog(handlerMethod);
        log.error(e);
        if(e.getStatus() == null)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(e.getData(), e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e, HandlerMethod handlerMethod){
        LoggingUtil.logException(e, getLog(handlerMethod));
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Log getLog(HandlerMethod handlerMethod){
        return LogFactory.getLog(handlerMethod.getMethod().getDeclaringClass());
    }

}
