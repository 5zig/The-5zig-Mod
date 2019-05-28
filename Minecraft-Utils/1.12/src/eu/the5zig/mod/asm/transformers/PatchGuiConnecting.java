package eu.the5zig.mod.asm.transformers;

import eu.the5zig.mod.asm.LogUtil;
import eu.the5zig.mod.asm.Names;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class PatchGuiConnecting implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("GuiConnecting (%s)", Names.guiConnecting.getName());

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
			if (Names.guiConnectingInit1.equals(name, desc)) {
				LogUtil.startMethod(Names.guiConnectingInit1.getName() + " " + Names.guiConnectingInit1.getDesc());
				return new PatchInitGui1(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.guiConnectingInit2.equals(name, desc)) {
				LogUtil.startMethod(Names.guiConnectingInit2.getName() + " " + Names.guiConnectingInit2.getDesc());
				return new PatchInitGui2(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}

	public class PatchInitGui1 extends MethodVisitor {

		public PatchInitGui1(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				LogUtil.log("init1");
				mv.visitVarInsn(ALOAD, 3);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiConnecting", "(Ljava/lang/Object;)V", false);
			}
			super.visitInsn(opcode);
		}

	}

	public class PatchInitGui2 extends MethodVisitor {

		public PatchInitGui2(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				LogUtil.log("init2");
				mv.visitTypeInsn(NEW, Names.serverData.getName());
				mv.visitInsn(DUP);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
				mv.visitInsn(DUP);
				mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
				mv.visitLdcInsn(":");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
				mv.visitVarInsn(ILOAD, 4);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
				mv.visitInsn(ICONST_0);
				mv.visitMethodInsn(INVOKESPECIAL, Names.serverData.getName(), "<init>", "(Ljava/lang/String;Ljava/lang/String;Z)V", false);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiConnecting", "(Ljava/lang/Object;)V", false);
			}
			super.visitInsn(opcode);
		}

	}

}
