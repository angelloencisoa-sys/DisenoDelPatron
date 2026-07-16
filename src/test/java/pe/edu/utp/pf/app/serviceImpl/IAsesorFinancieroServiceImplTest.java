package pe.edu.utp.pf.app.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;

import pe.edu.utp.pf.model.AsesorFinanciero;
import pe.edu.utp.pf.repository.AsesorFinancieroRepository;
import pe.edu.utp.pf.service.impl.AsesorFinancieroServiceImpl;

/**
 * Test unitario para AsesorFinancieroServiceImpl
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class IAsesorFinancieroServiceImplTest {

    @Mock
    private AsesorFinancieroRepository repoMock;

    @InjectMocks
    private AsesorFinancieroServiceImpl serviceMock;

    private AsesorFinanciero asesor;
    private AsesorFinanciero saveAsesor;
    private List<AsesorFinanciero> asesorList;

    @BeforeEach
    void setup() {
        // Asesor sin guardar
        asesor = new AsesorFinanciero();
        asesor.setNombreAsesor("Carlos Mendez");
        asesor.setCodigoAgencia("AG-001");

        // Asesor guardado (con ID)
        saveAsesor = new AsesorFinanciero();
        saveAsesor.setIdAsesor(1);
        saveAsesor.setNombreAsesor("Carlos Mendez");
        saveAsesor.setCodigoAgencia("AG-001");

        asesorList = List.of(asesor, saveAsesor);
    }

    @DisplayName("AsesorFinancieroServiceImpl - Crear Asesor")
    @Test
    void testCreate_SaveAsesor() {
        when(this.repoMock.save(any())).thenReturn(saveAsesor);
        AsesorFinanciero result = null;

        try {
            result = this.serviceMock.create(asesor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(result).isNotNull();
        assertThat(result.getIdAsesor()).isEqualTo(1);
        assertThat(result.getNombreAsesor()).isEqualTo("Carlos Mendez");
        assertThat(result.getCodigoAgencia()).isEqualTo("AG-001");
    }

    @DisplayName("AsesorFinancieroServiceImpl - Crear Asesor maneja excepción")
    @Test
    void testCreate_HandleException() {
        when(this.repoMock.save(any())).thenThrow(RuntimeException.class);

        try {
            this.serviceMock.create(asesor);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("AsesorFinancieroServiceImpl - Obtener todos los Asesores")
    @Test
    void testGetAll_ReturnAllAsesores() {
        when(this.repoMock.findAll()).thenReturn(asesorList);
        List<AsesorFinanciero> result = this.serviceMock.getAll();

        assertThat(result).isNotNull().hasSize(2).contains(saveAsesor, asesor);
    }

    @DisplayName("AsesorFinancieroServiceImpl - Obtener Asesor por ID")
    @Test
    void testGetById_ReturnAsesor() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(saveAsesor));
        Optional<AsesorFinanciero> result = this.serviceMock.getById(1);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getIdAsesor()).isEqualTo(1);
        assertThat(result.get().getNombreAsesor()).isEqualTo("Carlos Mendez");
    }

    @DisplayName("AsesorFinancieroServiceImpl - Obtener Asesor por ID no encontrado")
    @Test
    void testGetById_NotFound() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        Optional<AsesorFinanciero> result = this.serviceMock.getById(999);

        assertThat(result).isEmpty();
    }

    @DisplayName("AsesorFinancieroServiceImpl - Obtener Asesor por ID maneja excepción")
    @Test
    void testGetById_HandleException() {
        when(this.repoMock.findById(anyInt()))
                .thenThrow(new DataRetrievalFailureException("Error simulado de base de datos"));

        Optional<AsesorFinanciero> result = this.serviceMock.getById(1);

        assertThat(result).isEmpty();
    }

    @DisplayName("AsesorFinancieroServiceImpl - Actualizar Asesor")
    @Test
    void testUpdate_ReturnUpdatedAsesor() {
        AsesorFinanciero asesorModificado = new AsesorFinanciero();
        asesorModificado.setNombreAsesor("Carlos Mendez Actualizado");
        asesorModificado.setCodigoAgencia("AG-002");

        when(this.repoMock.save(any())).thenReturn(saveAsesor);
        AsesorFinanciero result = this.serviceMock.update(saveAsesor, asesorModificado);

        assertThat(result).isNotNull();
        assertThat(result.getIdAsesor()).isEqualTo(1);
    }

    @DisplayName("AsesorFinancieroServiceImpl - Eliminar Asesor")
    @Test
    void testDeleteById_Success() {
        try {
            this.serviceMock.deleteById(1);
            assertTrue(true);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("AsesorFinancieroServiceImpl - Eliminar Asesor maneja excepción")
    @Test
    void testDeleteById_HandleException() {
        doThrow(RuntimeException.class).when(this.repoMock).deleteById(anyInt());

        try {
            this.serviceMock.deleteById(1);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }
}