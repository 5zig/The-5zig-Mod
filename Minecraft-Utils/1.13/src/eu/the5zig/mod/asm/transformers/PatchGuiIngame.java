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
public class PatchGuiIngame implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("GuiIngame (%s)", Names.guiIngame.getName());

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
			if (Names.renderGameOverlay.equals(name, desc)) {
				LogUtil.startMethod(Names.renderGameOverlay.getName() + " " + Names.renderGameOverlay.getDesc());
				return new PatchRenderGameOverlay(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.renderPotionEffectIndicator.equals(name, desc)) {
				LogUtil.startMethod(Names.renderPotionEffectIndicator.getName() + " " + Names.renderPotionEffectIndicator.getDesc());
				return new PatchPotionEffectIndicator(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.renderHotbar.equals(name, desc)) {
				LogUtil.startMethod(Names.renderHotbar.getName() + " " + Names.renderHotbar.getDesc());
				return new PatchRenderHotbar(cv.visitMethod(access, name, desc, signature, exceptions));
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
		public void visitMethodInsn(int i, String s, String s1, String s2, boolean b) {
			super.visitMethodInsn(i, s, s1, s2, b);
			if (patchChat) {
				LogUtil.log("drawChat");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, Names.guiIngame.getName(), "m", "I");
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onDrawChat", "(I)V", false);
				patchChat = false;
			}
		}
	}

	public class PatchPotionEffectIndicator extends MethodVisitor {

		public PatchPotionEffectIndicator(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("renderPotionEffectIndicator");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRenderPotionEffectIndicator", "()Z", false);
			Label label = new Label();
			mv.visitJumpInsn(IFNE, label);
			mv.visitInsn(RETURN);
			mv.visitLabel(label);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		}
	}

	public class PatchRenderHotbar extends MethodVisitor {

		public PatchRenderHotbar(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			LogUtil.log("renderHotbar");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRenderHotbar", "()V", false);
			super.visitCode();
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
			if (opcode == INVOKESTATIC && owner.equals(Names.glStateManager.getName()) && Names.glColor.equals(name, desc) && !ifc) {
				count++;
				if (count == 2) {
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
			if (o instanceof String && "atb".equals(o))
				hasVisitedLDC = true;
		}
	}

}
