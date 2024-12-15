package co.edu.iudigital.helpmeiud.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class CasoResponseDTO {

    Long id;

    @JsonProperty("fecha_hora")
    LocalDateTime fechaHora;

    Float latitud;

    Float longitud;

    Float altitud;

    @JsonProperty("is_visible")
    Boolean isVisible;

    String detalle;

    @JsonProperty("url_map")
    String urlMap;

    @JsonProperty("rmi_map")
    String rmiMap;

    @JsonProperty("nombre_delito")
    String delito;

    @JsonProperty("username")
    String usuario;

    @JsonProperty("created_at")
    LocalDateTime createdAt;
}
