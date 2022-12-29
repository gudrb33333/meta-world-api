package click.gudrb33333.metaworldapi.entity.type;

public enum AssetType {
  AVATAR(Values.AVATAR),
  CLOTHING(Values.CLOTHING);

  AssetType(String val) {}

  public static class Values {
    public static final String AVATAR = "AVATAR";
    public static final String CLOTHING = "CLOTHING";
  }
}
