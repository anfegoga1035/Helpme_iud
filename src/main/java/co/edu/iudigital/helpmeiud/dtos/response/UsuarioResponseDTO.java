package co.edu.iudigital.helpmeiud.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class UsuarioResponseDTO {

    Long id;

    String username;

    String nombre;

    String apellidos;

    @JsonProperty("fecha_nacimiento")
    LocalDate fechaNacimiento;

    @JsonProperty("created_at")
    LocalDateTime createdAt;
}
