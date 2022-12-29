package click.gudrb33333.metaworldapi.entity.type;

public class LoginTypeConverter extends BaseEnumConverter<LoginType, String> {

  @Override
  public LoginType convertToEntityAttribute(String dbData) {
    return BaseEnum.fromValue(dbData, LoginType.class);
  }
}
