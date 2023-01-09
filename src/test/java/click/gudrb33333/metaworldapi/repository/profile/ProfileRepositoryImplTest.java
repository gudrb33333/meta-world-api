package click.gudrb33333.metaworldapi.repository.profile;

import static click.gudrb33333.metaworldapi.entity.QProfile.profile;
import static click.gudrb33333.metaworldapi.entity.QAvatar.avatar;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

import click.gudrb33333.metaworldapi.api.v1.profile.dto.ProfileSearchCondition;
import click.gudrb33333.metaworldapi.entity.Avatar;
import click.gudrb33333.metaworldapi.entity.Profile;
import click.gudrb33333.metaworldapi.entity.type.AssetType;
import click.gudrb33333.metaworldapi.entity.type.ExtensionType;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.PublicType;
import click.gudrb33333.metaworldapi.entity.type.S3DirectoryType;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ProfileRepositoryImplTest {

  @InjectMocks ProfileRepositoryImpl profileRepositoryImpl;

  @Mock JPAQueryFactory queryFactory;

  @Mock JPAQuery<Profile> jpaQuery;

  @Nested
  class findAllWithCondition {
    Pageable testPageable = PageRequest.of(0, 8);

    Avatar testAvatar =
        Avatar.builder()
            .id(UUID.randomUUID())
            .assetType(AssetType.AVATAR)
            .s3AssetUUID(UUID.randomUUID())
            .extension(ExtensionType.GLB)
            .s3DirectoryType(S3DirectoryType.AVATAR)
            .genderType(GenderType.MALE)
            .publicType(PublicType.PRIVATE)
            .build();

    Profile testProfile = Profile.builder().nickname("testNickname").avatar(testAvatar).build();
    List<Profile> testProfileList = Stream.of(testProfile).collect(Collectors.toList());

    Method testNicknameContains;

    @BeforeEach
    public void setTestMethod() throws NoSuchMethodException {
      testNicknameContains =
          profileRepositoryImpl.getClass().getDeclaredMethod("nicknameContains", String.class);
      testNicknameContains.setAccessible(true);
    }

    @Test
    void whenProfileOrderIsNotProper() {
      ArrayList<String> testProfileOrder = new ArrayList<>(List.of("" + "not_proper"));

      ProfileSearchCondition testProfileSearchCondition =
          ProfileSearchCondition.builder()
              .nickname("testNickname")
              .profileOrders(testProfileOrder)
              .build();

      given(queryFactory.selectFrom(profile)).willReturn(jpaQuery);
      given(jpaQuery.innerJoin(profile.avatar, avatar)).willReturn(jpaQuery);
      given(jpaQuery.fetchJoin()).willReturn(jpaQuery);
      given(jpaQuery.where(any(Predicate.class))).willReturn(jpaQuery);
      given(jpaQuery.offset(testPageable.getOffset())).willReturn(jpaQuery);
      given(jpaQuery.limit(testPageable.getPageSize())).willReturn(jpaQuery);
      given(jpaQuery.fetch()).willReturn(testProfileList);

      Page<Profile> profilePage =
          profileRepositoryImpl.findAllWithCondition(testProfileSearchCondition, testPageable);

      assertThat(profilePage.getContent().contains(testProfile)).isEqualTo(true);
      assertThat(profilePage.getTotalPages()).isEqualTo(1);
    }

    @Test
    void whenProfileOrderIsNull() {
      ArrayList<String> testProfileOrder = null;

      ProfileSearchCondition testProfileSearchCondition =
          ProfileSearchCondition.builder()
              .nickname("testNickname")
              .profileOrders(testProfileOrder)
              .build();

      given(queryFactory.selectFrom(profile)).willReturn(jpaQuery);
      given(jpaQuery.innerJoin(profile.avatar, avatar)).willReturn(jpaQuery);
      given(jpaQuery.fetchJoin()).willReturn(jpaQuery);
      given(jpaQuery.where(any(Predicate.class))).willReturn(jpaQuery);
      given(jpaQuery.offset(testPageable.getOffset())).willReturn(jpaQuery);
      given(jpaQuery.limit(testPageable.getPageSize())).willReturn(jpaQuery);
      given(jpaQuery.fetch()).willReturn(testProfileList);

      Page<Profile> profilePage =
          profileRepositoryImpl.findAllWithCondition(testProfileSearchCondition, testPageable);

      assertThat(profilePage.getContent().contains(testProfile)).isEqualTo(true);
      assertThat(profilePage.getTotalPages()).isEqualTo(1);
    }

    @Test
    void whenConditionNicknameIsNull() throws InvocationTargetException, IllegalAccessException {
      ArrayList<String> testProfileOrder =
          new ArrayList<>(
              List.of(
                  ""
                      + "profile_inserted_at_asc|"
                      + "profile_inserted_at_dsc|"
                      + "avatar_inserted_at_asc|"
                      + "avatar_inserted_at_dsc"));

      ProfileSearchCondition testProfileSearchCondition =
          ProfileSearchCondition.builder().nickname(null).profileOrders(testProfileOrder).build();

      given(queryFactory.selectFrom(profile)).willReturn(jpaQuery);
      given(jpaQuery.innerJoin(profile.avatar, avatar)).willReturn(jpaQuery);
      given(jpaQuery.fetchJoin()).willReturn(jpaQuery);
      given(jpaQuery.where((Predicate) null)).willReturn(jpaQuery);
      given(jpaQuery.offset(testPageable.getOffset())).willReturn(jpaQuery);
      given(jpaQuery.limit(testPageable.getPageSize())).willReturn(jpaQuery);
      given(jpaQuery.fetch()).willReturn(testProfileList);

      Page<Profile> profilePage =
          profileRepositoryImpl.findAllWithCondition(testProfileSearchCondition, testPageable);

      assertThat(profilePage.getContent().contains(testProfile)).isEqualTo(true);
      assertThat(profilePage.getTotalPages()).isEqualTo(1);
    }

    @Test
    void whenProfileOrderIsProperAndConditionHasText() {
      ArrayList<String> testProfileOrder =
          new ArrayList<>(
              List.of(
                  ""
                      + "profile_inserted_at_asc|"
                      + "profile_inserted_at_dsc|"
                      + "avatar_inserted_at_asc|"
                      + "avatar_inserted_at_dsc"));

      ProfileSearchCondition testProfileSearchCondition =
          ProfileSearchCondition.builder()
              .nickname("testNickname")
              .profileOrders(testProfileOrder)
              .build();

      given(queryFactory.selectFrom(profile)).willReturn(jpaQuery);
      given(jpaQuery.innerJoin(profile.avatar, avatar)).willReturn(jpaQuery);
      given(jpaQuery.fetchJoin()).willReturn(jpaQuery);
      given(jpaQuery.where(any(Predicate.class))).willReturn(jpaQuery);
      given(jpaQuery.offset(testPageable.getOffset())).willReturn(jpaQuery);
      given(jpaQuery.limit(testPageable.getPageSize())).willReturn(jpaQuery);
      given(jpaQuery.fetch()).willReturn(testProfileList);

      Page<Profile> profilePage =
          profileRepositoryImpl.findAllWithCondition(testProfileSearchCondition, testPageable);

      assertThat(profilePage.getContent().contains(testProfile)).isEqualTo(true);
      assertThat(profilePage.getTotalPages()).isEqualTo(1);
    }
  }
}
