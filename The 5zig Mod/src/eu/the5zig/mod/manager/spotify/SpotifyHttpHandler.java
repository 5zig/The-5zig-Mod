package eu.the5zig.mod.manager.spotify;

import eu.the5zig.util.io.http.HttpResponseCallback;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;

public class SpotifyHttpHandler extends SimpleChannelInboundHandler<HttpObject> {

	private final HttpResponseCallback callback;
	private final StringBuilder buffer = new StringBuilder();
	private int responseCode = 500;
	private AtomicBoolean hasResponded = new AtomicBoolean(false);

	public SpotifyHttpHandler(HttpResponseCallback callback) {
		this.callback = callback;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		try {
			if (hasResponded.compareAndSet(false, true)) {
				callback.call(buffer.toString(), responseCode, cause);
			}
		} finally {
			ctx.channel().close();
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		if (msg instanceof HttpResponse) {
			HttpResponse response = (HttpResponse) msg;
			responseCode = response.getStatus().code();

			if (responseCode == HttpResponseStatus.NO_CONTENT.code()) {
				done(ctx);
				return;
			}

			if (responseCode != HttpResponseStatus.OK.code()) {
				throw new IllegalStateException("Expected HTTP response 200 OK, got " + response.getStatus());
			}
		}
		if (msg instanceof HttpContent) {
			HttpContent content = (HttpContent) msg;
			buffer.append(content.content().toString(Charset.forName("UTF-8")));

			if (msg instanceof LastHttpContent) {
				done(ctx);
			}
		}
	}

	private void done(ChannelHandlerContext ctx) {
		try {
			if (hasResponded.compareAndSet(false, true)) {
				callback.call(buffer.toString(), responseCode, null);
			}
		} finally {
			ctx.channel().close();
		}
	}
}
