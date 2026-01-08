package br.com.minimundo.domain.project;

import web.exception.BusinessException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ProjectService {

  @Inject
  private ProjectRepository repo;

  public List<Project> list(String search) {
    return repo.findAll(search);
  }

  public Project findOrFail(Long id) {
    if (id == null) {
      throw new BusinessException("ID inválido.");
    }

    Project project = repo.findById(id);
    if (project == null) {
      throw new BusinessException("Projeto não encontrado.");
    }

    return project;
  }

  @Transactional
  public void create(Project project) {
    validate(project);
    repo.persist(project);
  }

  @Transactional
  public void update(Project project) {
    if (project.getId() == null) {
      throw new BusinessException("ID do projeto é obrigatório.");
    }

    validate(project);
    repo.merge(project);
  }

  @Transactional
  public void delete(Long id) {
    Project project = findOrFail(id);
    repo.remove(project);
  }

  private void validate(Project project) {

    if (project == null) {
      throw new BusinessException("Projeto inválido.");
    }

    if (project.getName() == null || project.getName().trim().length() < 3) {
      throw new BusinessException("Nome do projeto deve ter no mínimo 3 caracteres.");
    }

    if (repo.existsByName(project.getName(), project.getId())) {
      throw new BusinessException("Já existe um projeto com esse nome.");
    }
  }
}
