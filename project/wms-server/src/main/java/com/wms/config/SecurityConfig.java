package com.wms.config;

import com.wms.filter.jwt.JwtAuthenticationTokenFilter;
import com.wms.handler.AccessDeniedHandlerImpl;
import com.wms.handler.AuthenticationEntryPointImpl;
import com.wms.handler.SessionStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author Disney
 * @version 1.0
 * @since 2024年12月7日22:23:55
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new PasswordEncoderForSalt();
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Resource
    AuthenticationEntryPointImpl authenticationEntryPoint;

    @Resource
    AccessDeniedHandlerImpl accessDeniedHandler;
    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/member/login").permitAll()
                .antMatchers("/member/register").permitAll()
                .anyRequest().authenticated()
        ;
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //  配置异常处理器
        http.exceptionHandling()
                //  认真失败处理器
                .authenticationEntryPoint(authenticationEntryPoint)
                //  授权失败处理器
                .accessDeniedHandler(accessDeniedHandler);


        http.sessionManagement(session->{
            //  最大用户在线
           session.maximumSessions(1).expiredSessionStrategy(new SessionStrategy());
        });
        http.cors();
    }


}