package com.xxl.sayhello.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AndroidIdMethodVisitor extends MethodVisitor {

    private static final String SETTINGS_SECURE_OWNER = "android/provider/Settings$Secure";
    private static final String GET_STRING_NAME = "getString";
    private static final String GET_STRING_DESC = "(Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;";
    private static final String AOP_HELPER_OWNER = "com/xxl/sayhello/utils/AopHelper";

    public AndroidIdMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name,
                                 String descriptor, boolean isInterface) {
        if (opcode == Opcodes.INVOKESTATIC
                && SETTINGS_SECURE_OWNER.equals(owner)
                && GET_STRING_NAME.equals(name)
                && GET_STRING_DESC.equals(descriptor)) {
            super.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    AOP_HELPER_OWNER,
                    "getSecureString",
                    GET_STRING_DESC,
                    false
            );
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}
