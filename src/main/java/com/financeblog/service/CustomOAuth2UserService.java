package com.financeblog.service;

import com.financeblog.entity.User;
import com.financeblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    private final UserRepository userRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        String email = extractEmail(registrationId, attributes);
        String name = extractName(registrationId, attributes);
        String providerId = extractProviderId(registrationId, attributes);
        
        log.info("OAuth2 로그인 시도: provider={}, email={}, name={}", registrationId, email, name);
        
        User user = findOrCreateUser(email, name, registrationId, providerId);
        
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
            attributes,
            getNameAttributeKey(registrationId)
        );
    }
    
    @SuppressWarnings("unchecked")
    private String extractEmail(String registrationId, Map<String, Object> attributes) {
        switch (registrationId) {
            case "google":
                return (String) attributes.get("email");
            case "github":
                return (String) attributes.get("email");
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                return (String) kakaoAccount.get("email");
            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                return (String) response.get("email");
            default:
                throw new OAuth2AuthenticationException("지원하지 않는 OAuth2 Provider입니다.");
        }
    }
    
    @SuppressWarnings("unchecked")
    private String extractName(String registrationId, Map<String, Object> attributes) {
        switch (registrationId) {
            case "google":
                return (String) attributes.get("name");
            case "github":
                return (String) attributes.get("name");
            case "kakao":
                Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
                return (String) properties.get("nickname");
            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                return (String) response.get("name");
            default:
                return "Unknown";
        }
    }
    
    @SuppressWarnings("unchecked")
    private String extractProviderId(String registrationId, Map<String, Object> attributes) {
        switch (registrationId) {
            case "google":
                return (String) attributes.get("sub");
            case "github":
                return String.valueOf(attributes.get("id"));
            case "kakao":
                return String.valueOf(attributes.get("id"));
            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                return (String) response.get("id");
            default:
                return null;
        }
    }
    
    private String getNameAttributeKey(String registrationId) {
        switch (registrationId) {
            case "google":
                return "sub";
            case "github":
                return "id";
            case "kakao":
                return "id";
            case "naver":
                return "response";
            default:
                return "id";
        }
    }
    
    private User findOrCreateUser(String email, String name, String provider, String providerId) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setProvider(provider);
            user.setProviderId(providerId);
            return userRepository.save(user);
        }
        
        User newUser = User.builder()
            .email(email)
            .username(email.split("@")[0] + "_" + provider)
            .name(name)
            .password("")
            .userRole(User.Role.USER)
            .provider(provider)
            .providerId(providerId)
            .build();
            
        return userRepository.save(newUser);
    }
}