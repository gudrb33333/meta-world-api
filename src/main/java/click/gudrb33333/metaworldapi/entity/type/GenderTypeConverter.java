package click.gudrb33333.metaworldapi.entity.type;

public class GenderTypeConverter extends BaseEnumConverter<GenderType, String> {

  @Override
  public GenderType convertToEntityAttribute(String dbData) {
    return BaseEnum.fromValue(dbData, GenderType.class);
  }
}
