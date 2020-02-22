package nl.tudelft.cse.sem.group34.desktop.authentication.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class UserTest {
    private final transient  User test = new User("verjak", "123");
    private final transient  User test2 = new User();

    @Test
    public void getUsername() {
        assertEquals("verjak", test.getUsername());
    }

    @Test
    public void setUsername() {
        test2.setUsername("agrayel");
        assertEquals("agrayel", test2.getUsername());
    }

    @Test
    public void getPassword() {
        assertEquals("123", test.getPassword());
    }

    @Test
    public void setPassword() {
        test2.setPassword("1234");
        assertEquals("1234", test2.getPassword());
    }
}