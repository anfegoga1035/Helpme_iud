package co.edu.iudigital.helpmeiud.services.impl;

import co.edu.iudigital.helpmeiud.dtos.ErrorDTO;
import co.edu.iudigital.helpmeiud.exceptions.NotFoundException;
import co.edu.iudigital.helpmeiud.models.Delito;
import co.edu.iudigital.helpmeiud.repositories.IDelitoRepository;
import co.edu.iudigital.helpmeiud.services.IDelitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DelitoServiceImpl implements IDelitoService {

    @Autowired
    private IDelitoRepository delitoRepository;

    @Override
    public Delito crearDelito(Delito delito) { // control con Excepciones
        delito = delitoRepository.save(delito);
        return delito;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Delito> obtenerDelitos() {
        List<Delito> delitos = delitoRepository.findAll();
        return delitos;
    }

    @Override
    public Delito obtenerDelitoPorId(Long id) throws NotFoundException {
        Delito delito = delitoRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorDTO.getErrorDto(
                        "Error de Delito", "No existe delito con Id: " + id, HttpStatus.NOT_FOUND.value()
                ))
        );
        return delito;
    }

    @Override
    public Delito actualizarDelitoPorId(Long id, Delito delito) {
        Delito delitoBD = delitoRepository.findById(id).orElseThrow( () -> new RuntimeException("No existe delito"));
        delitoBD.setNombre(delito.getNombre() != null ? delito.getNombre() : delitoBD.getNombre());
        delitoBD.setDescripcion(delito.getDescripcion() != null ? delito.getDescripcion() : delitoBD.getDescripcion());
        delitoBD = delitoRepository.save(delitoBD);
        return delitoBD;
    }

    @Override
    public void borrarDelitoPorId(Long id) {
        delitoRepository.deleteById(id);
    }
}
