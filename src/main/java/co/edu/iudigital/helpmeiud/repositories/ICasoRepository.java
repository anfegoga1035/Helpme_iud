package co.edu.iudigital.helpmeiud.repositories;

import co.edu.iudigital.helpmeiud.models.Caso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICasoRepository extends JpaRepository<Caso, Long> {

    List<Caso> findByIsVisibleTrue(); // SELECT * FROM casos WHERE is_visible=1;
}
