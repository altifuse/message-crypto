package arturhgca.datablink.messagecrypto.models;

public class Message
{
    private String encryptedMessage;
    private String decryptedMessage;
    private String initializationVector;

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
