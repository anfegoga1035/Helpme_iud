package co.edu.iudigital.helpmeiud.mappers;

import co.edu.iudigital.helpmeiud.dtos.requests.UsuarioRequestDTO;
import co.edu.iudigital.helpmeiud.dtos.response.UsuarioResponseDTO;
import co.edu.iudigital.helpmeiud.models.User;
import org.springframework.stereotype.Component;

@Component // se puede usar MapStruct
public class UsuarioMapper {

    public User toUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        User user = new User();
        user.setUsername(usuarioRequestDTO.getUsername());
        user.setNombre(usuarioRequestDTO.getNombre());
        user.setApellidos(usuarioRequestDTO.getApellidos());
        user.setPassword(usuarioRequestDTO.getPassword());
        user.setFechaNacimiento(usuarioRequestDTO.getFechaNacimiento());
        return user;
    }

    public UsuarioResponseDTO toUsuarioResponseDTO(User user) {
        return UsuarioResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .fechaNacimiento(user.getFechaNacimiento())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
