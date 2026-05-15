package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;
    private String nombresCompletos;
    private String direccion;
    private String telefono;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "perfil_riesgo_id")
    private PerfilRiesgo perfilRiesgo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "historial_crediticio_id")
    private HistorialCrediticio historialCrediticio;

    @OneToMany(mappedBy = "cliente")
    private List<SolicitudCredito> solicitudes;
}
