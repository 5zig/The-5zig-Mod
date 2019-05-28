package eu.the5zig.util.io.http;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class HttpInitializer extends ChannelInitializer<Channel> {

	private static final TrustManager[] INSECURE_TRUST_MANAGER = new TrustManager[]{new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}
	}
	};

	private final HttpResponseCallback callback;
	private final boolean ssl;
	private final String host;
	private final int port;

	public HttpInitializer(HttpResponseCallback callback, boolean ssl, String host, int port) {
		this.callback = callback;
		this.ssl = ssl;
		this.host = host;
		this.port = port;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline().addLast("timeout", new ReadTimeoutHandler(HttpClient.TIMEOUT, TimeUnit.MILLISECONDS));
		if (ssl) {
			SSLEngine engine;
			try {
				Class<?> sslContextClass = Class.forName("io.netty.handler.ssl.SslContext");
				Object clientContext = sslContextClass.getMethod("newClientContext").invoke(null);
				engine = (SSLEngine) clientContext.getClass().getMethod("newEngine", ByteBufAllocator.class, String.class, int.class).invoke(clientContext, ch.alloc(), host, port);
			} catch (Exception ignored) {
				engine = createInsecureSSLEngine();
			}
			ch.pipeline().addLast("ssl", new SslHandler(engine));
		}
		ch.pipeline().addLast("http", new HttpClientCodec());
		ch.pipeline().addLast("handler", new HttpHandler(callback));
	}

	private SSLEngine createInsecureSSLEngine() throws KeyManagementException, NoSuchAlgorithmException {
		SSLContext context = SSLContext.getInstance("SSL");
		context.init(null, INSECURE_TRUST_MANAGER, new SecureRandom());
		SSLEngine engine = context.createSSLEngine();
		engine.setUseClientMode(true);
		return engine;
	}
}

