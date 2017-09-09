package arturhgca.datablink.messagecrypto.controllers;

import arturhgca.datablink.messagecrypto.models.Message;
import arturhgca.datablink.messagecrypto.persistence.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test suite responsible for message-related methods
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest
{
    // tests follow the pattern what_when_then

    /**
     * Beans autowired in the tested class
     */
    @TestConfiguration
    static class MessageControllerTestContextConfiguration
    {
        @Bean
        public MessageController messageController()
        {
            return new MessageController();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageController messageController;

    @MockBean
    private MessageRepository messageRepository;

    /**
     * Mocked objects and method calls
     */
    @Before
    public void setup()
    {
        // message mocks
        Message userHasMessageMessage = new Message();
        userHasMessageMessage.setUsername("userHasMessage");
        userHasMessageMessage.setDecryptedMessage("message");
        userHasMessageMessage.setCryptoKeySalt("$2a$12$lxUln4WZl3H5f/r3QV4QBu");
        userHasMessageMessage.setCryptoSalt("ee7a62bb62094831");
        userHasMessageMessage.setEncryptedMessage("99d0ef2a701b21838e2ebceaa9c305c63f189ea92f327e8763f5a24f460810bb");

        Message userHasNoEncryptedMessageMessage = new Message();
        userHasNoEncryptedMessageMessage.setUsername("userHasNoEncryptedMessage");
        userHasNoEncryptedMessageMessage.setDecryptedMessage("message");

        Mockito.when(messageRepository.findOne("userHasMessage")).thenReturn(userHasMessageMessage);
        Mockito.when(messageRepository.findOne("userHasNoMessage")).thenReturn(null);
        Mockito.when(messageRepository.findOne("userHasNoEncryptedMessage")).thenReturn(userHasNoEncryptedMessageMessage);
    }

    // BUSINESS LOGIC

    /**
     * If the user has a message stored and a method tries to get it, they should receive it
     */
    @Test
    public void getMessage_userHasMessage_returnMessage()
    {
        String username = "userHasMessage";

        Message response = messageController.getMessage(username);

        assertThat(response.getUsername(), equalTo("userHasMessage"));
        assertThat(response.getDecryptedMessage(), equalTo("message"));
        assertThat(response.getCryptoKeySalt(), equalTo("$2a$12$lxUln4WZl3H5f/r3QV4QBu"));
        assertThat(response.getCryptoSalt(), equalTo("ee7a62bb62094831"));
        assertThat(response.getEncryptedMessage(), equalTo("99d0ef2a701b21838e2ebceaa9c305c63f189ea92f327e8763f5a24f460810bb"));
    }

    /**
     * If the user has no message stored and a method tries to get it, they should receive an empty Message object
     */
    @Test
    public void getMessage_userHasNoMessage_returnEmptyMessage()
    {
        String username = "userHasNoMessage";

        Message response = messageController.getMessage(username);

        assertThat(response.getUsername(), nullValue());
        assertThat(response.getDecryptedMessage(), nullValue());
        assertThat(response.getCryptoKeySalt(), nullValue());
        assertThat(response.getCryptoSalt(), nullValue());
        assertThat(response.getEncryptedMessage(), nullValue());
    }

    /**
     * If the user inputs a valid message and a method tries to encrypt it, they should receive the Message object
     * with the encrypted content
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void encryptMessage_messageIsValid_returnMessageWithEncryptedMessage()
    {
        Message message = new Message();
        message.setUsername("user");
        message.setDecryptedMessage("message");

        Message response = messageController.encryptMessage(message);

        assertThat(response.getUsername(), equalTo("user"));
        assertThat(response.getDecryptedMessage(), equalTo("")); // this field is cleared after encryption
        assertThat(response.getCryptoKeySalt(), notNullValue());
        assertThat(response.getCryptoSalt(), notNullValue());
        assertThat(response.getEncryptedMessage(), notNullValue());
    }

    /**
     * If the user has an encrypted message in the database and a method tries to decrypt it, they should receive
     * the Message object with the decrypted content
     */
    @Test
    @WithMockUser(username = "userHasMessage", password = "password", roles = "USER")
    public void decryptMessage_cryptoSaltExists_returnMessageWithDecryptedMessage()
    {
        Message message = new Message();
        message.setUsername("userHasMessage");
        message.setDecryptedMessage("");
        message.setCryptoKeySalt("$2a$12$lxUln4WZl3H5f/r3QV4QBu");
        message.setCryptoSalt("ee7a62bb62094831");
        message.setEncryptedMessage("99d0ef2a701b21838e2ebceaa9c305c63f189ea92f327e8763f5a24f460810bb");

        Message response = messageController.decryptMessage(message);

        assertThat(response.getUsername(), equalTo("userHasMessage"));
        assertThat(response.getDecryptedMessage(), equalTo("message"));
        assertThat(response.getCryptoKeySalt(), equalTo("$2a$12$lxUln4WZl3H5f/r3QV4QBu"));
        assertThat(response.getCryptoSalt(), equalTo("ee7a62bb62094831"));
        assertThat(response.getEncryptedMessage(), equalTo("99d0ef2a701b21838e2ebceaa9c305c63f189ea92f327e8763f5a24f460810bb"));
    }

    /**
     * If the user doesn't have an encrypted message in the database and a method tries to decrypt it, they should
     * receive the original Message object
     */
    @Test
    @WithMockUser(username = "userHasNoEncryptedMessage", password = "password", roles = "USER")
    public void decryptMessage_cryptoSaltDoesNotExist_returnOriginalMessage()
    {
        Message message = new Message();
        message.setUsername("userHasNoEncryptedMessage");

        Message response = messageController.decryptMessage(message);

        assertThat(response.getUsername(), equalTo("userHasNoEncryptedMessage"));
        assertThat(response.getDecryptedMessage(), nullValue());
        assertThat(response.getCryptoKeySalt(), nullValue());
        assertThat(response.getCryptoSalt(), nullValue());
        assertThat(response.getEncryptedMessage(), nullValue());
    }

    // VIEWS

    /**
     * If the user is logged in and tries to load the edit page, they should be sent to the edit page with their
     * Message object, be it empty or not
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "userHasMessage", password = "password", roles = "USER")
    public void editGET_userIsLoggedIn_returnModelWithMessage() throws Exception
    {
        mockMvc.perform(get("/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message",
                        hasProperty("decryptedMessage", is("message"))));
    }

    /**
     * If the user is logged in, is currently in the edit page and tries to save a message under the conditions:
     * - the message is valid,
     * they should be sent to the control panel
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "userHasMessage", password = "password", roles = "USER")
    public void editPOST_userIsLoggedInAndMessageIsValid_redirectToControlPanelView() throws Exception
    {
        Message message = messageRepository.findOne("userHasMessage"); // using predefined mock
        mockMvc.perform(post("/edit")
                    .with(csrf())
                    .param("decryptedMessage", message.getDecryptedMessage()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cpanel.html"));
    }

    /**
     * If the user is logged in, is currently in the edit page and tries to save a message under the conditions:
     * - the message is too long,
     * they should be sent back to the edit page with an error
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "userHasMessage", password = "password", roles = "USER")
    public void editPOST_userIsLoggedInAndMessageIsTooLong_redirectToEditViewWithError() throws Exception
    {
        Message message = messageRepository.findOne("userHasMessage"); // using predefined mock

        Mockito.when(messageRepository.save(org.mockito.Matchers.any(Message.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/edit")
                .with(csrf())
                .param("decryptedMessage", message.getDecryptedMessage()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/edit?error"));
    }

    /**
     * If the user is logged in and tries to load the decrypt page, they should be sent to the decrypt page with their
     * Message object in the model, containing the decrypted message
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "userHasMessage", password = "password", roles = "USER")
    public void decrypt_userIsLoggedIn_returnModelWithMessage() throws Exception
    {
        mockMvc.perform(get("/decrypt"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message",
                        hasProperty("decryptedMessage", is("message"))));
    }

    /**
     * If the user is logged in and tries to load the view page, they should be sent to the decrypt page with their
     * Message object in the model, containing the encrypted message
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "userHasMessage", password = "password", roles = "USER")
    public void view_userIsLoggedIn_returnModelWithMessage() throws Exception
    {
        mockMvc.perform(get("/view"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message",
                        hasProperty("encryptedMessage",
                                is("99d0ef2a701b21838e2ebceaa9c305c63f189ea92f327e8763f5a24f460810bb"))));
    }
}
