package co.edu.iudigital.helpmeiud.controllers;

import co.edu.iudigital.helpmeiud.dtos.requests.CasoRequestDTO;
import co.edu.iudigital.helpmeiud.dtos.response.CasoResponseDTO;
import co.edu.iudigital.helpmeiud.exceptions.NotFoundException;
import co.edu.iudigital.helpmeiud.services.ICasoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Caso Controller",
        description = "Controller para gestión de los casos"
)
@CrossOrigin(
        origins = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.HEAD},
        allowedHeaders = {"Content-Type", "Authorization"}
)
@RestController
@RequestMapping("/casos")
public class CasoController {

    @Autowired
    private ICasoService casoService;

    @Operation(
            summary = "Reportar un caso",
            description = "Crear un caso de algún delito"
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<CasoResponseDTO> crearCaso(@RequestBody CasoRequestDTO casoRequestDTO) throws NotFoundException {// TODO: IMPLEMENTAR AUTENTICACION
        CasoResponseDTO responseDTO = casoService.crearCaso(casoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(
            summary = "Consultar casos visibles",
            description = "Consultar casos visibles en el mapa"
    )
    @GetMapping("/visibles")
    public ResponseEntity<List<CasoResponseDTO>> consultarVisibles() {
        List<CasoResponseDTO> casos = casoService.consultarCasosVisibles();
        return ResponseEntity.ok(casos);
    }

    @Operation(
            summary = "Consultar todos los casos",
            description = "Consultar todos los casos en el mapa"
    )
    @GetMapping
    public ResponseEntity<List<CasoResponseDTO>> consultarTodos() {
        List<CasoResponseDTO> casos = casoService.consultarCasos();
        return ResponseEntity.ok(casos);
    }

    @Operation(
            summary = "Cambiar caso a visible u ocultarlo",
            description = "Cambiar un caso a visible u ocultarlo"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/visible/{is_visible}")
    public ResponseEntity<CasoResponseDTO> cambiarVisibilidad(
            @PathVariable final Long id,
            @PathVariable("is_visible") final Integer visible
    ) throws NotFoundException {
        CasoResponseDTO caso = casoService.visibilizarCaso(id, visible);
        return ResponseEntity.ok(caso);
    }
}
