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

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

public class AndroidIdTransform extends Transform {

    private static final String SETTINGS_SECURE = "android/provider/Settings$Secure";
    private static final String GET_STRING_DESC = "(Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;";
    private static final String AOP_HELPER = "com/xxl/sayhello/utils/AopHelper";

    @Override
    public String getName() {
        return "AndroidIdTransform";
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
                if (className.startsWith("com/xxl/sayhello/utils/AopHelper")) {
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
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String mName,
                                                 String mDesc, boolean isInterface) {
                        if (opcode == Opcodes.INVOKESTATIC
                                && SETTINGS_SECURE.equals(owner)
                                && "getString".equals(mName)
                                && GET_STRING_DESC.equals(mDesc)) {
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, AOP_HELPER,
                                    "getSecureString", GET_STRING_DESC, false);
                        } else {
                            super.visitMethodInsn(opcode, owner, mName, mDesc, isInterface);
                        }
                    }
                };
            }
        }, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
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
