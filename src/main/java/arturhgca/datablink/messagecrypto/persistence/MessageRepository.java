package arturhgca.datablink.messagecrypto.persistence;

import arturhgca.datablink.messagecrypto.models.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, String>
{
}
