package eu.the5zig.mod.asm.transformers;

import eu.the5zig.mod.asm.LogUtil;
import eu.the5zig.mod.asm.Names;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PatchGuiOptions implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("GuiOptions (%s)", Names.guiOptions.getName());

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
			if (Names.initGui.equals(name, desc)) {
				LogUtil.startMethod("InitGui " + Names.initGui.getName() + "(%s)", Names.initGui.getDesc());
				return new PatchInitGui(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.actionPerformed.equals(name, desc)) {
				LogUtil.startMethod("actionPerformed " + Names.actionPerformed.getName() + "(%s)", Names.actionPerformed.getDesc());
				return new PatchActionPerformed(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchInitGui extends MethodVisitor {

		public PatchInitGui(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			LogUtil.log("Adding button");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "get5zigOptionButton", "(Ljava/lang/Object;)Leu/the5zig/mod/gui/elements/IButton;", false);
			mv.visitTypeInsn(CHECKCAST, Names.guiButton.getName());
			mv.visitMethodInsn(INVOKEVIRTUAL, Names.guiScreen.getName(), "a", "(L" + Names.guiButton.getName() + ";)L" + Names.guiButton.getName() + ";", false);
			mv.visitInsn(POP);
			super.visitCode();
		}
	}

	public class PatchActionPerformed extends MethodVisitor {

		public PatchActionPerformed(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			LogUtil.log("action performed proxy");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onOptionsActionPerformed", "(Ljava/lang/Object;Ljava/lang/Object;)V", false);
			super.visitCode();
		}
	}

}
