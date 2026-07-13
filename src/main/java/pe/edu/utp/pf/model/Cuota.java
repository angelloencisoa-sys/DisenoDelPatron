package pe.edu.utp.pf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidad que define el desglose financiero, fechas límites y estados de pago
 * de una parcialidad individual dentro de un cronograma.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties
public class Cuota {

    /**
     * Identificador único de la cuota en el sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCuota;

    /**
     * Número correlativo de la cuota dentro del préstamo.
     */
    private Integer numeroCuota;

    /**
     * Fecha límite establecida para que el cliente liquide el monto de la cuota sin penalizaciones.
     */
    private LocalDate fechaVencimiento;

    /**
     * Fracción del monto que se destina directamente a amortizar el capital prestado.
     */
    private Double montoCapital;

    /**
     * Monto correspondiente al cobro del interés generado en el periodo.
     */
    private Double montoInteres;

    /**
     * Estado administrativo actual de la cuota.
     */
    private String estadoPago;

    /**
     * Plan de pagos general en el que está integrada esta cuota.
     */
    @ManyToOne
    @JoinColumn(name = "cronograma_id")
    private Cronograma cronograma;

    /**
     * Transacciones o abonos económicos realizados por el cliente dirigidos a esta cuota.
     */
    @OneToMany(mappedBy = "cuota")
    private List<Pago> pagos;

    /**
     * Historial de alertas enviadas al cliente en caso de que la cuota caiga en retraso.
     */
    @OneToMany(mappedBy = "cuota")
    private List<NotificacionMora> notificaciones;
}
