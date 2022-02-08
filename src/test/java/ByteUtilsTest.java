import be.ecornely.ByteUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsArrayContaining;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class ByteUtilsTest {

    @Test
    public void longBytesTest() {
        byte[] uint;
        uint = new byte[]{(byte) 0xfb, (byte) 0xc5, 0x00, 0x00};
        assertThat(ByteUtils.toLong(uint), equalTo(50683L));
        uint = new byte[]{(byte) 0xfb, (byte) 0xc5, 0x00, 0x01};
        assertThat(ByteUtils.toLong(uint), equalTo(16827899L));

        //TODO pass more bytes

        //TODO pass less bytes

    }

    @Test
    public void intBytesTest() {
        byte[] ushort;
        ushort = new byte[]{(byte) 0xfb, (byte) 0xc5};
        assertThat(ByteUtils.toInt(ushort), equalTo(50683));
        ushort = new byte[]{0x00, 0x01};
        assertThat(ByteUtils.toInt(ushort), equalTo(256));
        ushort = new byte[]{0x01, 0x00};
        assertThat(ByteUtils.toInt(ushort), equalTo(1));

        //TODO pass more bytes

        //TODO pass less bytes
    }

    @Test
    public void hexStringTest() {
        byte[] bytes;
        bytes = new byte[]{(byte) 0xfb, (byte) 0xc5};
        assertThat(ByteUtils.toHexString(bytes), equalTo("fbc5"));
        bytes = new byte[]{(byte) 0xfb, (byte) 0xc5, 0x00, 0x01};
        assertThat(ByteUtils.toHexString(bytes), equalTo("fbc50001"));
        bytes = new byte[]{(byte) 0xfb, (byte) 0xc5, 0x00, 0x01, 4, 8, 64, (byte)128};
        assertThat(ByteUtils.toHexString(bytes), equalTo("fbc5000104084080"));
    }

    @Test
    public void bitSetTest() {
        byte[] iv = {(byte)0xfc, (byte)0xdb, (byte)0x2e, (byte)0x21};

        assertThat(ByteUtils.toBinaryString((byte)0xfc), equalTo("11111100"));
        assertThat(ByteUtils.toBinaryString((byte)0xdb), equalTo("11011011"));
        assertThat(ByteUtils.toBinaryString((byte)0x2e), equalTo("00101110"));
        assertThat(ByteUtils.toBinaryString((byte)0x21), equalTo("00100001"));

        assertThat(ByteUtils.toBinaryString(iv), equalTo("11111100110110110010111000100001"));
    }

    @Test
    public void intToByte() {
        assertThat(ByteUtils.toHexString(ByteUtils.fromInt(56316, 2)), equalTo("fcdb"));
        assertThat(ByteUtils.toHexString(ByteUtils.fromInt(252, 2)), equalTo("fc00"));
        assertThat(ByteUtils.toHexString(ByteUtils.fromInt(252, 1)), equalTo("fc"));

        System.out.printf("0x%s%n", ByteUtils.toHexString(ByteUtils.fromInt(-3, 1)));
        System.out.printf("0x%x%n", (byte)252);
    }

    @Test
    public void fromBinaryString() {
        boolean[] booleans = ByteUtils.fromBinaryString("1000111100001001");
        boolean[] expected = new boolean[] {true, false, false, false, true, true, true, true, false, false, false, false, true, false, false, true};
        assertThat(booleans, Matchers.is(expected));

        booleans = ByteUtils.fromBinaryString("111100001001");
        expected = new boolean[] {true, true, true, true, false, false, false, false, true, false, false, true};
        assertThat(booleans, Matchers.is(expected));
    }

    @Test
    public void fromBits() {
        boolean[] bits = new boolean[] {true, false, false, false, true, true, true, true, false, false, false, false, true, false, false, true};
        byte[] bytes = ByteUtils.fromBits(bits);
        byte[] expected = new byte[] {(byte)0x8f, 0x09};
        assertThat(bytes, Matchers.is(expected));

        bits = new boolean[] {false, false, false, false, true, true, true, true, false, false, false, false, true, false, false, true};
        bytes = ByteUtils.fromBits(bits);
        expected = new byte[] {(byte)0x0f, 0x09};
        assertThat(bytes, Matchers.is(expected));

        bits = new boolean[] {true, true, true, true, false, false, false, false, true, false, false, true};
        bytes = ByteUtils.fromBits(bits);
        expected = new byte[] {(byte)0x0f, 0x09};
        assertThat(bytes, Matchers.is(expected));

        bits = new boolean[]{false, false, false, false, true, false, true};
        bytes = ByteUtils.fromBits(bits);
        expected = new byte[] {(byte)0x05};
        assertThat(bytes, Matchers.is(expected));
    }
}