package com.xxl.sayhello.asm;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

public class VipCheckTransform extends Transform {

    private static final String ANNOTATION_DESC = "Lcom/xxl/sayhello/annotations/VipCheck;";
    private static final String VIP_HELPER = "com/xxl/sayhello/utils/VipHelper";

    @Override
    public String getName() {
        return "VipCheckTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        outputProvider.deleteAll();

        for (TransformInput input : transformInvocation.getInputs()) {
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File srcDir = directoryInput.getFile();
                File destDir = outputProvider.getContentLocation(
                        directoryInput.getName(),
                        directoryInput.getContentTypes(),
                        directoryInput.getScopes(),
                        Format.DIRECTORY
                );
                if (srcDir.isDirectory()) {
                    processDirectory(srcDir, srcDir, destDir);
                }
            }
            for (JarInput jarInput : input.getJarInputs()) {
                File destJar = outputProvider.getContentLocation(
                        jarInput.getName(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR
                );
                copyFile(jarInput.getFile(), destJar);
            }
        }
    }

    private void processDirectory(File srcRoot, File srcDir, File destDir) throws IOException {
        if (!destDir.exists()) destDir.mkdirs();
        File[] files = srcDir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                processDirectory(srcRoot, file, new File(destDir, file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = getRelativePath(srcRoot, file).replace(File.separatorChar, '/');
                if (className.startsWith("com/xxl/sayhello/utils/VipHelper")) {
                    copyFileTo(file, destDir, getRelativePath(srcRoot, file));
                } else {
                    byte[] result = transformClass(file);
                    File destFile = new File(destDir, getRelativePath(srcRoot, file));
                    destFile.getParentFile().mkdirs();
                    FileOutputStream fos = new FileOutputStream(destFile);
                    fos.write(result);
                    fos.close();
                }
            } else {
                copyFileTo(file, destDir, getRelativePath(srcRoot, file));
            }
        }
    }

    private byte[] transformClass(File classFile) throws IOException {
        FileInputStream fis = new FileInputStream(classFile);
        byte[] bytes = new byte[(int) classFile.length()];
        fis.read(bytes);
        fis.close();

        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        cr.accept(new org.objectweb.asm.ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor,
                                              String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM9, mv) {
                    private boolean hasAnnotation = false;

                    @Override
                    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                        if (ANNOTATION_DESC.equals(desc)) {
                            hasAnnotation = true;
                        }
                        return super.visitAnnotation(desc, visible);
                    }

                    @Override
                    public void visitCode() {
                        if (hasAnnotation) {
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, VIP_HELPER,
                                    "checkVip", "()Z", false);
                            Label label = new Label();
                            mv.visitJumpInsn(Opcodes.IFNE, label);
                            emitDefaultReturn(mv, descriptor);
                            mv.visitLabel(label);
                        }
                        super.visitCode();
                    }
                };
            }
        }, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

    private void emitDefaultReturn(MethodVisitor mv, String descriptor) {
        char returnType = descriptor.charAt(descriptor.indexOf(')') + 1);
        switch (returnType) {
            case 'V':
                mv.visitInsn(Opcodes.RETURN);
                break;
            case 'I':
            case 'Z':
            case 'B':
            case 'C':
            case 'S':
                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitInsn(Opcodes.IRETURN);
                break;
            case 'J':
                mv.visitInsn(Opcodes.LCONST_0);
                mv.visitInsn(Opcodes.LRETURN);
                break;
            case 'F':
                mv.visitInsn(Opcodes.FCONST_0);
                mv.visitInsn(Opcodes.FRETURN);
                break;
            case 'D':
                mv.visitInsn(Opcodes.DCONST_0);
                mv.visitInsn(Opcodes.DRETURN);
                break;
            default:
                mv.visitInsn(Opcodes.ACONST_NULL);
                mv.visitInsn(Opcodes.ARETURN);
                break;
        }
    }

    private String getRelativePath(File root, File file) {
        return root.toPath().relativize(file.toPath()).toString();
    }

    private void copyFileTo(File src, File destDir, String relativePath) throws IOException {
        File destFile = new File(destDir, relativePath);
        destFile.getParentFile().mkdirs();
        copyFile(src, destFile);
    }

    private void copyFile(File src, File dest) throws IOException {
        dest.getParentFile().mkdirs();
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dest);
        byte[] buffer = new byte[4096];
        int len;
        while ((len = fis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fis.close();
        fos.close();
    }
}
