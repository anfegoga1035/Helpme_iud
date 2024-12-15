package co.edu.iudigital.helpmeiud.exceptions;

import co.edu.iudigital.helpmeiud.dtos.ErrorDTO;


public class NotFoundException extends RestException {

    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super();
    }

    public NotFoundException(ErrorDTO errorDto) {
        super(errorDto);
    }
}