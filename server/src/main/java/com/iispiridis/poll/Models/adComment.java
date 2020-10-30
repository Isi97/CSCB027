package com.iispiridis.poll.Models;

import com.iispiridis.poll.Models.Audit.UserDateAudit;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class adComment extends UserDateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @OneToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    public adComment(){}

    public adComment(String text, Ad ad) {
        this.text = text;
        this.ad = ad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }
}
