package pe.edu.utp.PF.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad especializada que representa a empresas, microempresas u organizaciones
 * constituidas legalmente como personas jurídicas.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteJuridico extends Cliente{

    /** Registro Único de Contribuyentes de la empresa o negocio. */
    private String ruc;

    /** Nombre legal o razón social registrada de la organización. */
    private String razonSocial;

    /** Nombre de la persona natural facultada para firmar o solicitar el crédito a nombre de la empresa. */
    private String representanteLegal;

    /** Score o puntaje corporativo asignado en base al tamaño e historial de la empresa. */
    private Integer puntosCorporativos;
}
