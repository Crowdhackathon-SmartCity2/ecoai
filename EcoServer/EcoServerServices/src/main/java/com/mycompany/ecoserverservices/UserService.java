/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverservices;

import com.mycompany.ecoserverpersistence.models.User;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author bitsikokos
 */
public interface UserService {

    public String createUserAndSave(User user) throws ConstraintViolationException;

    public User userByIDExists(String fbId);

    public Iterable<User> getEveryUser();

    public String validation(User user);

    public boolean userByEmailExists(String email);

    public User updateUser(User user);
}
