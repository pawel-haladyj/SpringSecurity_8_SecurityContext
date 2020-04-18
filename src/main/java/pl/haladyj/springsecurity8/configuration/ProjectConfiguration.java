package pl.haladyj.springsecurity8.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.haladyj.springsecurity8.security.filter.TokenAuthFilter;
import pl.haladyj.springsecurity8.security.filter.UsernamePasswordAuthFilter;
import pl.haladyj.springsecurity8.security.provider.OtpAuthProvider;
import pl.haladyj.springsecurity8.security.provider.TokenAuthProvider;
import pl.haladyj.springsecurity8.security.provider.UsernamePasswordAuthProvider;

@Configuration
public class ProjectConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private OtpAuthProvider otpAuthProvider;

    @Autowired
    private UsernamePasswordAuthProvider usernamePasswordAuthProvider;

    @Autowired
    private TokenAuthProvider tokenAuthProvider;

    @Bean
    public UsernamePasswordAuthFilter usernamePasswordAuthFilter(){
        return new UsernamePasswordAuthFilter();
    };



    @Bean
    public TokenAuthFilter tokenAuthFilter(){
        return new TokenAuthFilter();
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(usernamePasswordAuthProvider)
                .authenticationProvider(otpAuthProvider)
                .authenticationProvider(tokenAuthProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAt(usernamePasswordAuthFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(tokenAuthFilter(),BasicAuthenticationFilter.class);
    }
}
