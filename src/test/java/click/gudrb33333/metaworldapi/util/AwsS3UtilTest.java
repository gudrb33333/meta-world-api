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
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class AwsS3UtilTest {

  @InjectMocks private AwsS3Util awsS3Util;

  @Mock private AmazonS3Client amazonS3Client;

  @Test
  void uploadUrlFileToS3() {
    UUID fileUuid = UUID.randomUUID();
    ExtensionType extension = ExtensionType.GLB;
    S3DirectoryType s3DirectoryType = S3DirectoryType.CLOTHING;
    String url = "https://meta-world.gudrb33333.click/favicon.ico";

    awsS3Util.uploadUrlFileToS3(fileUuid, extension, s3DirectoryType, url);
  }

  @Test
  void uploadUrlFileToS3ThrowIOException() {
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
  void uploadLocalFileToS3() {
    UUID fileUuid = UUID.randomUUID();
    ExtensionType extension = ExtensionType.GLB;
    S3DirectoryType s3DirectoryType = S3DirectoryType.CLOTHING;
    MockMultipartFile testFile =
        new MockMultipartFile("multipartFile", "filename.txt", "text/plain", "some xml".getBytes());

    awsS3Util.uploadLocalFileToS3(fileUuid, extension, s3DirectoryType, testFile);
  }

  @Test
  void uploadLocalFileToS3ThrowIOException() {
    UUID fileUuid = UUID.randomUUID();
    ExtensionType extension = ExtensionType.GLB;
    S3DirectoryType s3DirectoryType = S3DirectoryType.CLOTHING;
    IOExceptionMockMultipartFile ioExceptionTestFile =
        new IOExceptionMockMultipartFile(
            "multipartFile", "filename.txt", "text/plain", (byte[]) null);

    assertThatThrownBy(
            () -> {
              awsS3Util.uploadLocalFileToS3(
                  fileUuid, extension, s3DirectoryType, ioExceptionTestFile);
            })
        .isInstanceOf(CatchedException.class)
        .hasMessageContaining(ErrorMessage.AWS_S3_UTIL_IO_ERROR);
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

  private class IOExceptionMockMultipartFile extends MockMultipartFile {

    public IOExceptionMockMultipartFile(
        String name, String originalFilename, String contentType, byte[] content) {
      super(name, originalFilename, contentType, content);
    }

    // Method is overrided, so that it throws an IOException, when it's called
    @Override
    public InputStream getInputStream() throws IOException {
      throw new IOException();
    }
  }
}
