package click.gudrb33333.metaworldapi.entity.type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BaseEnumConverterTest {

  @InjectMocks private BaseEnumConverter<ExtensionType, String> baseEnumConverter;

  @Test
  void convertToDatabaseColumnArgIsNull() {
    String value = baseEnumConverter.convertToDatabaseColumn(null);

    assertThat(value).isEqualTo(null);
  }

  @Test
  void convertToDatabaseColumnArgIsNotNull() {
    String value = baseEnumConverter.convertToDatabaseColumn(ExtensionType.GLB);

    assertThat(value).isEqualTo("glb");
  }

  @Test
  void convertToEntityAttribute() {
    assertThatThrownBy(
            () -> {
              baseEnumConverter.convertToEntityAttribute(null);
            })
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("override this method.");
  }
}
