package org.etf.unibl.SecureForum.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateEntryException extends HttpException{

    public DuplicateEntryException() {
        super(HttpStatus.CONFLICT, null);
    }

    public DuplicateEntryException(Object data)
    {
        super(HttpStatus.CONFLICT, data);
    }
}
