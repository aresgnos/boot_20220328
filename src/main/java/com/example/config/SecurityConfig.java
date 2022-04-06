package com.example.config;

import com.example.handler.MyLoginSuccessHandler;
import com.example.handler.MyLogoutSuccessHandler;
import com.example.service.MemberDetailServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
// Web~ 부모, security~로 받아옴
public class SecurityConfig extends WebSecurityConfigurerAdapter {

        // 1. 직접 만든 detailService 객체 가져오기
        @Autowired
        MemberDetailServiceImpl mService;

        // 2. 암호화 방법 객체 생성, @Bean은 서버 구동시 자동으로 객체 생성됨
        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // 3. 직접 만든 detailService에 암호화 방법 적용
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(mService)
                                .passwordEncoder(bCryptPasswordEncoder());
        }

        // 기존 기능을 제거한 후 필요한 것을 추가
        @Override
        protected void configure(HttpSecurity http) throws Exception {
                // super.configure(http);
                // super = 부모에 있는 기능을 쓸 수 있다. super가 있으면 기존의 기능을 쓰고 거기에 추가 가능
                // super를 쓰지 않으면 기존의 기능을 제거, 새로운 기능 추가 가능

                // 페이지별 접근 권한 설정
                http.authorizeRequests()
                                .antMatchers("/security_admin", "/security_admin/**")
                                .hasAuthority("ADMIN")
                                .antMatchers("/security_seller", "/security_seller/**")
                                .hasAnyAuthority("ADMIN", "SELLER")
                                .antMatchers("/security_customer", "/security_customer/**")
                                .hasAuthority("CUSTOMER")
                                .anyRequest().permitAll(); // 나머지 요청은 다 허용

                // 로그인 페이지 설정, 단 POST는 직접 만들지 않음
                // login.html과 값을 맞춰야한다.
                http.formLogin()
                                .loginPage("/member/security_login") // 주소
                                .loginProcessingUrl("/member/security_loginaction") // action
                                .usernameParameter("uemail") // name
                                .passwordParameter("upw") // name
                                .successHandler(new MyLoginSuccessHandler()) // 로그인을 성공하면 이 클래스를 구동하라
                                // .defaultSuccessUrl("/security_home")
                                .permitAll();

                // 로그아웃 페이지 설정, url에 맞게 POST로 호출하면 됨.
                http.logout()
                                .logoutUrl("/member/security_logout")
                                // .logoutSuccessUrl("/security_home")
                                .logoutSuccessHandler(new MyLogoutSuccessHandler())
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .permitAll();

                // 접근 권한 불가 403
                http.exceptionHandling().accessDeniedPage("/security_403");

                // h2 console(db) 사용을 위해 임시로(보안이 높아져 사용이 불가하므로)
                // 나중엔 빼야함(관리자용이며 보안을 위해)
                // http.csrf().disable(); => 보안에 취약, 다 풀어짐

                // h2 console만 풀어짐
                http.csrf().ignoringAntMatchers("/h2-console/**");
                http.headers().frameOptions().sameOrigin();
        }
}
