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
		ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
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
			if (Names.handlePacketChat.equals(name, desc)) {
				LogUtil.startMethod(Names.handlePacketChat.getName() + "(%s)", Names.handlePacketChat.getDesc());
				return new PatchHandlePacketChat(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.handlePacketSetSlot.equals(name, desc)) {
				LogUtil.startMethod(Names.handlePacketSetSlot.getName() + "(%s)", Names.handlePacketSetSlot.getDesc());
				return new PatchHandlePacketSetSlot(cv.visitMethod(access, name, desc, signature, exceptions));
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
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetPayload.getName(), "c", "()Ljava/lang/String;", false);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetPayload.getName(), "d", "()[B", false);
				mv.visitMethodInsn(INVOKESTATIC, "io/netty/buffer/Unpooled", "wrappedBuffer", "([B)Lio/netty/buffer/ByteBuf;", false);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onCustomPayload", "(Ljava/lang/String;Lio/netty/buffer/ByteBuf;)V", false);
			}
			super.visitInsn(opcode);
		}
	}

	public class PatchHandlePacketChat extends MethodVisitor {

		public PatchHandlePacketChat(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetChat.getName(), "c", "()L" + Names.chatComponent.getName() + ";", false);
			mv.visitMethodInsn(INVOKEINTERFACE, Names.chatComponent.getName(), "d", "()Ljava/lang/String;", true);
			mv.visitLdcInsn(ChatColor.RESET.toString());
			mv.visitLdcInsn("");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "replace", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetChat.getName(), "c", "()L" + Names.chatComponent.getName() + ";", false);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onChat", "(Ljava/lang/String;Ljava/lang/Object;)Z", false);
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(400, l2);
			mv.visitInsn(RETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(402, l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
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
					mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetSetSlot.getName(), "b", "()I", false);
					mv.visitTypeInsn(NEW, "WrappedItemStack");
					mv.visitInsn(DUP);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitMethodInsn(INVOKEVIRTUAL, Names.packetSetSlot.getName(), "c", "()L" + Names.itemStack.getName() + ";", false);
					mv.visitMethodInsn(INVOKESPECIAL, "WrappedItemStack", "<init>", "(L" + Names.itemStack.getName() + ";)V", false);
					mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onSetSlot", "(ILeu/the5zig/mod/gui/ingame/ItemStack;)V", false);
				}
				count++;
			}
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
