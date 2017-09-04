package arturhgca.datablink.messagecrypto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessageController
{
    @RequestMapping(value="/edit", method= RequestMethod.GET)
    public String edit()
    {
        return "edit";
    }

    @RequestMapping(value="/edit", method= RequestMethod.POST)
    public String submit()
    {
        // TO-DO: save new message
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
