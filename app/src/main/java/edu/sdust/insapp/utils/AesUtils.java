package edu.sdust.insapp.utils;

import android.text.TextUtils;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {
    private static final String ivParameter = "zhelixie16weimim";

    /*
     * 加密
     */
    public static String encrypt(String cleartext) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encrypt("zhelixie16weimim", cleartext);
            return new String(Base64.encode(result, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encrypt(String password, String clear) throws Exception {
        // 创建AES秘钥
        SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes("UTF-8"), "AES");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        //补齐密码
        int blockSize = cipher.getBlockSize();
        byte[] dataBytes = clear.getBytes("UTF-8");
        int plaintextLength = dataBytes.length;
        if(plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        //偏移
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes("UTF-8"));
        // 初始化加密器
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
        // 加密
        return cipher.doFinal(plaintext);
    }

    private byte[] decrypt(byte[] content, String password) throws Exception {
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES/CBC/PKCS5PADDING");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化解密器
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 解密
        return cipher.doFinal(content);
    }

}