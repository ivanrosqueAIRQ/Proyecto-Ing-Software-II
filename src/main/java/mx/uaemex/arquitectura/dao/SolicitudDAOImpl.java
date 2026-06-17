package mx.uaemex.arquitectura.dao;

import mx.uaemex.arquitectura.model.Solicitud;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class SolicitudDAOImpl implements SolicitudDAO {

    @PersistenceContext(unitName = "sgsaPU")
    private EntityManager em;

    @Override
    public Solicitud guardar(Solicitud solicitud) {
        em.persist(solicitud);
        return solicitud;
    }

    @Override
    public Solicitud actualizar(Solicitud solicitud) {
        return em.merge(solicitud);
    }

    @Override
    public Solicitud buscarPorId(Long id) {
        return em.find(Solicitud.class, id);
    }

    @Override
    public List<Solicitud> listarTodas() {
        return em.createQuery("SELECT s FROM Solicitud s ORDER BY s.fechaRegistro DESC", Solicitud.class)
                .getResultList();
    }
}
