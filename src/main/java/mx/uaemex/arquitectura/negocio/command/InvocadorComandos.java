package mx.uaemex.arquitectura.negocio.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Invocador del patron Command. Almacena historial de comandos ejecutados.
 */
public class InvocadorComandos {

    private final List<Comando> historial = new ArrayList<>();

    public void ejecutar(Comando comando) {
        comando.ejecutar();
        historial.add(comando);
    }

    public List<Comando> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    public int getTotalEjecutados() {
        return historial.size();
    }
}
