/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverpersistence.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author bitsikokos
 */

@Entity
@Table(name = "user_badge")
public class UserBadge implements Serializable{
    private int id;
    private User user;
    private Badge badge;
    @Column(name = "dateOfBadge", insertable = true, updatable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date dateOfBadge = new Date();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public User getUser() {
        return user;
    }

    @ManyToOne
    @JoinColumn(name = "badge_id")
    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "date_of_badge")

    public Date getDateOfBadge() {
        return dateOfBadge;
    }

    public void setDateOfBadge(Date dateOfBadge) {
        this.dateOfBadge = dateOfBadge;
    }

}



