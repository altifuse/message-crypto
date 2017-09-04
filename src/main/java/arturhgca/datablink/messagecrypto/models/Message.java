package arturhgca.datablink.messagecrypto.models;

public class Message
{
    private String encryptedMessage;
    private String initializationVector;

    public String getEncryptedMessage()
    {
        return encryptedMessage;
    }

    public void setEncryptedMessage(String encryptedMessage)
    {
        this.encryptedMessage = encryptedMessage;
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
