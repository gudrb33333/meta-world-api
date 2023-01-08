package click.gudrb33333.metaworldapi.repository.avatar;

import click.gudrb33333.metaworldapi.entity.Avatar;
import click.gudrb33333.metaworldapi.entity.Member;
import java.util.Optional;

public interface AvatarRepositoryCustom {
  Optional<Avatar> findOneMemberAvatar(Member member);
}
