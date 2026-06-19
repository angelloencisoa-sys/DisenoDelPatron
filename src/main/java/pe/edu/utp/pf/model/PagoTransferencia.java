package pe.edu.utp.pf.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad especializada que registra los abonos originados mediante transferencias de fondos
 * bancarias bancarias, ya sean locales o interbancarias.
 *
 * @author Grupo 07
 * @version 2.0
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagoTransferencia extends Pago{

    /** Número de cuenta bancaria desde donde el cliente transfirió el dinero. */
    private String cuentaOrigen;

    /** Nombre de la entidad bancaria externa de procedencia (Ej. BBVA, Scotiabank). */
    private String bancoOrigen;

    /** Código de Cuenta Interbancario (CCI) utilizado en caso de operaciones cruzadas. */
    private String codigoInterbancario;
}
