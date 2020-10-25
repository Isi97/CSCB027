package com.iispiridis.poll.Payload;

import com.iispiridis.poll.Models.ContactInformation;

public class UserSummary {
    private Long id;
    private String username;
    private String name;

    private ContactInformation contactInformation = null;

    public UserSummary(Long id, String username, String name, ContactInformation contactInformation) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.contactInformation = contactInformation;
    }

    public UserSummary(Long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
