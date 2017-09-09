package arturhgca.datablink.messagecrypto.controllers;

import arturhgca.datablink.messagecrypto.models.User;
import arturhgca.datablink.messagecrypto.persistence.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test suite responsible for authentication- and authorization-related methods
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccessControllerTest
{
    // tests follow the pattern what_when_then

    /**
     * Beans autowired in the tested class
     */
    @TestConfiguration
    static class MessageControllerTestContextConfiguration
    {
        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder()
        {
            return new BCryptPasswordEncoder(12);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * If the user is not logged in and tries to load the login page, they should be sent to the login page
     * @throws Exception
     */
    @Test
    @WithAnonymousUser
    public void logIn_userIsNotLoggedIn_goToLogInView() throws Exception
    {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    /**
     * If the user is logged in and tries to load the login page, they should be sent to the control panel
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void logIn_userIsLoggedIn_redirectToControlPanelView() throws Exception
    {
        mockMvc.perform(get("/login"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cpanel"));
    }

    /**
     * If the user is not logged in and tries to load the registration page, they should be sent to the registration page
     * @throws Exception
     */
    @Test
    @WithAnonymousUser
    public void registerGET_userIsNotLoggedIn_returnModelWithEmptyUser() throws Exception
    {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user",
                        hasProperty("username", isEmptyOrNullString())));
    }

    /**
     * If the user is logged in and tries to load the registration page, they should be sent to the control panel
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void registerGET_userIsLoggedIn_redirectToControlPanelView() throws Exception
    {
        mockMvc.perform(get("/register"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cpanel"));
    }

    /**
     * If the user is not logged in, is currently in the registration page and tries to register under the conditions:
     * - their input is valid;
     * - the username does not exist in the database,
     * they should be sent to the login page
     * @throws Exception
     */
    @Test
    @WithAnonymousUser
    public void registerPOST_userIsNotLoggedInAndInfoIsValidAndUserDoesNotExist_redirectToLogInView() throws Exception
    {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");

        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", user.getUsername())
                .param("password", user.getPassword()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }

    /**
     * If the user is not logged in, is currently in the registration page and tries to register under the conditions:
     * - their input is valid;
     * - the username exists in the database,
     * they should be sent back to the registration page with an error
     * @throws Exception
     */
    @Test
    @WithAnonymousUser
    public void registerPOST_userIsNotLoggedInAndInfoIsValidAndUserExists_redirectToRegisterViewWithError() throws Exception
    {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        Mockito.when(userRepository.findOne("user")).thenReturn(user);

        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", user.getUsername())
                .param("password", user.getPassword()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/register?exists"));
    }

    /**
     * If the user is not logged in, is currently in the registration page and tries to register under the condition:
     * - their input is invalid,
     * they should be sent back to the registration page with an error
     * @throws Exception
     */
    @Test
    @WithAnonymousUser
    public void registerPOST_userIsNotLoggedInAndInfoIsNotValid_redirectToRegisterViewWithError() throws Exception
    {
        User user = new User();
        user.setUsername("");
        user.setPassword("");

        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", user.getUsername())
                .param("password", user.getPassword()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }

    /**
     * If the user is not logged in, is currently in the registration page and tries to register under the condition:
     * - their username is null (this condition should not be possible under normal usage),
     * they should be sent back to the registration page with an error
     * @throws Exception
     */
    @Test
    @WithAnonymousUser
    public void registerPOST_userIsNotLoggedInAndUsernameIsNull_redirectToRegisterViewWithError() throws Exception
    {
        User user = new User();
        user.setPassword("");

        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", user.getUsername())
                .param("password", user.getPassword()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }

    /**
     * If the user is not logged in, is currently in the registration page and tries to register under the condition:
     * - their password is null (this condition should not be possible under normal usage),
     * they should be sent back to the registration page with an error
     * @throws Exception
     */
    @Test
    @WithAnonymousUser
    public void registerPOST_userIsNotLoggedInAndPasswordIsNull_redirectToRegisterViewWithError() throws Exception
    {
        User user = new User();
        user.setUsername("");

        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", user.getUsername())
                .param("password", user.getPassword()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }
}
