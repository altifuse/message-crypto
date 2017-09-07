package arturhgca.datablink.messagecrypto.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * This model contains all User-related information as well as validation parameters
 */
@Entity
public class User
{
    @Id
    @Size(min=4, max=20, message = "Username must have between 4 and 20 characters")
    private String username;

    @Size(min=8, message = "Password must contain at least 8 characters")
    private String password;

    private final Boolean enabled = true; // Spring Security needs this property

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Boolean getEnabled()
    {
        return enabled;
    }
}
