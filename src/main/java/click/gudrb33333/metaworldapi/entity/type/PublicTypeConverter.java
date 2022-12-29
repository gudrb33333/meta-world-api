package click.gudrb33333.metaworldapi.entity.type;

public class PublicTypeConverter extends BaseEnumConverter<PublicType, String> {

  @Override
  public PublicType convertToEntityAttribute(String dbData) {
    return BaseEnum.fromValue(dbData, PublicType.class);
  }
}
