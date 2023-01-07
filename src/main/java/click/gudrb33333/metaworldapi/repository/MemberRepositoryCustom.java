package click.gudrb33333.metaworldapi.repository;

import click.gudrb33333.metaworldapi.entity.Member;
import java.util.Optional;

public interface MemberRepositoryCustom {
  Optional<Member> findMemberWithProfile(Member member);
}
