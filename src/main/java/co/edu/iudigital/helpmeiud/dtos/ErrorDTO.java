package co.edu.iudigital.helpmeiud.dtos;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class ErrorDTO implements Serializable {

    static final long serialVersionUID = 1L;

    String error;

    String message;

    int status;

    LocalDateTime date;

    /**
     * Obtiene nuevo error
     *
     * @param error   String Nombre error HTTP
     * @param message String Mensaje personalizado del error HTTP
     * @param status  int Codigo error HTTP
     * @return ErrorDto
     */
    public static ErrorDTO getErrorDto(String error, String message, int status) {
        return ErrorDTO.builder()
                .error(error)
                .message(message)
                .status(status)
                .date(LocalDateTime.now())
                .build();
    }
}