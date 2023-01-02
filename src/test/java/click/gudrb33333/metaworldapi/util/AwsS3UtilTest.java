package click.gudrb33333.metaworldapi.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AwsS3UtilTest {

  @InjectMocks private AwsS3Util awsS3Util;

  @Mock private AmazonS3Client amazonS3Client;

  @Test
  void uploadUrlFileToS3() {
    UUID fileUuid = UUID.randomUUID();
    ExtensionType extension = ExtensionType.GLB;
    S3DirectoryType s3DirectoryType = S3DirectoryType.AVATAR;
    String url = "https://test.com";

    assertThatThrownBy(
            () -> {
              awsS3Util.uploadUrlFileToS3(fileUuid, extension, s3DirectoryType, url);
            })
        .isInstanceOf(CatchedException.class)
        .hasMessageContaining(ErrorMessage.AWS_S3_UTIL_IO_ERROR);
  }

  @Test
  void uploadUrlFileToS32() throws IOException {
//    String bucketName = "testBuket";
//    String storeFileName = "testStoreFileName";
//
//    UUID fileUuid = UUID.randomUUID();
//    ExtensionType extension = ExtensionType.GLB;
//    S3DirectoryType s3DirectoryType = S3DirectoryType.AVATAR;
//    String url = "https://test.com";
//    InputStream inputStream = new BufferedInputStream(new URL("https://meta-world.gudrb33333.click/assets/AdobeStock_191213422_11zon.jpeg").openStream());

//    willDoNothing().willThrow(new IOException()).given(
//        amazonS3Client
//    ).putObject(
//        new PutObjectRequest(
//            bucketName + s3DirectoryType.getValue(), storeFileName, inputStream, null)
//            .withCannedAcl(CannedAccessControlList.PublicRead));

//    awsS3Util.uploadUrlFileToS3(fileUuid, extension, s3DirectoryType, url);
  }

  @Test
  void createSignedUrl() throws Exception {
    S3DirectoryType testS3DirectoryType = S3DirectoryType.AVATAR;
    UUID objectUuid = UUID.randomUUID();

    String signUrl = awsS3Util.createSignedUrl(testS3DirectoryType, objectUuid);

    assertThat(signUrl).contains("https://");
    assertThat(signUrl).contains("/avatar");
    assertThat(signUrl).contains(".glb");
    assertThat(signUrl).contains("?Policy=");
    assertThat(signUrl).contains("&Key-Pair-Id");
  }
}
