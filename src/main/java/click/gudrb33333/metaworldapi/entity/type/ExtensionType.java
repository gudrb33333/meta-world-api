package click.gudrb33333.metaworldapi.entity.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ExtensionType implements BaseEnum<String> {
  GLB("glb");

  private final String extensionType;

  ExtensionType(String extensionType) {
    this.extensionType = extensionType;
  }

  @Override
  @JsonValue
  public String getValue() {
    return extensionType;
  }
}
