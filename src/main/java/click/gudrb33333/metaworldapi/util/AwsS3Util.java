package click.gudrb33333.metaworldapi.util;

import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AwsS3Util {

  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  public void uploadFileToS3(
      UUID fileUuid,
      String extension,
      S3DirectoryType s3DirectoryType,
      MultipartFile multipartFile) {

    String storeFileName = fileUuid + "." + extension;

    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(multipartFile.getContentType());

    try (InputStream inputStream = multipartFile.getInputStream()) {
      amazonS3Client.putObject(
          new PutObjectRequest(
                  bucketName + s3DirectoryType.getValue(),
                  storeFileName,
                  inputStream,
                  objectMetadata)
              .withCannedAcl(CannedAccessControlList.PublicRead));
    } catch (IOException e) {
      throw new CatchedException(
          ErrorMessage.AWS_S3_UTIL_IO_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
