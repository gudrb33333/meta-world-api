package click.gudrb33333.metaworldapi.entity.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LoginType implements BaseEnum<String> {
  LOCAL("local"),
  KAKAO("kakao"),
  GOOGLE("google"),
  NAVER("naver");

  private final String loginType;

  LoginType(String loginType) {
    this.loginType = loginType;
  }

  @Override
  @JsonValue
  public String getValue() {
    return loginType;
  }
}
