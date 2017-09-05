package arturhgca.datablink.messagecrypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Datablink Message Crypto is a web application developed to demonstrate my workflow,
 * my programming abilities and, most importantly, my learning capacities.
 *
 * It offers to registered users an interface to save, read and decrypt a secret
 * message that only they can access.
 *
 * @author arturhgca
 */
@SpringBootApplication
public class Main
{
    /**
     * Application starting point
     * @param args Unused
     */
    public static void main(String[] args)
    {
        SpringApplication.run(Main.class, args);
    }
}
