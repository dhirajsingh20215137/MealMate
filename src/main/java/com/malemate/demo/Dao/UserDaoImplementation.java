package com.malemate.demo.Dao;

import com.malemate.demo.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
@Transactional
public class UserDaoImplementation implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> getUserById(int id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }



    @Override
    public User saveUser(User user) {
        if (user.getUserId() == 0) {

            entityManager.persist(user);
        } else {

            entityManager.merge(user);
        }
        return user;
    }

    @Override
    public void deleteUser(int id) {
        User user = getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
        entityManager.remove(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {

        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
