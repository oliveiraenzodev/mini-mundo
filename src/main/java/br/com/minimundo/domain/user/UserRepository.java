package br.com.minimundo.domain.user;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class UserRepository {

  private final EntityManager em;

  public UserRepository(EntityManager em) {
    this.em = em;
  }

  public User findByEmail(String email) {
    try {
      Query q = em.createQuery("SELECT u FROM User u WHERE u.email = :email");
      q.setParameter("email", email);

      return (User) q.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public boolean existsByEmail(String email) {
    Query q = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email");
    q.setParameter("email", email);

    Long count = (Long) q.getSingleResult();
    return count != null && count.longValue() > 0L;
  }

  public void save(User user) {
    em.persist(user);
  }
}
