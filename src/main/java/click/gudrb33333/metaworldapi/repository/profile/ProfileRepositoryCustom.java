package click.gudrb33333.metaworldapi.repository.profile;

import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileSearchCondition;
import click.gudrb33333.metaworldapi.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileRepositoryCustom {
  Page<Profile> findAllWithCondition(ProfileSearchCondition condition, Pageable pageable);
}
