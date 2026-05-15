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
public class ClienteJuridico extends Cliente{
    private String ruc;
    private String razonSocial;
    private String representanteLegal;
    private Integer puntosCorporativos;
}
