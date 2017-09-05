package arturhgca.datablink.messagecrypto.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Message
{
    @Id
    private String username;
    @Column(columnDefinition = "TEXT")
    private String encryptedMessage;
    @Column(columnDefinition = "TEXT")
    private String cryptoKeySalt;
    @Column(columnDefinition = "TEXT")
    private String cryptoSalt;

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

    public String getCryptoKeySalt()
    {
        return cryptoKeySalt;
    }

    public void setCryptoKeySalt(String cryptoKeySalt)
    {
        this.cryptoKeySalt = cryptoKeySalt;
    }

    public String getCryptoSalt()
    {
        return cryptoSalt;
    }

    public void setCryptoSalt(String cryptoSalt)
    {
        this.cryptoSalt = cryptoSalt;
    }
}
