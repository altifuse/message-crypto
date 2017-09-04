package arturhgca.datablink.messagecrypto.controllers;

import arturhgca.datablink.messagecrypto.models.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessageController
{
    @RequestMapping(value="/edit", method=RequestMethod.GET)
    public String edit(Model model)
    {
        // TO-DO: get Message object from storage -> decrypt and store in message.decryptedMessage -> send to model -> clear message.decryptedMessage
        // placeholder:
        model.addAttribute("message", new Message());
        return "edit";
    }

    @RequestMapping(value="/edit", method=RequestMethod.POST)
    public String submit(@ModelAttribute Message message)
    {
        // TO-DO: get Message object from view -> filter -> encrypt and store in message.encryptedMessage -> clear message.decryptedMessage -> save in storage
        // TO-DO: redirect and show a success message
        return "redirect:/cpanel.html";
    }

    @RequestMapping("/decrypt")
    public String decrypt()
    {
        return "decrypt";
    }

    @RequestMapping("/view")
    public String view()
    {
        return "view";
    }
}
