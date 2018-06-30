/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverpersistence.repositories;

import com.mycompany.ecoserverpersistence.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author bitsikokos
 */
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    User findById(String Id);
}