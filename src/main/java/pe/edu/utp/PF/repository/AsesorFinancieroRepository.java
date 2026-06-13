package pe.edu.utp.PF.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.PF.model.AsesorFinanciero;

import java.util.List;

public interface AsesorFinancieroRepository extends JpaRepository<AsesorFinanciero, Long> {

    List<AsesorFinanciero> findByCodigoAgencia(String codigoAgencia);
}
