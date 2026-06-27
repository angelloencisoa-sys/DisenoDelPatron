package pe.edu.utp.pf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Representa al personal de la entidad financiera encargado de la gestión,
 * evaluación y seguimiento de las solicitudes de microcréditos.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AsesorFinanciero {

    /**
     * Identificador único autoincremental del asesor en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAsesor;

    /**
     * Nombre completo del asesor financiero.
     */
    private String nombreAsesor;

    /**
     * Código identificador de la agencia o sucursal a la que pertenece el asesor.
     */
    private String codigoAgencia;

    /**
     * Lista de solicitudes de crédito asignadas a este asesor para su evaluación.
     */
    @OneToMany(mappedBy = "asesor")
    private List<SolicitudCredito> solicitudes;
}
