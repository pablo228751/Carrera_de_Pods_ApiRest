package ar.com.pablofelice.carreraDePods.persistence.repository;

import ar.com.pablofelice.carreraDePods.persistence.entity.DatosAntena;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AntenaRepository extends JpaRepository<DatosAntena, Long> {
    
}
