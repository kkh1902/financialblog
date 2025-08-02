package com.financeblog.config;

import com.financeblog.entity.Category;
import com.financeblog.entity.Post;
import com.financeblog.entity.User;
import com.financeblog.repository.CategoryRepository;
import com.financeblog.repository.PostRepository;
import com.financeblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "production"})  
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    
    public DataInitializer(UserRepository userRepository, 
                          CategoryRepository categoryRepository,
                          PostRepository postRepository,
                          @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Create admin user
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .displayName("관리자")
                    .userRole(User.Role.ADMIN)
                    .build();
            userRepository.save(admin);
            
            // Create categories
            Category investment = Category.builder()
                    .name("투자")
                    .slug("investment")
                    .description("주식, 펀드, 부동산 등 투자 정보")
                    .build();
            categoryRepository.save(investment);
            
            Category savings = Category.builder()
                    .name("저축")
                    .slug("savings")
                    .description("예금, 적금 등 저축 상품 정보")
                    .build();
            categoryRepository.save(savings);
            
            Category fintech = Category.builder()
                    .name("핀테크")
                    .slug("fintech")
                    .description("금융 기술 및 핀테크 서비스")
                    .build();
            categoryRepository.save(fintech);
            
            // Create sample posts
            Post post1 = Post.builder()
                    .title("주식 투자 초보자를 위한 가이드")
                    .slug("beginner-stock-investment-guide")
                    .content("주식 투자를 처음 시작하는 분들을 위한 기본적인 가이드입니다. 주식이란 무엇인지, 어떻게 투자해야 하는지, 주의사항은 무엇인지 자세히 알아보겠습니다.\n\n주식은 기업의 소유권을 나타내는 증서입니다. 주식을 구매한다는 것은 해당 기업의 일부를 소유하게 되는 것을 의미합니다.")
                    .summary("주식 투자를 처음 시작하는 분들을 위한 기본적인 가이드")
                    .author(admin)
                    .category(investment)
                    .published(true)
                    .featured(true)
                    .viewCount(1234)
                    .build();
            postRepository.save(post1);
            
            Post post2 = Post.builder()
                    .title("2024년 최고 금리 적금 상품 비교")
                    .slug("best-savings-products-2024")
                    .content("2024년 현재 시중에 나와있는 적금 상품들을 비교 분석해보겠습니다. 각 은행별 특징과 금리, 가입 조건 등을 상세히 살펴보겠습니다.\n\n현재 시중은행들의 적금 금리는 연 3.5%~4.5% 수준을 보이고 있습니다.")
                    .summary("2024년 최고 금리를 제공하는 적금 상품들을 비교 분석")
                    .author(admin)
                    .category(savings)
                    .published(true)
                    .featured(false)
                    .viewCount(856)
                    .build();
            postRepository.save(post2);
            
            Post post3 = Post.builder()
                    .title("핀테크 혁명: 금융의 미래")
                    .slug("fintech-revolution-future-of-finance")
                    .content("핀테크 기술이 전통적인 금융 서비스를 어떻게 변화시키고 있는지 살펴보겠습니다. 모바일 뱅킹, 디지털 결제, 로보어드바이저 등 다양한 핀테크 서비스들이 우리의 금융 생활을 편리하게 만들고 있습니다.")
                    .summary("핀테크 기술이 가져오는 금융 서비스의 혁신적 변화")
                    .author(admin)
                    .category(fintech)
                    .published(true)
                    .featured(true)
                    .viewCount(642)
                    .build();
            postRepository.save(post3);
            
            // Update category post counts
            investment.setPostCount(1);
            savings.setPostCount(1);
            fintech.setPostCount(1);
            categoryRepository.saveAll(java.util.Arrays.asList(investment, savings, fintech));
        }
    }
}