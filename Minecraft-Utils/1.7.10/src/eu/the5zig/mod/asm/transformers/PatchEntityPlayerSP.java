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
public class PatchEntityPlayerSP implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("EntityPlayerSP (%s)", Names.entityPlayerSP.getName());

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
			if (Names.getFOVModifier.equals(name, desc)) {
				LogUtil.startMethod(Names.getFOVModifier.getName() + " " + Names.getFOVModifier.getDesc());
				return new PatchGetFOVModifier(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchGetFOVModifier extends MethodVisitor {

		public PatchGetFOVModifier(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("getFOVModifier");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "isStaticFOV", "()Z", false);
			Label label = new Label();
			mv.visitJumpInsn(IFEQ, label);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "getCustomFOVModifier", "(Ljava/lang/Object;)F", false);
			mv.visitInsn(FRETURN);
			mv.visitLabel(label);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		}

	}

}
