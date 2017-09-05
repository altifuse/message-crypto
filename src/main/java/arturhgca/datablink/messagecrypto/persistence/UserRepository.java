package arturhgca.datablink.messagecrypto.persistence;

import arturhgca.datablink.messagecrypto.models.User;
import org.springframework.data.repository.CrudRepository;

/**
 * This Repository connects the User model to the database
 */
public interface UserRepository extends CrudRepository<User, String>
{
}
