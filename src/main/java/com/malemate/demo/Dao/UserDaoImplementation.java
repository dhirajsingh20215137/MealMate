package com.malemate.demo.Dao;

import com.malemate.demo.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@Transactional
public class UserDaoImplementation implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImplementation.class);

    @Override
    public Optional<User> getUserById(int id) {
        logger.info("Fetching user by ID: {}", id);
        User user = entityManager.find(User.class, id);
        if (user != null) {
            logger.debug("User found with ID: {}", id);
        } else {
            logger.debug("No user found with ID: {}", id);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User saveUser(User user) {
        if (user.getUserId() == 0) {
            logger.info("Persisting new user: {}", user.getEmail());
            entityManager.persist(user);
            logger.debug("New user persisted: {}", user.getEmail());
        } else {
            logger.info("Merging user: {}", user.getEmail());
            user = entityManager.merge(user);
            logger.debug("User merged: {}", user.getEmail());
        }
        return user;
    }

    @Override
    public void deleteUser(int id) {
        logger.info("Attempting to delete user with ID: {}", id);
        User user = getUserById(id).orElseThrow(() -> {
            logger.error("User not found with ID: {}", id);
            return new RuntimeException("User not found");
        });
        entityManager.remove(user);
        logger.debug("User deleted with ID: {}", id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        logger.info("Fetching user by email: {}", email);
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            logger.debug("User found with email: {}", email);
            return Optional.of(user);
        } catch (Exception e) {
            logger.error("No user found with email: {}", email, e);
            return Optional.empty();
        }
    }
}
