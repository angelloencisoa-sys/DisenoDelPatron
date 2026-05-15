package pe.edu.utp.PF.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagoTransferencia extends Pago{
    private String cuentaOrigen;
    private String bancoOrigen;
    private String codigoInterbancario;
}
