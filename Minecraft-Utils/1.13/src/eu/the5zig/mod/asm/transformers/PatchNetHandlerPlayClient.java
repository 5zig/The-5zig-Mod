package eu.the5zig.mod.asm.transformers;

import eu.the5zig.mod.asm.LogUtil;
import eu.the5zig.mod.asm.Names;
import eu.the5zig.util.minecraft.ChatColor;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PatchNetHandlerPlayClient implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("NetHandlerPlayClient (%s)", Names.netHandlerPlayClient.getName());

		ClassReader reader = new ClassReader(bytes);
		ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		ClassPatcher visitor = new ClassPatcher(writer);
		reader.accept(visitor, 0);
		LogUtil.endClass();
		return writer.toByteArray();
	}

	public class ClassPatcher extends ClassVisitor {

		public ClassPatcher(ClassVisitor visitor) {
			super(ASM5, visitor);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (Names.handleCustomPayload.equals(name, desc)) {
				LogUtil.startMethod(Names.handleCustomPayload.getName() + "(%s)", Names.handleCustomPayload.getDesc());
				return new PatchHandleCustomPayload(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.handlePacketPlayerListHeaderFooter.equals(name, desc)) {
				LogUtil.startMethod(Names.handlePacketPlayerListHeaderFooter.getName() + "(%s)", Names.handlePacketPlayerListHeaderFooter.getDesc());
				return new PatchHandlePlayerListHeaderFooter(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.handlePacketChat.equals(name, desc)) {
				LogUtil.startMethod(Names.handlePacketChat.getName() + "(%s)", Names.handlePacketChat.getDesc());
				return new PatchHandlePacketChat(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.handlePacketSetSlot.equals(name, desc)) {
				LogUtil.startMethod(Names.handlePacketSetSlot.getName() + "(%s)", Names.handlePacketSetSlot.getDesc());
				return new PatchHandlePacketSetSlot(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.handlePacketTitle.equals(name, desc)) {
				LogUtil.startMethod(Names.handlePacketTitle.getName() + "(%s)", Names.handlePacketTitle.getDesc());
				return new PatchHandlePacketTitle(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.handlePacketTeleport.equals(name, desc)) {
				LogUtil.startMethod(Names.handlePacketTeleport.getName() + "(%s)", Names.handlePacketTeleport.getDesc());
				return new PatchHandlePacketTeleport(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchHandleCustomPayload extends MethodVisitor {

		public PatchHandleCustomPayload(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				LogUtil.log("payload");
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetPayload.getName(), "b", "()L" + Names.resourceLocation.getName() + ";", false);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.resourceLocation.getName(), "toString", "()Ljava/lang/String;", false);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetPayload.getName(), "c", "()L" + Names.packetBuffer.getName() + ";", false);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "packetBufferToByteBuf", "(Ljava/lang/Object;)Lio/netty/buffer/ByteBuf;", false);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onCustomPayload", "(Ljava/lang/String;Lio/netty/buffer/ByteBuf;)V", false);
			}
			super.visitInsn(opcode);
		}
	}

	public class PatchHandlePlayerListHeaderFooter extends MethodVisitor {

		public PatchHandlePlayerListHeaderFooter(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				LogUtil.log("playerListHeaderFooter");
				mv.visitTypeInsn(NEW, "eu/the5zig/mod/util/TabList");
				mv.visitInsn(DUP);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetHeaderFooter.getName(), "b", "()L" + Names.chatComponent.getName() + ";", false);
				mv.visitMethodInsn(INVOKEINTERFACE, Names.chatComponent.getName(), "d", "()Ljava/lang/String;", true);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetHeaderFooter.getName(), "c", "()L" + Names.chatComponent.getName() + ";", false);
				mv.visitMethodInsn(INVOKEINTERFACE, Names.chatComponent.getName(), "d", "()Ljava/lang/String;", true);
				mv.visitMethodInsn(INVOKESPECIAL, "eu/the5zig/mod/util/TabList", "<init>", "(Ljava/lang/String;Ljava/lang/String;)V", false);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onPlayerListHeaderFooter", "(Leu/the5zig/mod/util/TabList;)V", false);
			}
			super.visitInsn(opcode);
		}
	}

	public class PatchHandlePacketChat extends MethodVisitor {

		private boolean patched = false;

		public PatchHandlePacketChat(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}


		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean ifc) {
			super.visitMethodInsn(opcode, owner, name, desc, ifc);
			if (opcode == INVOKESTATIC && !patched) {
				LogUtil.log("chat & action bar");
				patched = true;
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetChat.getName(), "d", "()L" + Names.packetChatAction.getName() + ";", false);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetChatAction.getName(), "ordinal", "()I", false);
				mv.visitInsn(ICONST_2);
				Label label = new Label();
				mv.visitJumpInsn(IF_ICMPNE, label);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetChat.getName(), "b", "()L" + Names.chatComponent.getName() + ";", false);
				mv.visitMethodInsn(INVOKEINTERFACE, Names.chatComponent.getName(), "d", "()Ljava/lang/String;", true);
				mv.visitLdcInsn(ChatColor.RESET.toString());
				mv.visitLdcInsn("");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "replace", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;", false);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onActionBar", "(Ljava/lang/String;)Z", false);
				mv.visitJumpInsn(IFEQ, label);
				mv.visitInsn(RETURN);
				mv.visitLabel(label);
				mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetChat.getName(), "b", "()L" + Names.chatComponent.getName() + ";", false);
				mv.visitMethodInsn(INVOKEINTERFACE, Names.chatComponent.getName(), "d", "()Ljava/lang/String;", true);
				mv.visitLdcInsn(ChatColor.RESET.toString());
				mv.visitLdcInsn("");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "replace", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;", false);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetChat.getName(), "b", "()L" + Names.chatComponent.getName() + ";", false);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onChat", "(Ljava/lang/String;Ljava/lang/Object;)Z", false);
				Label label2 = new Label();
				mv.visitJumpInsn(IFEQ, label2);
				mv.visitInsn(RETURN);
				mv.visitLabel(label2);
				mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			}
		}

	}

	public class PatchHandlePacketSetSlot extends MethodVisitor {

		private int count = 0;

		public PatchHandlePacketSetSlot(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
			if (opcode == INVOKEVIRTUAL && owner.equals(Names.openContainer.getName()) && name.equals("a") && desc.equals("(IL" + Names.itemStack.getName() + ";)V") && !itf) {
				if (count == 1) {
					LogUtil.log("handleInventorySetSlot at c=" + count);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetSetSlot.getName(), "c", "()I", false);
					mv.visitTypeInsn(NEW, "WrappedItemStack");
					mv.visitInsn(DUP);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetSetSlot.getName(), "d", "()L" + Names.itemStack.getName() + ";", false);
					mv.visitMethodInsn(INVOKESPECIAL, "WrappedItemStack", "<init>", "(L" + Names.itemStack.getName() + ";)V", false);
					mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onSetSlot", "(ILeu/the5zig/mod/gui/ingame/ItemStack;)V", false);
				}
				count++;
			}
		}
	}

	public class PatchHandlePacketTitle extends MethodVisitor {

		private int returnCount;

		public PatchHandlePacketTitle(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				returnCount++;
				if (returnCount == 1) {
					mv.visitVarInsn(ALOAD, 5);
					mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onActionBar", "(Ljava/lang/String;)Z", false);
				} else if (returnCount == 2) {
					mv.visitInsn(ACONST_NULL);
					mv.visitInsn(ACONST_NULL);
					mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onTitle", "(Ljava/lang/String;Ljava/lang/String;)V", false);
				} else if (returnCount == 3) {
					mv.visitVarInsn(ALOAD, 3);
					mv.visitVarInsn(ALOAD, 4);
					mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onTitle", "(Ljava/lang/String;Ljava/lang/String;)V", false);
				}
			}
			super.visitInsn(opcode);
		}
	}

	public class PatchHandlePacketTeleport extends MethodVisitor {

		public PatchHandlePacketTeleport(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				LogUtil.log("teleport");
				mv.visitVarInsn(DLOAD, 3);
				mv.visitVarInsn(DLOAD, 5);
				mv.visitVarInsn(DLOAD, 7);
				mv.visitVarInsn(FLOAD, 9);
				mv.visitVarInsn(FLOAD, 10);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onTeleport", "(DDDFF)V", false);
			}
			super.visitInsn(opcode);
		}

	}

}
