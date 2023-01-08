package click.gudrb33333.metaworldapi.repository.avatar;

import click.gudrb33333.metaworldapi.entity.Avatar;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, UUID>, AvatarRepositoryCustom {}
