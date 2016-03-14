package io.nowave.api.model;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {


    @Test
    public void shouldValidateValidPassword() {
        assertTrue("The password should be valid", new User("Bert", "Poller", "bpoller@ekito.fr", "toto").validate("toto"));
    }

    @Test
    public void shouldNotValidateInvalidPassword() {
        assertFalse("The password should not be valid", new User("Bert", "Poller", "bpoller@ekito.fr", "toto").validate("titi"));
    }

}
