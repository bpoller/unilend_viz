package io.nowave.api.repository;

import io.nowave.api.AppConfiguration;
import io.nowave.api.model.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.nowave.api.repository.UserDataProvider.password;
import static io.nowave.api.repository.UserDataProvider.userToBeSaved;
import static org.junit.Assert.*;

/**
 * UserRepository tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {AppConfiguration.class})
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository repo;

    @After
    public void clean() {

        repo.deleteAll();
    }

    @Test
    public void testCreate() {
        User result = repo.save(userToBeSaved());
        result = repo.findOne(result.getId());
        assertNotNull("1 Customer in list", result);
    }

    @Test
    public void testUserPasswordCredentialValidation() {
        User result = repo.save(userToBeSaved());
        result = repo.findOne(result.getId());
        assertTrue("passwords must match", result.validate(password));
    }

    @Test
    public void testFindByEmail() {

        User user1 = userToBeSaved();
        repo.save(user1);

        User user2 = new User("Olivier", "Bearn", "obearn@ekito.fr", "titi");
        repo.save(user2);

        assertEquals(2, repo.count());
        assertEquals("Bert", repo.findByEmail("bpoller@ekito.fr").getFirstName());
    }
}