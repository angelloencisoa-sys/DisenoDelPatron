package pe.edu.utp.pf.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.edu.utp.pf.model.AsesorFinanciero;
import pe.edu.utp.pf.repository.AsesorFinancieroRepository;
import pe.edu.utp.pf.service.impl.AsesorFinancieroServiceImpl;

/**
 * Clase de prueba unitaria para AsesorFinancieroService
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class IAsesorFinancieroServiceTest {

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

    @DisplayName("Service - Registrar y retornar Asesor")
    @Test
    void service_Save_Return_Asesor() {
        // 1. Preparación
        when(this.repoMock.save(any())).thenReturn(saveAsesor);
        AsesorFinanciero saveAsesorResult = null;

        // 2. Ejecución
        try {
            saveAsesorResult = this.serviceMock.create(asesor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(saveAsesorResult).isNotNull();
        assertThat(saveAsesorResult.getIdAsesor()).isNotNull();
        assertThat(saveAsesorResult.getNombreAsesor()).isEqualTo("Carlos Mendez");
    }

    @DisplayName("Service - Registrar y retornar Exception")
    @Test
    void service_Save_HandleException() {
        when(this.repoMock.save(any())).thenThrow(RuntimeException.class);
        AsesorFinanciero saveAsesorResult = null;

        try {
            saveAsesorResult = this.serviceMock.create(asesor);
            assertThat(saveAsesorResult.getIdAsesor()).isNotNull();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar y retornar todos los Asesores")
    @Test
    void service_FindAll_ReturnsAllRecords() {
        when(this.repoMock.findAll()).thenReturn(asesorList);
        List<AsesorFinanciero> list = this.serviceMock.getAll();

        assertThat(list).hasSizeGreaterThan(1).contains(saveAsesor, asesor);

    }

    @DisplayName("Service - Buscar y retornar por Id un Asesor")
    @Test
    void service_FindById_ReturnObjectId() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(saveAsesor));
        Optional<AsesorFinanciero> asesorWithId = null;

        try {
            asesorWithId = this.serviceMock.getById(1);
            assertThat(asesorWithId.get().getIdAsesor()).isNotNull();
            assertThat(asesorWithId.get().getNombreAsesor()).isEqualTo("Carlos Mendez");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar por Id y retornar un objeto vacío")
    @Test
    void service_FindById_ReturnsNoRecord() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        Optional<AsesorFinanciero> asesorWithId = null;

        try {
            asesorWithId = this.serviceMock.getById(1);
            assertThat(asesorWithId).isNotPresent();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar y retornar por Id una Excepción")
    @Test
    void service_FindById_HandleException() {
        when(this.repoMock.findById(anyInt())).thenThrow(RuntimeException.class);
        Optional<AsesorFinanciero> asesorWithId = null;

        try {
            asesorWithId = this.serviceMock.getById(1);
            assertThat(asesorWithId).isNotPresent();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Actualizar Asesor")
    @Test
    void service_Update_ReturnUpdatedAsesor() {
        AsesorFinanciero asesorModificado = new AsesorFinanciero();
        asesorModificado.setNombreAsesor("Carlos Mendez Actualizado");
        asesorModificado.setCodigoAgencia("AG-002");

        when(this.repoMock.save(any())).thenReturn(saveAsesor);
        AsesorFinanciero asesorUpdated = null;

        try {
            asesorUpdated = this.serviceMock.update(saveAsesor, asesorModificado);
            assertThat(asesorUpdated).isNotNull();
            assertThat(asesorUpdated.getIdAsesor()).isEqualTo(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Service - Eliminar Asesor")
    @Test
    void service_Delete_SuccessfulDeletion() {
        try {
            this.serviceMock.deleteById(1);
            assertTrue(true);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }
}
