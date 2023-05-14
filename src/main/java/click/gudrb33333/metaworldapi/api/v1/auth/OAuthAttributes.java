package click.gudrb33333.metaworldapi.api.v1.auth;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;

    @Builder
    public OAuthAttributes(
        Map<String, Object> attributes,
        String nameAttributeKey,
        String name,
        String email,
        String picture
    ) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(
        String registrationId,
        String userNameAttributeName,
        Map<String, Object> attributes
    ) {
        return switch (registrationId) {
            case "kakao" -> ofKakao(userNameAttributeName, attributes);
            case "naver" -> ofNaver(userNameAttributeName, attributes);
            case "google" -> ofGoogle(userNameAttributeName, attributes);
            default -> null;
        };
    }

    private static OAuthAttributes ofGoogle(
        String userNameAttributeName,
        Map<String, Object> attributes
    ) {
        return OAuthAttributes.builder()
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .picture((String) attributes.get("profile_image"))
            .build();
    }

    private static OAuthAttributes ofNaver(
        String userNameAttributeName,
        Map<String, Object> attributes
    ) {
        HashMap<String,String> map = (HashMap<String, String>) attributes.get("response");

        return OAuthAttributes.builder()
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .name((String) map.get("name"))
            .email((String) map.get("email"))
            .picture((String) map.get("profile_image_url"))
            .build();
    }

    private static OAuthAttributes ofKakao(
        String userNameAttributeName,
        Map<String, Object> attributes
    ) {
        HashMap <String,String> map = (HashMap<String, String>) attributes.get("kakao_account");

        return OAuthAttributes.builder()
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .name((String) attributes.get("nickname"))
            .email((String) map.get("email"))
            .picture((String) attributes.get("profile_image_url"))
            .build();
    }
}
