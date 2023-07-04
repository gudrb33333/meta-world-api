package click.gudrb33333.metaworldapi.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.annotations.Where;

@Entity
@Getter
@Table(name = "profiles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at is null")
public class Profile {

  @Id
  @GeneratedValue(generator = "UUID")
  @Type(type = "org.hibernate.type.UUIDCharType")
  @Column(name = "profile_id", columnDefinition = "char(36)")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @NotNull
  private String nickname;

  @CreationTimestamp
  private LocalDateTime insertedAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  private LocalDateTime deletedAt;

  @JoinColumn(name = "asset_id", unique = true)
  @OneToOne(targetEntity = Avatar.class, fetch = FetchType.EAGER)
  private Avatar avatar;

  @Builder
  public Profile(UUID id, String nickname, Avatar avatar) {
    this.id = id;
    this.nickname = nickname;
    this.avatar = avatar;
  }

  public void changeAvatar(Avatar avatar) {
    this.avatar = avatar;
  }

  public void changeAvatarAndNickname(Avatar avatar, String nickname) {
    this.avatar = avatar;
    this.nickname = nickname;
  }

  public void changeDeleteTimeToNow(){
    this.deletedAt = LocalDateTime.now();
  }
}
