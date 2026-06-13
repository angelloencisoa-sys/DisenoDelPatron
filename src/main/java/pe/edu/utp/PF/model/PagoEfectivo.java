package pe.edu.utp.PF.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad especializada que registra las transacciones monetarias liquidadas de forma física
 * directamente en ventanilla o cajas autorizadas de la institución.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagoEfectivo extends Pago{

    /** Código identificador del terminal físico o caja donde se recibió el dinero. */
    private String codigoCaja;

    /** Número correlativo impreso en el comprobante físico de pago. */
    private String numeroVoucher;
}
