/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverweb.controllers;

import com.mycompany.ecoserverpersistence.models.Badge;
import com.mycompany.ecoserverpersistence.models.Transaction;
import com.mycompany.ecoserverpersistence.models.User;
import com.mycompany.ecoserverservices.BadgeService;
import com.mycompany.ecoserverservices.TransactionService;
import com.mycompany.ecoserverservices.UserService;
import com.mycompany.ecoserverweb.responses.ControllerEnum;
import com.mycompany.ecoserverweb.responses.CustomErrorType;
import com.mycompany.ecoserverweb.responses.SuccessEnum;
import com.mycompany.ecoserverweb.responses.SuccessResponses;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author bitsikokos
 */
@RestController
@RequestMapping(path = "/ecoTransactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private BadgeService badgeService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(path = "/all")
    @ResponseBody
    public Iterable<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @RequestMapping(value = "/user/{fbId}", method = RequestMethod.POST)
    public ResponseEntity<?> createTransaction(@PathVariable("fbId") String fbId, @RequestBody Transaction transaction, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Transaction : {}", transaction);
        User user = userService.userByIDExists(fbId);
        if (user == null) {
            return new ResponseEntity(new CustomErrorType("User with fbId " + fbId
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        if (!transactionService.validation(transaction)) {
            return new ResponseEntity(new CustomErrorType("Invalid transaction."), HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        try {
            
            try {
                LabelApp.googleApiProcess(transaction);
            } catch (URISyntaxException ex) {
                java.util.logging.Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
                logger.error("Bad URI "+transaction.getPictureUrl());
                return new ResponseEntity(new CustomErrorType("Bad URI"), HttpStatus.BAD_REQUEST);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
                logger.error("No file found "+transaction.getPictureUrl());
                return new ResponseEntity(new CustomErrorType("No file found"), HttpStatus.BAD_REQUEST);
            } catch (GeneralSecurityException ex) {
                java.util.logging.Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
                logger.error("Unable to make google request ");
                return new ResponseEntity(new CustomErrorType("Unable to make google request"), HttpStatus.BAD_REQUEST);
            }
            transactionService.createTransactionAndSave(user, transaction);
            headers.setLocation(ucBuilder.path("/ecoTransactions/transaction/{id}").buildAndExpand(transaction.getId()).toUri());
            badgeLogic(user);
        } catch (ConstraintViolationException e) {
            logger.error("Unable to create a transaction with id "
                    + transaction.getId());
            return new ResponseEntity(new CustomErrorType("Unable to create a transaction with id "
                    + transaction.getId()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
//        return new ResponseEntity(new SuccessResponses(SuccessEnum.TRANSACTION_SUCCESS, ControllerEnum.BADGE, transaction.getPictureUrl()), headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getTransaction(@PathVariable("id") Long id) {
        logger.info("Fetching Badge with id {}", id);
        Transaction transaction = transactionService.transactionByIDExists(id);
        if (transaction != null) {
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity(new CustomErrorType("Transaction with Id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    private void badgeLogic(User user){
        Badge badge = badgeService.findBadgeByPoints(user.getEcoPoints());
        if (badge != null) {
            logger.info("Updating user with id {} with badge {}", user.getId(), badge.getName());
            user.addBadge(badge);
            user = userService.updateUser(user);            
        }
    }
}
