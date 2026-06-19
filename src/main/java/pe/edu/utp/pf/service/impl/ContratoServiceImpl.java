package pe.edu.utp.pf.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pe.edu.utp.pf.exception.ServiceException;
import pe.edu.utp.pf.model.SolicitudCredito;
import pe.edu.utp.pf.repository.ContratoRepository;
import pe.edu.utp.pf.service.ContratoService;
import pe.edu.utp.pf.service.patron.prototype.Contrato;
import pe.edu.utp.pf.service.patron.prototype.GestorContrato;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Implementación de la interface ContratoService.
 * Encargada de gestionar la creación de contratos aplicando el patrón de diseño Prototype
 * para optimizar la generación de cláusulas repetitivas.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContratoServiceImpl implements ContratoService {

    private ContratoRepository repo;

    private GestorContrato gestorContrato; // Inyectamos tu Gestor

    /**
     * Recupera un contrato existente a partir de su identificador.
     *
     * @param id El ID del contrato que se desea consultar.
     * @return Optional con el contrato si fue hallado en base de datos.
     */
    @Transactional
    @Override
    public Optional<Contrato> getById(Integer id) {
        try {
            Optional<Contrato> contrato = repo.findById(id);
            if (contrato.isPresent()) {
                log.debug("Contrato encontrado: ID {}", id);
            } else {
                log.debug("Contrato no encontrado con ID: {}", id);
            }
            return contrato;
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al buscar contrato con ID {}: {}", id, e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error inesperado al buscar contrato con ID {}: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Genera un nuevo contrato mediante la clonación de un prototipo guardado en memoria,
     * vinculándolo posteriormente a una solicitud de crédito.
     *
     * @param tipoContrato El nombre de la plantilla a clonar (ej. "Consumo", "Microempresa").
     * @param idSolicitud El ID de la solicitud a la cual se anexa el contrato.
     * @return El contrato ya generado, personalizado y guardado en base de datos.
     * @throws RuntimeException si ocurre un fallo al clonar o al persistir en base de datos.
     */
    @Transactional
    @Override
    public Contrato generarContratoDesdePlantilla(String tipoContrato, Integer idSolicitud) {
        try {
            // 1. Clonar el objeto prototipo de forma segura usando el constructor de copia interno
            Contrato nuevoContrato = gestorContrato.obtenerContrato(tipoContrato);
            nuevoContrato.clonar();

            // 2. Personalizar campos especificos del negocio
            SolicitudCredito solicitud = new SolicitudCredito();
            solicitud.setIdSolicitud(idSolicitud);

            nuevoContrato.setSolicitud(solicitud);
            nuevoContrato.setFechaFirma(LocalDate.now(java.time.ZoneId.of("America/Lima")));

            // 3. Persistir el clon generado
            Contrato contratoGuardado = repo.save(nuevoContrato);
            log.info("Nuevo contrato de tipo {} generado exitosamente por Prototype con ID {}", tipoContrato, contratoGuardado.getIdContrato());

            return contratoGuardado;

        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al guardar contrato clonado: {}", e.getMessage());
            throw new ServiceException("Error de base de datos al crear contrato", e);
        } catch (Exception e) {
            log.error("Error inesperado al generar contrato por clonacion: {}", e.getMessage(), e);
            throw new ServiceException("Error interno del servidor", e);
        }
    }
}
