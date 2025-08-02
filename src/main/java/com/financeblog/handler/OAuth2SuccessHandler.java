package com.financeblog.handler;

import com.financeblog.entity.User;
import com.financeblog.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final UserRepository userRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        String email = extractEmail(authentication, attributes);
        
        log.info("OAuth2 로그인 성공: email={}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("OAuth2 인증 후 사용자를 찾을 수 없습니다."));
        
        log.info("사용자 정보: username={}, role={}", user.getUsername(), user.getRole());
        
        setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authentication);
    }
    
    @SuppressWarnings("unchecked")
    private String extractEmail(Authentication authentication, Map<String, Object> attributes) {
        String registrationId = getRegistrationId(authentication);
        
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
                throw new IllegalArgumentException("지원하지 않는 OAuth2 Provider입니다.");
        }
    }
    
    private String getRegistrationId(Authentication authentication) {
        String authenticationName = authentication.getName();
        if (authenticationName.contains("google")) {
            return "google";
        } else if (authenticationName.contains("github")) {
            return "github";
        } else if (authenticationName.contains("kakao")) {
            return "kakao";
        } else if (authenticationName.contains("naver")) {
            return "naver";
        }
        
        return "";
    }
}