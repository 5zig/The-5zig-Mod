package eu.the5zig.mod.asm.transformers;

import eu.the5zig.mod.asm.LogUtil;
import eu.the5zig.mod.asm.Names;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class PatchGuiChat implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("GuiChat (%s)", Names.guiChat.getName());

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
			if (Names.handleMouseInput.equals(name, desc)) {
				LogUtil.startMethod(Names.handleMouseInput.getName() + " " + Names.handleMouseInput.getDesc());
				return new PatchHandleMouseInput(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.mouseClicked.equals(name, desc)) {
				LogUtil.startMethod(Names.mouseClicked.getName() + " " + Names.mouseClicked.getDesc());
				return new PatchMouseClicked(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.guiClosed.equals(name, desc)) {
				LogUtil.startMethod(Names.guiClosed.getName() + " " + Names.guiClosed.getDesc());
				return new PatchGuiClosed(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.keyTyped.equals(name, desc)) {
				LogUtil.startMethod(Names.keyTyped.getName() + " " + Names.keyTyped.getDesc());
				return new PatchKeyTyped(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.drawScreen.equals(name, desc)) {
				LogUtil.startMethod(Names.drawScreen.getName() + " " + Names.drawScreen.getDesc());
				return new PatchDrawScreen(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}

	public class PatchHandleMouseInput extends MethodVisitor {

		public PatchHandleMouseInput(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
			if (Names.guiChatNew.getName().equals(owner)) {
				LogUtil.log("handleMouseInput");
				mv.visitVarInsn(ILOAD, 1);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onChatMouseInput", "(I)V", false);
			}
		}
	}

	public class PatchMouseClicked extends MethodVisitor {

		public PatchMouseClicked(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("mouseClicked");
			mv.visitVarInsn(DLOAD, 1);
			mv.visitVarInsn(DLOAD, 3);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onChatMouseClicked", "(DDI)Z", false);
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
		}
	}

	public class PatchGuiClosed extends MethodVisitor {

		public PatchGuiClosed(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("guiClosed");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onChatClosed", "()V", false);
		}
	}

	public class PatchKeyTyped extends MethodVisitor {

		public PatchKeyTyped(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("keyTyped");
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onChatKeyTyped", "(I)V", false);
		}
	}

	public class PatchDrawScreen extends MethodVisitor {

		public PatchDrawScreen(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("drawScreen");
			mv.visitIntInsn(ILOAD, 1);
			mv.visitIntInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onChatDrawScreen", "(II)V", false);
		}
	}
}
