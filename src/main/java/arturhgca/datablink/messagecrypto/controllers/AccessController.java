package arturhgca.datablink.messagecrypto.controllers;

import arturhgca.datablink.messagecrypto.models.User;
import arturhgca.datablink.messagecrypto.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Controller responsible for access views
 */
@Controller
public class AccessController
{
    /**
     * This autowired property implements the Repository connected to the User model
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * This autowired property enables access to the password encoder used for authentication
     */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Maps and handles custom behavior in the Login page
     * @return Custom message sent to the client
     */
    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }

    /**
     * Maps and handles custom behavior in the Register page (GET mode)
     * @param model The information that is bound to the HTML form - in the case of the GET request
     *              this object is empty
     * @return Custom message sent to the client
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model)
    {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Maps and handles custom behavior in the Register page (POST mode)
     * @param user The information that is bound to the HTML form and used to update the database
     * @param bindingResult Tells the controller if the validator found any errors in the form data
     * @return Custom message sent to the client - in this case, a redirect
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute User user, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return "register";
        }

        // encode password
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // save to db
        userRepository.save(user);

        return "redirect:/login";
    }
}
