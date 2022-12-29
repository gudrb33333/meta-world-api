package click.gudrb33333.metaworldapi.entity.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PublicType implements BaseEnum<String> {
  PUBLIC("public"),
  PRIVATE("private");

  private final String publicType;

  PublicType(String publicType) {
    this.publicType = publicType;
  }

  @Override
  @JsonValue
  public String getValue() {
    return publicType;
  }
}
