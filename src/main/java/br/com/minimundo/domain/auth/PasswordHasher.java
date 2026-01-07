package br.com.minimundo.domain.auth;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordHasher {

  private PasswordHasher() {}

  public static String hash(String plain) {
    return BCrypt.hashpw(plain, BCrypt.gensalt());
  }

  public static boolean matches(String plain, String hash) {
    if (plain == null || hash == null) return false;
    return BCrypt.checkpw(plain, hash);
  }
}
