package be.ecornely;

import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.BitSet;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class ByteUtils {
    public static int toInt(byte[] unsignedShortBytes) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).put(unsignedShortBytes).rewind().getInt();
    }

    public static byte[] toUnsignedShortBytes(int value) {
        byte[] buffer = new byte[2];
        ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).rewind().get(buffer);
        return buffer;
    }

    public static String toHexString(byte[] b) {
        return new String(Hex.encodeHex(b));
    }

    public static boolean[] toBits(byte[] bytes){
        char[] chars = toBinaryString(bytes).toCharArray();
        boolean[] bits = new boolean[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bits[i] = chars[i] == '1';
        }
        return bits;
    }

    public static String toBinaryString(byte b) {
        final StringBuilder result = new StringBuilder();
        for (int i=0; i<8; i++) {
            result.append((int)(b >> (8-(i+1)) & 0x0001));
        }
        return result.toString();
    }

    public static String toBinaryString(boolean[] bits){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bits.length; i++) {
            sb.append(bits[i] ? '1':'0');
        }
        return sb.toString();
    }

    public static long toLong(byte[] unsignedIntBytes){
        return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).put(unsignedIntBytes).rewind().getLong();
    }

    public static String toBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(toBinaryString(bytes[i]));
        }
        return sb.toString();
    }

    public static byte[] fromInt(int value, int size) {
        return Arrays.copyOfRange(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array(), 0, size);
    }

    public static byte[] fromLong(long value, int size) {
        return Arrays.copyOfRange(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(value).array(), 0, size);
    }

    public static String toBinaryString(BitSet bits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bits.length(); i++) {
            sb.append(bits.get(i) ? '1': '0');
        }
        return sb.toString();
    }

    public static boolean[] fromBinaryString(String s) {
        boolean[] bits = new boolean[s.length()];
        for (int i = 0; i <s.length(); i++) {
            bits[i] = s.charAt(i) == '1' ? true : false;
        }
        return bits;
    }

    public static byte[] fromBits(boolean[] bits) {
        int byteCount = (int) (bits.length / 8);
        int missingBits = 0;
        if (bits.length % 8 != 0){
            missingBits = 8 - (bits.length % 8);
        }
        if(missingBits>0) byteCount++;
        byte[] bytes = new byte[byteCount];
        boolean[] completeBits = new boolean[bits.length + missingBits];
        Arrays.fill(completeBits, false);
        for (int i = 0; i < bits.length; i++) {
            completeBits[missingBits+i] = bits[i];
        }
        for (int i = 0; i < byteCount; i++) {
            String byteString = ByteUtils.toBinaryString(Arrays.copyOfRange(completeBits, i * 8, (i * 8) + 8));
            byte b = (byte) Integer.parseInt(byteString, 2);
            bytes[i] = b;
        }
        return bytes;
    }

    public static byte[] fromHexString(String s) {
        byte[] bytes = new byte[s.length()/2];
        for (int i = 0; i < bytes.length; i++) {
            String byteString = s.substring(i*2, i*2+2);
            byte b = (byte) Integer.parseInt(byteString, 16);
            bytes[i] = b;
        }
        return bytes;
    }
}
