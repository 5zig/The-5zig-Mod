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
public class PatchRendererLivingEntity implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("RendererLivingEntity (%s)", Names.rendererLivingEntity.getName());

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
			if (Names.canRenderName.equals(name, desc)) {
				LogUtil.startMethod(Names.canRenderName.getName() + "(%s)", Names.canRenderName.getDesc());
				return new PatchCanRenderName(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchCanRenderName extends MethodVisitor {

		public PatchCanRenderName(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();

			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, Names.minecraft.getName(), Names.getMinecraft.getName(), Names.getMinecraft.getDesc(), false);
			mv.visitFieldInsn(GETFIELD, Names.minecraft.getName(), Names.getEntityPlayer.getName(), Names.getEntityPlayer.getDesc());
			Label label = new Label();
			mv.visitJumpInsn(IF_ACMPNE, label);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "shouldRenderOwnName", "()Z", false);
			mv.visitJumpInsn(IFEQ, label);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(label);
		}
	}

}
