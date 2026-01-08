package br.com.minimundo.domain.auth;

public class AuthResult {
  private final boolean ok;
  private final String token;
  private final String message;

  private AuthResult(boolean ok, String token, String message) {
    this.ok = ok;
    this.token = token;
    this.message = message;
  }

  public static AuthResult ok(String token) {
    return new AuthResult(true, token, null);
  }

  public static AuthResult fail(String message) {
    return new AuthResult(false, null, message);
  }

  public boolean isOk() { return ok; }
  public String getToken() { return token; }
  public String getMessage() { return message; }
}
