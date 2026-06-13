package pe.edu.utp.PF.model;

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

    /** Identificador único del cliente dentro de la entidad financiera. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;

    /** Nombres y apellidos completos o denominación de la persona. */
    private String nombresCompletos;

    /** Dirección del domicilio fiscal o residencial del cliente. */
    private String direccion;

    /** Número telefónico de contacto del cliente. */
    private String telefono;

    /** Correo electrónico institucional o personal del cliente. */
    private String email;

    /** Perfil de riesgo asociado que determina la viabilidad de otorgarle préstamos. */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "perfil_riesgo_id")
    private PerfilRiesgo perfilRiesgo;

    /** Historial crediticio que detalla su comportamiento con créditos previos. */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "historial_crediticio_id")
    private HistorialCrediticio historialCrediticio;

    /** Listado de expedientes o solicitudes de crédito que el cliente ha tramitado. */
    @OneToMany(mappedBy = "cliente")
    private List<SolicitudCredito> solicitudes;
}
