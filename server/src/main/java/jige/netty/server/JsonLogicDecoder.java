package jige.netty.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.util.CharsetUtil.UTF_8;

/**
 * Created by Weiji on 9/27/2016.
 */
public class JsonLogicDecoder extends MessageToMessageDecoder<String> {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final AtomicInteger atomicInt = new AtomicInteger(0);

    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        int i=atomicInt.incrementAndGet();
        if ( i> 9990) {
            System.out.println("Server received " +i + " at " + new Date());
        System.out.println(msg);

        }
        if (msg.startsWith(Constant.MacthAuth)) {
            Auth auth = jsonMapper.readValue(msg, Auth.class);
            out.add(auth);
        } else if (msg.startsWith(Constant.MacthEmail)) {
            EmailPayload payload = jsonMapper.readValue(msg, EmailPayload.class);

            payload.setUid(UUID.randomUUID());
            EmailLog log = new EmailLog();
            log.setUid(payload.getUid());
            log.setState("received");
            log.setApp("more serious app");
            log.setPayload(msg);
            log.setDetail(payload);
            out.add(log);
        } else {

            ctx.writeAndFlush(Response.RESPONSE_INVALID);
            ctx.close();
        }

    }
}
