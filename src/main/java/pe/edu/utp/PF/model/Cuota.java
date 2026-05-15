package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cuota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCuota;
    private Integer numeroCuota;
    private LocalDate fechaVencimiento;
    private Double montoCapital;
    private Double montoInteres;
    private String estadoPago;

    @ManyToOne
    @JoinColumn(name = "cronograma_id")
    private Cronograma cronograma;

    @OneToMany(mappedBy = "cuota")
    private List<Pago> pagos;

    @OneToMany(mappedBy = "cuota")
    private List<NotificacionMora> notificaciones;
}
