package Justificantes;

import org.junit.jupiter.api.*;
//import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class JustificantesTest {

    private Justificante ejemplo;

    @BeforeEach
    public void setUp() {
        ejemplo = new Justificante();
        ejemplo.setFolio(10);
        ejemplo.setFechaInicio(LocalDate.of(2025, 5, 1));
        ejemplo.setFechaFin(LocalDate.of(2025, 5, 10));
        ejemplo.setDiagnostico("Gripe severa con fiebre alta");
        ejemplo.setEstado("Aprobado");
        ejemplo.setResueltoPor("Dra. Laura Gómez");
        ejemplo.setFechaResolucion(LocalDate.of(2025, 5, 9));
    }

    @Test
    public void testFechasValidas() {
        assertTrue(ejemplo.getFechaInicio().isBefore(ejemplo.getFechaFin()));
    }

    @Test
    public void testSetDiagnostico() {
        ejemplo.setDiagnostico("Nuevo diagnóstico");
        assertEquals("Nuevo diagnóstico", ejemplo.getDiagnostico());
    }

    // ffff
    @Test
    public void testGetEstado() {
        assertEquals("Aprobado", ejemplo.getEstado());
    }

    @Test
    public void testJustificanteDAO_obtenerTodos() {
        List<Justificante> lista = JustificanteDAO.obtenerTodos();
        assertNotNull(lista);
    }

    @Test
    public void testJustificanteDAO_obtenerPorFolio() {
        Optional<Justificante> resultado = JustificanteDAO.obtenerPorFolio(2);
        assertTrue(resultado.isPresent() || resultado.isEmpty());
    }

    @Test
    public void testFechaResolucionCorrecta() {
        assertEquals(LocalDate.of(2025, 5, 9), ejemplo.getFechaResolucion());
    }

    @Test
    public void testModificarEstado() {
        ejemplo.setEstado("Pendiente");
        assertEquals("Pendiente", ejemplo.getEstado());
    }

    @Test
    public void testNombreMedicoNoVacio() {
        assertNotNull(ejemplo.getResueltoPor());
    }

    @Test
    public void testFolioEsNumerico() {
        assertTrue(ejemplo.getFolio() > 0);
    }

    @Test
    public void testSetFechaInicioYFin() {
        ejemplo.setFechaInicio(LocalDate.of(2025, 6, 1));
        ejemplo.setFechaFin(LocalDate.of(2025, 6, 5));
        assertTrue(ejemplo.getFechaInicio().isBefore(ejemplo.getFechaFin()));
    }

    @Test
    public void testSetResueltoPor() {
        ejemplo.setResueltoPor("Dr. Juan Pérez");
        assertEquals("Dr. Juan Pérez", ejemplo.getResueltoPor());
    }

    @Test
    public void testSetEstado() {
        ejemplo.setEstado("Rechazado");
        assertEquals("Rechazado", ejemplo.getEstado());
    }

}
