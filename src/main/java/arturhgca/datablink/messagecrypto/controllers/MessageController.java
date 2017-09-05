package arturhgca.datablink.messagecrypto.controllers;

import arturhgca.datablink.messagecrypto.Util;
import arturhgca.datablink.messagecrypto.models.Message;
import arturhgca.datablink.messagecrypto.persistence.MessageRepository;
import arturhgca.datablink.messagecrypto.security.CustomBCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller responsible for message-related views
 */
@Controller
public class MessageController
{
    /**
     *  This autowired property implements the Repository connected to the Message model
     */
    @Autowired
    private MessageRepository messageRepository;

    /**
     * Maps and handles custom behavior in the Edit page (GET mode)
     * @param model The information that is bound to the HTML form
     * @return Custom message sent to the client
     */
    @RequestMapping(value="/edit", method=RequestMethod.GET)
    public String edit(Model model)
    {
        String username = Util.getUsername();
        Message message = getMessage(username);

        // TO-DO: clear message.decryptedMessage
        message = decryptMessage(message);

        model.addAttribute("message", message);
        return "edit";
    }

    /**
     * Maps and handles custom behavior in the Edit page (POST mode)
     * @param message The information that is bound to the HTML form and used to update the database
     * @return Custom message sent to the client - in this case, a redirect
     */
    @RequestMapping(value="/edit", method=RequestMethod.POST)
    public String submit(@ModelAttribute Message message)
    {
        String username = Util.getUsername();
        message.setUsername(username);

        // TO-DO: filter
        message = encryptMessage(message);
        messageRepository.save(message);

        // TO-DO: redirect and show a success message
        return "redirect:/cpanel.html";
    }

    /**
     * Maps and handles custom behavior in the Decrypt page
     * @param model The information that is bound to the HTML form
     * @return Custom message sent to the client
     */
    @RequestMapping(value="/decrypt", method=RequestMethod.GET)
    public String decrypt(Model model)
    {
        String username = Util.getUsername();
        Message message = getMessage(username);

        // TO-DO: clear message.decryptedMessage
        // (same as edit GET)
        message = decryptMessage(message);

        model.addAttribute("message", message);
        return "decrypt";
    }

    /**
     * Maps and handles custom behavior in the View page
     * @param model The information that is bound to the HTML form
     * @return Custom message sent to the client
     */
    @RequestMapping(value="/view", method =RequestMethod.GET)
    public String view(Model model)
    {
        String username = Util.getUsername();
        Message message = getMessage(username);
        model.addAttribute("message", message);
        return "view";
    }

    /**
     * Queries the database through a Repository to get a user's message
     * If no message is found, instantiates a new one
     * @param username The username to be used as parameter in the query
     * @return The message found in the database or a new one if none is found
     */
    private Message getMessage(String username)
    {
        Message message = messageRepository.findOne(username);
        if(message == null)
        {
            message = new Message();
        }
        return message;
    }

    /**
     * Gets a raw message, generates a new encryption key and encrypts the message
     * @param message The object that contains the message to be encrypted
     * @return The Message object with the encrypted message and the decryptedMessage field cleared
     */
    private Message encryptMessage(Message message)
    {
        PasswordEncoder encoder = new BCryptPasswordEncoder(12);
        Object credentials = Util.getCredentials();
        String cryptoKey = encoder.encode(credentials.toString()); // bcrypt: $version$cost$salthash, with 22 chars for salt
        // Spring BCrypt considers version and cost as part of the salt, so:
        String cryptoKeySalt = cryptoKey.substring(0, 29);
        message.setCryptoKeySalt(cryptoKeySalt);
        String cryptoSalt = KeyGenerators.string().generateKey();
        message.setCryptoSalt(cryptoSalt);
        String encryptedMessage = Encryptors.text(cryptoKey, cryptoSalt).encrypt(message.getDecryptedMessage());
        message.setEncryptedMessage(encryptedMessage);
        message.setDecryptedMessage("");
        return message;
    }

    /**
     * Gets an encrypted message, reconstructs the encryption key and decrypts the message
     * @param message The object that contains the message to be decrypted and its encryption information
     * @return The Message object with the decrypted message
     */
    private Message decryptMessage(Message message)
    {
        if(message.getCryptoSalt() != null)
        {
            CustomBCryptPasswordEncoder encoder = new CustomBCryptPasswordEncoder();
            Object credentials = Util.getCredentials();
            String cryptoKeySalt = message.getCryptoKeySalt();
            String cryptoKey = encoder.encode(credentials.toString(), cryptoKeySalt);
            String cryptoSalt = message.getCryptoSalt();
            String encryptedMessage = message.getEncryptedMessage();
            String decryptedMessage = Encryptors.text(cryptoKey, cryptoSalt).decrypt(encryptedMessage);
            message.setDecryptedMessage(decryptedMessage);
        }
        return message;
    }
}
