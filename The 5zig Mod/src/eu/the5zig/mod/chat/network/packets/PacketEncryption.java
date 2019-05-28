package eu.the5zig.mod.chat.network.packets;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.CryptManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.UUID;

public class PacketEncryption implements Packet {

	private PublicKey publicKey;
	private byte[] verifyToken;

	private byte[] secretKeyEncrypted;
	private byte[] verifyTokenEncrypted;

	public PacketEncryption(SecretKey secretKey, PublicKey publicKey, byte[] verifyToken) {
		this.secretKeyEncrypted = CryptManager.encryptData(publicKey, secretKey.getEncoded());
		this.verifyTokenEncrypted = CryptManager.encryptData(publicKey, verifyToken);
	}

	public PacketEncryption() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.publicKey = CryptManager.decodePublicKey(PacketBuffer.readByteArray(buffer));
		this.verifyToken = PacketBuffer.readByteArray(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeByteArray(buffer, secretKeyEncrypted);
		PacketBuffer.writeByteArray(buffer, verifyTokenEncrypted);
	}

	@Override
	public void handle() {
		final SecretKey secretKey = CryptManager.createNewSharedKey();
		String hash = (new BigInteger(CryptManager.getServerIdHash("", publicKey, secretKey))).toString(16);
		MinecraftSessionService yggdrasil = new YggdrasilAuthenticationService(The5zigMod.getVars().getProxy(), UUID.randomUUID().toString()).createMinecraftSessionService();
		try {
			yggdrasil.joinServer(The5zigMod.getVars().getGameProfile(), The5zigMod.getDataManager().getSession(), hash);
		} catch (AuthenticationException e) {
			The5zigMod.getNetworkManager().disconnect(I18n.translate("connection.bad_login"));
			throw new RuntimeException(e);
		}
		The5zigMod.getNetworkManager().sendPacket(new PacketEncryption(secretKey, publicKey, verifyToken), new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				The5zigMod.getNetworkManager().enableEncryption(secretKey);
			}
		});
	}
}
