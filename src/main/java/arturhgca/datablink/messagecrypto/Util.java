package arturhgca.datablink.messagecrypto;

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
     * Empty private constructor to define as utility class
     */
    private Util()
    {
    }
}
