package co.edu.iudigital.helpmeiud.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CasoRequestDTO implements Serializable {

    static final long serialVersionUID = 1L;

    @JsonProperty("fecha_hora")
    LocalDateTime fechaHora;

    Float latitud;

    Float longitud;

    Float altitud;

    String detalle;

    @JsonProperty("url_map")
    String urlMap;

    @JsonProperty("rmi_map")
    String rmiMap;

    @JsonProperty("delito_id")
    Long delitoId;
}
