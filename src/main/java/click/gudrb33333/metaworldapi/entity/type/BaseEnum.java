package click.gudrb33333.metaworldapi.entity.type;

public interface BaseEnum<V> {
  V getValue();

  static <E extends Enum<E> & BaseEnum<ID>, ID> E fromValue(ID value, Class<E> type) {
    if (value == null) {
      return null;
    }

    for (E e : type.getEnumConstants()) {
      if (e.getValue().equals(value)) {
        return e;
      }
    }

    return null;
  }
}
