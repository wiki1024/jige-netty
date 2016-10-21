package jige.netty.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by Weiji on 9/27/2016.
 */
public class JsonLogicEncoder  extends MessageToMessageEncoder<Object>{
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        if(msg instanceof String){
            out.add(msg);
            return;
        }
        String json = jsonMapper.writeValueAsString(msg);
        if (json.length() == 0) {
            return;
        }
        out.add(json);
    }
}
