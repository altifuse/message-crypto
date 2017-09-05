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

/**
 * This class manages all Spring Security settings related to authentication
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    /**
     * This autowired property implements the DataSource set in the application.properties file
     */
    @Autowired
    private DataSource dataSource;

    /**
     * This bean implements the BCrypt password encoder used for authentication
     * @return The PasswordEncoder instance used in all auth business
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * This method sets which pages require auth and authorization level, if applicable
     * @param http The HTTP connector (switched for a HTTPS one) that handles requests
     * @throws Exception
     */
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

    /**
     *
     * @param auth Object responsible for checking credentials against the database
     * @throws Exception
     */
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception
    {
        auth
            .eraseCredentials(false) // so we can use the password to create an encryption key for messages
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
