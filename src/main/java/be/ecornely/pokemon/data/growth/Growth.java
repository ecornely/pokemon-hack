package be.ecornely.pokemon.data.growth;

import be.ecornely.ByteUtils;
import be.ecornely.pokemon.PokemonAnalyser;
import be.ecornely.pokemon.data.DataPart;
import be.ecornely.pokemon.data.growth.PokemonDetail;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

import static be.ecornely.ByteUtils.toHexString;
import static be.ecornely.ByteUtils.toInt;

public class Growth implements DataPart {
    private final byte[] bytes;
    private byte[] speciesBytes;
    private byte[] itemBytes;
    private byte[] experienceBytes;
    private byte powerPointBonuses;
    private byte friendship;
    private final PokemonAnalyser pokemonAnalyser;


    @Override
    public byte[] getBytes() {
        return bytes;
    }

    public byte[] getSpeciesBytes() {
        return speciesBytes;
    }

    public byte[] getItemBytes() {
        return itemBytes;
    }

    public byte[] getExperienceBytes() {
        return experienceBytes;
    }

    public byte getPowerPointBonuses() {
        return powerPointBonuses;
    }

    public byte getFriendship() {
        return friendship;
    }

    public Growth(byte[] bytes, PokemonAnalyser pokemonAnalyser) {
        this.pokemonAnalyser = pokemonAnalyser;
        this.bytes = bytes;
        this.speciesBytes = Arrays.copyOfRange(bytes, 0, 2);
        this.itemBytes = Arrays.copyOfRange(bytes, 2, 4);
        this.experienceBytes = Arrays.copyOfRange(bytes, 4, 8);
        this.powerPointBonuses = bytes[8];
        this.friendship = bytes[9];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Growth: [");
        sb.append(String.format("species: %s (0x%s - %d)", PokemonDetail.getPokemonMap().get(toInt(speciesBytes)).getName(), toHexString(speciesBytes), toInt(speciesBytes)));
        sb.append(", ");
        sb.append(String.format("item: 0x%s(%d)", toHexString(itemBytes), toInt(itemBytes)));
        sb.append(", ");
        sb.append(String.format("experience: 0x%s", toHexString(experienceBytes)));
        sb.append(", ");
        sb.append(String.format("PP bonuses: 0x%02x(%d)", powerPointBonuses, powerPointBonuses));
        sb.append(", ");
        sb.append(String.format("friendship: 0x%02x(%d)", friendship, Byte.toUnsignedInt(friendship)));
        sb.append("]");
        return sb.toString();
    }

    @Override
    public char getType() {
        return 'G';
    }

    public void setSpecies(int species){
        this.speciesBytes = ByteUtils.fromInt(species, 2);
        this.bytes[0] = this.speciesBytes[0];
        this.bytes[1] = this.speciesBytes[1];
        this.pokemonAnalyser.updateDatapart(this);
    }

    public void setItem(int item){
        this.itemBytes = ByteUtils.fromInt(item, 2);
        this.bytes[2] = this.itemBytes[0];
        this.bytes[3] = this.itemBytes[1];
        this.pokemonAnalyser.updateDatapart(this);
    }

    public void setExperience(long xp){
        this.experienceBytes = ByteUtils.fromLong(xp, 4);
        this.bytes[4] = this.experienceBytes[0];
        this.bytes[5] = this.experienceBytes[1];
        this.bytes[6] = this.experienceBytes[2];
        this.bytes[7] = this.experienceBytes[3];
        this.pokemonAnalyser.updateDatapart(this);
    }

    public void setPPBonuse(int move1, int move2, int move3, int move4){
        if(move1<0 || move2<0 || move3<0 || move4 <0 || move1>3 || move2 >3 || move3>3 || move4>3){
            throw new IllegalArgumentException("Move have to be between [0-3]");
        }
//        boolean[] ppBonusBits = ArrayUtils.toPrimitive(
//                StreamEx.of(
//                        Booleans.asList(ByteUtils.toBits(new byte[]{(byte)move1})).stream().skip(6),
//                        Booleans.asList(ByteUtils.toBits(new byte[]{(byte)move2})).stream().skip(6),
//                        Booleans.asList(ByteUtils.toBits(new byte[]{(byte)move3})).stream().skip(6),
//                        Booleans.asList(ByteUtils.toBits(new byte[]{(byte)move4})).stream().skip(6)
//                ).toArray(Boolean[]::new)
//        );

        boolean[] move1Bits = Arrays.copyOfRange(ByteUtils.toBits(new byte[]{(byte)move1}), 6, 8);
        boolean[] move2Bits = Arrays.copyOfRange(ByteUtils.toBits(new byte[]{(byte)move2}), 6, 8);
        boolean[] move3Bits = Arrays.copyOfRange(ByteUtils.toBits(new byte[]{(byte)move3}), 6, 8);
        boolean[] move4Bits = Arrays.copyOfRange(ByteUtils.toBits(new byte[]{(byte)move3}), 6, 8);
        boolean[] ppBonusBits = ArrayUtils.addAll(ArrayUtils.addAll(ArrayUtils.addAll(move1Bits, move2Bits), move3Bits), move4Bits);
        this.powerPointBonuses = Byte.parseByte(ByteUtils.toBinaryString(ppBonusBits), 2);
        bytes[8] = this.powerPointBonuses;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public void setFriendship(int friendship){
        this.friendship = (byte) friendship;
        this.bytes[9] = this.friendship;
        this.pokemonAnalyser.updateDatapart(this);
    }

}
