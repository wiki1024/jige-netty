package jige.netty.server;

import io.netty.util.internal.chmv8.ForkJoinPool;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * Created by wchen on 10/5/2016.
 */
public class EmailProcessor {

    private final static JdbcTemplate EmailDB  = getJDBCTemplate();

    private final static ForkJoinPool Pool = new ForkJoinPool(4);

    private final static EmailProcessor Instance=new EmailProcessor();

    public static EmailProcessor getInstance(){
        return Instance;
    }

    public void submit(EmailLog oLog){
        final EmailLog log=oLog;
        if("received".equals(log.getState())){

            CompletableFuture<EmailLog> beforeSend=CompletableFuture.supplyAsync(()->{
               // System.out.println("before send" +Thread.currentThread().getId() + "_"+ log.getDetail().getSubject());
                try
                {
                EmailDB.update("insert into email (uid,payload) values(?,?)",log.getUid().toString(),log.getPayload());
                EmailDB.update("insert into emaillog (uid,app,state,createdon) VALUES (?,?,?,now()) ",
                        log.getUid().toString(), log.getApp(), log.getState());
                    log.setState("Sent");
                }
                catch(Exception ex){
                    log.setState("Failed");
                    ex.printStackTrace();
                }

                return log;
            },Pool);

            CompletableFuture<Integer> after = beforeSend.thenApplyAsync((ll)-> {
                //System.out.println("after send" + Thread.currentThread().getId() + "_"+log.getDetail().getSubject());

                EmailDB.update("insert into emaillog (uid,app,state,createdon) VALUES (?,?,?,now()) ",
                        ll.getUid().toString(),ll.getApp(),ll.getState());
                return 1;
            },Pool);

            after.exceptionally((t)->{
                t.printStackTrace();
                return -1;
            });

        }
        else{
            EmailDB.update("insert into emaillog (uid,app,state,createdon) VALUES (?,?,?,now()) ",
                    log.getUid().toString(),log.getApp(),log.getState());
        }
    }

    private static JdbcTemplate getJDBCTemplate()
    {
        BasicDataSource dataSource=null;
        try {
            Properties properties = new Properties();
            properties.load(App.class.getResourceAsStream("/properties/database.properties"));

            dataSource =new BasicDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl(properties.getProperty("url"));
            dataSource.setUsername(properties.getProperty("username"));
            dataSource.setPassword(properties.getProperty("password"));

        }
        catch (Exception ex){

        }

        if(dataSource == null){
            return null;
        }

        return new JdbcTemplate(dataSource);
    }
}
