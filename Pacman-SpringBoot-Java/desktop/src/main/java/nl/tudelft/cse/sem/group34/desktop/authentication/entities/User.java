package nl.tudelft.cse.sem.group34.desktop.authentication.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class User {

    @Id
    @Column(length = 50, updatable = false, nullable = false)
    private String username;

    @Column(length = 50, updatable = false, nullable = false)
    private String password;

    @Column(length = 50, updatable = true, nullable = false)
    private int score;

    @Column(length = 50, updatable = true, nullable = false)
    private String nickname;

    public User() {

    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * This is the constructor of the class.
     * 
     * @param username is username
     * @param password is password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
        this.nickname = "";
    }

    /**
     * Method returns the username.
     * @return string
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method sets the username.
     * @param username is username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * This method sets the password of the user.
     * @param password is password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method sets the score of the user.
     * @param score the score to which the user's score needs to be set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * This method get the score of the user.
     * @return
     */
    public int getScore() {
        return score;
    }
}
