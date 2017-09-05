package arturhgca.datablink.messagecrypto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible for control panel views
 */
@Controller
public class HomeController
{
    /**
     * Maps and handles custom behavior in the main page
     * @return Custom message sent to the client - in this case, a redirect
     */
    // Root
    @RequestMapping("/")
    public String root()
    {
        return "redirect:/cpanel.html";
    }

    /**
     * Maps and handles custom behavior in the Control Panel page
     * @return Custom message sent to the client
     */
    @RequestMapping("/cpanel")
    public String cpanel()
    {
        return "cpanel";
    }
}
