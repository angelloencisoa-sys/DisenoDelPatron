package pe.edu.utp.pf.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudCreditoDTO {
    private Double montoSolicitado;
    private Integer plazoMeses;
    private String estado;
}