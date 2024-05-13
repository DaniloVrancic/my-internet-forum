package org.etf.unibl.SecureForum.exceptions;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException{

    private HttpStatus status;
    private Object data;

    public HttpException(){
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.data = null;
    }

    public HttpException(Object data) { this(HttpStatus.INTERNAL_SERVER_ERROR, data);}

    public HttpException(HttpStatus status, Object data)
    {
        this.status = status;
        this.data = data;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toString()
    {
        return super.toString() + System.lineSeparator() +
                "Data: " + (data != null ? data.toString() : "") + System.lineSeparator() +
                "Status: " + this.status.toString();
    }

}
