package co.edu.iudigital.helpmeiud.controllers;

import co.edu.iudigital.helpmeiud.dtos.requests.UsuarioRequestDTO;
import co.edu.iudigital.helpmeiud.dtos.requests.UsuarioRequestUpdateDTO;
import co.edu.iudigital.helpmeiud.dtos.response.UsuarioResponseDTO;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.services.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "Usuarios Controller",
        description = "Controller de gestión de Usuarios del Sistema"
)
@CrossOrigin(
        origins = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.HEAD},
        allowedHeaders = {"Content-Type", "Authorization"}
)
@RestController
@RequestMapping("/users")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;


    @Operation(
            summary = "Registro en HelpmeIUD",
            description = "Registra un usuario en HelmeIUD"
    )
    @PostMapping("/signup")
    public ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody UsuarioRequestDTO usuarioRequestDTO)
            throws RestException {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.registrar(usuarioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDTO);
    }

    @Operation(
            summary = "Consultar perfil de usuario logueado",
            description = "Consultar perfil de usuario logueado"
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<UsuarioResponseDTO> consultarPerfil() throws RestException {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.consutarPorUsername();
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @Operation(
            summary = "Actualizar perfil de usuario logueado",
            description = "Actualizar perfil de usuario logueado"
    )
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/profile")
    public ResponseEntity<UsuarioResponseDTO>  actualizarPerfil(
            @RequestBody UsuarioRequestUpdateDTO usuarioRequestUpdateDTO
    ) throws RestException {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.actualizar(usuarioRequestUpdateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDTO);
    }


    @Operation(
            summary = "Subir foto de perfil",
            description = "Sube archivo de foto de perfil de un usuario"
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload")
    public ResponseEntity<UsuarioResponseDTO> subirImagen(@RequestParam("image") MultipartFile image)
            throws RestException {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.subirImagen(image);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDTO);
    }


    @Operation(
            summary = "Ver foto de perfil",
            description = "Ver foto de perfil de un usuario logueado"
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/upload/img/{name:.+}")
    public ResponseEntity<Resource> verImagen(
                @PathVariable String name
            ) throws RestException {
        Resource imagen = usuarioService.obtenerImagen(name);
        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + imagen.getFilename()
        );
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
}
