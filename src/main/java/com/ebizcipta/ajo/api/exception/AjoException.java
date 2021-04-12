package com.ebizcipta.ajo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class AjoException extends RuntimeException {
    private StatusCode code = StatusCode.ERROR;

    public AjoException() {
        super();
    }

    public AjoException(String message) {
        super(message);
    }

    public AjoException(StatusCode code, String message) {
        super(message);
        this.code = code;
    }

    public StatusCode getCode() {
        return code;
    }

    public void setCode(StatusCode code) {
        this.code = code;
    }
}
