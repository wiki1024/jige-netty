package jige.netty.server;

import java.util.Date;
import java.util.UUID;

/**
 * Created by wchen on 10/5/2016.
 */
public class EmailLog {

    private int id;

    private UUID uid;

    private String app;

    private String state;

    private Date CreatedOn;

    private String payload;

    public EmailPayload getDetail() {
        return detail;
    }

    public void setDetail(EmailPayload detail) {
        this.detail = detail;
    }

    private EmailPayload detail;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(Date createdOn) {
        CreatedOn = createdOn;
    }
}
