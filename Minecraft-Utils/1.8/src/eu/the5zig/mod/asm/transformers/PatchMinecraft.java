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
public class PatchMinecraft implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("Minecraft (%s)", Names.minecraft.getName());

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
			if (Names.startGame.equals(name, desc)) {
				LogUtil.startMethod(Names.startGame.getName() + "(%s)", Names.startGame.getDesc());
				return new PatchStartGame(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.dispatchKeypresses.equals(name, desc)) {
				LogUtil.startMethod(Names.dispatchKeypresses.getName() + "(%s)", Names.dispatchKeypresses.getDesc());
				return new PatchDispatchKeypresses(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.shutdown.equals(name, desc)) {
				LogUtil.startMethod(Names.shutdown.getName() + "(%s)", Names.shutdown.getDesc());
				return new PatchShutdown(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.displayCrashReport.equals(name, desc)) {
				LogUtil.startMethod(Names.displayCrashReport.getName() + "(%s)", Names.displayCrashReport.getDesc());
				return new PatchDisplayCrashReport(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.tick.equals(name, desc)) {
				LogUtil.startMethod(Names.tick.getName() + "(%s)", Names.tick.getDesc());
				return new PatchTick(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.leftClickMouse.equals(name, desc)) {
				LogUtil.startMethod(Names.leftClickMouse.getName() + "(%s)", Names.leftClickMouse.getDesc());
				return new PatchLeftClickMouse(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.rightClickMouse.equals(name, desc)) {
				LogUtil.startMethod(Names.rightClickMouse.getName() + "(%s)", Names.rightClickMouse.getDesc());
				return new PatchRightClickMouse(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.displayScreen.equals(name, desc)) {
				LogUtil.startMethod(Names.displayScreen.getName() + "(%s)", Names.displayScreen.getDesc());
				return new PatchDisplayScreen(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchStartGame extends MethodVisitor {

		public PatchStartGame(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			LogUtil.log("add resource pack");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, Names.minecraft.getName(), Names.resourcePackList.getName(), "Ljava/util/List;");
			mv.visitTypeInsn(NEW, "The5zigModResourcePack");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "The5zigModResourcePack", "<init>", "()V", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
			mv.visitInsn(POP);
			super.visitCode();
		}
	}

	public class PatchDispatchKeypresses extends MethodVisitor {

		public PatchDispatchKeypresses(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("dispatchKeypresses");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onDispatchKeyPresses", "()V", false);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean ifc) {
			super.visitMethodInsn(opcode, owner, name, desc, ifc);
			if (opcode == INVOKESTATIC && "org/lwjgl/input/Keyboard".equals(owner) && "getEventCharacter".equals(name) && "()C".equals(desc) && !ifc) {
				LogUtil.log("fix keys");
				mv.visitIntInsn(SIPUSH, 256);
				mv.visitInsn(IADD);
			}
		}
	}

	public class PatchShutdown extends MethodVisitor {

		public PatchShutdown(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("shutdown");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onShutdown", "()V", false);
		}
	}

	public class PatchDisplayCrashReport extends MethodVisitor {

		private int count;

		public PatchDisplayCrashReport(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("append crash category");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "appendCrashCategory", "(Ljava/lang/Object;)V", false);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == ICONST_M1) {
				count++;
				if (count == 1) {
					LogUtil.log("display crash report #" + count);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitMethodInsn(INVOKEVIRTUAL, Names.crashReport.getName(), "b", "()Ljava/lang/Throwable;", false);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitMethodInsn(INVOKEVIRTUAL, Names.crashReport.getName(), "f", "()Ljava/io/File;", false);
					mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onDisplayCrashReport", "(Ljava/lang/Throwable;Ljava/io/File;)V", false);
				} else if (count == 2) {
					mv.visitVarInsn(ALOAD, 1);
					mv.visitMethodInsn(INVOKEVIRTUAL, Names.crashReport.getName(), "b", "()Ljava/lang/Throwable;", false);
					mv.visitVarInsn(ALOAD, 3);
					mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "getAbsoluteFile", "()Ljava/io/File;", false);
					mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onDisplayCrashReport", "(Ljava/lang/Throwable;Ljava/io/File;)V", false);
				}
			}
			super.visitInsn(opcode);
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
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRealTick", "()V", false);
		}

	}

	public class PatchLeftClickMouse extends MethodVisitor {

		public PatchLeftClickMouse(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("left click mouse");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onLeftClickMouse", "()V", false);
		}

	}

	public class PatchRightClickMouse extends MethodVisitor {

		public PatchRightClickMouse(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("right click mouse");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onRightClickMouse", "()V", false);
		}

	}

	public class PatchDisplayScreen extends MethodVisitor {

		public PatchDisplayScreen(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitCode() {
			super.visitCode();
			LogUtil.log("displayScreen");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onDisplayScreen", "()V", false);
		}

	}

}
