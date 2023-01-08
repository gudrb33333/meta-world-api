package click.gudrb33333.metaworldapi.repository.profile;

import click.gudrb33333.metaworldapi.entity.Profile;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {}
