package click.gudrb33333.metaworldapi.repository.clothing;

import click.gudrb33333.metaworldapi.entity.Clothing;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothingRepository extends JpaRepository<Clothing, UUID> {}
