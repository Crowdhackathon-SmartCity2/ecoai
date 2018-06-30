/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverweb.controllers;

import com.mycompany.ecoserverpersistence.models.User;
import com.mycompany.ecoserverservices.UserService;
import com.mycompany.ecoserverweb.responses.ControllerEnum;
import com.mycompany.ecoserverweb.responses.CustomErrorType;
import com.mycompany.ecoserverweb.responses.SuccessEnum;
import com.mycompany.ecoserverweb.responses.SuccessResponses;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author bitsikokos
 */
@RestController
@RequestMapping(path = "/ecoUsers")
public class UserController {
    
    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("id") String fbId) {
        logger.info("Fetching User with id {}", fbId);
        User user = userService.userByIDExists(fbId);
        if (user!=null) {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity(new CustomErrorType("User with fbId " + fbId
                    + " not found"), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        logger.info("Creating User : {}", user);
        
        String messageValidation = userService.validation(user);
        if (!"SUCCESS".equals(messageValidation)) {
            return new ResponseEntity(new CustomErrorType(messageValidation), HttpStatus.CONFLICT);
        }
        String id = "";
        HttpHeaders headers = new HttpHeaders();
        try {
            id = userService.createUserAndSave(user);
            headers.setLocation(ucBuilder.path("/atmUsers/user/{id}").buildAndExpand(id).toUri());
        } catch (ConstraintViolationException e) {
            logger.error("Unable to create a user with fbId "
                    + user.getId());
            return new ResponseEntity(new CustomErrorType("Unable to create a user with fbId "
                    + user.getId()), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity(new SuccessResponses(SuccessEnum.USER_SUCCESS,ControllerEnum.USER,user.getEmail()), headers, HttpStatus.CREATED);
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public Iterable<User> getAllUsers() {
        return userService.getEveryUser();
    }

    
}
