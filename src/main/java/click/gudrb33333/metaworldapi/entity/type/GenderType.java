package click.gudrb33333.metaworldapi.entity.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GenderType implements BaseEnum<String> {
  MALE("male"),
  FEMALE("female"),
  BOTH("both");

  private final String genderType;

  GenderType(String genderType) {
    this.genderType = genderType;
  }

  @Override
  @JsonValue
  public String getValue() {
    return genderType;
  }
}
