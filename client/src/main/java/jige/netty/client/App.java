package jige.netty.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.corba.se.spi.orbutil.fsm.Input;
import jige.netty.server.Auth;
import jige.netty.server.EmailPayload;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import java.io.RandomAccessFile;
import java.time.Clock;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class App
{
    public static void main( String[] args ) throws Exception
    {
       EchoClient client=new EchoClient("127.0.0.1",8007);


        client.run();
//        DateTime dt = new DateTime();
//
//        System.out.println(dt);
//        DateTime end= dt.plusMillis(600);
      //  DateTime before= end.minusMillis(1);
       // AtomicInteger atomicInt = new AtomicInteger(10000);
       ExecutorService source = Executors.newFixedThreadPool(4);
       final ScheduledExecutorService scheduler =
               Executors.newScheduledThreadPool(1);

        IntStream.range(0,100).forEach((i)-> source.submit(() -> {



                        EmailPayload payload = new EmailPayload("Email", "to", "from", "random_" + i, "sss");
                        try {
                            client.send(payload);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                }
        ));
        scheduler.schedule(()-> System.out.println(client.getBufferQueue().size()),3000, TimeUnit.MILLISECONDS);

//
    }

}
