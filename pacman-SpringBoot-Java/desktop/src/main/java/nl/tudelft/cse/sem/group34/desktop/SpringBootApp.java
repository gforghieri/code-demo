package nl.tudelft.cse.sem.group34.desktop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootApp {

    public static void main(String[] args) {
        System.out.println("starting server...");
        SpringApplication.run(SpringBootApp.class, args);
    }
}