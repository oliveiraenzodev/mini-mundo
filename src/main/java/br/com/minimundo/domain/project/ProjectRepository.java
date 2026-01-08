package br.com.minimundo.domain.project;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class ProjectRepository {

  @PersistenceContext(unitName = "mini-mundo-pu")
  private EntityManager em;

  public List<Project> findAll(String search) {
    String jpql = "select p from Project p ";
    boolean hasSearch = search != null && !search.trim().isEmpty();

    if (hasSearch) {
      jpql += "where lower(p.name) like :q ";
    }

    jpql += "order by p.name";

    TypedQuery<Project> query = em.createQuery(jpql, Project.class);

    if (hasSearch) {
      query.setParameter("q", "%" + search.trim().toLowerCase() + "%");
    }

    return query.getResultList();
  }

  public Project findById(Long id) {
    return em.find(Project.class, id);
  }

  public void persist(Project project) {
    em.persist(project);
  }

  public Project merge(Project project) {
    return em.merge(project);
  }

  public void remove(Project project) {
    em.remove(project);
  }

  public boolean existsByName(String name, Long ignoreId) {
    String sql =
        "select count(1) " +
        "from projects " +
        "where lower(name) = lower(?1) " +
        (ignoreId != null ? "and id <> ?2 " : "");

    javax.persistence.Query q = em.createNativeQuery(sql);
    q.setParameter(1, name);

    if (ignoreId != null) {
      q.setParameter(2, ignoreId);
    }

    Number count = (Number) q.getSingleResult();
    return count.longValue() > 0;
  }
}
