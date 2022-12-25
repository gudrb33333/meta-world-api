package click.gudrb33333.metaworldapi.entity;

import click.gudrb33333.metaworldapi.entity.type.Role;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "mambers")
public class Member {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "member_id", columnDefinition = "char(36)")
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID id;

  @Column(unique = true)
  @NotNull
  private String email;

  @NotNull
  private String password;

  @NotNull
  private String loginType;

  @Enumerated(EnumType.STRING)
  @NotNull
  private Role role;

  @Column(name = "inserted_at")
  @CreationTimestamp
  private LocalDateTime insertedAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Builder
  public Member(String email, String password, String loginType, Role role) {
    this.email = email;
    this.password = password;
    this.loginType = loginType;
    this.role = role;
  }
}
