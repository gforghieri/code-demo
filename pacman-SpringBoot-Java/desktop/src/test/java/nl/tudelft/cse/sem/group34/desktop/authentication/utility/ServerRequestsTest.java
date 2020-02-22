package nl.tudelft.cse.sem.group34.desktop.authentication.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import nl.tudelft.cse.sem.group34.desktop.authentication.entities.User;
import nl.tudelft.cse.sem.group34.desktop.authentication.repository.UserRepository;
import nl.tudelft.cse.sem.group34.desktop.authentication.services.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ServerRequestsTest {

    @Autowired
    private UserService userService;

    @Autowired
    private transient UserRepository userRepository;

    private static final transient String constantTestString = "test";
    private static final transient String constantJunitString = "junit";
    private static final transient String constantFailString = "fail";

    @Test
    public void getUsername() {
        ServerRequests testServerRequest = new ServerRequests(constantJunitString);
        assertEquals(constantJunitString, testServerRequest.getUsername());
    }


    @Test
    public void setUsername() {
        ServerRequests testServerRequest = new ServerRequests("");
        testServerRequest.setUsername(constantJunitString);
        assertEquals(constantJunitString, testServerRequest.getUsername());
    }

    @Test
    public void loginUsernameNull() throws IOException {

        assertNull(ServerRequests.login(null, "123"));
    }


    @Test
    public void loginPasswordNull() throws IOException {

        assertNull(ServerRequests.login(constantTestString, null));
    }


    @Test
    public void loginUsernameWrong() throws IOException {
        String response = ServerRequests.login("usernamewrong", constantJunitString);
        assertEquals(constantFailString, response);
    }



    @Test
    public void loginPasswordWrong() throws IOException {
        String response = ServerRequests.login(constantJunitString, "passwordwrong");
        assertEquals(constantFailString, response);
    }


    @Test
    public void loginCorrect() throws IOException {
        String response = ServerRequests.login("junit", "junit");
        assertTrue(response.startsWith("success:"));
    }



    @Test
    public void signUpUsernameNull() throws IOException {
        assertNull(ServerRequests.signUp(null, "123"));
    }


    @Test
    public void signUpEmailNull() throws IOException {
        String response = (ServerRequests.signUp(constantTestString, "123"));
        assertEquals(constantFailString, response);
    }


    @Test
    public void signUpPasswordNull() throws IOException {
        assertNull(ServerRequests.signUp(constantTestString, null));
    }


    @Test
    public void signUpUserSyntax() throws IOException {
        String response = ServerRequests.signUp("test!!!!!!", constantTestString);
        assertEquals("syntax", response);
    }


    @Test
    public void signUpEmailSyntax() throws IOException {
        String response = ServerRequests.signUp(constantTestString, constantTestString);
        assertEquals(constantFailString, response);
    }


    @Test
    public void signUpPasswordSyntax() throws IOException {
        String response = ServerRequests.signUp(constantTestString, "test!!!!");
        assertEquals("syntax", response);
    }

    @Test
    public void signUpComplete() throws IOException {
        User newUser = new User("test100", "test100");
        String response = ServerRequests.signUp(newUser.getUsername(), newUser.getPassword());
        assertEquals("ok", response);
        userRepository.delete(newUser);
    }

    @Test
    public void signUpfailure() throws IOException {
        String response = ServerRequests.signUp(constantTestString, constantTestString);
        assertEquals(constantFailString, response);
    }

    @Test
    public void sendRequestToServerNoJson() throws IOException {
        assertNull(ServerRequests.sendRequestToServer("/", " no json "));
    }

    @Test
    public void sendRequestToServerEmptyJsonString() throws IOException {
        assertNull(ServerRequests.sendRequestToServer("/", ""));
    }

    @Test
    public void updateScoreSuccess() throws IOException {
        String response = ServerRequests.updateLeaderBoards(constantJunitString, 10, "bob");
        Assertions.assertEquals(response, "success");
    }

    @Test
    public void updateScoreFailure() throws IOException {
        String response = ServerRequests.updateLeaderBoards("doesnotexist", 5, "bob");
        Assertions.assertEquals(response, "fail");
    }

    @Test
    public void getAllUsersSuccess() throws IOException {
        List<User> userList = ServerRequests.getAllUsers();
        Assertions.assertEquals(!userList.isEmpty(), true);
    }
}
