package org.modeshape.example.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Arrays;

/**
 * @author M.Sarhan
 */

@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        UserDetails admin = new User("admin", "123"
                , Arrays.asList(new SimpleGrantedAuthority("admin")
                , new SimpleGrantedAuthority("readonly")
                , new SimpleGrantedAuthority("readwrite")));

        UserDetails user1 = new User("user1", "123"
                , Arrays.asList(new SimpleGrantedAuthority("readonly")
                , new SimpleGrantedAuthority("readwrite")));

        UserDetails user2 = new User("user2", "123"
                , Arrays.asList(new SimpleGrantedAuthority("readonly")));

        UserDetailsManager userDetailsManager = new InMemoryUserDetailsManager(Arrays.asList(admin, user1, user2));
        auth.userDetailsService(userDetailsManager);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests().antMatchers("/static/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().defaultSuccessUrl("/jcr/")
                .and().logout().and()
                .httpBasic();
    }

}