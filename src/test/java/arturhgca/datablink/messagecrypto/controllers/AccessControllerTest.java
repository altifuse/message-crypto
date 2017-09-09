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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccessControllerTest
{
    // tests follow the pattern what_when_then

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

    @Test
    @WithAnonymousUser
    public void logIn_userIsNotLoggedIn_goToLogInView() throws Exception
    {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void logIn_userIsLoggedIn_redirectToControlPanelView() throws Exception
    {
        mockMvc.perform(get("/login"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cpanel"));
    }

    @Test
    @WithAnonymousUser
    public void registerGET_userIsNotLoggedIn_returnModelWithEmptyUser() throws Exception
    {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user",
                        hasProperty("username", isEmptyOrNullString())));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void registerGET_userIsLoggedIn_redirectToControlPanelView() throws Exception
    {
        mockMvc.perform(get("/register"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cpanel"));
    }

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

    @Test
    @WithAnonymousUser
    public void registerPOST_userIsNotLoggedInAndInfoIsValidAndUserExists_redirectToRegisterView() throws Exception
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

    @Test
    @WithAnonymousUser
    public void registerPOST_userIsNotLoggedInAndInfoIsNotValid_redirectToRegisterView() throws Exception
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

    @Test
    @WithAnonymousUser
    public void registerPOST_userIsNotLoggedInAndUsernameIsNull_redirectToRegisterView() throws Exception
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

    @Test
    @WithAnonymousUser
    public void registerPOST_userIsNotLoggedInAndPasswordIsNull_redirectToRegisterView() throws Exception
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
