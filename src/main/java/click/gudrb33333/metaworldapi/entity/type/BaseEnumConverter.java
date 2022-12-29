package click.gudrb33333.metaworldapi.entity.type;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BaseEnumConverter<E extends BaseEnum<V>, V> implements AttributeConverter<E, V> {

  @Override
  public V convertToDatabaseColumn(E attribute) {
    return attribute != null ? attribute.getValue() : null;
  }

  @Override
  public E convertToEntityAttribute(V dbData) {
    throw new RuntimeException("override this method.");
  }
}
