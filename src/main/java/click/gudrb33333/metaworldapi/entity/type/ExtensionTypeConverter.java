package click.gudrb33333.metaworldapi.entity.type;

public class ExtensionTypeConverter extends BaseEnumConverter<ExtensionType, String> {

  @Override
  public ExtensionType convertToEntityAttribute(String dbData) {
    return BaseEnum.fromValue(dbData, ExtensionType.class);
  }
}
