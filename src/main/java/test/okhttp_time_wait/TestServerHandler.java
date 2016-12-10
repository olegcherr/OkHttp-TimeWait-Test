package test.okhttp_time_wait;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class TestServerHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		((ByteBuf)msg).release();

		byte[] data = "HTTP/1.1 200 OK\n\nhello".getBytes();
		final ByteBuf buf = ctx.alloc().buffer(data.length);
		buf.writeBytes(data);

		final ChannelFuture f = ctx.writeAndFlush(buf);
		f.addListener(ChannelFutureListener.CLOSE);
	}
}
