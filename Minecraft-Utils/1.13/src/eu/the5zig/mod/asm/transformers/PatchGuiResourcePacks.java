package eu.the5zig.mod.asm.transformers;

import eu.the5zig.mod.asm.LogUtil;
import eu.the5zig.mod.asm.Names;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class PatchGuiResourcePacks implements IClassTransformer {

    @Override
    public byte[] transform(String s, String s1, byte[] bytes) {
        LogUtil.startClass("GuiResourcePacks (%s)", Names.guiResourcePacks.getName());

        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        ClassPatcher visitor = new ClassPatcher(writer);
        reader.accept(visitor, 0);
        LogUtil.endClass();
        return writer.toByteArray();
    }

    public class ClassPatcher extends ClassVisitor {

        private boolean patchedGuiClosedMethod = false;
        private boolean patchedKeyTypedMethod = false;
        private boolean patchedKeyPressedMethod = false;
        private boolean patchedMouseClickedMethod = false;

        public ClassPatcher(ClassVisitor visitor) {
            super(ASM5, visitor);
        }

        @Override
        public void visitEnd() {
            if (!patchedGuiClosedMethod) {
                LogUtil.startMethod(Names.guiClosed.getName() + " " + Names.guiClosed.getDesc());
                MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, Names.guiClosed.getName(), Names.guiClosed.getDesc(), null, null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiResourcePacksClosed", "()V", false);
                mv.visitInsn(RETURN);
                Label l2 = new Label();
                mv.visitLabel(l2);
                mv.visitLocalVariable("this", "L" + Names.guiResourcePacks.getName() + ";", null, l0, l2, 0);
                mv.visitMaxs(0, 1);
                mv.visitEnd();
            }
            if (!patchedKeyTypedMethod) {
                LogUtil.startMethod(Names.keyTyped.getName() + " " + Names.keyTyped.getDesc());
                MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, Names.keyTyped.getName(), "(CI)Z", null, null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitLineNumber(109, l0);
                mv.visitVarInsn(ILOAD, 1);
                mv.visitVarInsn(ILOAD, 2);
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiResourcePacksKey", "(CI)V", false);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLineNumber(110, l1);
                mv.visitInsn(ICONST_0);
                mv.visitInsn(IRETURN);
                Label l2 = new Label();
                mv.visitLabel(l2);
                mv.visitLocalVariable("this", "L" + Names.guiResourcePacks.getName() + ";", null, l0, l2, 0);
                mv.visitLocalVariable("c", "C", null, l0, l2, 1);
                mv.visitLocalVariable("i", "I", null, l0, l2, 2);
                mv.visitMaxs(2, 3);
                mv.visitEnd();
            }
            if (!patchedKeyPressedMethod) {
                LogUtil.startMethod(Names.keyPressed.getName() + " " + Names.keyPressed.getDesc());
                MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, Names.keyPressed.getName(), "(III)Z", null, null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitLineNumber(103, l0);
                mv.visitVarInsn(ILOAD, 1);
                mv.visitVarInsn(ILOAD, 2);
                mv.visitVarInsn(ILOAD, 3);
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiResourcePacksKey", "(III)V", false);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLineNumber(104, l1);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ILOAD, 1);
                mv.visitVarInsn(ILOAD, 2);
                mv.visitVarInsn(ILOAD, 3);
                mv.visitMethodInsn(INVOKESPECIAL, Names.guiScreen.getName(), Names.keyPressed.getName(), "(III)Z", false);
                mv.visitInsn(IRETURN);
                Label l2 = new Label();
                mv.visitLabel(l2);
                mv.visitLocalVariable("this", "L" + Names.guiResourcePacks.getName() + ";", null, l0, l2, 0);
                mv.visitLocalVariable("i", "I", null, l0, l2, 1);
                mv.visitLocalVariable("i1", "I", null, l0, l2, 2);
                mv.visitLocalVariable("i2", "I", null, l0, l2, 3);
                mv.visitMaxs(4, 4);
                mv.visitEnd();
            }
            if (!patchedMouseClickedMethod) {
                LogUtil.startMethod(Names.mouseClicked.getName() + " " + Names.mouseClicked.getDesc());
                MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, Names.mouseClicked.getName(), "(DDI)Z", null, null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(DLOAD, 1);
                mv.visitVarInsn(DLOAD, 3);
                mv.visitVarInsn(ILOAD, 5);
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiResourcePacksMouseClicked", "(DDI)V", false);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(DLOAD, 1);
                mv.visitVarInsn(DLOAD, 3);
                mv.visitVarInsn(ILOAD, 5);
                mv.visitMethodInsn(INVOKESPECIAL, Names.guiScreen.getName(), Names.mouseClicked.getName(), "(DDI)Z", false);
                mv.visitInsn(IRETURN);
                Label l2 = new Label();
                mv.visitLabel(l2);
                mv.visitLocalVariable("this", "L" + Names.guiResourcePacks.getName() + ";", null, l0, l2, 0);
                mv.visitLocalVariable("v", "D", null, l0, l2, 1);
                mv.visitLocalVariable("v1", "D", null, l0, l2, 3);
                mv.visitLocalVariable("i", "I", null, l0, l2, 5);
                mv.visitMaxs(6, 6);
                mv.visitEnd();
            }
            super.visitEnd();
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (Names.initGui.equals(name, desc)) {
                LogUtil.startMethod(Names.initGui.getName() + " " + Names.initGui.getDesc());
                return new PatchInitGui(cv.visitMethod(access, name, desc, signature, exceptions));
            }
            if (Names.drawScreen.equals(name, desc)) {
                LogUtil.startMethod(Names.drawScreen.getName() + " " + Names.drawScreen.getDesc());
                return new PatchDrawScreen(cv.visitMethod(access, name, desc, signature, exceptions));
            }
            if (Names.keyPressed.equals(name, desc)) {
                patchedKeyPressedMethod = true;
                LogUtil.startMethod(Names.keyPressed.getName() + " " + Names.keyPressed.getDesc());
                return new PatchKeyTyped(cv.visitMethod(access, name, desc, signature, exceptions));
            }
            if (Names.guiClosed.equals(name, desc)) {
                patchedGuiClosedMethod = true;
                LogUtil.startMethod(Names.guiClosed.getName() + " " + Names.guiClosed.getDesc());
                return new PatchGuiClosed(cv.visitMethod(access, name, desc, signature, exceptions));
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
        public void visitInsn(int opcode) {
            if (opcode == RETURN) {
                LogUtil.log("init");
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, Names.guiResourcePacks.getName(), "f", "L" + Names.resourcePackList.getName() + ";");
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, Names.guiResourcePacks.getName(), "g", "L" + Names.resourcePackList2.getName() + ";");
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiResourcePacksInit", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", false);
            }
            super.visitInsn(opcode);
        }

    }

    public class PatchGuiClosed extends MethodVisitor {

        public PatchGuiClosed(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == RETURN) {
                LogUtil.log("guiclosed");
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiResourcePacksClosed", "()V", false);
            }
            super.visitInsn(opcode);
        }

    }

    public class PatchKeyTyped extends MethodVisitor {

        public PatchKeyTyped(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            LogUtil.log("keytyped");
            mv.visitVarInsn(ILOAD, 1);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitVarInsn(ILOAD, 3);
            mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiResourcePacksKey", "(III)V", false);
        }

    }

    public class PatchDrawScreen extends MethodVisitor {

        public PatchDrawScreen(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == RETURN) {
                LogUtil.log("drawscreen");
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiResourcePacksDraw", "()V", false);
            }
            super.visitInsn(opcode);
        }

    }

}
