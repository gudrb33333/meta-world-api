package click.gudrb33333.metaworldapi.api.v1.profile.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileResponseDto {

    private String nickname;
    private String signedAvatarUrl;

    @Builder
    public ProfileResponseDto(String nickname, String signedAvatarUrl){
        this.nickname = nickname;
        this.signedAvatarUrl = signedAvatarUrl;
    }
}
