package pe.edu.utp.PF.service;

import pe.edu.utp.PF.service.patron.prototype.Contrato;

public interface ContratoService {
    Contrato generarContratoDesdePlantilla(String tipoContrato, Integer idSolicitud);
}
