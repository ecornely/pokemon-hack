package be.ecornely;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.Arrays;

public class Section {
    private static final Logger LOGGER = LoggerFactory.getLogger(Section.class);
    private final Path filePath;

    private byte[] bytes;

    private final byte[] saveIndexBytes;
    private final int saveIndex;

    private final byte[] signatureBytes;
    private final int signature;
    private final int index;

    private byte[] checksumBytes;
    private short checksum;

    private final byte[] sectionIdBytes;
    private final short sectionId;

    private final SectionType sectionType;


    public Section(byte[] bytes, SectionType sectionType, int index, Path filePath) {
        this.filePath = filePath;
        this.index = index;
        this.bytes = bytes;
        LOGGER.trace("Create section with {} bytes", bytes.length);
        LOGGER.trace("First bytes are {}", new String(Hex.encodeHex(Arrays.copyOfRange(bytes, 0, 12))));
        this.sectionType = sectionType;
        this.saveIndexBytes = Arrays.copyOfRange(bytes, 4092, 4096);
        this.saveIndex = ByteBuffer.wrap(saveIndexBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        this.signatureBytes = Arrays.copyOfRange(bytes, 4088, 4092);
        this.signature = ByteBuffer.wrap(signatureBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        this.checksumBytes = Arrays.copyOfRange(bytes, 4086, 4088);
        this.checksum = ByteBuffer.wrap(checksumBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        this.sectionIdBytes = Arrays.copyOfRange(bytes, 4084, 4086);
        this.sectionId = ByteBuffer.wrap(sectionIdBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        LOGGER.trace("Last bytes are {}", new String(Hex.encodeHex(Arrays.copyOfRange(bytes, 4080, 4096))));
    }

    public byte[] calculateChecksumBytes(){
        byte[] data = Arrays.copyOfRange(bytes, 0, 4084);
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < this.sectionType.dataSize(); i+=4) {
            byte[] bytesToSum = Arrays.copyOfRange(data, i, i+4);
            BigInteger summable = BigInteger.valueOf(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).put(bytesToSum).putInt(0).rewind().getLong());
            sum = sum.add(summable);
        }

        ByteBuffer bb = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(sum.intValue()).rewind();
        int chk = bb.getShort() + bb.getShort();
        return Arrays.copyOfRange(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(chk).array(), 0, 2);
    }

    public void updateChecksumBytes(){
        byte[] newChecksum = this.calculateChecksumBytes();
        this.bytes[4086] = newChecksum[0];
        this.bytes[4087] = newChecksum[1];
        this.checksumBytes = Arrays.copyOfRange(bytes, 4086, 4088);
        this.checksum = ByteBuffer.wrap(checksumBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public SectionType getSectionType() {
        return sectionType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
        this.updateChecksumBytes();
        this.filePath.toFile().renameTo(this.filePath.resolve(".bak").toFile());
        try(FileOutputStream fos = new FileOutputStream(this.filePath.toFile())){
            IOUtils.write(this.bytes, fos);
        } catch (IOException e) {
            LOGGER.error("Impossible to save bytes of section {} to file {}", this.sectionType, this.filePath);
        }
    }

    public byte[] getSaveIndexBytes() {
        return saveIndexBytes;
    }

    public int getSaveIndex() {
        return saveIndex;
    }

    public byte[] getSignatureBytes() {
        return signatureBytes;
    }

    public int getSignature() {
        return signature;
    }

    public byte[] getChecksumBytes() {
        return checksumBytes;
    }

    public short getChecksum() {
        return checksum;
    }

    public byte[] getSectionIdBytes() {
        return sectionIdBytes;
    }

    public short getSectionId() {
        return sectionId;
    }

    public int getIndex() {
        return index;
    }

}
