package nl.tudelft.cse.sem.group34.desktop.authentication.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import nl.tudelft.cse.sem.group34.desktop.authentication.entities.User;
import nl.tudelft.cse.sem.group34.desktop.authentication.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserServiceTest {

    @Autowired
    private transient UserService userService;

    @Autowired
    private transient UserRepository userRepository;

    private static final transient String constantString = "junit";


    @Test
    public void exists() {
        User user = new User(constantString, constantString);
        userRepository.save(user);
        assertTrue(userService.exists(constantString));
    }


    @Test
    public void userExist() {
        User user = new User(constantString, constantString);
        userRepository.save(user);
        assertTrue(userService.authenticate(user.getUsername(), user.getPassword()));
    }

    @Test
    public void userExistsWrongPass() {
        User user = new User("none", "none");
        assertFalse(userService.authenticate(user.getUsername(), user.getPassword()));
    }


    @Test
    public void signUp() {
        userService.userSignUp(constantString, constantString);
        assertTrue(userRepository.existsById(constantString));
    }

}