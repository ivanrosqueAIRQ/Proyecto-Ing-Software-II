package mx.uaemex.arquitectura.negocio.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvocadorComandosTest {

    private InvocadorComandos invocador;

    @BeforeEach
    void setUp() {
        invocador = new InvocadorComandos();
    }

    @Test
    void ejecutar_invocaComando() {
        Comando comando = mock(Comando.class);

        invocador.ejecutar(comando);

        verify(comando, times(1)).ejecutar();
    }

    @Test
    void ejecutar_agregaAlHistorial() {
        Comando comando = mock(Comando.class);

        invocador.ejecutar(comando);

        assertEquals(1, invocador.getHistorial().size());
    }

    @Test
    void getHistorial_inicialmenteVacio() {
        assertTrue(invocador.getHistorial().isEmpty());
    }

    @Test
    void getTotalEjecutados_inicialmenteCero() {
        assertEquals(0, invocador.getTotalEjecutados());
    }

    @Test
    void ejecutar_multiples_incrementaContador() {
        Comando c1 = mock(Comando.class);
        Comando c2 = mock(Comando.class);
        Comando c3 = mock(Comando.class);

        invocador.ejecutar(c1);
        invocador.ejecutar(c2);
        invocador.ejecutar(c3);

        assertEquals(3, invocador.getTotalEjecutados());
        assertEquals(3, invocador.getHistorial().size());
    }

    @Test
    void getHistorial_esInmodificable() {
        Comando comando = mock(Comando.class);
        invocador.ejecutar(comando);

        assertThrows(UnsupportedOperationException.class, () ->
                invocador.getHistorial().add(mock(Comando.class)));
    }

    @Test
    void getHistorial_conservaOrden() {
        Comando c1 = mock(Comando.class);
        Comando c2 = mock(Comando.class);

        invocador.ejecutar(c1);
        invocador.ejecutar(c2);

        assertSame(c1, invocador.getHistorial().get(0));
        assertSame(c2, invocador.getHistorial().get(1));
    }
}
