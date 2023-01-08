package click.gudrb33333.metaworldapi.repository;

import static click.gudrb33333.metaworldapi.entity.QProfile.profile;
import static click.gudrb33333.metaworldapi.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.Profile;
import click.gudrb33333.metaworldapi.exception.CatchedException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.member.MemberRepositoryImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class MemberRepositoryImplTest {

  @InjectMocks
  MemberRepositoryImpl memberRepository;

  @Mock JPAQueryFactory queryFactory;

  @Mock private JPAQuery<Member> jpaQuery;

  @Test
  void findMemberWithProfile() {
    Member testMember =
        Member.builder()
            .id(UUID.randomUUID())
            .build();

    Profile testProfile =
        Profile.builder()
            .id(UUID.randomUUID())
            .build();

    Member testMemberWithProfile =
        Member.builder()
            .id(testMember.getId())
            .profile(testProfile)
            .build();

    given(queryFactory.selectFrom(member)).willReturn(jpaQuery);
    given(jpaQuery.innerJoin(member.profile, profile)).willReturn(jpaQuery);
    given(jpaQuery.fetchJoin()).willReturn(jpaQuery);
    given(jpaQuery.where(any(Predicate.class))).willReturn(jpaQuery);
    given(jpaQuery.fetchOne()).willReturn(testMemberWithProfile);

    Member findMember =
        memberRepository
            .findMemberWithProfile(testMember)
            .orElseThrow(
                () -> {
                  throw new CatchedException(ErrorMessage.NOT_FOUND_MEMBER, HttpStatus.NOT_FOUND);
                });

    assertThat(findMember.getId()).isEqualTo(testMember.getId());
    assertThat(findMember.getProfile()).isEqualTo(testProfile);
  }
}
