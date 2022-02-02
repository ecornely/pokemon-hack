package be.ecornely;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MaskConverter {
    private final short mask;

    public MaskConverter(short mask) {
        this.mask = mask;
    }
    public MaskConverter(String mask) throws DecoderException {
        this.mask = hexStringToShort(mask);
    }

    private short hexStringToShort(String hex)  throws DecoderException{
        if(hex.length()!=4) throw new IllegalArgumentException();
        byte[] bytes = Hex.decodeHex(hex.toCharArray());
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).put(bytes).rewind().getShort();
    }

    public int toDecimal(String hex)  throws DecoderException{
        return toDecimal(hexStringToShort(hex), false);
    }
    public int toDecimal(short hex)  throws DecoderException{
        return toDecimal(hex, true);
    }
    public int toDecimal(short hex, boolean inverseByteOrder)  throws DecoderException{
        if(inverseByteOrder){
            hex = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(hex).rewind().order(ByteOrder.LITTLE_ENDIAN).getShort();
        }
        int value = this.mask ^ hex;
        return value;
    }

    public short toMaskedHexShort(int decimal){
        return (short) (this.mask ^ decimal);
    }

    public byte[] toMaskedBytes(int decimal){
        byte[] bytes = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(toMaskedHexShort(decimal)).array();
        return bytes;
    }
}
