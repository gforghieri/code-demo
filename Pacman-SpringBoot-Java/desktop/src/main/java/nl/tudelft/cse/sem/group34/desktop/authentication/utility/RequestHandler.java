package nl.tudelft.cse.sem.group34.desktop.authentication.utility;

import com.google.gson.Gson;
import java.util.List;
import nl.tudelft.cse.sem.group34.desktop.authentication.entities.User;
import nl.tudelft.cse.sem.group34.desktop.authentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RequestHandler {

    @Autowired
    private transient UserService userService;

    private transient String password;

    /**
     * This function handles the request mapping for a user going to the /login url.
     * Requires two parameters, namely the username and the password.
     * Makes a query to the database and returns the username and password if it exists.
     * @param user String[] type
     * @return a response as a String
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String[] loginResponse(@RequestBody String[] user) {
        String username = user[0];
        String password = user[1];

        if (userService.authenticate(username, password)) {

            return user;

        }
        return new String[0];
    }

    /**
     * This function handles the request mapping for a user going to /signup url.
     * Requires two parameters, namely the username, email and hashed password.
     * It will make a query to insert a new user into the database.
     * @param newUser A String array containing the username, email and password.
     * @return the signed up user information.
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String[] signupResponse(@RequestBody String[] newUser) {
        String username = newUser[0];
        password = newUser[1];


        if (!userService.exists(username)) {

            userService.userSignUp(username, password);
            return newUser;
        }
        return new String[0];
    }

    /**
     * Handles the request to update the high score of the user.
     * @param user The username and score of the user.
     * @return The same user that was received.
     */
    @RequestMapping(value = "/score", method = RequestMethod.POST)
    public String[] scoreResponse(@RequestBody String[] user) {
        String username = user[0];
        int score = Integer.parseInt(user[1]);
        String nickname = user[2];

        userService.updateScore(username, score, nickname);

        return user;
    }

    /**
     * This reques mapping, maps the getrequest to /getusers endpoint.
     * @return return a String in JSON format of the list of users in the database.
     */
    @RequestMapping(value = {"/getusers"}, method = {RequestMethod.GET})
    public String getAllUsers() {
        List<User> userList = this.userService.findAll();
        String json = (new Gson()).toJson(userList);
        return json;
    }
}
