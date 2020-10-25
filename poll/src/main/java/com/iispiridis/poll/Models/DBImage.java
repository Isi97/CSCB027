package com.iispiridis.poll.Models;


import com.iispiridis.poll.Models.Audit.UserDateAudit;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class DBImage extends UserDateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;
    private String type;

    @Lob
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    private DBImage(){}

    public DBImage(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
