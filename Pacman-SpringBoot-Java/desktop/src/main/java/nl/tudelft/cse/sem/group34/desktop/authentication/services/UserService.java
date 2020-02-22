package nl.tudelft.cse.sem.group34.desktop.authentication.services;

import java.util.List;

import nl.tudelft.cse.sem.group34.desktop.authentication.entities.User;
import nl.tudelft.cse.sem.group34.desktop.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class UserService {

    @Autowired
    public transient UserRepository userRepository;

    public boolean exists(String username) {
        return userRepository.existsById(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * This method is used to authenticate the user.
     * @param username is the username of the user.
     * @param password is the password of the user.
     * @return boolean authentication approval.
     */
    public boolean authenticate(String username, String password) {
        if (userRepository.existsById(username)) {
            User user = userRepository.findById(username).get();
            return password.equals(user.getPassword());
        }
        return false;
    }

    /**
     * This method is used to signup the user.
     * @param username is the username of the user.
     * @param password is the password of the user.
     */
    public void userSignUp(String username, String password) {

        User user = new User(username, password);
        userRepository.save(user);

    }

    /**
     * Updates the score of the user in the database if score is higher than previous high-score.
     * @param username The username of the user.
     * @param score The new score of the user.
     */
    public void updateScore(String username, int score, String nickname) {
        User user = userRepository.findById(username).get();
        user.setNickname(nickname);
        if (score > user.getScore()) {
            user.setScore(score);
        }
        userRepository.save(user);
    }
}
