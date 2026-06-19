package pe.edu.utp.pf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.pf.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
}
