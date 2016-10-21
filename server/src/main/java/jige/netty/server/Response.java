package jige.netty.server;

/**
 * Created by Weiji on 9/27/2016.
 */
public class Response {

    private int status;

    public  static final String AUTH_OK  = "{\"auth\":200}\r\n";
    public  static final String AUTH_FAIL  = "{\"auth\":-1}\r\n";

    public  static final String RESPONSE_OK  = "{\"status\":200}\r\n";
    public  static final String RESPONSE_INVALID  = "{\"status\":500}\r\n";


    public  Response(){

    }

    public Response(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
