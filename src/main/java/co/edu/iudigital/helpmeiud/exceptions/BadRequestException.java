package co.edu.iudigital.helpmeiud.exceptions;

import co.edu.iudigital.helpmeiud.dtos.ErrorDTO;

public class BadRequestException extends RestException {

    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(ErrorDTO errorDto) {
        super(errorDto);
    }

    public BadRequestException(String msg) {
        super(msg);
    }
}