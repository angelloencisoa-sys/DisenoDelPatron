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
public class PagoBilleteraDigital extends Pago{
    private String numeroCelularOrigen;
    private String operadora;
    private String numeroAprobacion;
}
