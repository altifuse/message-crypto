package arturhgca.datablink.messagecrypto.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomBCryptPasswordEncoder extends BCryptPasswordEncoder
{
    public String encode(CharSequence rawPassword, String customSalt)
    {
        return BCrypt.hashpw(rawPassword.toString(), customSalt);
    }
}
