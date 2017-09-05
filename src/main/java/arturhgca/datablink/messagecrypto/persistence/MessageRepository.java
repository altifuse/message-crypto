package arturhgca.datablink.messagecrypto.persistence;

import arturhgca.datablink.messagecrypto.models.Message;
import org.springframework.data.repository.CrudRepository;

/**
 * This Repository connects the Message model to the database
 */
public interface MessageRepository extends CrudRepository<Message, String>
{
}
