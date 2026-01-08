package br.com.minimundo.domain.auth;

import br.com.minimundo.domain.user.User;
import br.com.minimundo.domain.user.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class AuthService {

  @Inject
  private UserRepository repo;

  public AuthResult login(String email, String password) {
    if (email == null || password == null) {
      return AuthResult.fail("E-mail ou senha inválidos.");
    }

    User user = repo.findByEmail(email.trim().toLowerCase());

    if (user == null || !PasswordHasher.matches(password, user.getPasswordHash())) {
      return AuthResult.fail("E-mail ou senha inválidos.");
    }

    String token = JwtUtil.generateToken(user.getId(), user.getEmail());
    return AuthResult.ok(token);
  }

  @Transactional
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

    String normalizedEmail = email.trim().toLowerCase();

    if (repo.existsByEmail(normalizedEmail)) {
      return RegisterResult.fail("Este e-mail já está cadastrado.");
    }

    User user = new User();
    user.setName(name.trim());
    user.setEmail(normalizedEmail);
    user.setPasswordHash(PasswordHasher.hash(password));

    repo.save(user);

    return RegisterResult.ok();
  }
}
