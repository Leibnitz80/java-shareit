package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@AutoConfigureTestDatabase
@DataJpaTest
public class JPAUserTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private final User user1 = new User(null, "ivanov", "ivanov@gmail.com");

    @Test
    @DisplayName("Тест на сохранение и получение id сохранённого юзера")
    void saveUserAndGetIdTest() {
        Assertions.assertNull(user1.getId());
        entityManager.persist(user1);
        Assertions.assertNotNull(user1.getId());
    }
}