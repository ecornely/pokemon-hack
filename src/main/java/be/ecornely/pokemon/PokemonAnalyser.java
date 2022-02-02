package be.ecornely.pokemon;

import be.ecornely.ByteUtils;
import be.ecornely.pokemon.data.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;

public class PokemonAnalyser {

    private static final Logger LOGGER = LoggerFactory.getLogger(PokemonAnalyser.class);

    private final byte[] bytes;
    private final byte[] personalityBytes;
    private final byte[] nicknameBytes;
    private final byte[] originalTrainerBytes;
    private final byte language;
    private final byte egg;
    private final byte marking;
    private final byte[] originalTrainerNameBytes;
    private final int encryptionKey;
    private final byte level;
    private byte[] checksumBytes;
    private byte[] encryptedDataBytes;
    private final byte[] decryptedDataBytes;
    private final byte[] statusConditionBytes;
    private final byte pokerus;
    private final byte[] currentHealthPointBytes;
    private final byte[] totalHealthPointBytes;
    private final byte[] attackBytes;
    private final byte[] defenseBytes;
    private final byte[] speedBytes;
    private final byte[] specialAttackBytes;
    private final byte[] specialDefenseBytes;
    private final Order order;
    private final HashMap<Character, DataPart> dataParts = new HashMap<>();

