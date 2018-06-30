/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverservices.impl;

import com.mycompany.ecoserverpersistence.models.Badge;
import com.mycompany.ecoserverpersistence.models.Transaction;
import com.mycompany.ecoserverpersistence.models.User;
import com.mycompany.ecoserverpersistence.repositories.TransactionRepository;
import com.mycompany.ecoserverservices.TransactionService;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author bitsikokos
 */
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public Transaction transactionByIDExists(Long id){
        Transaction transaction = transactionRepository.findById(id);
        if (transaction == null) {
            logger.error("Transaction with id {} not found.", id);
            return null;
        }
        return transaction;
    }
    
    @Override
    public Iterable<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }
    
    @Override
    public boolean validation(Transaction transaction){
        if (!isValidBadge(transaction)) {
            logger.error("Unable to create transaction. Invalid request ! Must contain at least a valid garbage type and a picture !");
            return false;
        }
        return true;
    }
    
    @Override
    public void createTransactionAndSave(User user, Transaction transaction) throws ConstraintViolationException{
        transaction.setUser(user);
        user.setEcoPoints(user.getEcoPoints()+transaction.getEcoPoints());
        transactionRepository.save(transaction);
    }
    
    private boolean isValidBadge(Transaction transaction) {
        return transaction != null && transaction.getPictureUrl()!= null;
    }
}
