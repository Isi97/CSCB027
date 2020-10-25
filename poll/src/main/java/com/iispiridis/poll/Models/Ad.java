package com.iispiridis.poll.Models;


import com.iispiridis.poll.Models.Audit.UserDateAudit;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ads")
public class Ad extends UserDateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 500)
    private String description;

    @Size(max = 100)
    private String title;

    public Ad(){}

    public Ad( @Size(max = 500) String description, @Size(max = 100) String title) {
        this.description = description;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
