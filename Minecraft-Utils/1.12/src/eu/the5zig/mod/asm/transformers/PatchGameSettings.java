package eu.the5zig.mod.asm.transformers;

import eu.the5zig.mod.asm.LogUtil;
import eu.the5zig.mod.asm.Names;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM5;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PatchGameSettings implements IClassTransformer {

	@Override
	public byte[] transform(String s, String s1, byte[] bytes) {
		LogUtil.startClass("GameSettings (%s)", Names.gameSettings.getName());

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
			if (Names.getKeyBinding.equals(name, desc)) {
				LogUtil.startMethod(Names.getKeyBinding.getName() + "(%s)", Names.getKeyBinding.getDesc());
				return new PatchGetKeyBinding(cv.visitMethod(access, name, desc, signature, exceptions));
			}
			LogUtil.endMethod();
			return super.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	public class PatchGetKeyBinding extends MethodVisitor {

		private boolean found = false;

		public PatchGetKeyBinding(MethodVisitor methodVisitor) {
			super(ASM5, methodVisitor);
		}

		@Override
		public void visitLdcInsn(Object o) {
			if (o instanceof Float) {
				Float value = (Float) o;
				if (value == 100.0F && !found) {
					LogUtil.log("gamma");
					mv.visitLdcInsn(1000.0F);
					found = true;
					return;
				}
			}
			super.visitLdcInsn(o);
		}
	}

}