    public PokemonAnalyser(Path pokemonFile) throws IOException {
        this.bytes = IOUtils.toByteArray(new FileInputStream(pokemonFile.toFile()));
        this.personalityBytes = Arrays.copyOfRange(this.bytes, 0, 4);
        this.originalTrainerBytes = Arrays.copyOfRange(this.bytes, 4, 8);
        this.nicknameBytes = Arrays.copyOfRange(this.bytes, 8, 18);
        this.language = this.bytes[18];
        this.egg = this.bytes[19];
        this.originalTrainerNameBytes = Arrays.copyOfRange(this.bytes, 20, 27);
        this.marking = this.bytes[27];
        this.checksumBytes = Arrays.copyOfRange(this.bytes, 28, 30);
        this.encryptedDataBytes = Arrays.copyOfRange(this.bytes, 32, 80);
        this.statusConditionBytes = Arrays.copyOfRange(this.bytes, 80, 84);
        this.level = this.bytes[84];
        this.pokerus = this.bytes[85];
        this.currentHealthPointBytes = Arrays.copyOfRange(this.bytes, 86, 88);
        this.totalHealthPointBytes = Arrays.copyOfRange(this.bytes, 88, 90);
        this.attackBytes = Arrays.copyOfRange(this.bytes, 90, 92);
        this.defenseBytes = Arrays.copyOfRange(this.bytes, 92, 94);
        this.speedBytes = Arrays.copyOfRange(this.bytes, 94, 96);
        this.specialAttackBytes = Arrays.copyOfRange(this.bytes, 96, 98);
        this.specialDefenseBytes = Arrays.copyOfRange(this.bytes, 98, 100);

        this.encryptionKey = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).put(this.personalityBytes).rewind().getInt() ^
                                ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).put(this.originalTrainerBytes).rewind().getInt();

        this.decryptedDataBytes = encryptDecryptData(this.encryptedDataBytes);

        Long personality = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).put(this.personalityBytes).rewind().getLong();
        Long modulo = personality % 24;
        this.order = Order.byModulo(modulo.intValue());
        for (int i = 0; i < 4; i++) {
            byte[] bytes = Arrays.copyOfRange(this.decryptedDataBytes, i * 12, (i * 12) + 12);
            char indexedType = this.order.getIndexedType(i);
            switch(indexedType){
                case 'G':
                    this.dataParts.put(indexedType, new Growth(bytes, this));
                break;
                case 'A':
                    this.dataParts.put(indexedType, new Attack(bytes, this));
                break;
                case 'M':
                    this.dataParts.put(indexedType, new Misc(bytes, this));
                break;
                case 'E':
                    this.dataParts.put(indexedType, new EffortValues(bytes, this));
                break;
                default:
                    LOGGER.warn("Unexpected indexType:{}", indexedType);
            }
        }
    }

    private byte[] encryptDecryptData(byte[] data) {
        ByteBuffer encryptedByteBuffer = ByteBuffer.allocate(48).order(ByteOrder.LITTLE_ENDIAN).put(data).rewind();
        ByteBuffer decryptedByteBuffer = ByteBuffer.allocate(48).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < 12; i++) {
            decryptedByteBuffer.putInt(encryptedByteBuffer.getInt() ^ this.encryptionKey);
        }
        return decryptedByteBuffer.array();
    }

    @Override
    public String toString() {
        StringBuilder datapartsString = new StringBuilder();
        datapartsString.append("[");
        this.dataParts.values().forEach(dp -> datapartsString.append("\n  "+dp.toString()));
        datapartsString.append("\n]");

        return "PokemonAnalyser{" +
                "\n personalityBytes=" + ByteUtils.toHexString(personalityBytes) +
                "\n originalTrainerBytes=" + ByteUtils.toHexString(originalTrainerBytes) +
                "\n nicknameBytes=" + ByteUtils.toHexString(nicknameBytes) +
                "\n language=" + Byte.toUnsignedInt(language) +
                ", egg=" + Byte.toUnsignedInt(egg) +
                ", marking=" + Byte.toUnsignedInt(marking) +
                "\n originalTrainerNameBytes=" + ByteUtils.toHexString(originalTrainerNameBytes) +
                "\n checksumBytes=" + ByteUtils.toHexString(this.checksumBytes) +
                "\n encryptedDataBytes=" + ByteUtils.toHexString(this.encryptedDataBytes) +
                "\n decryptedDataBytes=" + ByteUtils.toHexString(this.decryptedDataBytes) +
                "\n data parts=" + datapartsString.toString() +
                "\n statusConditionBytes=" + ByteUtils.toHexString(statusConditionBytes) +
                "\n pokerus=" + Byte.toUnsignedInt(pokerus) +
                "\n HP=" + ByteUtils.toInt(currentHealthPointBytes) +
                "/" + ByteUtils.toInt(totalHealthPointBytes) +
                ", attack=" + ByteUtils.toInt(attackBytes) +
                ", defense=" + ByteUtils.toInt(defenseBytes) +
                ", speed=" + ByteUtils.toInt(speedBytes) +
                ", special attack=" + ByteUtils.toInt(specialAttackBytes) +
                ", special defense=" + ByteUtils.toInt(specialDefenseBytes) +
                "\n order=" + order +
                "\n}";
    }

    public byte[] getBytes() {
        return bytes;
    }

    public byte[] getEncryptedDataBytes() {
        return encryptedDataBytes;
    }

    public byte[] getDecryptedDataBytes() {
        return decryptedDataBytes;
    }

    public byte[] getChecksumBytes() {
        return checksumBytes;
    }

    public byte[] calculateChecksum() {
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < this.decryptedDataBytes.length; i+=2) {
            byte[] bytesToSum = Arrays.copyOfRange(this.decryptedDataBytes, i, i+2);
            BigInteger summable = BigInteger.valueOf(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).put(bytesToSum).putInt(0).rewind().getLong());
            sum = sum.add(summable);
        }
        return Arrays.copyOfRange(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(sum.intValue()).array(), 0, 2);
    }

    public void updateChecksum(){
        byte[] newChecksumBytes = this.calculateChecksum();
        bytes[28] = newChecksumBytes[0];
        bytes[29] = newChecksumBytes[1];
        this.checksumBytes = newChecksumBytes;
    }

    public void updateDatapart(DataPart dp){
        byte[] dataPartBytes = dp.getBytes();
        int index = this.order.getTypeIndex(dp.getType());
        for (int i = 0; i < 12; i++) {
            int j = (index * 12) + i;
            this.decryptedDataBytes[j] = dataPartBytes[i];
        }
        this.updateChecksum();
        this.encryptedDataBytes = this.encryptDecryptData(this.decryptedDataBytes);
        for (int i = 0; i < this.encryptedDataBytes.length; i++) {
            int j = 32+i;
            this.bytes[j] = this.encryptedDataBytes[i];
        }
    }

    public DataPart getDataPart(char type){
        return this.dataParts.get(type);
    }

    public Growth getGrowth(){
        return (Growth) this.getDataPart('G');
    }

    public Attack getAttack(){
        return (Attack) this.getDataPart('A');
    }

    public Misc getMisc(){
        return (Misc) this.getDataPart('M');
    }

    public EffortValues getEffortValues(){
        return (EffortValues) this.getDataPart('E');
    }

}
