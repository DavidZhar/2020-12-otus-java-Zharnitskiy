package ru.otus.asm;

import org.objectweb.asm.*;

import java.io.IOException;

public class ASMBuilder {

    public static <I, T extends I> I get(Class<T> clazz) throws Exception {

        ClassReader cr = new ClassReader(clazz.getName());
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM7, cw) {

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM7, methodVisitor) {
                    boolean isAnnotationPresent;

                    @Override
                    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                        if ("Lru/otus/Log;".equals(descriptor)) {
                            isAnnotationPresent = true;
                        }
                        return super.visitAnnotation(descriptor, visible);
                    }

                    @Override
                    public void visitCode() {
                        if (isAnnotationPresent) {
                            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                            mv.visitLdcInsn("My ASM LOGGING for method " + name + descriptor);
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                        }
                    }
                };
            }
        };

        cr.accept(cv, 0);

        byte[] changedClass = cw.toByteArray();
        Class<?> finalClass = new MyClassLoader().defineClass(clazz.getName(), changedClass);

        return (I) finalClass.newInstance();
    }

    private static class MyClassLoader extends ClassLoader {
        Class<?> defineClass(String className, byte[] bytecode) throws IOException {
            return super.defineClass(className, bytecode, 0, bytecode.length);
        }
    }
}
