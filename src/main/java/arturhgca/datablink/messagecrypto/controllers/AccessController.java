package arturhgca.datablink.messagecrypto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccessController
{

    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }
}
