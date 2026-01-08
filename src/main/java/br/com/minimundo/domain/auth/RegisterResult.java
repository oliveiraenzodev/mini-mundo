package br.com.minimundo.domain.auth;

public class RegisterResult {
  private final boolean ok;
  private final String message;

  private RegisterResult(boolean ok, String message) {
    this.ok = ok;
    this.message = message;
  }

  public static RegisterResult ok() {
    return new RegisterResult(true, null);
  }

  public static RegisterResult fail(String message) {
    return new RegisterResult(false, message);
  }

  public boolean isOk() { return ok; }
  public String getMessage() { return message; }
}
