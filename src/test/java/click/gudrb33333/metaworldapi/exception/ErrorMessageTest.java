package click.gudrb33333.metaworldapi.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ErrorMessageTest {

  @InjectMocks ErrorMessage errorMessage;

  @Test
  void constantsValue() {
    assertThat(ErrorMessage.CONFLICT_EMAIL)
        .isEqualTo("email already exists(unique key violation).");
    assertThat(ErrorMessage.CONFLICT_PROFILE).isEqualTo("profile already exists.");
    assertThat(ErrorMessage.NOT_FOUND_MEMBER).isEqualTo("member not found.");
    assertThat(ErrorMessage.NOT_FOUND_AVATAR).isEqualTo("avatar not found.");
    assertThat(ErrorMessage.NOT_FOUND_PROFILE).isEqualTo("profile not found.");
    assertThat(ErrorMessage.NOT_FOUND_CLOTHING).isEqualTo("clothing not found.");
    assertThat(ErrorMessage.AWS_S3_UTIL_IO_ERROR).isEqualTo("IO error in AwsS3Util.");
    assertThat(ErrorMessage.NOT_FOUND_SESSION).isEqualTo("session not found.");
  }
}
