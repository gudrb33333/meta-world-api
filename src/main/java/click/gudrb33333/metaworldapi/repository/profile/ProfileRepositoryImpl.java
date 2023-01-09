package click.gudrb33333.metaworldapi.repository.profile;

import static click.gudrb33333.metaworldapi.entity.QAvatar.avatar;
import static click.gudrb33333.metaworldapi.entity.QProfile.profile;
import static org.springframework.util.StringUtils.hasText;

import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileSearchCondition;
import click.gudrb33333.metaworldapi.entity.Profile;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Profile> findAllWithCondition(ProfileSearchCondition condition, Pageable pageable) {
    JPAQuery<Profile> contentQuery =
        queryFactory
            .selectFrom(profile)
            .innerJoin(profile.avatar, avatar)
            .fetchJoin()
            .where(nicknameContains(condition.getNickname()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

    if(condition.getProfileOrders() != null){
      addOrderToQuery(contentQuery, condition.getProfileOrders());
    }

    JPAQuery<Profile> countQuery = queryFactory
        .selectFrom(profile)
        .innerJoin(profile.avatar, avatar)
        .where(nicknameContains(condition.getNickname()));

    return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery::fetchCount);
  }

  private BooleanExpression nicknameContains(String nickname) {
    return hasText(nickname) ? profile.nickname.contains(nickname) : null;
  }

  private void addOrderToQuery(JPAQuery<Profile> contentQuery, List<String> profileOrders) {
    for(String profileOrder : List.of(profileOrders.get(0).split("\\|"))){
      switch (profileOrder) {
        case "profile_inserted_at_asc" -> contentQuery.orderBy(profile.insertedAt.asc());
        case "profile_inserted_at_dsc" -> contentQuery.orderBy(profile.insertedAt.desc());
        case "avatar_inserted_at_asc" -> contentQuery.orderBy(avatar.insertedAt.asc());
        case "avatar_inserted_at_dsc" -> contentQuery.orderBy(avatar.insertedAt.desc());
      }
    }
  }
}
