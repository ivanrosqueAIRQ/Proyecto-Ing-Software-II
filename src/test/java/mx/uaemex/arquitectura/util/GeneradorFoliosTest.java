package mx.uaemex.arquitectura.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class GeneradorFoliosTest {

    @BeforeEach
    void setUp() {
        GeneradorFolios.resetInstancia();
    }

    @Test
    void getInstancia_devuelveMismaInstancia() {
        GeneradorFolios instancia1 = GeneradorFolios.getInstancia();
        GeneradorFolios instancia2 = GeneradorFolios.getInstancia();
        assertSame(instancia1, instancia2);
    }

    @Test
    void getInstancia_noEsNula() {
        assertNotNull(GeneradorFolios.getInstancia());
    }

    @Test
    void siguienteFolio_formatoCorrecto() {
        String folio = GeneradorFolios.getInstancia().siguienteFolio();
        String hoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        assertTrue(folio.startsWith("SGSA-" + hoy + "-"));
        assertEquals(18, folio.length());
    }

    @Test
    void siguienteFolio_incrementaSecuencia() {
        GeneradorFolios generador = GeneradorFolios.getInstancia();
        String folio1 = generador.siguienteFolio();
        String folio2 = generador.siguienteFolio();
        String folio3 = generador.siguienteFolio();

        assertTrue(folio1.endsWith("0001"));
        assertTrue(folio2.endsWith("0002"));
        assertTrue(folio3.endsWith("0003"));
    }

    @Test
    void siguienteFolio_noDevuelveNulo() {
        assertNotNull(GeneradorFolios.getInstancia().siguienteFolio());
    }

    @Test
    void siguienteFolio_noDevuelveVacio() {
        assertFalse(GeneradorFolios.getInstancia().siguienteFolio().isEmpty());
    }

    @Test
    void resetInstancia_reiniciaElSingleton() {
        GeneradorFolios instancia1 = GeneradorFolios.getInstancia();
        instancia1.siguienteFolio();
        GeneradorFolios.resetInstancia();
        GeneradorFolios instancia2 = GeneradorFolios.getInstancia();
        assertNotSame(instancia1, instancia2);
    }

    @Test
    void siguienteFolio_foliosUnicos() {
        GeneradorFolios generador = GeneradorFolios.getInstancia();
        String folio1 = generador.siguienteFolio();
        String folio2 = generador.siguienteFolio();
        assertNotEquals(folio1, folio2);
    }

    @Test
    void siguienteFolio_prefijoSGSA() {
        String folio = GeneradorFolios.getInstancia().siguienteFolio();
        assertTrue(folio.startsWith("SGSA-"));
    }

    @Test
    void siguienteFolio_contieneFechaActual() {
        String hoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String folio = GeneradorFolios.getInstancia().siguienteFolio();
        assertTrue(folio.contains(hoy));
    }
}
