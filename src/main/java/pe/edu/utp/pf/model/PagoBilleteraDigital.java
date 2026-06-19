package pe.edu.utp.pf.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad especializada que registra los pagos efectuados de manera electrónica
 * mediante aplicativos móviles o canales digitales (Ej. Yape, Plin).
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagoBilleteraDigital extends Pago{

    /** Número telefónico celular desde el cual el usuario emitió los fondos. */
    private String numeroCelularOrigen;

    /** Empresa proveedora de la billetera o pasarela (Ej. BCP, Interbank). */
    private String operadora;

    /** Código, número de operación o token de conformidad provisto por la app. */
    private String numeroAprobacion;
}
