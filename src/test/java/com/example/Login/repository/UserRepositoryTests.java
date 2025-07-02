package com.example.Login.repository;

import com.example.Login.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("lochana2025@gmail.com");
        user.setPassword("password123");
        user.setFirstName("Lochana");
        user.setLastName("Jayasinghe");

        User savedUser = repo.save(user);
        User existingUser = entityManager.find(User.class, savedUser.getId());

        assertThat(existingUser.getEmail()).isEqualTo(user.getEmail());
    }
}
