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
public class PatchRenderItem implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("RenderItem (%s)", Names.renderItem.getName());

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
			if (Names.renderItemPerson.equals(name, desc)) {
				LogUtil.startMethod(Names.renderItemPerson.getName() + "(%s)", Names.renderItemPerson.getDesc());
				return new PatchRenderPerson(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.renderItemInventory.equals(name, desc)) {
				LogUtil.startMethod(Names.renderItemInventory.getName() + "(%s)", Names.renderItemInventory.getDesc());
				return new PatchRenderInventory(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchRenderPerson extends MethodVisitor {

		public PatchRenderPerson(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("renderItemPerson");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRenderItemPerson", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Z)Z", false);
			Label label = new Label();
			mv.visitJumpInsn(IFEQ, label);
			mv.visitInsn(RETURN);
			mv.visitLabel(label);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		}

	}

	public class PatchRenderInventory extends MethodVisitor {

		public PatchRenderInventory(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("renderItemInventory");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRenderItemInventory", "(Ljava/lang/Object;Ljava/lang/Object;II)Z", false);
			Label label = new Label();
			mv.visitJumpInsn(IFEQ, label);
			mv.visitInsn(RETURN);
			mv.visitLabel(label);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		}

	}

}
