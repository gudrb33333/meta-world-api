package click.gudrb33333.metaworldapi.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name = "member_assets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAsset {

  @Id
  @GeneratedValue(generator = "UUID")
  @Type(type = "org.hibernate.type.UUIDCharType")
  @Column(name = "member_asset_id", columnDefinition = "char(36)")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @CreationTimestamp
  private LocalDateTime insertedAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(targetEntity = Asset.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "asset_id")
  private Asset asset;

  @Builder
  public MemberAsset(UUID id, Member member, Asset asset) {
    this.id = id;
    this.member = member;
    this.asset = asset;
  }
}
