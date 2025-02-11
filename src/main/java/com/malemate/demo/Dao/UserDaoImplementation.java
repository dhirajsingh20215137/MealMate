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
        logger.info("Fetching active user by ID: {}", id);
        try {
            User user = entityManager.createQuery(
                            "SELECT u FROM User u WHERE u.userId = :id AND u.deleted = false", User.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();

            logger.debug("User found with ID: {}", id);
            return Optional.of(user);
        } catch (Exception e) {
            logger.warn("Error fetching user with ID {}: {}", id, e.getMessage());
            return Optional.empty();
        }
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
        logger.info("Attempting soft delete for user with ID: {}", id);
        Optional<User> optionalUser = getUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeleted(true);
            entityManager.merge(user);
            logger.info("User soft deleted with ID: {}", id);
        } else {
            logger.warn("User not found with ID: {}", id);
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        logger.info("Fetching active user by email: {}", email);
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.deleted = false", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            logger.debug("User found with email: {}", email);
            return Optional.of(user);
        } catch (Exception e) {
            logger.warn("No active user found with email: {}", email);
            return Optional.empty();
        }
    }
}
