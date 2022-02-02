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
}
