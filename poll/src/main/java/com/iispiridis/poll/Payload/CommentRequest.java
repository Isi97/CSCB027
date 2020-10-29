package com.iispiridis.poll.Payload;

public class CommentRequest
{
    String text;
    Long adId;

    public CommentRequest(String text, Long adId) {
        this.text = text;
        this.adId = adId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }
}
