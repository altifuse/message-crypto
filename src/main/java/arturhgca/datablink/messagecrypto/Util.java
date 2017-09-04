package arturhgca.datablink.messagecrypto;

import org.springframework.security.core.context.SecurityContextHolder;

public final class Util
{
    public static String getUsername()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Util()
    {
    }
}
