/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecoserverpersistence.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author bitsikokos
 */
@Entity // This tells Hibernate to make a table out of this class
//@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"badge_picture_url"})})
public class Badge {
    
    private long id;
    
    @Column(unique = true)
    private String name;
    
    private String description;
    
    private long points = 0L;
    
    //@Column(name="badge_picture_url")
    @Column(unique = true)
    private String badgePictureUrl;
    
    private Set<UserBadge> userBadges;
    
    public Badge() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public String getBadgePictureUrl() {
        return badgePictureUrl;
    }

    public void setBadgePictureUrl(String badgePictureUrl) {
        this.badgePictureUrl = badgePictureUrl;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "badge")
    public Set<UserBadge> getUserBadges() {
        return userBadges;
    }

    public void setUserBadges(Set<UserBadge> userBadges) {
        this.userBadges = userBadges;
    }

    /*@JsonIgnore
    public Set<UserBadge> getUsers() {
        return users;
    }

    public void setUsers(Set<UserBadge> users) {
        this.users = users;
    }*/

    @Override
    public String toString() {
        return "Badge{" + "id=" + id + ", name=" + name + ", description=" + description + ", badgePictureUrl=" + badgePictureUrl + '}';
    }    
    
}
