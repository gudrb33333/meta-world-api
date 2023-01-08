package click.gudrb33333.metaworldapi.repository.member;

import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.LoginType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID>, MemberRepositoryCustom {

  Optional<Member> findByEmail(String email);

  Optional<Member> findByEmailAndLoginType(String email, LoginType loginType);
}
