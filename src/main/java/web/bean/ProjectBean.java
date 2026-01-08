package br.com.minimundo.web.bean;

import br.com.minimundo.domain.project.Project;
import br.com.minimundo.domain.project.ProjectService;
import web.exception.BusinessException;
import web.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named("projectBean")
@ViewScoped
public class ProjectBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject
  private ProjectService service;

  private List<Project> projects;
  private Project project;
  private String search;

  @PostConstruct
  public void init() {
    // não faça refresh() aqui porque esse bean é usado em list e edit
    // cada tela chama seu viewAction específico.
  }

  /**
   * Use no list.xhtml
   */
  public void loadForList() {
    if (!FacesUtil.isPostback()) {
      refresh();
    }
  }

  /**
   * Use no edit.xhtml
   * - Se vier id na URL, carrega o projeto
   * - Se não vier, prepara um novo
   */
  public void loadForEdit() {
    if (FacesUtil.isPostback()) {
      return;
    }

    Long id = getLongParam("id");

    if (id == null) {
      this.project = new Project();
      return;
    }

    try {
      this.project = service.findOrFail(id);
      if (this.project == null) {
        FacesUtil.error("Projeto não encontrado.");
        FacesUtil.redirect("/pages/project/list.xhtml");
      }
    } catch (BusinessException e) {
      FacesUtil.error(e.getMessage());
      FacesUtil.redirect("/pages/project/list.xhtml");
    }
  }

  public void refresh() {
    projects = service.list(search);
  }

  public void save() {
    try {
      if (project == null) {
        project = new Project();
      }

      if (project.getId() == null) {
        service.create(project);
        FacesUtil.info("Projeto criado com sucesso.");
      } else {
        service.update(project);
        FacesUtil.info("Projeto atualizado com sucesso.");
      }

      FacesUtil.redirect("/pages/project/list.xhtml");

    } catch (BusinessException e) {
      FacesUtil.error(e.getMessage());
    }
  }

  public void delete(Long id) {
    try {
      service.delete(id);
      FacesUtil.info("Projeto excluído com sucesso.");
      refresh();
    } catch (BusinessException e) {
      FacesUtil.error(e.getMessage());
    }
  }

  private Long getLongParam(String name) {
    try {
      Map<String, String> params = FacesContext.getCurrentInstance()
          .getExternalContext()
          .getRequestParameterMap();

      String raw = params.get(name);
      if (raw == null || raw.trim().isEmpty()) return null;

      return Long.valueOf(raw.trim());
    } catch (Exception e) {
      return null;
    }
  }

  public void loadForView() {
    if (FacesUtil.isPostback()) return;

    Long id = getLongParam("id");

    if (id == null) {
      FacesUtil.error("Projeto não informado.");
      FacesUtil.redirect("/pages/project/list.xhtml");
      return;
    }

    try {
      this.project = service.findOrFail(id);

      if (this.project == null) {
        FacesUtil.error("Projeto não encontrado.");
        FacesUtil.redirect("/pages/project/list.xhtml");
      }

    } catch (BusinessException e) {
      FacesUtil.error(e.getMessage());
      FacesUtil.redirect("/pages/project/list.xhtml");
    }
  }

  // getters / setters
  public List<Project> getProjects() { return projects; }
  public void setProjects(List<Project> projects) { this.projects = projects; }

  public Project getProject() {
    if (project == null) project = new Project();
    return project;
  }
  public void setProject(Project project) { this.project = project; }

  public String getSearch() { return search; }
  public void setSearch(String search) { this.search = search; }
}
