package co.edu.iudigital.helpmeiud.services.impl;

import co.edu.iudigital.helpmeiud.dtos.ErrorDTO;
import co.edu.iudigital.helpmeiud.dtos.requests.UsuarioRequestDTO;
import co.edu.iudigital.helpmeiud.dtos.requests.UsuarioRequestUpdateDTO;
import co.edu.iudigital.helpmeiud.dtos.response.UsuarioResponseDTO;
import co.edu.iudigital.helpmeiud.exceptions.BadRequestException;
import co.edu.iudigital.helpmeiud.exceptions.NotFoundException;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.mappers.UsuarioMapper;
import co.edu.iudigital.helpmeiud.models.Role;
import co.edu.iudigital.helpmeiud.models.User;
import co.edu.iudigital.helpmeiud.repositories.IRoleRepository;
import co.edu.iudigital.helpmeiud.repositories.IUserRepository;
import co.edu.iudigital.helpmeiud.security.UtilService;
import co.edu.iudigital.helpmeiud.services.IEmailService;
import co.edu.iudigital.helpmeiud.services.IUsuarioService;
import co.edu.iudigital.helpmeiud.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUserRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private UtilService utilService;

    @Override
    public UsuarioResponseDTO registrar(UsuarioRequestDTO usuarioRequestDTO) throws RestException {
        Optional<User> usuarioOpt = usuarioRepository.findByUsername(usuarioRequestDTO.getUsername());
        if(usuarioOpt.isPresent() ) {
            throw new BadRequestException(
                    ErrorDTO.builder()
                            .error("Error Usuario")
                            .message("Usuario ya existe")
                            .status(HttpStatus.BAD_REQUEST.value())
                            .date(LocalDateTime.now())
                            .build()
            );
        }


        User user = usuarioMapper.toUsuario(usuarioRequestDTO);

        Role role = roleRepository.findByNombre(Constants.ROLE_USER).orElseThrow(
                () -> new NotFoundException(ErrorDTO.getErrorDto(
                        "Error de Rol", "No existe rol" + Constants.ROLE_USER, HttpStatus.NOT_FOUND.value()
                ))
        );

        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = usuarioRepository.save(user);// TODO: MANIPULAR ERROR, AQUI PUEDE OCURRIR UNA EXCEPCION

        // TODO: COLOCARLO VOID Y ASYNCRONO
        emailService.sendMail(
                "Te has registrado exitosamente en HelpmeIUD con contraseña: " + usuarioRequestDTO.getPassword(),
                user.getUsername(),
                "Te has dado de Alta en HelpmeIUD"
        );

        return usuarioMapper.toUsuarioResponseDTO(user);
    }

    @Override
    public User findByUsername(String username) throws RestException {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(ErrorDTO.getErrorDto(
                        "Error de Usuario", "No existe usuario" + username, HttpStatus.NOT_FOUND.value()
                )));
    }

    @Override
    public UsuarioResponseDTO consutarPorId(Long id) throws RestException {
        return null;
    }

    @Override
    public UsuarioResponseDTO consutarPorUsername() throws RestException { // usuario logueado
        final String username = utilService.obtenerUsuarioActual();
        User user = usuarioRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(ErrorDTO.getErrorDto(
                        "Error de Usuario", "No existe usuario" + username, HttpStatus.NOT_FOUND.value()
                )));
        return usuarioMapper.toUsuarioResponseDTO(user);
    }

    @Override
    public UsuarioResponseDTO actualizar(UsuarioRequestUpdateDTO usuarioRequestUpdateDTO) throws RestException {
        final String username = utilService.obtenerUsuarioActual();
        User user = usuarioRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(ErrorDTO.getErrorDto(
                        "Error de Usuario", "No existe usuario" + username, HttpStatus.NOT_FOUND.value()
                )));
        user.setNombre(usuarioRequestUpdateDTO.getNombre() != null ? usuarioRequestUpdateDTO.getNombre() : user.getNombre());
        user.setApellidos(usuarioRequestUpdateDTO.getApellidos() != null ? usuarioRequestUpdateDTO.getApellidos() : user.getApellidos());
        user.setPassword(
                usuarioRequestUpdateDTO.getPassword() != null ||  usuarioRequestUpdateDTO.getPassword().length() > 0
                    ? passwordEncoder.encode(usuarioRequestUpdateDTO.getPassword()) : user.getPassword());
        user.setFechaNacimiento(
                usuarioRequestUpdateDTO.getFechaNacimiento() != null
                        ? usuarioRequestUpdateDTO.getFechaNacimiento() : user.getFechaNacimiento()
        );
        user = usuarioRepository.save(user);
        return usuarioMapper.toUsuarioResponseDTO(user);
    }

    @Override
    public UsuarioResponseDTO subirImagen(MultipartFile image) throws RestException {
        String username = utilService.obtenerUsuarioActual();
        User user = usuarioRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(
                        ErrorDTO.getErrorDto(
                        "Error de Usuario", "No existe usuario" + username, HttpStatus.NOT_FOUND.value()
                        )
                )
        );
        // TODO: VALIDAR QUE SE SUBAN SOLO FOTOS.
        if(!image.isEmpty()) {
            String nombreImage = UUID
                    .randomUUID()
                    .toString()
                    .concat("_")
                    .concat(image.getOriginalFilename())
                    .replace(" ", "");
            Path path = Paths.get("uploads").resolve(nombreImage).toAbsolutePath();
            try {
                Files.copy(image.getInputStream(), path);
            } catch (IOException e) {
                log.error("Error {}", e.getCause());
                throw new BadRequestException(
                        ErrorDTO.builder()
                                .message(e.getMessage())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Error De imagen")
                                .build()
                );
            }

            String imageAnterior = user.getImage();

            if(imageAnterior != null && imageAnterior.length() > 0 && !imageAnterior.startsWith("http")){
                Path pathAnterior = Paths.get("uploads").resolve(imageAnterior).toAbsolutePath();
                File fileAnterior = pathAnterior.toFile();
                if(fileAnterior.exists() && fileAnterior.canRead()) {
                    fileAnterior.delete();
                }
            }

            user.setImage(nombreImage);

            usuarioRepository.save(user);

        }
        return usuarioMapper.toUsuarioResponseDTO(user);
    }

    @Override
    public Resource obtenerImagen(String nombre) throws RestException {
        Path path = Paths.get("uploads").resolve(nombre).toAbsolutePath();
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
            if(!resource.exists()) {
                path = Paths.get("uploads").resolve("default.jpg").toAbsolutePath();
                resource = new UrlResource(path.toUri());
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
        return resource;
    }
}
