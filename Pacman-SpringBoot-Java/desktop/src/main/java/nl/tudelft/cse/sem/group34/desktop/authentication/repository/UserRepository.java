package nl.tudelft.cse.sem.group34.desktop.authentication.repository;

import nl.tudelft.cse.sem.group34.desktop.authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
