package click.gudrb33333.metaworldapi.api.v1.profile.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileCreateDto {

    private String nickname;

    @Builder
    public ProfileCreateDto(String nickname) {
        this.nickname = nickname;
    }
}
