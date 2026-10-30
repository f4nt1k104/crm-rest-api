package com.example.crm.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sellers")
public class Seller
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;


    public Seller(){}

    public Seller(int id, String name, String contactInfo, LocalDateTime registrationDate)
    {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.registrationDate = registrationDate;
    }


    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public void setContactInfo(String contactInfo){
        this.contactInfo = contactInfo;
    }
    public String getContactInfo(){
        return this.contactInfo;
    }

    public void setRegistrationDate(LocalDateTime registrationDate){
        this.registrationDate = registrationDate;
    }
    public LocalDateTime getRegistrationDate(){
        return this.registrationDate;
    }

    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
