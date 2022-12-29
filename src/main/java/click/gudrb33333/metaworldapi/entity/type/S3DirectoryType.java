package click.gudrb33333.metaworldapi.entity.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum S3DirectoryType implements BaseEnum<String> {
  AVATAR("/avatar"),
  CLOTHING("/clothing");

  private final String directory;

  S3DirectoryType(String directory) {
    this.directory = directory;
  }

  @Override
  @JsonValue
  public String getValue() {
    return directory;
  }
}
