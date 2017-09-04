package arturhgca.datablink.messagecrypto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController
{
    // Root
    @RequestMapping("/")
    public String root()
    {
        return "redirect:/cpanel.html";
    }

    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }

    @RequestMapping("/cpanel")
    public String cpanel()
    {
        return "cpanel";
    }

    @RequestMapping("/edit")
    public String edit()
    {
        return "edit";
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
