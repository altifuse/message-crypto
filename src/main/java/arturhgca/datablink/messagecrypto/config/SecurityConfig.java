package arturhgca.datablink.messagecrypto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
            .authorizeRequests()
                .anyRequest().authenticated() // all pages require auth
                .and()
            .formLogin()
                .loginPage("/login") // this is the login page
                .permitAll() // anyone can access it
                .defaultSuccessUrl("/cpanel")
                .and()
            .logout()
                .permitAll(); // anyone can logout
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER"); // for testing only
    }
}
