/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverpersistence.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

/**
 *
 * @author bitsikokos
 */
@Entity // This tells Hibernate to make a table out of this class
//@Table(name = "User")
@Table(name = "user")
public class User {

    @Column(unique = true)
    private String id;

    private String municipality;

    private Long ecoPoints = 0L;

    @Column(unique = true)
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message = "{invalid.email}")
    private String email;

    private String type;

//    @OneToMany(mappedBy = "user")
    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<UserBadge> badges = new HashSet<UserBadge>();
    
    private Set<Transaction> transactions = new HashSet<Transaction>();

    public User() {
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public Long getEcoPoints() {
        return ecoPoints;
    }

    public void setEcoPoints(Long ecoPoints) {
        this.ecoPoints = ecoPoints;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //@JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<UserBadge> getBadges() {
        return badges;
    }

    public void setBadges(Set<UserBadge> badges) {
        this.badges = badges;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)//, fetch=FetchType.EAGER)
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addBadge(Badge badge) {
        UserBadge userBadge = new UserBadge();
        userBadge.setBadge(badge);
        userBadge.setUser(this);
        Date date = new Date();
        userBadge.setDateOfBadge(date);
        this.getBadges().add(userBadge);
    }
    /*@Override
    public String toString() {
        return "User{" + "id=" + id
                + ", municipality=" + municipality
                + ", ecoPoints=" + ecoPoints
                + ", email=" + email
                + ", type=" + type
                + ", badges={" + this.setToString() + '}';
    }
    
    private String setToString(){
        String result = "";
        if (!badges.isEmpty()) {
            result+="Badges{";
            for(UserBadge badge : badges){
                result+=badge.toString()+",";
            }
        }
        return result;
        
    }*/

}
