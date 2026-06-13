package pe.edu.utp.PF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AsesorFinanciero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsesor;
    private String nombreAsesor;
    private String codigoAgencia;

    @OneToMany(mappedBy = "asesor")
    private List<SolicitudCredito> solicitudes;
}
