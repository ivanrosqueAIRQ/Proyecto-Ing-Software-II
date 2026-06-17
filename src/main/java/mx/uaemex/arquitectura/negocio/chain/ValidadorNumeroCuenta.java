package mx.uaemex.arquitectura.negocio.chain;

import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;

/**
 * Segundo eslabón: valida que el numero de cuenta tenga exactamente 7 digitos.
 */
public class ValidadorNumeroCuenta extends Manejador {

    private static final String PATRON_CUENTA = "\\d{7}";

    @Override
    protected boolean manejar(Solicitud solicitud) {
        if (!solicitud.getNumeroCuenta().matches(PATRON_CUENTA)) {
            solicitud.setEstado(EstadoSolicitud.RECHAZADO);
            solicitud.setResueltoPor("SISTEMA-VALIDACION");
            return true;
        }
        return false;
    }
}
