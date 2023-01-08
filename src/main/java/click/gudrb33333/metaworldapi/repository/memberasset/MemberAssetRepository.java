package click.gudrb33333.metaworldapi.repository.memberasset;

import click.gudrb33333.metaworldapi.entity.Clothing;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.MemberAsset;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAssetRepository extends JpaRepository<MemberAsset, UUID> {

  Optional<MemberAsset> findByMemberAndAsset(Member sessionMember, Clothing clothing);
}
