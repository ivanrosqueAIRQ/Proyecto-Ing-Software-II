package mx.uaemex.arquitectura.negocio.chain;

import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;

/**
 * Primer eslabón: valida que los datos obligatorios estén completos.
 * Si faltan datos, rechaza la solicitud inmediatamente.
 */
public class ValidadorDatosCompletos extends Manejador {

    @Override
    protected boolean manejar(Solicitud solicitud) {
        if (solicitud.getNumeroCuenta() == null || solicitud.getNumeroCuenta().trim().isEmpty()) {
            solicitud.setEstado(EstadoSolicitud.RECHAZADO);
            solicitud.setResueltoPor("SISTEMA-VALIDACION");
            return true;
        }
        if (solicitud.getTipoTramite() == null) {
            solicitud.setEstado(EstadoSolicitud.RECHAZADO);
            solicitud.setResueltoPor("SISTEMA-VALIDACION");
            return true;
        }
        return false;
    }
}
