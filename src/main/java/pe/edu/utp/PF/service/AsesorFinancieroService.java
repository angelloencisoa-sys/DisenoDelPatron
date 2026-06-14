package pe.edu.utp.PF.service;

import pe.edu.utp.PF.model.AsesorFinanciero;

import java.util.List;
import java.util.Optional;

public interface AsesorFinancieroService {

    Optional<AsesorFinanciero> getById(Integer id);
    List<AsesorFinanciero> getAll();
    AsesorFinanciero create(AsesorFinanciero asesor);
    AsesorFinanciero update(AsesorFinanciero old, AsesorFinanciero asesor);
    void deleteById(Integer id);
}
