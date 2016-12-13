package test.okhttp_time_wait;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpResponseEncoder;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Main
{
	private static int THREAD_COUNT = 4;

	private static int PORT = 8080;


	public static void main(String[] args) {
		if (args.length > 0) PORT = Integer.parseInt(args[0]);
		if (args.length > 1) THREAD_COUNT = Integer.parseInt(args[1]);


		System.out.println("Port: " + PORT);
		System.out.println("Thread count: " + THREAD_COUNT);

		runServer(PORT);

		System.out.println("Server started");
		System.out.println("------------------------------");
		System.out.println("Preparing benchmark");

		final AtomicInteger workerCounter = new AtomicInteger(0);
		final AtomicInteger resultCounter = new AtomicInteger(0);
		final ThreadPoolExecutor pool = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 1, TimeUnit.MINUTES, new LinkedBlockingQueue());

		final OkHttpClient client = new OkHttpClient.Builder()
				.connectionPool(new ConnectionPool(THREAD_COUNT, 5, TimeUnit.MINUTES))
				.build();

		String url = "http://127.0.0.1:" + PORT;
		final Request request = new Request.Builder()
				.url(url)
				.build();

		System.out.println("Requested URL: " + url);
		System.out.println("------------------------------");


		new CounterPrintThread(resultCounter).start();

		while (true)
			if (workerCounter.get() < THREAD_COUNT) {
				workerCounter.incrementAndGet();

				pool.submit(new Runnable()
				{
					@Override
					public void run() {
						try {
							Response response = client.newCall(request).execute();

//							String str = response.body().string();
//							System.out.println(str);

							response.close();

							resultCounter.incrementAndGet();
						}
						catch (IOException e) {
							System.out.println(e);
						}

						workerCounter.decrementAndGet();
					}
				});
			}
	}

	private static void runServer(int port) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(
									new HttpResponseEncoder(),
									new TestServerHandler()
							);
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			b.bind(port).sync();
		}
		catch (Exception e) {}
	}
}
