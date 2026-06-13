package pe.edu.utp.PF.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad especializada que describe los créditos otorgados a microempresas
 * o negocios independientes para financiar capital de trabajo o activos fijos.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditoMicroempresa extends Credito{

    /** El sector comercial o actividad económica de la empresa (Ej. Comercio, Manufactura). */
    private String rubroNegocio;

    /** Tiempo en meses que el negocio lleva operando de manera formal o comprobable. */
    private Integer tiempoOperacionMeses;
}
