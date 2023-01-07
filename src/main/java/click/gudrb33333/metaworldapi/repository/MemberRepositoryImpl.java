package click.gudrb33333.metaworldapi.repository;

import static click.gudrb33333.metaworldapi.entity.QProfile.profile;
import static click.gudrb33333.metaworldapi.entity.QMember.member;

import click.gudrb33333.metaworldapi.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Member> findMemberWithProfile(Member member1111) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(member)
            .innerJoin(member.profile, profile)
            .fetchJoin()
            .where(member.eq(member1111))
            .fetchOne());
  }
}
