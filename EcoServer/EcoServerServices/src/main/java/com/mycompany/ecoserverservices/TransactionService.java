/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverservices;

import com.mycompany.ecoserverpersistence.models.Badge;
import com.mycompany.ecoserverpersistence.models.Transaction;
import com.mycompany.ecoserverpersistence.models.User;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author bitsikokos
 */
public interface TransactionService {

    public Transaction transactionByIDExists(Long id);
    public Iterable<Transaction> getAllTransactions();

    public boolean validation(Transaction transaction);
    public void createTransactionAndSave(User user, Transaction transaction) throws ConstraintViolationException;
}
