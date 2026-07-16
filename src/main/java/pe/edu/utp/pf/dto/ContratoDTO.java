package pe.edu.utp.pf.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO para la transferencia segura de datos de Contratos.
 * Resuelve las alertas de exposición de entidades de SonarQube.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratoDTO {

    private Integer idContrato;
    private LocalDate fechaFirma;
    private String clausulasExtras;
    private String tipo;
    private Integer idSolicitud;
}
