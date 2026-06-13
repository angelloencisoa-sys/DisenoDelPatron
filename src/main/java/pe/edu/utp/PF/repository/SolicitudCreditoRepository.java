package pe.edu.utp.PF.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.PF.model.SolicitudCredito;

import java.util.List;

public interface SolicitudCreditoRepository extends JpaRepository<SolicitudCredito, Integer> {

    List<SolicitudCredito> findByEstado(String estado);
}
