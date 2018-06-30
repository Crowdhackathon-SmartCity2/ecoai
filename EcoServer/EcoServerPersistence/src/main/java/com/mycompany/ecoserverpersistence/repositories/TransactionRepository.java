/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverpersistence.repositories;

import com.mycompany.ecoserverpersistence.models.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author bitsikokos
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findById(Long id);
}
