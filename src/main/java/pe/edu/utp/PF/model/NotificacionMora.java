package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificacionMora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNotificacion;
    private LocalDate fechaEmision;
    private Integer diasRetraso;
    private String mensajeAlerta;

    @ManyToOne
    @JoinColumn(name = "cuota_id")
    private Cuota cuota;
}
