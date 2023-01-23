package click.gudrb33333.metaworldapi.exception;

public class ErrorMessage {

  // CONFLICT
  public static final String CONFLICT_EMAIL = "email already exists(unique key violation).";
  public static final String CONFLICT_PROFILE = "profile already exists.";

  /// NOT FOUND
  public static final String NOT_FOUND_MEMBER = "member not found.";
  public static final String NOT_FOUND_AVATAR = "avatar not found.";
  public static final String NOT_FOUND_PROFILE = "profile not found.";
  public static final String NOT_FOUND_CLOTHING = "clothing not found.";

  // INTERNAL_SERVER_ERROR
  public static final String AWS_S3_UTIL_IO_ERROR = "IO error in AwsS3Util.";

  // FORBIDDEN
  public static final String NOT_FOUND_SESSION = "session not found.";
  public static final String ACCESS_DENIED = "access denied.";

  // BAD REQUEST
  public static final String NOT_SUPPORT_METHOD = "Authentication method not supported:";
  public static final String REQUEST_IS_NOT_JSON = "Authentication request is not JSON";
  public static final String REQUEST_CONTENT_TYPE_PARSING_ERROR =
      "Request Content-Type(application/json) Parsing Error";
}
