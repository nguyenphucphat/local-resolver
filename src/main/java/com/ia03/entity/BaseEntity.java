package com.ia03.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  private Long id;

  @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN")
  protected boolean isActive = Boolean.TRUE;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void prePersist() {
    if (this.createdAt == null) createdAt = LocalDateTime.now();
    if (this.updatedAt == null) updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  @PreRemove
  protected void preRemove() {
    this.updatedAt = LocalDateTime.now();
  }

  public boolean isActive() {
    return Boolean.TRUE.equals(this.isActive);
  }

  public boolean isNotActive() {
    return Boolean.FALSE.equals(this.isActive);
  }

  public void deactivate() {
    this.isActive = Boolean.FALSE;
  }

  public void activate() {
    this.isActive = Boolean.TRUE;
  }
}
