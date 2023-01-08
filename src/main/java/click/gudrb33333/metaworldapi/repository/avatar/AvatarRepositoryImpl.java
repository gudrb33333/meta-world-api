package click.gudrb33333.metaworldapi.repository.avatar;

import static click.gudrb33333.metaworldapi.entity.QAsset.asset;
import static click.gudrb33333.metaworldapi.entity.QMemberAsset.memberAsset;

import click.gudrb33333.metaworldapi.entity.Avatar;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AvatarRepositoryImpl implements AvatarRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Avatar> findOneMemberAvatar(Member member) {

    return Optional.ofNullable(
            queryFactory
                .selectFrom(memberAsset)
                .innerJoin(memberAsset.asset, asset)
                .fetchJoin()
                .where(asset.assetType.eq(AssetType.AVATAR).and(memberAsset.member.eq(member)))
                .orderBy(memberAsset.insertedAt.desc())
                .fetchFirst())
        .map(result -> (Avatar) result.getAsset());
  }
}
