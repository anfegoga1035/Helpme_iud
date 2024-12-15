package co.edu.iudigital.helpmeiud.services;

import co.edu.iudigital.helpmeiud.exceptions.NotFoundException;
import co.edu.iudigital.helpmeiud.models.Delito;

import java.util.List;

public interface IDelitoService {

    // TODO: CAMBIAR TIPO OBJECT POR ENTITY O POR DTO y COLOCAR EXCEPTIONS
    // crear
    Delito crearDelito(Delito delito);

    // consultar todos
    List<Delito> obtenerDelitos();

    // consultar por id
    Delito obtenerDelitoPorId(Long id) throws NotFoundException;

    // modificar por id
    Delito actualizarDelitoPorId(Long id, Delito delito);

    // borrar por id
     void borrarDelitoPorId(Long id);
}
