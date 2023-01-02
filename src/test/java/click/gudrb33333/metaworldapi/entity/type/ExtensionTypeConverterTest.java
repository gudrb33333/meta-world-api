package click.gudrb33333.metaworldapi.entity.type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExtensionTypeConverterTest {

  @InjectMocks private ExtensionTypeConverter extensionTypeConverter;

  @Test
  void convertToEntityAttribute() {
    String testUnKnownDbData = "unknownData";
    String testNullDbData = null;
    String dbData = ExtensionType.GLB.getValue();

    ExtensionType extensionType1 = extensionTypeConverter.convertToEntityAttribute(testUnKnownDbData);
    ExtensionType extensionType2 = extensionTypeConverter.convertToEntityAttribute(testNullDbData);
    ExtensionType extensionType3 = extensionTypeConverter.convertToEntityAttribute(dbData);

    assertThat(extensionType1).isEqualTo(null);
    assertThat(extensionType2).isEqualTo(null);
    assertThat(extensionType3.getValue()).isEqualTo(dbData);
  }
}
