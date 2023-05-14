package click.gudrb33333.metaworldapi.api.v1.auth;

import click.gudrb33333.metaworldapi.entity.Member;
import click.gudrb33333.metaworldapi.entity.type.LoginType;
import click.gudrb33333.metaworldapi.entity.type.Role;
import click.gudrb33333.metaworldapi.repository.member.MemberRepository;
import java.util.Collections;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService =
            new DefaultOAuth2UserService();

        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(
            registrationId,
            userNameAttributeName,
            oAuth2User.getAttributes()
        );

        LoginType loginType = null;
        if (userNameAttributeName.equals("sub")) {
            loginType = LoginType.GOOGLE;
        } else if (userNameAttributeName.equals("response")) {
            loginType = LoginType.NAVER;
        } else if (userNameAttributeName.equals("id") && registrationId.equals("kakao")) {
            loginType = LoginType.KAKAO;
        }

        if (loginType == null) {
            throw new IllegalStateException("loginType cannot be null");
        }

        String email = Objects.requireNonNull(attributes).getEmail();

        LoginType finalLoginType = loginType;
        Member foundMember = memberRepository.findByEmailAndLoginType(email, loginType)
            .orElseGet(
                () -> createMember(email,finalLoginType)
            );

        session.setAttribute("role", "MEMBER");
        session.setAttribute("current-member" , foundMember);

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER"))
            ,attributes.getAttributes()
            ,attributes.getNameAttributeKey());
    }

    private Member createMember(String email, LoginType finalLoginType) {
        Member newMember =Member.builder()
            .email(email)
            .password("noPassword")
            .loginType(finalLoginType)
            .role(Role.MEMBER)
            .build();

        return memberRepository.save(newMember);
    }
}
