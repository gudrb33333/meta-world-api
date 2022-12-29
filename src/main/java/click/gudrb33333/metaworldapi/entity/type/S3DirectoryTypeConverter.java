package click.gudrb33333.metaworldapi.entity.type;

public class S3DirectoryTypeConverter extends BaseEnumConverter<S3DirectoryType, String> {

  @Override
  public S3DirectoryType convertToEntityAttribute(String dbData) {
    return BaseEnum.fromValue(dbData, S3DirectoryType.class);
  }
}
