package eu.the5zig.mod.asm.transformers;

import eu.the5zig.mod.asm.LogUtil;
import eu.the5zig.mod.asm.Names;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class PatchGuiMultiplayer implements IClassTransformer {

    @Override
    public byte[] transform(String s, String s1, byte[] bytes) {
        LogUtil.startClass("GuiMultiplayer (%s)", Names.guiMultiplayer.getName());

        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        ClassPatcher visitor = new ClassPatcher(writer);
        reader.accept(visitor, 0);
        LogUtil.endClass();
        return writer.toByteArray();
    }

    public class ClassPatcher extends ClassVisitor {

        private boolean patchedGuiClosedMethod = false;
        private boolean patchedKeyTypedMethod = false;

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
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiMultiplayerClosed", "()V", false);
                mv.visitInsn(RETURN);
                Label l2 = new Label();
                mv.visitLabel(l2);
                mv.visitLocalVariable("this", "L" + Names.guiMultiplayer.getName() + ";", null, l0, l2, 0);
                mv.visitMaxs(0, 1);
                mv.visitEnd();
            }
            if (!patchedKeyTypedMethod) {
                LogUtil.startMethod(Names.keyTyped.getName() + " " + Names.keyTyped.getDesc());
                MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, Names.keyTyped.getName(), Names.keyTyped.getDesc(), null, null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitLineNumber(148, l0);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ILOAD, 1);
                mv.visitVarInsn(ILOAD, 2);
                mv.visitMethodInsn(INVOKESPECIAL, Names.guiScreen.getName(), Names.keyTyped.getName(), Names.keyTyped.getDesc(), false);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLineNumber(149, l1);
                mv.visitVarInsn(ILOAD, 1);
                mv.visitVarInsn(ILOAD, 2);
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiMultiplayerKey", "(CI)V", false);
                Label l2 = new Label();
                mv.visitLabel(l2);
                mv.visitLineNumber(150, l2);
                mv.visitInsn(RETURN);
                Label l3 = new Label();
                mv.visitLabel(l3);
                mv.visitLocalVariable("this", "L" + Names.guiMultiplayer.getName() + ";", null, l0, l3, 0);
                mv.visitLocalVariable("c", "C", null, l0, l3, 1);
                mv.visitLocalVariable("i", "I", null, l0, l3, 2);
                mv.visitMaxs(3, 3);
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
            if (Names.guiClosed.equals(name, desc)) {
                patchedGuiClosedMethod = true;
                LogUtil.startMethod(Names.guiClosed.getName() + " " + Names.guiClosed.getDesc());
                return new PatchGuiClosed(cv.visitMethod(access, name, desc, signature, exceptions));
            }
            if (Names.drawScreen.equals(name, desc)) {
                LogUtil.startMethod(Names.drawScreen.getName() + " " + Names.drawScreen.getDesc());
                return new PatchDrawScreen(cv.visitMethod(access, name, desc, signature, exceptions));
            }
            if (Names.keyTyped.equals(name, desc)) {
                patchedKeyTypedMethod = true;
                LogUtil.startMethod(Names.keyTyped.getName() + " " + Names.keyTyped.getDesc());
                return new PatchKeyTyped(cv.visitMethod(access, name, desc, signature, exceptions));
            }
            if (Names.mouseClicked.equals(name, desc)) {
                LogUtil.startMethod(Names.mouseClicked.getName() + " " + Names.mouseClicked.getDesc());
                return new PatchMouseClicked(cv.visitMethod(access, name, desc, signature, exceptions));
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
                mv.visitFieldInsn(GETFIELD, Names.guiMultiplayer.getName(), "h", "L" + Names.serverSelectionList.getName() + ";");
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiMultiplayerInit", "(Ljava/lang/Object;Ljava/lang/Object;)V", false);
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
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiMultiplayerClosed", "()V", false);
            }
            super.visitInsn(opcode);
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
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiMultiplayerDraw", "()V", false);
            }
            super.visitInsn(opcode);
        }

    }

    public class PatchKeyTyped extends MethodVisitor {

        public PatchKeyTyped(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == RETURN) {
                LogUtil.log("keytyped");
                mv.visitVarInsn(ILOAD, 1);
                mv.visitVarInsn(ILOAD, 2);
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiMultiplayerKey", "(CI)V", false);
            }
            super.visitInsn(opcode);
        }

    }

    public class PatchMouseClicked extends MethodVisitor {

        public PatchMouseClicked(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == RETURN) {
                LogUtil.log("mouse clicked");
                mv.visitVarInsn(ILOAD, 1);
                mv.visitVarInsn(ILOAD, 2);
                mv.visitVarInsn(ILOAD, 3);
                mv.visitMethodInsn(INVOKESTATIC, "BytecodeHook", "onGuiMultiplayerMouseClicked", "(III)V", false);
            }
            super.visitInsn(opcode);
        }

    }

}
