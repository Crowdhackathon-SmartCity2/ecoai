/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverweb.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author bitsikokos
 */
@JsonInclude(Include.NON_NULL)
public class SuccessResponses {

    private String successMessage;
    private String email;
    private String type;
    private String pictureId;
    final String status = "SUCCESS";

    public SuccessResponses(SuccessEnum successEnum, ControllerEnum controllerEnum, String uniqueField) {
        this.successMessage = successEnum.message();
        if (ControllerEnum.USER.equals(controllerEnum)) {
            this.email = uniqueField;
        } else if (ControllerEnum.BADGE.equals(controllerEnum)) {
            this.type = uniqueField;
        } else if (ControllerEnum.TRANSACTION.equals(controllerEnum)) {
            this.pictureId = uniqueField;
        }
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

}
