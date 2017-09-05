package arturhgca.datablink.messagecrypto.models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * This model contains all User-related information
 */
@Entity
public class User
{
    @Id
    private String username;
    private String password;
    private Boolean enabled;

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

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }
}
