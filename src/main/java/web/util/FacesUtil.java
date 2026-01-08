package web.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

public final class FacesUtil {

  private FacesUtil() {}

  public static void info(String msg) {
    addMessage(FacesMessage.SEVERITY_INFO, msg);
  }

  public static void warn(String msg) {
    addMessage(FacesMessage.SEVERITY_WARN, msg);
  }

  public static void error(String msg) {
    addMessage(FacesMessage.SEVERITY_ERROR, msg);
  }

  private static void addMessage(FacesMessage.Severity severity, String msg) {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, msg, null));
  }

  public static String param(String name) {
    return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
  }

  public static void redirect(String path) {
    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    try {
      ec.redirect(ec.getRequestContextPath() + path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean isPostback() {
    return FacesContext.getCurrentInstance().isPostback();
  }
}
