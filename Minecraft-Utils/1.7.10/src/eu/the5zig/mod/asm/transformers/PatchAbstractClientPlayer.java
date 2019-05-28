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
public class PatchAbstractClientPlayer implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("AbstractClientPlayer (%s)", Names.abstractClientPlayer.getName());

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
			if (Names.abstractClientPlayerInit.equals(name, desc)) {
				LogUtil.startMethod(Names.abstractClientPlayerInit.getName() + " " + Names.abstractClientPlayerInit.getDesc());
				return new PatchInit(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.hasCape.equals(name, desc)) {
				LogUtil.startMethod(Names.hasCape.getName() + " " + Names.hasCape.getDesc());
				return new PatchHasCape(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.getResourceLocation.equals(name, desc)) {
				LogUtil.startMethod(Names.getResourceLocation.getName() + " " + Names.getResourceLocation.getDesc());
				return new PatchGetResourceLocation(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchInit extends MethodVisitor {

		public PatchInit(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				LogUtil.log("Init");
				mv.visitVarInsn(ALOAD, 2);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onAbstractClientPlayerInit", "(Lcom/mojang/authlib/GameProfile;)V", false);
			}
			super.visitInsn(opcode);
		}
	}

	public class PatchHasCape extends MethodVisitor {

		public PatchHasCape(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("hasCape");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "getCapeLocation", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNULL, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(28, l2);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(29, l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		}
	}

	public class PatchGetResourceLocation extends MethodVisitor {

		public PatchGetResourceLocation(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			LogUtil.log("get resource location");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "getCapeLocation", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNULL, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "getCapeLocation", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
			mv.visitTypeInsn(CHECKCAST, Names.resourceLocation.getName());
			mv.visitInsn(ARETURN);
			mv.visitLabel(l1);
			super.visitCode();
		}
	}

}
