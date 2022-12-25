package click.gudrb33333.metaworldapi.entity.type;

public enum Role {
  ROLE_USER("ROLE_MEMBER"),
  ROLE_ADMIN("ROLE_ADMIN");

  private final String role;

  Role(String role) {
    this.role = role;
  }

  public String value() {
    return role;
  }
}
