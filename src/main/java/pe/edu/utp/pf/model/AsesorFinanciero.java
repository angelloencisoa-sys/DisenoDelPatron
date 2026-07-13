package pe.edu.utp.pf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // 👈 Aseguren esta importación
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
@JsonIgnoreProperties({"solicitudes"}) // 👈 SOLUCIÓN: Evita leer las solicitudes en diferido y elimina el Lazy Error
public class AsesorFinanciero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAsesor;

    private String nombreAsesor;
    private String codigoAgencia;

    @OneToMany(mappedBy = "asesor")
    private List<SolicitudCredito> solicitudes;
}
