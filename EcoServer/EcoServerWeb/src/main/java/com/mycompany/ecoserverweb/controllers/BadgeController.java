/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverweb.controllers;

import com.mycompany.ecoserverpersistence.models.Badge;
import com.mycompany.ecoserverpersistence.models.User;
import com.mycompany.ecoserverservices.BadgeService;
import com.mycompany.ecoserverservices.UserService;
import com.mycompany.ecoserverweb.responses.ControllerEnum;
import com.mycompany.ecoserverweb.responses.CustomErrorType;
import com.mycompany.ecoserverweb.responses.SuccessEnum;
import com.mycompany.ecoserverweb.responses.SuccessResponses;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author bitsikokos
 */
@RestController
@RequestMapping(path = "/ecoBadges")
public class BadgeController {

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/badge/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getBadge(@PathVariable("id") Long id) {
        logger.info("Fetching Badge with id {}", id);
        Badge badge = badgeService.badgeByIDExists(id);
        if (badge != null) {
            return new ResponseEntity<>(badge, HttpStatus.OK);
        } else {
            return new ResponseEntity(new CustomErrorType("Badge with Id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/badge/", method = RequestMethod.POST)
    public ResponseEntity<?> createBadge(@RequestBody Badge badge, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Badge : {}", badge);

        if (!badgeService.validation(badge)) {
            return new ResponseEntity(new CustomErrorType("Invalid badge."), HttpStatus.CONFLICT);
        }
        Long id = 0L;
        HttpHeaders headers = new HttpHeaders();
        try {
            id = badgeService.createBadgeAndSave(badge);
            headers.setLocation(ucBuilder.path("/ecoBadges/badge/{id}").buildAndExpand(id).toUri());
        } catch (ConstraintViolationException e) {
            logger.error("Unable to create a badge with id "
                    + badge.getId());
            return new ResponseEntity(new CustomErrorType("Unable to create a badge with id "
                    + badge.getId()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new SuccessResponses(SuccessEnum.BADGE_SUCCESS,ControllerEnum.BADGE, badge.getName()), headers, HttpStatus.CREATED);
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public Iterable<Badge> getAllBadges() {
        return badgeService.getEveryBadge();
    }

    @RequestMapping(value = "/badge", method = RequestMethod.PUT)
    public ResponseEntity<?> addBadgeToUser(@RequestParam String nameBadge, @RequestParam String id) {
        Badge badge = badgeService.findBadgeByName(nameBadge);
        User user = userService.userByIDExists(id);
        if (badge != null && user != null) {
            logger.info("Updating user with id {} with badge {}", id, nameBadge);
            user.addBadge(badge);
            User userUpdated = userService.updateUser(user);
            user = userUpdated;
            
        } else if (badge == null) {
            logger.error("Unable to update badge with nameBadge: " + nameBadge + ", not found.");
            return new ResponseEntity(new CustomErrorType("Unable to update badge with nameBadge: " + nameBadge + ", not found."),
                    HttpStatus.NOT_FOUND);
        } else if (user == null) {
            logger.error("Unable to update user with id: " + id + ", not found.");
            return new ResponseEntity(new CustomErrorType("Unable to update user with id: " + id + ", not found."),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}
