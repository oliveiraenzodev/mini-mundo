package web.bean;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "forgotPasswordBean")
@ViewScoped
public class ForgotPasswordBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;

    public void send() {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Se o e-mail existir, você receberá as instruções em instantes.",
                null
            )
        );
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
