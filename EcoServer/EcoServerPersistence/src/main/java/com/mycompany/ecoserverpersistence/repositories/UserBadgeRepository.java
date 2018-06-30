/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverpersistence.repositories;

import com.mycompany.ecoserverpersistence.models.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author bitsikokos
 */
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

}
