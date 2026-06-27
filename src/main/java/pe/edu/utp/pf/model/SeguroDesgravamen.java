package pe.edu.utp.pf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa la póliza de seguro de desgravamen obligatoria u opcional,
 * cuyo fin es cubrir el saldo pendiente de la deuda ante contingencias del titular.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeguroDesgravamen {

    /**
     * Identificador único del registro de póliza en el sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSeguro;

    /**
     * Código identificador oficial del contrato con la compañía aseguradora externa.
     */
    private String codigoPoliza;

    /**
     * Prima o costo mensual adicionado de forma fija a las cuotas del préstamo.
     */
    private Double costoMensual;

    /**
     * Crédito principal al cual se le asocia y protege este seguro.
     */
    @ManyToOne
    @JoinColumn(name = "credito_id")
    private Credito credito;
}
