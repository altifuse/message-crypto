package arturhgca.datablink.messagecrypto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * A collection of general utility methods
 */
public final class Util
{
    /**
     * Gets the username of the current user
     * @return Username
     */
    public static String getUsername()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Gets the password of the current user
     * @return Password
     */
    public static Object getCredentials()
    {
        return SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }

    /**
     * Gets the auth object of the current user
     * @return Authentication information
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Empty private constructor to define as utility class
     */
    private Util()
    {
    }
}
