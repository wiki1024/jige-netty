package jige.netty.server;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Created by Weiji on 9/27/2016.
 */
@JsonPropertyOrder({ "type","username","password" })
public class Auth {

    private String type;

    private String username;

    private String password;

    public Auth(){

    }

    public Auth(String type,String username, String password){
        this.setType(type);
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
