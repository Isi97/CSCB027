package com.iispiridis.poll.Models;

import javax.persistence.*;

@Entity
@Table(name = "contact_information")
public class ContactInformation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "contactInformation")
    private User user;

    private String address;
    private String phone;

    public ContactInformation(String address, String phone) {
        this.address = address;
        this.phone = phone;
    }

    public ContactInformation(){}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
