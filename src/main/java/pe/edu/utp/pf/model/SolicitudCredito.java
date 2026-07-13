package pe.edu.utp.pf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSolicitud;

    private Double montoSolicitado;
    private Integer plazoMeses;
    private String estado;

    /**
     * Cliente titular que realiza la postulación al crédito.
     */
    @JsonIgnoreProperties("solicitudes") // 👈 SOLUCIÓN: Rompe el bucle con Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    /**
     * Asesor financiero asignado para el estudio y seguimiento de este expediente.
     */
    @JsonIgnoreProperties("solicitudes")
    @ManyToOne
    @JoinColumn(name = "asesor_id")
    private AsesorFinanciero asesor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "evaluacion_id")
    private EvaluacionRiesgo evaluacion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "garantia_id")
    private Garantia garantia;

    @OneToOne(mappedBy = "solicitud")
    private Contrato contrato;
}
