package click.gudrb33333.metaworldapi.entity;

import click.gudrb33333.metaworldapi.entity.type.LoginType;
import click.gudrb33333.metaworldapi.entity.type.LoginTypeConverter;
import click.gudrb33333.metaworldapi.entity.type.Role;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @GeneratedValue(generator = "UUID")
  @Type(type = "org.hibernate.type.UUIDCharType")
  @Column(name = "member_id", columnDefinition = "char(36)")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @NotNull
  @Column(unique = true)
  private String email;

  @NotNull private String password;

  @NotNull
  @Column(name = "login_type")
  @Convert(converter = LoginTypeConverter.class)
  private LoginType loginType;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Role role;

  @CreationTimestamp
  @Column(name = "inserted_at")
  private LocalDateTime insertedAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @JoinColumn(name = "profile_id")
  @OneToOne(targetEntity = Profile.class, fetch = FetchType.LAZY)
  private Profile profile;

  @Builder
  public Member(UUID id, String email, String password, LoginType loginType, Role role, Profile profile) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.loginType = loginType;
    this.role = role;
    this.profile = profile;
  }

  public void changeProfile(Profile profile){
    this.profile = profile;
  }
}
