package pe.edu.utp.pf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Clase base que define las características globales y compartidas de los usuarios
 * del sistema que solicitan financiamientos (Raíz del Agregado de Clientes).
 *
 * @author Grupo 07
 * @version 2.0
 */
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

    /**
     * Listado de expedientes o solicitudes de crédito que el cliente ha tramitado.
     */
    @JsonIgnoreProperties({"cliente", "asesor", "evaluacion", "garantia", "contrato"})
    @OneToMany(mappedBy = "cliente")
    private List<SolicitudCredito> solicitudes;
}
