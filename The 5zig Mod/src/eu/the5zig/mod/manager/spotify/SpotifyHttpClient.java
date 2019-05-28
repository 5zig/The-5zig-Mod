package eu.the5zig.mod.manager.spotify;

import com.google.common.base.Preconditions;
import eu.the5zig.util.io.http.HttpResponseCallback;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Map;

public class SpotifyHttpClient {


	@SuppressWarnings("UnusedAssignment")
	public static void get(String url, final Map<String, String> headers, int timeout, boolean ignoreInvalidCertificates, EventLoopGroup eventLoop, final HttpResponseCallback callback) {
		Preconditions.checkNotNull(url, "url");
		Preconditions.checkNotNull(eventLoop, "eventLoop");
		Preconditions.checkNotNull(callback, "callBack");

		final URI uri = URI.create(url);

		Preconditions.checkNotNull(uri.getScheme(), "scheme");
		Preconditions.checkNotNull(uri.getHost(), "host");
		boolean ssl = uri.getScheme().equals("https");
		int port = uri.getPort();
		if (port == -1) {
			if (uri.getScheme().equals("http"))
				port = 80;
			else if (uri.getScheme().equals("https"))
				port = 443;
			else
				throw new IllegalArgumentException("Unknown scheme " + uri.getScheme());
		}

		InetAddress inetHost = null;
		try {
			inetHost = InetAddress.getByName(uri.getHost());
		} catch (UnknownHostException ex) {
			callback.call(null, -1, ex);
			return;
		}

		ChannelFutureListener future = new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					String path = uri.getRawPath() + ((uri.getRawQuery() == null) ? "" : "?" + uri.getRawQuery());

					HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
					request.headers().set(HttpHeaders.Names.HOST, uri.getHost());
					request.headers().set(HttpHeaders.Names.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
					for (Map.Entry<String, String> entry : headers.entrySet()) {
						request.headers().set(entry.getKey(), entry.getValue());
					}

					future.channel().writeAndFlush(request);
				} else {
					callback.call(null, -1, future.cause());
				}
			}
		};

		new Bootstrap().channel(NioSocketChannel.class).group(eventLoop).handler(new SpotifyHttpInitializer(callback, ssl, uri.getHost(), port, timeout, ignoreInvalidCertificates)).option(
				ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout).remoteAddress(inetHost, port).connect().addListener(future);
	}

}
