package be.ecornely.pokemon.data;

import be.ecornely.pokemon.PokemonAnalyser;

import java.util.Arrays;

import static be.ecornely.ByteUtils.toHexString;
import static be.ecornely.ByteUtils.toInt;

public class Growth implements DataPart{
    private final byte[] bytes;
    private final byte[] speciesBytes;
    private final byte[] itemBytes;
    private final byte[] experienceBytes;
    private final byte powerPointBonuses;
    private final byte friendship;

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

    // TODO add setters
}
