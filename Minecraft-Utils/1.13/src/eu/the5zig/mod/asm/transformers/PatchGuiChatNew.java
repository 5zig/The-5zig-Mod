package eu.the5zig.mod.asm.transformers;

import eu.the5zig.mod.asm.LogUtil;
import eu.the5zig.mod.asm.Names;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class PatchGuiChatNew implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("GuiNewChat (%s)", Names.guiChatNew.getName());

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
			if (Names.setChatLine.equals(name, desc)) {
				LogUtil.startMethod(Names.setChatLine.getName() + " " + Names.setChatLine.getDesc());
				return new PatchSetChatLine(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.drawChat.equals(name, desc)) {
				LogUtil.startMethod(Names.drawChat.getName() + " " + Names.drawChat.getDesc());
				return new PatchDrawChat(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.clearChat.equals(name, desc)) {
				LogUtil.startMethod(Names.clearChat.getName() + " " + Names.clearChat.getDesc());
				return new PatchClearChat(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			// TODO
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}


	public class PatchSetChatLine extends MethodVisitor {

		public PatchSetChatLine(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("chat time");
			mv.visitVarInsn(ILOAD, 4);
			Label label = new Label();
			mv.visitJumpInsn(IFNE, label);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "isShowTimeBeforeChatMessage", "()Z", false);
			mv.visitJumpInsn(IFEQ, label);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "getChatComponentWithTime", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
			mv.visitTypeInsn(CHECKCAST, Names.chatComponent.getName());
			mv.visitVarInsn(ASTORE, 1);
			mv.visitLabel(label);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		}

		@Override
		public void visitIntInsn(int opcode, int number) {
			if (number == 100) {
				LogUtil.log("setChatLine");
				mv.visitMethodInsn(INVOKESTATIC, "eu/the5zig/mod/MinecraftFactory", "getClassProxyCallback", "()Leu/the5zig/mod/util/ClassProxyCallback;", false);
				mv.visitMethodInsn(INVOKEINTERFACE, "eu/the5zig/mod/util/ClassProxyCallback", "getMaxChatLines", "()I", true);
			} else {
				super.visitIntInsn(opcode, number);
			}
		}

	}

	public class PatchDrawChat extends MethodVisitor {

		private boolean wait = true;
		private boolean hasBiPush9 = false;
		private int previousVar = -1;
		private int previousVarVisited = 0;
		private boolean addMul = false;

		public PatchDrawChat(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean ifc) {
			super.visitMethodInsn(opcode, owner, name, desc, ifc);
			if (opcode == INVOKESTATIC && Names.guiChatNew.getName().equals(owner) && "a".equals(name) && "(IIIII)V".equals(desc) && !ifc && wait) {
				wait = false;
				LogUtil.log("highlightChatLine");
				mv.visitVarInsn(ALOAD, 12);
				mv.visitMethodInsn(INVOKEVIRTUAL, Names.guiChatLine.getName(), "a", "()L" + Names.chatComponent.getName() + ";", false);
				mv.visitInsn(ICONST_0);
				mv.visitVarInsn(ILOAD, 18);
				mv.visitIntInsn(BIPUSH, 9);
				mv.visitInsn(ISUB);
				mv.visitVarInsn(ILOAD, 16);
				mv.visitMethodInsn(INVOKESTATIC, "Gui2ndChat", "highlightChatLine", "(L" + Names.chatComponent.getName() + ";III)V", false);
			}
		}

		@Override
		public void visitIntInsn(int opcode, int i) {
			super.visitIntInsn(opcode, i);
			if (!hasBiPush9 && opcode == BIPUSH && i == 9 && previousVar == -1) {
				hasBiPush9 = true;
			}
		}

		@Override
		public void visitVarInsn(int opcode, int i) {
			super.visitVarInsn(opcode, i);
			if (hasBiPush9 && opcode == ISTORE) {
				previousVar = i;
				hasBiPush9 = false;
			}
			if (previousVar != -1 && opcode == ILOAD && i == previousVar && previousVarVisited++ == 1) {
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "getChatAlphaMultiplicator", "()I", false);
				addMul = true;
				previousVar = -1;
			} else if (addMul) {
				addMul = false;
				mv.visitInsn(IMUL);
			}
		}

	}

	public class PatchClearChat extends MethodVisitor {

		public PatchClearChat(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("clearChat");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onClearChat", "()V", false);
		}
	}

}
