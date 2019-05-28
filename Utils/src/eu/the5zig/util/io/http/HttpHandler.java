package eu.the5zig.util.io.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class HttpHandler extends SimpleChannelInboundHandler<HttpObject> {

	private final HttpResponseCallback callback;
	private final StringBuilder buffer = new StringBuilder();
	private int responseCode = 200;

	public HttpHandler(HttpResponseCallback callback) {
		this.callback = callback;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		try {
			callback.call(null, responseCode, cause);
		} finally {
			ctx.channel().close();
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		if (msg instanceof HttpResponse) {
			HttpResponse response = (HttpResponse) msg;
			this.responseCode = response.getStatus().code();

			if (responseCode == HttpResponseStatus.NO_CONTENT.code()) {
				done(ctx);
				return;
			}

			//			if (responseCode != HttpResponseStatus.OK.code()) {
			//				throw new IllegalStateException("Expected HTTP response 200 OK, got " + response.getStatus());
			//			}
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
			callback.call(buffer.toString(), responseCode, null);
		} finally {
			ctx.channel().close();
		}
	}
}
