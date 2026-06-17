package mx.uaemex.arquitectura.negocio.chain;

import mx.uaemex.arquitectura.model.Solicitud;

/**
 * Interfaz base del patron Chain of Responsibility.
 * Cada eslabón valida/procesa una solicitud y la pasa al siguiente.
 */
public abstract class Manejador {

    private Manejador siguiente;

    public Manejador setSiguiente(Manejador siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public Manejador getSiguiente() {
        return siguiente;
    }

    /**
     * Procesa la solicitud. Si no puede resolverla, la delega al siguiente.
     * @return true si fue procesada (aprobada/rechazada), false si se pasa al siguiente.
     */
    public boolean procesar(Solicitud solicitud) {
        if (manejar(solicitud)) {
            return true;
        }
        if (siguiente != null) {
            return siguiente.procesar(solicitud);
        }
        return false;
    }

    /**
     * Logica especifica del manejador.
     * @return true si este manejador resolvio la solicitud.
     */
    protected abstract boolean manejar(Solicitud solicitud);
}
