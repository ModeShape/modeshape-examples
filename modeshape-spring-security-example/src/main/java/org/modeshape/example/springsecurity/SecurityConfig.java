/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.modeshape.example.springsecurity;

import java.util.Arrays;
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