package arturhgca.datablink.messagecrypto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController
{
    // Root: redirect to login page
    @RequestMapping("/")
    public String root()
    {
        return "redirect:/login.html";
    }

    // Landing page with login form
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
}
