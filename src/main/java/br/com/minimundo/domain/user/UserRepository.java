package br.com.minimundo.domain.user;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class UserRepository {

  @PersistenceContext(unitName = "mini-mundo-pu")
  private EntityManager em;

  public User findByEmail(String email) {
    List<User> list = em
        .createQuery("select u from User u where u.email = :email")
        .setParameter("email", email)
        .setMaxResults(1)
        .getResultList();

    return list.isEmpty() ? null : list.get(0);
  }

  public boolean existsByEmail(String email) {
    Long total = (Long) em
        .createQuery("select count(u.id) from User u where u.email = :email")
        .setParameter("email", email)
        .getSingleResult();

    return total != null && total > 0;
  }

  public void save(User user) {
    em.persist(user);
  }
}
