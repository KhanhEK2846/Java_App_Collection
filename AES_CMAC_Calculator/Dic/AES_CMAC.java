package Dic;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

public class AES_CMAC {

    private static final int BLOCK = 16;
    private static final byte Rb = (byte) 0x87;

    public AES_CMAC() {
        // Constructor implementation
    }
    public String generateKey(String seedString, String hashString) throws Exception {
        // Key generation logic
        String returnValue = "";
        /*Check Length */
        if(precheck(seedString, hashString)){
            returnValue = cmac(hashString, seedString);
        }
        return returnValue;
    }

    private static boolean precheck(String seedString, String hashString){
        boolean returnValue = false;
        if (seedString.length() == 0 || hashString.length() == 0) {
            new PopUp("Seed and Hash must not be empty");
        } else if (seedString.length() < 32 || hashString.length() < 32) {
            new PopUp("Seed and Hash must be at least 16 Bytes long");
        //}else if ((!isValidHex(hashString)) || (!isValidHex(seedString))) {
        //  new PopUp("Invalid input");
        } else {
            returnValue = true;
        }
        return returnValue;
    }

    @SuppressWarnings("unused")
    private static boolean isValidHex(String hexString) {
        return hexString != null && hexString.matches("[0-9A-Fa-f]+");
        // "(0?[xX]?)([0-9a-fA-F][0-9a-fA-F]+)"
    }
    private static String cmac(String keyHex, String msgHex) throws Exception {
        byte[] key = hexToBytes(keyHex);
        byte[] msg = hexToBytes(msgHex);

        // L = AES_K(0^128)
        byte[] zero = new byte[BLOCK];
        byte[] L = aesEncryptBlock(key, zero);

        // K1, K2
        byte[] K1 = generateSubkey(L);
        byte[] K2 = generateSubkey(K1);

        //System.out.println(" L  = " + bytesToHex(L));
        //System.out.println(" K1 = " + bytesToHex(K1));
        //System.out.println(" K2 = " + bytesToHex(K2));

        // CMAC computation (single-block or multi-block)
        int n = (msg.length + BLOCK - 1) / BLOCK;
        if (n == 0) n = 1;
        boolean lastBlockComplete = (msg.length != 0) && (msg.length % BLOCK == 0);

        byte[] M_last = new byte[BLOCK];
        if (lastBlockComplete) {
            byte[] lastBlk = Arrays.copyOfRange(msg, (n - 1) * BLOCK, (n) * BLOCK);
            M_last = xor(lastBlk, K1);
            //System.out.println(" last block (full)   = " + bytesToHex(lastBlk));
        } else {
            byte[] padded = new byte[BLOCK];
            int lastLen = msg.length % BLOCK;
            if (lastLen > 0) System.arraycopy(msg, (n - 1) * BLOCK, padded, 0, lastLen);
            padded[lastLen] = (byte) 0x80;
            M_last = xor(padded, K2);
            //System.out.println(" last block (padded) = " + bytesToHex(padded));
        }
        //System.out.println(" M_last = " + bytesToHex(M_last));

        // CBC-MAC
        byte[] X = new byte[BLOCK]; // 0^128
        byte[] Y = new byte[BLOCK];
        for (int i = 0; i < n - 1; i++) {
            byte[] blk = Arrays.copyOfRange(msg, i * BLOCK, i * BLOCK + BLOCK);
            Y = xor(X, blk);
            X = aesEncryptBlock(key, Y);
        }
        Y = xor(X, M_last);
        byte[] T = aesEncryptBlock(key, Y);
        //System.out.println(" CMAC  = " + bytesToHex(T));

        return bytesToHex(T);
    }

    // ---------- helpers ----------
    private static byte[] generateSubkey(byte[] input) {
        byte[] out = new byte[BLOCK];
        int carry = 0;
        for (int i = BLOCK - 1; i >= 0; i--) {
            int v = input[i] & 0xFF;
            int shifted = ((v << 1) & 0xFF) | carry;
            out[i] = (byte) shifted;
            carry = (v >>> 7) & 0x01;
        }
        if (carry == 1) {
            out[BLOCK - 1] ^= Rb;
        }
        return out;
    }

    private static byte[] aesEncryptBlock(byte[] key, byte[] block) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        SecretKeySpec ks = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, ks);
        return cipher.doFinal(block);
    }

    private static byte[] xor(byte[] a, byte[] b) {
        byte[] out = new byte[BLOCK];
        for (int i = 0; i < BLOCK; i++) {
            int va = i < a.length ? (a[i] & 0xFF) : 0;
            int vb = i < b.length ? (b[i] & 0xFF) : 0;
            out[i] = (byte) (va ^ vb);
        }
        return out;
    }

    private static byte[] hexToBytes(String s) {
        String hex = s.replaceAll("(?i)0x", "").replaceAll("[\\s_]", "");
        if (hex.length() == 0) return new byte[0];
        if ((hex.length() & 1) != 0) throw new IllegalArgumentException("hex string must be even-length");
        byte[] out = new byte[hex.length() / 2];
        for (int i = 0; i < out.length; i++) {
            int hi = Character.digit(hex.charAt(2 * i), 16);
            int lo = Character.digit(hex.charAt(2 * i + 1), 16);
            if (hi < 0 || lo < 0) throw new IllegalArgumentException("Invalid hex");
            out[i] = (byte) ((hi << 4) | lo);
        }
        return out;
    }

    private static String bytesToHex(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte x : b) sb.append(String.format("%02x", x & 0xFF));
        return sb.toString();
    }
}
