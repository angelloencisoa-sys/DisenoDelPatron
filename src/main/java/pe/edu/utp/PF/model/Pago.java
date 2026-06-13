package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Clase abstracta base que define la estructura transaccional común de cualquier
 * abono o ingreso de dinero al sistema financiero.
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
public abstract class Pago {

    /** Identificador único correlativo de la transacción de pago. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPago;

    /** Marca de tiempo exacta (fecha y hora) en la que se procesó el abono. */
    private LocalDateTime fechaHoraTransaccion;

    /** Importe monetario neto que el cliente entrega para amortizar su deuda. */
    private Double montoAbonado;

    /** Cuota específica del cronograma a la cual se le aplica el dinero recaudado. */
    @ManyToOne
    @JoinColumn(name = "cuota_id")
    private Cuota cuota;
}
