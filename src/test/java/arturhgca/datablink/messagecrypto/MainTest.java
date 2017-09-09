package arturhgca.datablink.messagecrypto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test suite responsible for the main application
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainTest
{
    /**
     * Verify that the main application starts correctly
     */
    @Test
    public void test()
    {
        Main.main(new String[]{});
    }
}
