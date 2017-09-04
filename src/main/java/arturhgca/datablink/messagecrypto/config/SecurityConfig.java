package arturhgca.datablink.messagecrypto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
            .authorizeRequests()
                .anyRequest().authenticated() // all other pages require auth
                .and()
            .formLogin()
                .loginPage("/login") // this is the login page
                .permitAll() // anyone can access it
                .defaultSuccessUrl("/cpanel")
                .and()
            .logout()
                .logoutSuccessUrl("/cpanel");
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception
    {
        auth
            .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username=?")
                // workaround, since this application does not use roles:
                .authoritiesByUsernameQuery("SELECT username, 'default' FROM user WHERE username=?")
                .and()
            .inMemoryAuthentication()
                .withUser("username").password("password").roles("USER");
    }
}
