package click.gudrb33333.metaworldapi.repository.member;

import static click.gudrb33333.metaworldapi.entity.QProfile.profile;

import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.QMember;
import click.gudrb33333.metaworldapi.repository.member.MemberRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Member> findMemberWithProfile(Member member) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(QMember.member)
            .innerJoin(QMember.member.profile, profile)
            .fetchJoin()
            .where(QMember.member.eq(member))
            .fetchOne());
  }
}
