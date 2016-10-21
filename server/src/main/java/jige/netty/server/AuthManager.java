package jige.netty.server;

/**
 * Created by Weiji on 9/27/2016.
 */
public class AuthManager {

    private boolean _isAuthenticated;

    public boolean isAuthenticated() {
        return _isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        _isAuthenticated = authenticated;
    }

    public void authenticate(Auth auth) {
        if(auth.getUsername().equals("admin") && auth.getPassword().equals("password")){
           try{

               //Thread.sleep(1000);
           }
           catch (Exception ex){

           }
            _isAuthenticated=true;
        }
        else {
            _isAuthenticated=false;
        }
    }
}
