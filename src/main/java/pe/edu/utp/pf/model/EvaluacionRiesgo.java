package pe.edu.utp.pf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad que registra el dictamen técnico, la decisión de aprobación y el sustento
 * elaborado por el analista sobre una solicitud de financiamiento.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EvaluacionRiesgo {

    /** Identificador único del reporte de evaluación de riesgo. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvaluacion;

    /** Fecha en la que el analista financiero emitió el dictamen. */
    private LocalDate fechaEvaluacion;

    /** Estado binario que indica si el crédito fue viable (true) o rechazado. */
    private Boolean esAprobado;

    /** Detalles técnicos, justificaciones o causales registradas por el personal evaluador. */
    private String observacionesAnalista;

    /** Expediente o solicitud de crédito evaluada en este proceso. */
    @OneToOne(mappedBy = "evaluacion")
    private SolicitudCredito solicitud;
}
