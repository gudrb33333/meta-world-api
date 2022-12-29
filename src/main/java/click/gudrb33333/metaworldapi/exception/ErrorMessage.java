package click.gudrb33333.metaworldapi.exception;

public class ErrorMessage {
    public static final String CONFLICT_EMAIL = "email already exists(unique key violation).";

    /// NOT FOUND
    public static final String NOT_FOUND_MEMBER = "member not found.";
    public static final String NOT_FOUND_AVATAR = "avatar not found.";

    // INTERNAL_SERVER_ERROR
    public static final String AWS_S3_UTIL_IO_ERROR = "IO error in AwsS3Util.";
}
