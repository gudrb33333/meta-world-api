package click.gudrb33333.metaworldapi.entity.type;

public enum Role {
  MEMBER("MEMBER"),
  ADMIN("ADMIN");

  private final String role;

  Role(String role) {
    this.role = role;
  }

  public String value() {
    return role;
  }
}
