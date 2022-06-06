package com.example.oauthprojects.service;

import com.example.oauthprojects.PrincipalDetails;
import com.example.oauthprojects.Role;
import com.example.oauthprojects.User;
import com.example.oauthprojects.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)throws OAuth2AuthenticationException{

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientId(); //gogle
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId; // 사용자가 입력한 적은 없지만 만들어준다.

        String uuid = UUID.randomUUID().toString().substring(0,6);
        String password = bCryptPasswordEncoder.encode("패스워드"+ uuid); // 사용자가 입력한 적은 없지만 만들어준다.

        String email = oAuth2User.getAttribute("email");
        Role role = Role.ROLE_USER;

        User byUsername = userRepository.findByUsername(username);

        //DB에 없는 User 회원가입처리
        if(byUsername == null){
            byUsername = User.oauth2Register()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(byUsername);
        }
        return new PrincipalDetails(byUsername, oAuth2User.getAttributes());
    }
}
