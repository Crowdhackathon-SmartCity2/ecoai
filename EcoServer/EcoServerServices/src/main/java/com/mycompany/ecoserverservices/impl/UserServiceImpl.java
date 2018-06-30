/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverservices.impl;

import com.mycompany.ecoserverpersistence.models.User;
import com.mycompany.ecoserverpersistence.repositories.UserRepository;
import com.mycompany.ecoserverservices.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author bitsikokos
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String createUserAndSave(User user) throws ConstraintViolationException {
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public User userByIDExists(String id) {
        User user = userRepository.findById(id);
        if (user == null) {
            logger.error("User with id {} not found.", id);
            return null;
        }
        return user;
    }

    @Override
    public Iterable<User> getEveryUser() {
        return userRepository.findAll();
    }

    @Override
    public String validation(User user) {
        if (!isValidUserDTO(user)) {
            logger.error("Unable to create. Invalid request ! Must contain at least a valid email !");
            return "Unable to create. Invalid request ! Must contain at least email !";
        }
        if (userExistsWithMail(user)) {
            logger.error("Unable to create. A User with email {} already exist", user.getEmail());
            return "Unable to create. A User with email " + user.getEmail() + " already exist.";
        }
        if (userExistsWithId(user)) {
            logger.error("Unable to create. A User with id {} already exist", user.getId());
            return "Unable to create. A User with email id " + user.getId() + " already exist.";
        }
        return "SUCCESS";
    }

    @Override
    public boolean userByEmailExists(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.error("User with email {} not found.", email);
            return false;
        }
        return true;
    }

    @Override
    public User updateUser(User user) {
        userRepository.save(user);
        return user;
    }

    private boolean userExistsWithMail(User user) {
        return userRepository.findByEmail(user.getEmail()) != null;
    }
    
    private boolean userExistsWithId(User user) {
        return userRepository.findById(user.getId()) != null;
    }

    private boolean isValidUserDTO(User user) {
        return user != null && user.getEmail() != null;
    }

}
