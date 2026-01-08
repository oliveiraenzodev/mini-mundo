package br.com.minimundo.domain.project;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "projects")
public class Project implements Serializable {

  private static final long serialVersionUID = 1L;

  public enum Status {
    ACTIVE, INACTIVE
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120)
  private String name;

  @Column(columnDefinition = "text")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 12)
  private Status status = Status.ACTIVE;

  @Column(precision = 12, scale = 2)
  private BigDecimal budget;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;

  @PrePersist
  public void prePersist() {
    Date now = new Date();
    this.createdAt = now;
    this.updatedAt = now;
    if (this.status == null) this.status = Status.ACTIVE;
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = new Date();
    if (this.status == null) this.status = Status.ACTIVE;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }

  public BigDecimal getBudget() { return budget; }
  public void setBudget(BigDecimal budget) { this.budget = budget; }

  public Date getCreatedAt() { return createdAt; }
  public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

  public Date getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
