package jige.netty.client;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Weiji on 9/20/2016.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    AtomicInteger atomicInt = new AtomicInteger(0);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        ctx.writeAndFlush(firstMessage);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String responseJson = (String) msg;

        CompletableFuture<String> authFuture = ctx.channel().attr(EchoClient.Auth_Future_Key).get();
        if (!authFuture.isDone()) {

            if ("{\"auth\":200}".equals(responseJson)) {
                authFuture.complete("YES");
            } else if ("{\"auth\":-1}".equals(responseJson)) {
                authFuture.complete("NO");
            }
        }

        if(atomicInt.incrementAndGet()>90) {
            System.out.println("received "+ "_ " +atomicInt.get()+msg +" at " + new Date());

        }
//        System.out.println(responseJson);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
