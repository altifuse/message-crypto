package arturhgca.datablink.messagecrypto.controllers;

import arturhgca.datablink.messagecrypto.models.Message;
import arturhgca.datablink.messagecrypto.persistence.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageControllerTest
{
    // tests follow the pattern what_when_then

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
    private MessageController messageController;

    @MockBean
    private MessageRepository messageRepository;
    
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
}
