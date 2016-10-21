package jige.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Weiji on 9/20/2016.
 */
public class EchoServerHandler extends SimpleChannelInboundHandler<Object> {

    private AuthManager authManager;

    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
      //  System.out.println(this.toString());
        if(o instanceof  Auth){
            authManager.authenticate((Auth)o);
            if (!authManager.isAuthenticated()){
                ctx.writeAndFlush(Response.AUTH_FAIL);
                ctx.close();
            }
            else {
                ctx.writeAndFlush(Response.AUTH_OK);
            }
        }
        else {
            if (!authManager.isAuthenticated()){
                ctx.writeAndFlush(Response.AUTH_FAIL);
                ctx.close();
            }
            else {
                EmailLog log=(EmailLog)o;
                EmailProcessor processor = EmailProcessor.getInstance();
                processor.submit(log);
                ctx.writeAndFlush(Response.RESPONSE_OK);
            }
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        authManager=new AuthManager();
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //task.unregisteredChannel();
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  {
        cause.printStackTrace();
        ctx.close();
    }


}
