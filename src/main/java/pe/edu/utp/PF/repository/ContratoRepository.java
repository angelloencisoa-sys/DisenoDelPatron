package pe.edu.utp.PF.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.PF.service.patron.prototype.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
}
