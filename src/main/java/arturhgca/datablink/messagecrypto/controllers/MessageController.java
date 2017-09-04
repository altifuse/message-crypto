package arturhgca.datablink.messagecrypto.controllers;

import arturhgca.datablink.messagecrypto.Util;
import arturhgca.datablink.messagecrypto.models.Message;
import arturhgca.datablink.messagecrypto.persistence.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessageController
{
    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(value="/edit", method=RequestMethod.GET)
    public String edit(Model model)
    {
        String username = Util.getUsername();
        Message message = getMessage(username);

        // TO-DO: decrypt and store in message.decryptedMessage -> send to model -> clear message.decryptedMessage
        // placeholder:
        message.setDecryptedMessage(message.getEncryptedMessage());

        model.addAttribute("message", message);
        return "edit";
    }

    @RequestMapping(value="/edit", method=RequestMethod.POST)
    public String submit(@ModelAttribute Message message)
    {
        String username = Util.getUsername();
        message.setUsername(username);

        // TO-DO: filter -> encrypt and store in message.encryptedMessage -> clear message.decryptedMessage
        // placeholder:
        message.setEncryptedMessage(message.getDecryptedMessage());

        messageRepository.save(message);

        // TO-DO: redirect and show a success message
        return "redirect:/cpanel.html";
    }

    @RequestMapping(value="/decrypt", method=RequestMethod.GET)
    public String decrypt(Model model)
    {
        String username = Util.getUsername();
        Message message = getMessage(username);

        // TO-DO: decrypt and store in message.decryptedMessage -> send to model -> clear message.decryptedMessage
        // (same as edit GET)
        // placeholder:
        message.setDecryptedMessage(message.getEncryptedMessage());

        model.addAttribute("message", message);
        return "decrypt";
    }

    @RequestMapping(value="/view", method =RequestMethod.GET)
    public String view(Model model)
    {
        String username = Util.getUsername();
        Message message = getMessage(username);
        model.addAttribute("message", message);
        return "view";
    }

    public Message getMessage(String username)
    {
        Message message = messageRepository.findOne(username);
        if(message == null)
        {
            message = new Message();
        }
        return message;
    }
}
