package me.nuubles.nettyfier.spigot.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Spigot server channel listener. Forwards all received messages to other handlers
 * @author Nuubles
 *
 */
public class ServerChannelInboundHandler extends ChannelInboundHandlerAdapter {
	private ByteBuf tmp;
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		System.out.println("Handler added");
		tmp = ctx.alloc().buffer(4);
	}
	
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		System.out.println("Handler removed");
		tmp.release();
		tmp = null;
	}
	
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf inBuffer = (ByteBuf) msg;
        
        String received = inBuffer.toString(CharsetUtil.UTF_8);
        System.out.println("Server received: " + received);

        ctx.write(Unpooled.copiedBuffer("Hello " + received, CharsetUtil.UTF_8));
    }
    

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel read complete");
    	ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
