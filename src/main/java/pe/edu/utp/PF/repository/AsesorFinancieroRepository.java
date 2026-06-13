package pe.edu.utp.PF.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.PF.model.AsesorFinanciero;

public interface AsesorFinancieroRepository extends JpaRepository<AsesorFinanciero, Long> {

    java.util.List<AsesorFinanciero> findByCodigoAgencia(String codigoAgencia);
}
