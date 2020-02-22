package nl.tudelft.cse.sem.group34.desktop.authentication.utility;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import nl.tudelft.cse.sem.group34.desktop.authentication.entities.User;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class ServerRequests {

    private static Pattern pattern;

    private static final transient int HTTP_SUCCESS = 200;

    private static transient User[] userArray;
    private static transient List<User> userList;

    private static final int LEAST_NUMBER_OF_RESPONSES = 2;

    private String username;

    private static String requestUrl = "http://localhost:3306/";

    public ServerRequests(String username) {
        this.username = username;
    }

    public void setUsername(String user) {
        username = user;
    }

    public String getUsername() {
        return username;
    }

    /**
     * This function takes in a username and password, both of type String.
     * If the project.server responds with the correct result it will print this to the console,
     * and processes the login.
     * if not valid it will print "Wrong username or password".
     *
     * @param username type.
     * @param password type.
     */
    public static String login(String username, String password) throws IOException {

        if (username == null || password == null) {
            return null;
        }

        String response = sendRequestToServer("login",
                new Gson().toJson(new String[]{username, password}));

        if (response != null) {
            String[] resArr = response.split("::");
            System.out.println("[INFO] User:" + username + " has logged in.");
            return "success: " + resArr[0];
        } else {
            System.out.println("[ERROR] Wrong username or password");
            return "fail";
        }
    }

    /**
     * This function takes in a username and password, both of type String.
     * If the username and/or password don't have the proper syntax it will return.
     * If the project.server responds with the correct result it will print this to the console,
     * if not valid it will print an error notifying the sign up was not successful.
     *
     * @param username parameter.
     * @param password parameter.
     * @return the input in JSON format.
     */
    public static String signUp(String username, String password) throws IOException {

        pattern = Pattern.compile("[A-Za-z0-9_]+");
        if (username == null || password == null) {
            return null;
        }

        if (!pattern.matcher(username).matches() || !pattern.matcher(password).matches()) {
            return "syntax";
        }

        String response = sendRequestToServer("signup",
                new Gson().toJson(new String[]{username, password}));

        if (response != null) {
            System.out.println("[INFO] The sign up was successful: " + response);
            return "ok";
        } else {
            System.out.println("[ERROR] The sign up was not successful");
            return "fail";
        }
    }

    /**
     * This function will prepare the HTTP project.client to send a request to the project.server.
     * With type it will know what URL to go to. The response will be a String,
     * which can be formatted afterwards via JSON.
     *
     * @param type is the specified url.
     * @param json is the user in json format.
     * @return a string which will be formatted as JSON.
     */
    public static String sendRequestToServer(String type, String json) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        try {

            HttpPost httpPost = new HttpPost(requestUrl + type);

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            try {
                String msg = new BasicResponseHandler().handleResponse(response);
                System.out.println(msg);
                System.out.println(msg);
                System.out.println(msg);
                System.out.println(msg);
                System.out.println(msg);
                if (msg.length() <= LEAST_NUMBER_OF_RESPONSES) {
                    throw new IOException();
                }
                System.out.println("[INFO] The project.server responded with " + msg);
                return msg;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            client.close();
        }
    }

    /**
     * Sends a request to the server with the username and the score of the player.
     * @param username Username of the player.
     * @param score The score that the player achieved.
     * @return Returns a message that either succeeds or fails.
     */
    public static String updateLeaderBoards(String username, int score, String nickname) throws IOException {
        if (username == null) {
            return null;
        }

        String response = sendRequestToServer("score", new Gson().toJson(
                new String[]{username, Integer.toString(score), nickname}));

        System.out.println(response);

        if (response != null) {
            System.out.println("[SUCCESS]");
            return "success";
        } else {
            System.out.println("[ERROR]");
            return "fail";
        }
    }

    /**
     * This method gets all the users from the database.
     * @return Returns a List of users.
     * @throws IOException if there was an issue with the request.
     */
    @SuppressWarnings("PMD.CloseResource")
    public static List<User> getAllUsers() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(requestUrl + "getusers");
        httpGet.addHeader("accept", "application/json");
        System.out.println("Request Type: " + httpGet.getMethod());
        CloseableHttpResponse response = client.execute(httpGet);
        try {
            String json = EntityUtils.toString(response.getEntity());

            userArray = new Gson().fromJson(json, User[].class);

            userList = Arrays.asList(userArray);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            response.close();
            client.close();
        }
        return userList;
    }
}
