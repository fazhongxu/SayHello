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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class AndroidIdTransform extends Transform {

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
            handleDirectoryInputs(input.getDirectoryInputs(), outputProvider);
            handleJarInputs(input.getJarInputs(), outputProvider);
        }
    }

    private void handleDirectoryInputs(Collection<DirectoryInput> directoryInputs, TransformOutputProvider outputProvider) throws IOException {
        for (DirectoryInput directoryInput : directoryInputs) {
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
    }

    private void processDirectory(File srcRoot, File srcDir, File destDir) throws IOException {
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        File[] files = srcDir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                File subDest = new File(destDir, file.getName());
                processDirectory(srcRoot, file, subDest);
            } else if (file.getName().endsWith(".class")) {
                byte[] transformed = transformClass(file, srcRoot, file);
                String relativePath = getRelativePath(srcRoot, file);
                File destFile = new File(destDir, relativePath);
                destFile.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(destFile);
                fos.write(transformed);
                fos.close();
            } else {
                String relativePath = getRelativePath(srcRoot, file);
                File destFile = new File(destDir, relativePath);
                destFile.getParentFile().mkdirs();
                copyFile(file, destFile);
            }
        }
    }

    private byte[] transformClass(File classFile, File srcRoot, File file) throws IOException {
        String className = getRelativePath(srcRoot, file).replace(File.separatorChar, '/');
        if (className.startsWith("com/xxl/sayhello/utils/AopHelper")) {
            FileInputStream fis = new FileInputStream(classFile);
            byte[] bytes = new byte[(int) classFile.length()];
            fis.read(bytes);
            fis.close();
            return bytes;
        }

        FileInputStream fis = new FileInputStream(classFile);
        byte[] bytes = new byte[(int) classFile.length()];
        fis.read(bytes);
        fis.close();

        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        AndroidIdClassVisitor classVisitor = new AndroidIdClassVisitor(classWriter);
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

    private void handleJarInputs(Collection<JarInput> jarInputs, TransformOutputProvider outputProvider) throws IOException {
        for (JarInput jarInput : jarInputs) {
            File srcJar = jarInput.getFile();
            File destJar = outputProvider.getContentLocation(
                    jarInput.getName(),
                    jarInput.getContentTypes(),
                    jarInput.getScopes(),
                    Format.JAR
            );
            copyFile(srcJar, destJar);
        }
    }

    private String getRelativePath(File root, File file) {
        return root.toPath().relativize(file.toPath()).toString();
    }

    private void copyFile(File src, File dest) throws IOException {
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
