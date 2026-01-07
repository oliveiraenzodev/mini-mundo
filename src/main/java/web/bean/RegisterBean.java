package br.com.minimundo.web.bean;

import br.com.minimundo.domain.auth.AuthService;
import br.com.minimundo.domain.auth.RegisterResult;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "registerBean")
@RequestScoped
public class RegisterBean {

  private String name;
  private String email;
  private String password;
  private String passwordConfirmation;

  private final AuthService authService = new AuthService();

  public void register() {
    RegisterResult result = authService.register(name, email, password, passwordConfirmation);

    if (!result.isOk()) {
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção", result.getMessage()));
      return;
    }

    FacesContext.getCurrentInstance().getExternalContext().getFlash().put(
        "REGISTER_SUCCESS",
        "Cadastro realizado! Faça login para continuar."
    );

    redirect("/pages/auth/login.xhtml");
  }

  private void redirect(String path) {
    try {
      String ctx = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
      FacesContext.getCurrentInstance().getExternalContext().redirect(ctx + path);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public String getPasswordConfirmation() { return passwordConfirmation; }
  public void setPasswordConfirmation(String passwordConfirmation) { this.passwordConfirmation = passwordConfirmation; }
}
