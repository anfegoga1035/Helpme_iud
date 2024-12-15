package co.edu.iudigital.helpmeiud.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class UsuarioRequestDTO implements Serializable {

    static final long serialVersionUID = 1L;

    @NotNull(message = "Username requerido")
    @Email(message = "Email no válido")
    String username;

    @NotNull(message = "Nombre requerido")
    String nombre;

    @NotNull(message = "Apellidos requerido")
    String apellidos;

    String password;

    @JsonProperty("fecha_nacimiento")
    LocalDate fechaNacimiento;
}
