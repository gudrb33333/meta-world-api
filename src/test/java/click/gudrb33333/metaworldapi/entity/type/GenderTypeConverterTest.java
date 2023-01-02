package click.gudrb33333.metaworldapi.entity.type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GenderTypeConverterTest {

  @InjectMocks private GenderTypeConverter genderTypeConverter;

  @Test
  void convertToEntityAttribute() {
    String testUnKnownDbData = "unknownData";
    String testNullDbData = null;
    String testDbData1 = GenderType.MALE.getValue();
    String testDbData2 = GenderType.FEMALE.getValue();

    GenderType genderType1 = genderTypeConverter.convertToEntityAttribute(testUnKnownDbData);
    GenderType genderType2 = genderTypeConverter.convertToEntityAttribute(testNullDbData);
    GenderType genderType3 = genderTypeConverter.convertToEntityAttribute(testDbData1);
    GenderType genderType4 = genderTypeConverter.convertToEntityAttribute(testDbData2);

    assertThat(genderType1).isEqualTo(null);
    assertThat(genderType2).isEqualTo(null);
    assertThat(genderType3.getValue()).isEqualTo(testDbData1);
    assertThat(genderType4.getValue()).isEqualTo(testDbData2);
  }
}
