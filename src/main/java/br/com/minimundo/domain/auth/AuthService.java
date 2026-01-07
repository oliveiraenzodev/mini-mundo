package br.com.minimundo.domain.auth;

import br.com.minimundo.domain.user.User;
import br.com.minimundo.domain.user.UserRepository;
import br.com.minimundo.infra.jpa.JPAUtil;

import javax.persistence.EntityManager;

public class AuthService {

  public AuthResult login(String email, String password) {

    if (email == null || password == null) {
      return AuthResult.fail("E-mail ou senha inválidos.");
    }

    EntityManager em = JPAUtil.getEntityManager();
    try {
      UserRepository repo = new UserRepository(em);
      User user = repo.findByEmail(email.trim().toLowerCase());

      if (user == null || !PasswordHasher.matches(password, user.getPasswordHash())) {
        return AuthResult.fail("E-mail ou senha inválidos.");
      }

      String token = JwtUtil.generateToken(user.getId(), user.getEmail());
      return AuthResult.ok(token);

    } finally {
      em.close();
    }
  }

  public RegisterResult register(
      String name,
      String email,
      String password,
      String passwordConfirmation
  ) {

    if (name == null || name.trim().length() < 3) {
      return RegisterResult.fail("Nome completo deve ter no mínimo 3 caracteres.");
    }

    if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
      return RegisterResult.fail("E-mail inválido.");
    }

    if (password == null || password.length() < 6) {
      return RegisterResult.fail("Senha deve ter no mínimo 6 caracteres.");
    }

    if (passwordConfirmation == null || !password.equals(passwordConfirmation)) {
      return RegisterResult.fail("Senha e confirmação devem coincidir.");
    }

    EntityManager em = JPAUtil.getEntityManager();

    try {
      UserRepository repo = new UserRepository(em);

      if (repo.existsByEmail(email.trim().toLowerCase())) {
        return RegisterResult.fail("Este e-mail já está cadastrado.");
      }

      em.getTransaction().begin();

      User user = new User();
      user.setName(name.trim());
      user.setEmail(email.trim().toLowerCase());
      user.setPasswordHash(PasswordHasher.hash(password));

      repo.save(user);

      em.getTransaction().commit();
      return RegisterResult.ok();

    } catch (Exception e) {

      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }

      e.printStackTrace();
      return RegisterResult.fail("Erro ao cadastrar. Tente novamente.");

    } finally {
      em.close();
    }
  }
}
