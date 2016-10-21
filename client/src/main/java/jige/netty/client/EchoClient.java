package jige.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.LineEncoder;
import io.netty.handler.codec.string.LineSeparator;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.CompleteFuture;
import io.netty.util.concurrent.Future;
import jige.netty.server.Auth;
import jige.netty.server.EmailPayload;
import jige.netty.server.JsonLogicEncoder;
import org.joda.time.DateTime;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Weiji on 9/20/2016.
 */
public class EchoClient {

    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));
    public static AttributeKey<CompletableFuture<String>> Auth_Future_Key = AttributeKey.valueOf("authFuture");

    private int port;
    private String host;
    private EventLoopGroup group;
    private volatile Channel channel;
    private volatile ChannelFuture channelFuture;

    private ConcurrentLinkedQueue<EmailPayload> bufferQueue;

    public ConcurrentLinkedQueue<EmailPayload> getBufferQueue() {
        return bufferQueue;
    }

    public EventLoopGroup getGroup() {
        return this.group;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.bufferQueue = new ConcurrentLinkedQueue<>();
    }

    public void run() throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        this.group = group;
//        try {
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        //p.addLast(new LoggingHandler(LogLevel.INFO));
                        p.addLast(new LineBasedFrameDecoder(1024),
                                new StringDecoder(CharsetUtil.UTF_8),
                                new LineEncoder(LineSeparator.DEFAULT,CharsetUtil.UTF_8),
                                new JsonLogicEncoder(),
                                new EchoClientHandler());
                    }
                });

        // Start the client.
        ChannelFuture f = b.connect(host, port);
        this.channelFuture = f;
        EchoClient client = this;
        f.addListener((future) -> {
            System.out.println("addListener thread " + Thread.currentThread().getId());
            ChannelFuture ff = (ChannelFuture) future;
            client.setChannel(ff.channel());
            ff.channel().closeFuture().addListener((fff) -> {
                System.out.println("Closed by Server");
            });
            CompletableFuture<String> authFuture = new CompletableFuture<>();
            authFuture.thenAcceptAsync((au)->{
                try{
                    System.out.println("auth:" + new DateTime());
                    if(au.equals("YES")){

                        client.flushQueue();
                    }
                    else {
                        System.err.println("auth fails");
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            });
            client.getChannel().attr(EchoClient.Auth_Future_Key).set(authFuture);
            client.getChannel().writeAndFlush(new Auth("Auth", "admin", "password"));

        });
        // ch.writeAndFlush(new Auth("Auth","admin","password"));
        // Wait until the connection is closed.
//            f.channel().closeFuture().sync();
//        } finally {
//            // Shut down the event loop to terminate all threads.
//            group.shutdownGracefully();
//        }
    }

    private void flushQueue() throws Exception {
        System.out.println("queued total: "+bufferQueue.size());
        while(true){
            EmailPayload email=bufferQueue.poll();
            if(email != null && isReady()){

                this.channel.writeAndFlush(email);
            }
            else
            {

                if(email != null) { bufferQueue.add(email);}
                break;
            }
        }
    }

    public boolean isReady() throws Exception {
        return this.channelFuture.isSuccess()
                && (this.channel != null && this.channel.isActive())
                && (this.channel.attr(EchoClient.Auth_Future_Key).get() != null && this.channel.attr(EchoClient.Auth_Future_Key).get().isDone())
                && this.channel.attr(EchoClient.Auth_Future_Key).get().get().equals("YES");
    }

    public void send(EmailPayload email) throws Exception {
        if(!isReady()){
            email.setSubject(email.getSubject()+"_queued");
            bufferQueue.add(email);
        }
        else {
            channel.writeAndFlush(email);
        }
    }

}
