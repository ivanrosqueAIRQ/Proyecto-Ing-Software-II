package mx.uaemex.arquitectura.dao;

import mx.uaemex.arquitectura.model.Solicitud;
import java.util.List;

public interface SolicitudDAO {

    Solicitud guardar(Solicitud solicitud);

    Solicitud actualizar(Solicitud solicitud);

    Solicitud buscarPorId(Long id);

    List<Solicitud> listarTodas();
}
