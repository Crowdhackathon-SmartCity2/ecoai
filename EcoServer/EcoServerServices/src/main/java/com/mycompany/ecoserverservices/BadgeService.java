/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverservices;

import com.mycompany.ecoserverpersistence.models.Badge;
import com.mycompany.ecoserverpersistence.models.User;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author bitsikokos
 */
public interface BadgeService {

    public Long createBadgeAndSave(Badge badge) throws ConstraintViolationException;

    public Badge badgeByIDExists(Long id);

    public Iterable<Badge> getEveryBadge();

    public boolean validation(Badge badge);

    public boolean addBadgeToUser(Badge badge, User user);

    public Badge findBadgeByName(String name);
    
    public Badge findBadgeByPoints(Long points);
}
