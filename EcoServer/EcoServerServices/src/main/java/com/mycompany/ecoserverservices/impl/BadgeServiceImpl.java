/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverservices.impl;

import com.mycompany.ecoserverpersistence.models.Badge;
import com.mycompany.ecoserverpersistence.models.User;
import com.mycompany.ecoserverpersistence.repositories.BadgeRepository;
import com.mycompany.ecoserverpersistence.repositories.UserRepository;
import com.mycompany.ecoserverservices.BadgeService;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author bitsikokos
 */

@Service ("badgeService")
public class BadgeServiceImpl implements BadgeService {
    @Autowired
    private BadgeRepository badgeRepository;
    
    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public Long createBadgeAndSave(Badge badge) throws ConstraintViolationException{
        badgeRepository.save(badge);
        return badge.getId();
    }

    @Override
    public Badge badgeByIDExists(Long id) {
        Badge badge = badgeRepository.findById(id);
        if (badge == null) {
            logger.error("Badge with id {} not found.", id);
            return null;
        }
        return badge;
    }
    
    @Override
    public Badge findBadgeByName(String name) {
        Badge badge = badgeRepository.findByName(name);
        if (badge == null) {
            logger.error("Badge with name {} not found.", name);
            return null;
        }
        return badge;
    }
    
    @Override
    public Badge findBadgeByPoints(Long points) {
        Badge badge = badgeRepository.findByPoints(points);
        if (badge == null) {
            logger.error("Badge with points {} not found.", points);
            return null;
        }
        return badge;
    }

    @Override
    public Iterable<Badge> getEveryBadge() {
        return badgeRepository.findAll();
    }
    
    @Override
    public boolean validation(Badge badge){
        if (!isValidBadge(badge)) {
            logger.error("Unable to create badge. Invalid request ! Must contain at least a valid name and picture !");
            return false;
        }
        if (badgeRepository.findByName(badge.getName()) != null) {
            logger.error("Unable to create badge. Invalid request ! Name exists !");
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean addBadgeToUser(Badge badge,User user){
        user.addBadge(badge);
        badgeRepository.save(badge);
        userRepository.save(user);
        return true;
    }
    
    private boolean isValidBadge(Badge badge) {
        return badge != null && badge.getName() != null && badge.getBadgePictureUrl() != null;
    }

}
