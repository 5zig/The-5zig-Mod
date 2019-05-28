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
public class PatchGuiIngameForge implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("GuiIngameForge (%s)", Names.guiIngameForge.getName());

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
			if (Names.renderGameOverlayForge.equals(name, desc)) {
				LogUtil.startMethod(Names.renderGameOverlayForge.getName() + " " + Names.renderGameOverlayForge.getDesc());
				return new PatchRenderGameOverlay(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.renderChatForge.equals(name, desc)) {
				LogUtil.startMethod(Names.renderChatForge.getName() + " " + Names.renderChatForge.getDesc());
				return new PatchRenderChat(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.renderHotbarForge.equals(name, desc)) {
				LogUtil.startMethod(Names.renderHotbarForge.getName() + " " + Names.renderHotbarForge.getDesc());
				return new PatchRenderHotbar(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.ingameTick.equals(name, desc)) {
				LogUtil.startMethod(Names.ingameTick.getName() + " " + Names.ingameTick.getDesc());
				return new PatchTick(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.renderFoodForge.equals(name, desc)) {
				LogUtil.startMethod(Names.renderFoodForge.getName() + " " + Names.renderFoodForge.getDesc());
				return new PatchFood(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchRenderGameOverlay extends MethodVisitor {

		public PatchRenderGameOverlay(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			LogUtil.log("rendering mod");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRenderGameOverlay", "()V", false);
			super.visitCode();
		}
	}

	public class PatchRenderChat extends MethodVisitor {

		public PatchRenderChat(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean ifc) {
			if (opcode == INVOKESTATIC && "org/lwjgl/opengl/GL11".equals(owner) && "glPopMatrix".equals(name) && "()V".equals(desc) && !ifc) {
				LogUtil.log("chat");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, Names.guiIngameForge.getName().replace('.', '/'), "n", "I");
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onDrawChat", "(I)V", false);
			}
			super.visitMethodInsn(opcode, owner, name, desc, ifc);
		}
	}

	public class PatchRenderHotbar extends MethodVisitor {

		public PatchRenderHotbar(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("renderHotbar");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRenderHotbar", "()V", false);
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
			mv.visitMethodInsn(INVOKESTATIC, "eu/the5zig/mod/The5zigMod", "getGuiIngame", "()Leu/the5zig/mod/render/GuiIngame;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "eu/the5zig/mod/render/GuiIngame", "tick", "()V", false);
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
			if (o instanceof String && "food".equals(o))
				hasVisitedLDC = true;
		}
	}

}
