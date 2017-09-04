package arturhgca.datablink.messagecrypto.persistence;

import arturhgca.datablink.messagecrypto.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>
{
}
