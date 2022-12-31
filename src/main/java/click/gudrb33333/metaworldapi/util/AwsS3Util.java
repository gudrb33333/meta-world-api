package click.gudrb33333.metaworldapi.util;

import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Security;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontService;
import org.jets3t.service.CloudFrontServiceException;
import org.jets3t.service.utils.ServiceUtils;
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

  @Value("${cloud.aws.cloudFront.distributionDomain}")
  private String distributionDomain;

  @Value("${cloud.aws.cloudFront.keyPairId}")
  private String keyPairId;

  public void uploadUrlFileToS3(
      UUID fileUuid, ExtensionType extension, S3DirectoryType s3DirectoryType, String url) {

    String storeFileName = fileUuid + "." + extension.getValue();

    // TODO: fix no content length
    try (InputStream inputStream = new BufferedInputStream(new URL(url).openStream())) {
      amazonS3Client.putObject(
          new PutObjectRequest(
                  bucketName + s3DirectoryType.getValue(), storeFileName, inputStream, null)
              .withCannedAcl(CannedAccessControlList.PublicRead));
    } catch (IOException e) {
      throw new CatchedException(
          ErrorMessage.AWS_S3_UTIL_IO_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public String createSignedUrl(S3DirectoryType s3DirectoryType, UUID objectUuid)
      throws IOException, ParseException, CloudFrontServiceException {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    String privateKeyFilePath = "private_key.der";
    String s3ObjectKey = s3DirectoryType.getValue() + "/" + objectUuid + ".glb";
    String policyResourcePath = "https://" + distributionDomain + s3ObjectKey;

    byte[] derPrivateKey =
        ServiceUtils.readInputStreamToBytes(new FileInputStream(privateKeyFilePath));

    TimeZone timeZone = TimeZone.getTimeZone("UTC");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    dateFormat.setTimeZone(timeZone);
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, 1);
    Date date = new Date(calendar.getTimeInMillis());
    String nowAsISO = dateFormat.format(date);

    String signedUrlCanned =
        CloudFrontService.signUrlCanned(
            "https://" + distributionDomain + s3ObjectKey, // Resource URL or Path
            keyPairId, // Certificate identifier,
            // an active trusted signer for the distribution
            derPrivateKey, // DER Private key data
            ServiceUtils.parseIso8601Date(nowAsISO) // DateLessThan
            );

    String policy =
        CloudFrontService.buildPolicyForSignedUrl(
            // Resource path (optional, can include '*' and '?' wildcards)
            policyResourcePath,
            // DateLessThan
            // TODO: fix epochDateLessThan
            ServiceUtils.parseIso8601Date(nowAsISO),
            // CIDR IP address restriction (optional, 0.0.0.0/0 means everyone)
            "0.0.0.0/0",
            // DateGreaterThan (optional)
            null);

    return CloudFrontService.signUrl(
        // Resource URL or Path
        "https://" + distributionDomain + s3ObjectKey,
        // Certificate identifier, an active trusted signer for the distribution
        keyPairId,
        // DER Private key data
        derPrivateKey,
        // Access control policy
        policy);
  }
}
