package sjs.assignment.sjsassignment.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sjs.assignment.sjsassignment.filter.JwtAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring security룰을 무시하게 하는 url규칙
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico")
                .antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
//                .antMatchers("/resources/**")
//                .antMatchers("/css/**")
//                .antMatchers("/vendor/**")
//                .antMatchers("/js/**")
//                .antMatchers("/favicon*/**")
//                .antMatchers("/img/**")
    }

    // Spring security 규칙
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests() // 권한요청 처리 설정 메서드
                .antMatchers( "/h2-console/**" ).permitAll() // 누구나 h2-console 접속 허용
                .and() // ...
                .csrf().ignoringAntMatchers( "/h2-console/**" ).disable(); // GET메소드는 문제가 없는데 POST메소드만 안되서 CSRF 비활성화 시킴

        http.csrf().disable();

        // CSRF 설정 Disable
        http

                // h2-console 을 위한 설정을 추가
                .headers()
                .frameOptions()
                .sameOrigin()

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()
                .antMatchers( "/auth/**").permitAll() // 가입 및 인증 주소는 누구나 접근가능
                .antMatchers("/api/**").hasRole("USER")
                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));

    }
}
