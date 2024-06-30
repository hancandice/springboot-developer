package me.jeehahn.springbootdeveloper.repository;


import java.util.Optional;
import me.jeehahn.springbootdeveloper.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}