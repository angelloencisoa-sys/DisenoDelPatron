package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad que registra las alertas y comunicaciones emitidas de manera automática o manual
 * hacia los clientes que presentan retrasos en sus cuotas.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificacionMora {

    /** Identificador único del registro de notificación de mora. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNotificacion;

    /** Fecha exacta en la que se generó y envió el aviso de alerta. */
    private LocalDate fechaEmision;

    /** Cantidad de días transcurridos desde el vencimiento de la cuota hasta la emisión del aviso. */
    private Integer diasRetraso;

    /** Texto o cuerpo informativo de la advertencia enviado al cliente. */
    private String mensajeAlerta;

    /** Cuota específica vencida que originó el disparo de esta alerta de cobro. */
    @ManyToOne
    @JoinColumn(name = "cuota_id")
    private Cuota cuota;
}
