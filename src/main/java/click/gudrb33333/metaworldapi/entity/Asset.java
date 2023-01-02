package click.gudrb33333.metaworldapi.entity;

import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionTypeConverter;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.PublicTypeConverter;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryTypeConverter;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Table(name = "assets")
@DiscriminatorColumn(name = "asset_type")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Asset {

  @Id
  @GeneratedValue(generator = "UUID")
  @Type(type = "org.hibernate.type.UUIDCharType")
  @Column(name = "asset_id", columnDefinition = "char(36)")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "asset_type", insertable = false, updatable = false)
  protected AssetType assetType;

  @NotNull
  @Type(type = "org.hibernate.type.UUIDCharType")
  @Column(name = "s3_asset_uuid", unique = true, columnDefinition = "char(36)")
  private UUID s3AssetUUID;

  @NotNull
  @Column(name = "extension")
  @Convert(converter = ExtensionTypeConverter.class)
  private ExtensionType extension;

  @NotNull
  @Column(name = "s3_directory")
  @Convert(converter = S3DirectoryTypeConverter.class)
  private S3DirectoryType s3DirectoryType;

  @NotNull
  @Column(name = "public_type")
  @Convert(converter = PublicTypeConverter.class)
  private PublicType publicType;

  @CreationTimestamp
  @Column(name = "inserted_at")
  private LocalDateTime insertedAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public Asset(
      UUID id,
      AssetType assetType,
      UUID s3AssetUUID,
      ExtensionType extension,
      S3DirectoryType s3DirectoryType,
      PublicType publicType) {
    this.id = id;
    this.assetType = assetType;
    this.s3AssetUUID = s3AssetUUID;
    this.extension = extension;
    this.s3DirectoryType = s3DirectoryType;
    this.publicType = publicType;
  }
}
