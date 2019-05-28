package eu.the5zig.mod.asm.transformers;

import eu.the5zig.mod.asm.LogUtil;
import eu.the5zig.mod.asm.Names;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PatchGuiIngameMenu implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("GuiIngameMenu (%s)", Names.guiIngameMenu.getName());

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
			if (Names.actionPerformed.equals(name, desc)) {
				LogUtil.startMethod(Names.actionPerformed.getName() + " " + Names.actionPerformed.getDesc());
				return new PatchActionPerformed(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchActionPerformed extends MethodVisitor {

		public PatchActionPerformed(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("actionPerformed");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, Names.guiButton.getName(), "k", "I");
			mv.visitInsn(ICONST_1);
			Label label = new Label();
			mv.visitJumpInsn(IF_ICMPNE, label);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "isConfirmDisconnect", "(Ljava/lang/Object;)Z", false);
			mv.visitJumpInsn(IFEQ, label);
			mv.visitInsn(RETURN);
			mv.visitLabel(label);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

		}

	}

}
