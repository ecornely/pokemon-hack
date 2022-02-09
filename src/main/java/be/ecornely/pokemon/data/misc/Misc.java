package be.ecornely.pokemon.data.misc;

import be.ecornely.ByteUtils;
import be.ecornely.pokemon.PokemonAnalyser;
import be.ecornely.pokemon.data.DataPart;
import be.ecornely.pokemon.data.misc.IndividualValues;
import be.ecornely.pokemon.data.misc.Origin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static be.ecornely.ByteUtils.toHexString;

public class Misc implements DataPart {
    private static final Logger LOGGER = LoggerFactory.getLogger(Misc.class);

    private final byte[] bytes;
    private final PokemonAnalyser pokemonAnalyser;

    private byte pokerus;
    private byte metLocation;
    private Origin origin;
    private IndividualValues individualValues;
    private boolean egg;
    private boolean ability;
    private byte[] ribbonsObedienceBytes;

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    public byte getPokerus() {
        return pokerus;
    }

    public byte getMetLocation() {
        return metLocation;
    }

    public Origin getOrigin() {
        return origin;
    }

    public IndividualValues getIndividualValues() {
        return individualValues;
    }

    public Misc(byte[] bytes, PokemonAnalyser pokemonAnalyser) {
        this.pokemonAnalyser = pokemonAnalyser;
        this.bytes = bytes;
        this.pokerus = bytes[0];
        this.metLocation = bytes[1];
        this.origin = new Origin(Arrays.copyOfRange(bytes, 2, 4), this);

        byte[] ivEggAbilityBytes = swap4Bytes(Arrays.copyOfRange(bytes, 4, 8));
        boolean[] ivEggAbilityBits = ByteUtils.toBits(ivEggAbilityBytes);
        System.out.printf("ivEggAbilityBytes          = %s%n", toHexString(ivEggAbilityBytes));
        System.out.printf("ivEggAbilityBits          = %s%n", ByteUtils.toBinaryString(ivEggAbilityBytes));

        this.individualValues = new IndividualValues(ivEggAbilityBits, this);
        this.egg = ivEggAbilityBits[1];
        this.ability = ivEggAbilityBits[0];
        this.ribbonsObedienceBytes = Arrays.copyOfRange(bytes, 8, 12);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Misc: [");
        sb.append(String.format("pokerus: 0x%02x(%d)", pokerus, pokerus));
        sb.append(", ");
        sb.append(String.format("metLocation: 0x%02x(%d)", metLocation, metLocation));
        sb.append(", ");
        sb.append(String.format("origins: %s", this.origin.toString()));
        sb.append(", ");
        sb.append(String.format("Individual values: %s", this.individualValues.toString()));
        sb.append(", ");
        sb.append(String.format("Egg: %s", this.egg));
        sb.append(", ");
        sb.append(String.format("Ability: %s", this.ability));
        sb.append(", ");
        sb.append(String.format("ribbonsObedience: 0x%s", toHexString(ribbonsObedienceBytes)));
        sb.append("]");
        return sb.toString();
    }

    @Override
    public char getType() {
        return 'M';
    }

    //TODO Getter for obedience and analyse bits

    //TODO add setters for egg, ability, ribbon and obedience

    public void setPokerus(int pokerus){
        this.pokerus = (byte) pokerus;
        this.bytes[0] = this.pokerus;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public void setMetLocation(int metLocation){
        this.metLocation = (byte) metLocation;
        this.bytes[1] = this.metLocation;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public void updateOriginBytes(Origin origin){
        this.origin = origin;
        byte[] originBytes = origin.getBytes();
        this.bytes[2] = originBytes[0];
        this.bytes[3] = originBytes[1];
        this.pokemonAnalyser.updateDatapart(this);
    }

    public void updateIndividualValues(IndividualValues iv){
        //TODO Test iv change
        this.individualValues = iv;
        this.updateIvEggAbilityBytes();
    }

    public void setEgg(boolean egg) {
        this.egg = egg;
        this.updateIvEggAbilityBytes();
    }

    public void setAbility(boolean ability) {
        this.ability = ability;
        this.updateIvEggAbilityBytes();
    }

    private void updateIvEggAbilityBytes() {
        boolean[] ivEggAbilityBits = new boolean[32];
        ivEggAbilityBits[0] = this.ability;
        ivEggAbilityBits[1] = this.egg;
        boolean[] ivBits = this.individualValues.getBits();
        for (int i = 0; i < 30; i++) {
            ivEggAbilityBits[i+2] = ivBits[i];
        }
        LOGGER.debug("Recomposed ivEggAbilityBits: {}", ByteUtils.toBinaryString(ivEggAbilityBits));
        byte[] ivEggAbilityBytes = swap4Bytes(ByteUtils.fromBits(ivEggAbilityBits));

        for (int i = 0; i < ivEggAbilityBytes.length; i++) {
            this.bytes[4+i] = ivEggAbilityBytes[i];
        }
        LOGGER.debug("Updated ivEggAbilityBytes are now: {}", ByteUtils.toHexString(ivEggAbilityBytes));

        this.pokemonAnalyser.updateDatapart(this);
    }

    public static byte[] swap4Bytes(byte[] bytes) {
        byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = bytes[bytes.length-1-i];
        }
        return newBytes;
    }

    public static String splitIVEggAbilityBinaryString(byte[] bytes) {
        String binaryString = ByteUtils.toBinaryString(bytes);
        ArrayList<String> parts = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int from = 32 - (i * 5) - 5;
            int to = 32 - (i * 5);
            parts.add(binaryString.substring(from, to));
        }
        parts.add(binaryString.substring(1, 2));
        parts.add(binaryString.substring(0, 1));
        return parts.stream().collect(Collectors.joining(" "));
    }
}
