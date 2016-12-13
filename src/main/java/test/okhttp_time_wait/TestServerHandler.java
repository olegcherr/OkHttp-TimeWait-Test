package test.okhttp_time_wait;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;


public class TestServerHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		System.out.println(((ByteBuf)msg).toString(CharsetUtil.UTF_8));
		((ByteBuf)msg).release();

		byte[] data = "hello".getBytes(CharsetUtil.UTF_8);
		HttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(data));
		resp.headers().set("Content-Length", data.length);
		resp.headers().set("Connection", "Keep-Alive");

		final ChannelFuture f = ctx.writeAndFlush(resp);
//		f.addListener(ChannelFutureListener.CLOSE);
	}
}
