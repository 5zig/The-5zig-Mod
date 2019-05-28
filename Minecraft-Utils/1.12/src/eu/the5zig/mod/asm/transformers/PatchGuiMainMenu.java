package eu.the5zig.mod.asm.transformers;

import eu.the5zig.mod.asm.LogUtil;
import eu.the5zig.mod.asm.Names;
import eu.the5zig.mod.asm.Transformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class PatchGuiMainMenu implements IClassTransformer {

	public byte[] transform(String s, String arg, byte[] bytes) {
		LogUtil.startClass("GuiMainMenu (%s)", "ayb");

		ClassReader reader = new ClassReader(bytes);
		ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		ClassPatcher visitor = new ClassPatcher(writer);
		reader.accept(visitor, 0);
		LogUtil.endClass();
		return writer.toByteArray();
	}

	public class ClassPatcher extends ClassVisitor {

		public ClassPatcher(ClassVisitor visitor) {
			super(Opcodes.ASM5, visitor);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (Names.insertSingleMultiplayerButton.equals(name, desc)) {
				LogUtil.startMethod(Names.insertSingleMultiplayerButton.getName() + "(%s)", Names.insertSingleMultiplayerButton.getDesc());
				return new PatchInsertSingleMultiplayer(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.actionPerformed.equals(name, desc)) {
				LogUtil.startMethod(Names.actionPerformed.getName() + "(%s)", Names.actionPerformed.getDesc());
				return new PatchActionPerformed(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names._static.equals(name, desc) && access == ACC_STATIC) {
				LogUtil.startMethod(Names._static.getName() + "(%s)", Names._static.getDesc());
				return new PatchStatic(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			if (Names.drawScreen.equals(name, desc)) {
				LogUtil.startMethod(Names.drawScreen.getName() + "(%s)", Names.drawScreen.getDesc());
				return new PatchDrawScreen(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchInsertSingleMultiplayer extends MethodVisitor {

		public PatchInsertSingleMultiplayer(MethodVisitor visitor) {
			super(ASM5, visitor);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				LogUtil.log("Adding 'Last Server' Button... ");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ILOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitInsn(Transformer.FORGE ? ICONST_1 : ICONST_0);
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onInsertSingleMultiplayerButton", "(Ljava/lang/Object;IIZ)V", false);
			}
			super.visitInsn(opcode);
		}

	}

	public class PatchActionPerformed extends MethodVisitor {

		public PatchActionPerformed(MethodVisitor visitor) {
			super(ASM5, visitor);
		}

		@Override
		public void visitCode() {
			LogUtil.log("Adding Proxy access");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onMainActionPerformed", "(Ljava/lang/Object;)V", false);
			super.visitCode();
		}

	}

	public class PatchStatic extends MethodVisitor {

		public PatchStatic(MethodVisitor visitor) {
			super(ASM5, visitor);
		}

		@Override
		public void visitCode() {
			LogUtil.log("Adding The 5zig Mod");
			mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onMainStatic", "()V", false);
			super.visitCode();
		}
	}

	public class PatchDrawScreen extends MethodVisitor {

		public PatchDrawScreen(MethodVisitor visitor) {
			super(ASM5, visitor);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				LogUtil.log("Adding version");
				mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onMainDraw", "()V", false);
			}
			super.visitInsn(opcode);
		}

	}


}
