package jige.netty.server;



/**
 * Created by Weiji on 9/18/2016.
 */
public class App {
    public static void main(String[] args) throws Exception {
        EchoServer server = new EchoServer(8007);

        server.run();


        System.out.println(Thread.currentThread().getId());

    }



}
