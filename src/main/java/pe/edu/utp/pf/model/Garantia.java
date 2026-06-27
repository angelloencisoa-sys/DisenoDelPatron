package pe.edu.utp.pf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase abstracta base que representa los bienes, avales o derechos entregados por el
 * cliente para respaldar la obligación de pago del microcrédito.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class Garantia {

    /**
     * Identificador único del registro de la garantía en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idGarantia;

    /**
     * Valor económico del bien determinado mediante tasación profesional.
     */
    private Double valorTasacion;

    /**
     * Resumen de las condiciones, estado o características generales del respaldo.
     */
    private String descripcion;

    /**
     * Solicitud de crédito a la que se anexa este respaldo económico para mitigar el riesgo.
     */
    @OneToOne(mappedBy = "garantia")
    private SolicitudCredito solicitud;
}