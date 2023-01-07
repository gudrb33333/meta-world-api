package click.gudrb33333.metaworldapi.repository;

import click.gudrb33333.metaworldapi.entity.Member;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class MemberRepositoryImplTest {

  @InjectMocks MemberRepositoryImpl memberRepository;

  @Mock JPAQueryFactory queryFactory;

  @Mock private JPAQuery<Member> jpaQuery;
}
