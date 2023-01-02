package click.gudrb33333.metaworldapi.entity.type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PublicTypeConverterTest {

  @InjectMocks private PublicTypeConverter publicTypeConverter;

  @Test
  void convertToEntityAttribute() {
    String testUnKnownDbData = "unknownData";
    String testNullDbData = null;
    String testDbData1 = PublicType.PUBLIC.getValue();
    String testDbData2 = PublicType.PRIVATE.getValue();

    PublicType publicType1 = publicTypeConverter.convertToEntityAttribute(testUnKnownDbData);
    PublicType publicType2 = publicTypeConverter.convertToEntityAttribute(testNullDbData);
    PublicType publicType3 = publicTypeConverter.convertToEntityAttribute(testDbData1);
    PublicType publicType4 = publicTypeConverter.convertToEntityAttribute(testDbData2);

    assertThat(publicType1).isEqualTo(null);
    assertThat(publicType2).isEqualTo(null);
    assertThat(publicType3.getValue()).isEqualTo(testDbData1);
    assertThat(publicType4.getValue()).isEqualTo(testDbData2);
  }
}
