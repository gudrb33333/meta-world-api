package click.gudrb33333.metaworldapi.api.v1.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDto {

     private String email;
     private int maxInactive;

     @Builder
     public MemberResponseDto(String email, int maxInactive) {
          this.email = email;
          this.maxInactive = maxInactive;
     }
}
