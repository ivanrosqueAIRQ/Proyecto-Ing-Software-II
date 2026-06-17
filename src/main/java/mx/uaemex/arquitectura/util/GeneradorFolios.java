package mx.uaemex.arquitectura.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Singleton thread-safe que genera folios unicos para las solicitudes.
 * Formato: SGSA-YYYYMMDD-XXXX (secuencial diario).
 */
public final class GeneradorFolios {

    private static volatile GeneradorFolios instancia;

    private final AtomicLong contador = new AtomicLong(0);
    private volatile String fechaActual;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private GeneradorFolios() {
        this.fechaActual = LocalDate.now().format(formatter);
    }

    public static GeneradorFolios getInstancia() {
        if (instancia == null) {
            synchronized (GeneradorFolios.class) {
                if (instancia == null) {
                    instancia = new GeneradorFolios();
                }
            }
        }
        return instancia;
    }

    /**
     * Genera el siguiente folio. Si cambia el dia, reinicia el contador.
     */
    public String siguienteFolio() {
        String hoy = LocalDate.now().format(formatter);
        if (!hoy.equals(fechaActual)) {
            synchronized (this) {
                if (!hoy.equals(fechaActual)) {
                    fechaActual = hoy;
                    contador.set(0);
                }
            }
        }
        long numero = contador.incrementAndGet();
        return String.format("SGSA-%s-%04d", fechaActual, numero);
    }

    /**
     * Reinicia la instancia (solo para testing).
     */
    public static synchronized void resetInstancia() {
        instancia = null;
    }
}
