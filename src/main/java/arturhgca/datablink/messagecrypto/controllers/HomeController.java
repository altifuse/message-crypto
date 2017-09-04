package arturhgca.datablink.messagecrypto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
    // Root
    @RequestMapping("/")
    public String root()
    {
        return "redirect:/cpanel.html";
    }

    @RequestMapping("/cpanel")
    public String cpanel()
    {
        return "cpanel";
    }
}
