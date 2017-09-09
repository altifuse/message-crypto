package arturhgca.datablink.messagecrypto.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test suite responsible for the control panel view
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest
{
    // tests follow the pattern what_when_then

    @Autowired
    private MockMvc mockMvc;

    /**
     * If the user is logged in and tries to load the root page, they should be sent to the control panel
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void root_userIsLoggedIn_redirectToControlPanel() throws Exception
    {
        mockMvc.perform(get("/"))
                .andExpect(status().isFound())
        .andExpect(redirectedUrl("/cpanel.html"));
    }

    /**
     * If the user is logged in and tries to load the control panel, they should be sent to the control panel
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void cpanel_userIsLoggedIn_goToControlPanel() throws Exception
    {
        mockMvc.perform(get("/cpanel"))
                .andExpect(status().isOk());
    }
}
