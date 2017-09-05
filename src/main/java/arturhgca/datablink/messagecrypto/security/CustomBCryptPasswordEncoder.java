package arturhgca.datablink.messagecrypto.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * In order to reconstruct the encryption key used for the secret messages, a known salt must be fed to the encoder
 * The default Spring BCrypt implementation does not support this
 * This class addresses this need by extending the BCryptPasswordEncoder's functionality
 */
public class CustomBCryptPasswordEncoder extends BCryptPasswordEncoder
{
    /**
     * A new encode method that takes one extra parameter when compared to the original implementation
     * @param rawPassword The raw password to be encoded
     * @param customSalt The salt to be used for encoding
     * @return The encoded password
     */
    public String encode(CharSequence rawPassword, String customSalt)
    {
        return BCrypt.hashpw(rawPassword.toString(), customSalt);
    }
}
