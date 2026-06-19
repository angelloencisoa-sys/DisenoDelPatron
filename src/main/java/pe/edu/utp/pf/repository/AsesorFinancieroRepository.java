package pe.edu.utp.pf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.pf.model.AsesorFinanciero;

import java.util.List;

public interface AsesorFinancieroRepository extends JpaRepository<AsesorFinanciero, Integer> {

    List<AsesorFinanciero> findByCodigoAgencia(String codigoAgencia);
}
