package mx.uaemex.arquitectura.negocio.chain;

import mx.uaemex.arquitectura.model.EstadoSolicitud;
import mx.uaemex.arquitectura.model.Solicitud;
import mx.uaemex.arquitectura.model.TipoTramite;

/**
 * Tercer eslabón: aprueba automaticamente tramites de bajo riesgo
 * (BAJA_TEMPORAL y REINGRESO) si los datos son validos.
 * Los demás quedan EN_REVISION para aprobación manual.
 */
public class AprobadorAutomatico extends Manejador {

    @Override
    protected boolean manejar(Solicitud solicitud) {
        TipoTramite tipo = solicitud.getTipoTramite();
        if (tipo == TipoTramite.BAJA_TEMPORAL || tipo == TipoTramite.REINGRESO) {
            solicitud.setEstado(EstadoSolicitud.APROBADO_AUTOMATICO);
            solicitud.setResueltoPor("SISTEMA-AUTO");
            return true;
        }
        // Otros tramites requieren revision manual
        solicitud.setEstado(EstadoSolicitud.EN_REVISION);
        return true;
    }
}
