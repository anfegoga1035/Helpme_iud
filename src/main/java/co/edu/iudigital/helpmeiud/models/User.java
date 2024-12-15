package co.edu.iudigital.helpmeiud.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User implements Serializable {

    static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotBlank(message = "Username requerido")
    @Column(nullable = false, unique = true, length = 120)
    String username;

    @Column(nullable = false, length = 120)
    String nombre;

    @Column(length = 120)
    String apellidos;

    @NotBlank(message = "Password requerido")
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @Column
    String password;

    @JsonProperty("fecha_nacimiento")
    @Column(name = "fecha_nacimiento")
    LocalDate fechaNacimiento;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    Boolean enabled;

    @Column
    String image;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        if(enabled == null) {
            enabled = true;
        }
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        if(this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_users",
            joinColumns = {@JoinColumn(name = "users_id")},
            inverseJoinColumns = {@JoinColumn(name = "roles_id")},
            uniqueConstraints = {
                    @UniqueConstraint(
                       columnNames = {"users_id", "roles_id"}
                    )
            }
    )
    List<Role> roles;
}
