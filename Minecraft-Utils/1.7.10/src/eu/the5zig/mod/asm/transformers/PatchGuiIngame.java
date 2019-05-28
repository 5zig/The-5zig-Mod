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
public class PatchGuiIngame implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("GuiIngame (%s)", Names.guiIngame.getName());

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
			if (Names.renderGameOverlay.equals(name, desc)) {
				LogUtil.startMethod(Names.renderGameOverlay.getName() + " " + Names.renderGameOverlay.getDesc());
				return new PatchRenderGameOverlay(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.ingameTick.equals(name, desc)) {
				LogUtil.startMethod(Names.ingameTick.getName() + " " + Names.ingameTick.getDesc());
				return new PatchTick(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.renderVignette.equals(name, desc)) {
				LogUtil.startMethod(Names.renderVignette.getName() + " " + Names.renderVignette.getDesc());
				return new PatchVignette(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.renderFood.equals(name, desc)) {
				LogUtil.startMethod(Names.renderFood.getName() + " " + Names.renderFood.getDesc());
				return new PatchFood(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}

	public class PatchRenderGameOverlay extends MethodVisitor {

		private boolean patchChat = false;
		private int color4f = 0;

		public PatchRenderGameOverlay(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				LogUtil.log("renderGameOverlay");
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRenderGameOverlay", "()V", false);
			}
			super.visitInsn(opcode);
		}

		@Override
		public void visitLdcInsn(Object o) {
			super.visitLdcInsn(o);
			if ("chat".equals(o)) {
				patchChat = true;
			}
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean ifc) {
			if (patchChat) {
				super.visitMethodInsn(opcode, owner, name, desc, ifc);
				LogUtil.log("drawChat");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, Names.guiIngame.getName(), "n", "I");
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onDrawChat", "(I)V", false);
				patchChat = false;
				return;
			}
			if (opcode == INVOKESTATIC && "org/lwjgl/opengl/GL11".equals(owner) && "glColor4f".equals(name) && "(FFFF)V".equals(desc) && !ifc && color4f++ == 0) {
				LogUtil.log("renderHotbar");
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRenderHotbar", "()V", false);
			}
			super.visitMethodInsn(opcode, owner, name, desc, ifc);
		}
	}

	public class PatchTick extends MethodVisitor {

		public PatchTick(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("tick");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onIngameTick", "()V", false);
		}
	}

	public class PatchVignette extends MethodVisitor {

		private int count = 0;

		public PatchVignette(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean ifc) {
			super.visitMethodInsn(opcode, owner, name, desc, ifc);
			if (opcode == INVOKESTATIC && "org/lwjgl/opengl/GL11".equals(owner) && "glColor4f".equals(name) && "(FFFF)V".equals(desc) && !ifc) {
				count++;
				if (count == 1) {
					LogUtil.log("vignette");
					mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRenderVignette", "()V", false);
				}
			}
		}
	}

	public class PatchFood extends MethodVisitor {

		private boolean hasVisitedLDC = false;

		public PatchFood(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean ifc) {
			super.visitMethodInsn(opcode, owner, name, desc, ifc);
			if (hasVisitedLDC) {
				LogUtil.log("saturation");
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRenderFood", "()V", false);
				hasVisitedLDC = false;
			}
		}

		@Override
		public void visitLdcInsn(Object o) {
			super.visitLdcInsn(o);
			if (o instanceof String && "air".equals(o))
				hasVisitedLDC = true;
		}
	}

}
