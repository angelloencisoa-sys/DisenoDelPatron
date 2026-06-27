package pe.edu.utp.pf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.pf.service.patron.prototype.Contrato;

/**
 * Entidad central que representa el expediente o solicitud formal de financiamiento
 * registrada por un cliente y evaluada por la institución.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SolicitudCredito {

    /**
     * Identificador único de la solicitud de crédito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSolicitud;

    /**
     * Cantidad de dinero total que el cliente solicita en préstamo.
     */
    private Double montoSolicitado;

    /**
     * Periodo de tiempo en meses solicitado para amortizar la deuda.
     */
    private Integer plazoMeses;

    /**
     * Estado actual del trámite.
     */
    private String estado;

    /**
     * Cliente titular que realiza la postulación al crédito.
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    /**
     * Asesor financiero asignado para el estudio y seguimiento de este expediente.
     */
    @ManyToOne
    @JoinColumn(name = "asesor_id")
    private AsesorFinanciero asesor;

    /**
     * Informe técnico de evaluación de riesgos asociado a esta solicitud.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "evaluacion_id")
    private EvaluacionRiesgo evaluacion;

    /**
     * Bien o aval registrado en el sistema como resguardo de la operación.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "garantia_id")
    private Garantia garantia;

    /**
     * Contrato legal generado una vez que la solicitud pasa a estado aprobado.
     */
    @OneToOne(mappedBy = "solicitud")
    private Contrato contrato;
}
