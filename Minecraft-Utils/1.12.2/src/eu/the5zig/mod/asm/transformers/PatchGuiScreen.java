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
public class PatchGuiScreen implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("GuiScreen (%s)", Names.guiScreen.getName());

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
			if (Names.isCtrlKeyDown.equals(name, desc)) {
				LogUtil.startMethod(Names.isCtrlKeyDown.getName() + "(%s)", Names.isCtrlKeyDown.getDesc());
				return new PatchIsCtrlKeyDown(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.setWorldAndResolution.equals(name, desc)) {
				LogUtil.startMethod(Names.setWorldAndResolution.getName() + "(%s)", Names.setWorldAndResolution.getDesc());
				return new PatchSetWorldAndResolution(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.drawWorldBackground.equals(name, desc)) {
				LogUtil.startMethod(Names.drawWorldBackground.getName() + "(%s)", Names.drawWorldBackground.getDesc());
				return new PatchDrawWorldBackground(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchIsCtrlKeyDown extends MethodVisitor {

		public PatchIsCtrlKeyDown(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("isCtrlKeyDown");
			mv.visitFieldInsn(GETSTATIC, Names.minecraft.getName(), "a", "Z");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "isCtrlKeyDown", "(Z)Z", false);
			mv.visitInsn(IRETURN);
		}
	}

	public class PatchSetWorldAndResolution extends MethodVisitor {

		public PatchSetWorldAndResolution(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("setWorldAndResolution");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "setWorldAndResolution", "()V", false);
		}
	}

	public class PatchDrawWorldBackground extends MethodVisitor {

		public PatchDrawWorldBackground(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("drawWorldBackground");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "shouldDrawWorldBackground", "()Z", false);
			Label label = new Label();
			mv.visitJumpInsn(IFNE, label);
			mv.visitInsn(RETURN);
			mv.visitLabel(label);
			mv.visitFrame(F_SAME, 0, null, 0, null);
		}
	}

}
