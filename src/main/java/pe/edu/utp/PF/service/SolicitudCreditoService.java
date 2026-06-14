package pe.edu.utp.PF.service;

import pe.edu.utp.PF.model.SolicitudCredito;

import java.util.List;
import java.util.Optional;

public interface SolicitudCreditoService {

    Optional<SolicitudCredito> getById(Integer id);
    List<SolicitudCredito> getAll();
    SolicitudCredito create(SolicitudCredito solicitud);
    SolicitudCredito update(SolicitudCredito old, SolicitudCredito solicitud);
    void deleteById(Integer id);
}
