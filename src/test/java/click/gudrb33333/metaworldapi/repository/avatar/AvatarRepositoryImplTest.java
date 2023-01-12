package click.gudrb33333.metaworldapi.repository.avatar;

import static click.gudrb33333.metaworldapi.entity.QAsset.asset;
import static click.gudrb33333.metaworldapi.entity.QMemberAsset.memberAsset;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import click.gudrb33333.metaworldapi.entity.Avatar;
import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.MemberAsset;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.LoginType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import click.gudrb33333.metaworldapi.exception.CommonException;
import click.gudrb33333.metaworldapi.exception.ErrorMessage;
import click.gudrb33333.metaworldapi.repository.avatar.AvatarRepositoryImpl;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class AvatarRepositoryImplTest {

  @InjectMocks
  AvatarRepositoryImpl avatarRepositoryImpl;

  @Mock JPAQueryFactory queryFactory;

  @Mock private JPAQuery<MemberAsset> jpaQuery;

  @Test
  void findOneMemberAvatar() {
    Member testMember =
        Member.builder()
            .id(UUID.randomUUID())
            .email("test@test.com")
            .password("testPassword")
            .role(Role.MEMBER)
            .loginType(LoginType.LOCAL)
            .build();

    Avatar testAvatar =
        Avatar.builder()
            .id(UUID.randomUUID())
            .s3DirectoryType(S3DirectoryType.AVATAR)
            .s3AssetUUID(UUID.randomUUID())
            .publicType(PublicType.PRIVATE)
            .genderType(GenderType.MALE)
            .extension(ExtensionType.GLB)
            .assetType(AssetType.AVATAR)
            .build();

    MemberAsset testMemberAsset =
        MemberAsset.builder().id(UUID.randomUUID()).member(testMember).asset(testAvatar).build();

    given(queryFactory.selectFrom(memberAsset)).willReturn(jpaQuery);
    given(jpaQuery.innerJoin(memberAsset.asset, asset)).willReturn(jpaQuery);
    given(jpaQuery.fetchJoin()).willReturn(jpaQuery);
    given(jpaQuery.where(any(Predicate.class))).willReturn(jpaQuery);
    given(jpaQuery.orderBy(any(OrderSpecifier.class))).willReturn(jpaQuery);
    given(jpaQuery.limit(anyLong())).willReturn(jpaQuery);
    given(jpaQuery.fetchFirst()).willReturn(testMemberAsset);

    Optional<Avatar> OptionalFindAvatar = avatarRepositoryImpl.findOneMemberAvatar(testMember);
    Avatar findAvatar =
        OptionalFindAvatar.orElseThrow(
            () -> {
              throw new CommonException(ErrorMessage.NOT_FOUND_AVATAR, HttpStatus.NOT_FOUND);
            });

    assertThat(findAvatar.getId()).isEqualTo(testAvatar.getId());
  }
}
