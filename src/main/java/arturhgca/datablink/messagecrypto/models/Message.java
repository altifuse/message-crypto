package arturhgca.datablink.messagecrypto.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Message
{
    @Id
    private String username;
    private String encryptedMessage;
    private String initializationVector;

    @Transient
    private String decryptedMessage;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEncryptedMessage()
    {
        return encryptedMessage;
    }

    public void setEncryptedMessage(String encryptedMessage)
    {
        this.encryptedMessage = encryptedMessage;
    }

    public String getDecryptedMessage()
    {
        return decryptedMessage;
    }

    public void setDecryptedMessage(String decryptedMessage)
    {
        this.decryptedMessage = decryptedMessage;
    }

    public String getInitializationVector()
    {
        return initializationVector;
    }

    public void setInitializationVector(String initializationVector)
    {
        this.initializationVector = initializationVector;
    }
}
