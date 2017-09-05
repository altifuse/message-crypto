package arturhgca.datablink.messagecrypto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible for access views
 */
@Controller
public class AccessController
{
    /**
     * Maps and handles custom behavior in the Login page
     * @return Custom message sent to the client
     */
    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }
}
