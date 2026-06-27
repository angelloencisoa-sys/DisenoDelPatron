package pe.edu.utp.pf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.pf.service.patron.prototype.Contrato;

import java.time.LocalDate;
import java.util.List;

/**
 * Clase padre que administra las condiciones operativas de un préstamo aprobado y
 * desembolsado (Estructura central del Agregado de Créditos).
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
public class Credito {

    /**
     * Código único identificador de la cuenta del crédito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCredito;

    /**
     * Monto neto total del capital que se otorga en préstamo.
     */
    private Double capitalPrestado;

    /**
     * Tasa de interés pactada calculada en base anual para el cobro del servicio financiero.
     */
    private Double tasaInteresAnual;

    /**
     * Fecha en la que los fondos del crédito son efectivamente entregados al cliente.
     */
    private LocalDate fechaDesembolso;

    /**
     * Estado actual de la deuda.
     */
    private String estadoCredito;

    /**
     * Documento legal del contrato asociado a este préstamo.
     */
    @OneToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    /**
     * Plan de pagos o cronograma asignado para la amortización del crédito.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cronograma_id")
    private Cronograma cronograma;

    /**
     * Seguros de desgravamen asociados que cubren el saldo de la deuda frente a imprevistos.
     */
    @OneToMany(mappedBy = "credito")
    private List<SeguroDesgravamen> seguros;
}
