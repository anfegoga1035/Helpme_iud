package co.edu.iudigital.helpmeiud.mappers;

import co.edu.iudigital.helpmeiud.dtos.requests.CasoRequestDTO;
import co.edu.iudigital.helpmeiud.dtos.response.CasoResponseDTO;
import co.edu.iudigital.helpmeiud.models.Caso;
import co.edu.iudigital.helpmeiud.models.Delito;
import co.edu.iudigital.helpmeiud.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CasoMapper {

    public Caso toCaso(CasoRequestDTO casoRequestDTO) {
        Caso caso = new Caso();
        caso.setFechaHora(casoRequestDTO.getFechaHora());
        caso.setLatitud(casoRequestDTO.getLatitud());
        caso.setLongitud(casoRequestDTO.getLongitud());
        caso.setAltitud(casoRequestDTO.getAltitud());
        caso.setDetalle(casoRequestDTO.getDetalle());
        caso.setUrlMap(casoRequestDTO.getUrlMap());
        caso.setRmiMap(casoRequestDTO.getRmiMap());
        return caso;
    }

    public Caso toCaso(CasoRequestDTO casoRequestDTO, User user, Delito delito) {
        Caso caso = new Caso();
        caso.setFechaHora(casoRequestDTO.getFechaHora());
        caso.setLatitud(casoRequestDTO.getLatitud());
        caso.setLongitud(casoRequestDTO.getLongitud());
        caso.setAltitud(casoRequestDTO.getAltitud());
        caso.setDetalle(casoRequestDTO.getDetalle());
        caso.setUrlMap(casoRequestDTO.getUrlMap());
        caso.setRmiMap(casoRequestDTO.getRmiMap());

        caso.setDelito(delito);
        caso.setUser(user);

        return caso;
    }

    public CasoResponseDTO toCasoResponseDTO(Caso caso) {
        return  CasoResponseDTO.builder()
                .id(caso.getId())
                .fechaHora(caso.getFechaHora())
                .latitud(caso.getLatitud())
                .longitud(caso.getLongitud())
                .altitud(caso.getAltitud())
                .detalle(caso.getDetalle())
                .urlMap(caso.getUrlMap())
                .rmiMap(caso.getRmiMap())
                .delito(caso.getDelito().getNombre())
                .createdAt(caso.getCreatedAt())
                .usuario(caso.getUser().getUsername())
                .isVisible(caso.getIsVisible())
                .build();
    }

    public List<CasoResponseDTO> toCasoResponseDTOs(List<Caso> casos) {
        return casos.stream()
                        .map(caso -> toCasoResponseDTO(caso))
                        .collect(Collectors.toList());
    }
}
