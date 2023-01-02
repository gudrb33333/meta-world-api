package click.gudrb33333.metaworldapi.entity.type;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class S3DirectoryTypeConverterTest {

  @InjectMocks private S3DirectoryTypeConverter s3DirectoryTypeConverter;

  @Test
  void convertToEntityAttribute() {
    String testUnKnownDbData = "unknownData";
    String testNullDbData = null;
    String testDbData1 = S3DirectoryType.AVATAR.getValue();
    String testDbData2 = S3DirectoryType.CLOTHING.getValue();

    S3DirectoryType s3DirectoryType1 =
        s3DirectoryTypeConverter.convertToEntityAttribute(testUnKnownDbData);
    S3DirectoryType s3DirectoryType2 =
        s3DirectoryTypeConverter.convertToEntityAttribute(testNullDbData);
    S3DirectoryType s3DirectoryType3 =
        s3DirectoryTypeConverter.convertToEntityAttribute(testDbData1);
    S3DirectoryType s3DirectoryType4 =
        s3DirectoryTypeConverter.convertToEntityAttribute(testDbData2);

    assertThat(s3DirectoryType1).isEqualTo(null);
    assertThat(s3DirectoryType2).isEqualTo(null);
    assertThat(s3DirectoryType3.getValue()).isEqualTo(testDbData1);
    assertThat(s3DirectoryType4.getValue()).isEqualTo(testDbData2);
  }

}
