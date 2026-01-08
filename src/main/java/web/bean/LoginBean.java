package br.com.minimundo.web.bean;

import br.com.minimundo.domain.auth.AuthResult;
import br.com.minimundo.domain.auth.AuthService;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named("loginBean")
@RequestScoped
public class LoginBean {

  private String email;
  private String password;

  @Inject
  private AuthService authService;

  public void login() {
    AuthResult result = authService.login(email, password);

    if (!result.isOk()) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", result.getMessage())
      );
      return;
    }

    HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
        .getExternalContext().getSession(true);

    session.setAttribute("JWT_TOKEN", result.getToken());

    redirect("/pages/project/list.xhtml");
  }

  public void logout() {
    FacesContext fc = FacesContext.getCurrentInstance();

    HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
    if (session != null) {
      session.invalidate();
    }

    redirect("/pages/auth/login.xhtml");
  }

  private void redirect(String path) {
    try {
      String ctx = FacesContext.getCurrentInstance()
          .getExternalContext()
          .getRequestContextPath();

      FacesContext.getCurrentInstance()
          .getExternalContext()
          .redirect(ctx + path);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
}
