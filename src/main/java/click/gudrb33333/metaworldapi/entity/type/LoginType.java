package click.gudrb33333.metaworldapi.entity.type;

public enum LoginType {
  LOCAL("LOCAL");

  private final String loginType;

  LoginType(String loginType) {
    this.loginType = loginType;
  }

  public String value() {
    return loginType;
  }
}
