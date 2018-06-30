/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverweb.responses;

/**
 *
 * @author bitsikokos
 */
public enum SuccessEnum {
    USER_SUCCESS("Created User succesfully !"),
    BADGE_SUCCESS("Created Badge succesfully !"),
    TRANSACTION_SUCCESS("Created Transaction succesfully !");
    private String message;

    SuccessEnum(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}

