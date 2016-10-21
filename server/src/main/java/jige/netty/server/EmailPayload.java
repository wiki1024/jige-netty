package jige.netty.server;

import java.util.UUID;

/**
 * Created by wchen on 9/30/2016.
 */
public class EmailPayload {

    private String type;

    private String to;

    private String from;

    private String subject;

    private String htmlBody;

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    private UUID uid;

    public EmailPayload(){

    }

    public EmailPayload(String type, String to, String from, String subject, String htmlBody) {
        this.type = type;
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.htmlBody = htmlBody;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }
}
