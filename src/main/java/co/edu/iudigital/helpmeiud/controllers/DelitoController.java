package co.edu.iudigital.helpmeiud.controllers;

import co.edu.iudigital.helpmeiud.exceptions.NotFoundException;
import co.edu.iudigital.helpmeiud.models.Delito;
import co.edu.iudigital.helpmeiud.services.IDelitoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Delitos Controller",
        description = "Controller de gestión de Delitos"
)
@CrossOrigin(
        origins = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.HEAD},
        allowedHeaders = {"Content-Type", "Authorization"}
)
@RestController
@RequestMapping("/delitos")
public class DelitoController {

    @Autowired
    private IDelitoService delitoService;

    @Operation(
            summary = "Crear delito",
            description = "Crear un delito"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Delito> guardarDelito(@RequestBody Delito delito) {
        Delito delitoResponse = delitoService.crearDelito(delito);
        return ResponseEntity.status(HttpStatus.CREATED).body(delitoResponse);
    }

    @Operation(
            summary = "Consultar Delitos",
            description = "Consulta todos los delitos"
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Delito>> consultarTodos() {
        List<Delito> delitos = delitoService.obtenerDelitos();
        return ResponseEntity.ok(delitos);
    }

    @Operation(
            summary = "Consultar un Delito",
            description = "Consulta un delito por su ID"
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Delito> consultarPorId(@PathVariable Long id) throws NotFoundException {
        Delito delito = delitoService.obtenerDelitoPorId(id);
        return ResponseEntity.ok(delito);
    }

    @Operation(
            summary = "Actualizar delito por ID",
            description = "Actualizar un delito por su ID"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Delito> actualizarPorId(@RequestBody Delito delito, @PathVariable Long id) {
        Delito delitoBD = delitoService.actualizarDelitoPorId(id, delito);
        return ResponseEntity.status(HttpStatus.CREATED).body(delitoBD);
    }

    @Operation(
            summary = "Borrar delito por ID",
            description = "Eliminar un delito por su ID"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrarPorId(@PathVariable Long id) {
        delitoService.borrarDelitoPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
