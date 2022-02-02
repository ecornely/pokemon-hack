package be.ecornely.pokemon.data;

import be.ecornely.ByteUtils;
import be.ecornely.pokemon.PokemonAnalyser;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Set;

import static be.ecornely.ByteUtils.toHexString;

public class Misc implements DataPart {
    private final byte[] bytes;
    private final byte pokerus;
    private final byte metLocation;
    private final byte[] iVsEggAvailabilityBytes;
    private final byte[] ribbonsObedienceBytes;
    private final Origin origin;
    private final be.ecornely.pokemon.data.IndividualValues individualValues;

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

    public be.ecornely.pokemon.data.IndividualValues getIndividualValues() {
        return individualValues;
    }

    public Misc(byte[] bytes, PokemonAnalyser pokemonAnalyser) {
        this.bytes = bytes;
        this.pokerus = bytes[0];
        this.metLocation = bytes[1];
        this.origin = new Origin(Arrays.copyOfRange(bytes, 2, 4));
        this.iVsEggAvailabilityBytes = Arrays.copyOfRange(bytes, 4, 8);
        this.individualValues = new IndividualValues(Arrays.copyOfRange(ByteUtils.toBits(iVsEggAvailabilityBytes), 0, 30));
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
        sb.append(String.format("Individual values: %s - %s", ByteUtils.toHexString(iVsEggAvailabilityBytes), this.individualValues.toString()));
        sb.append(", ");
        sb.append(String.format("Egg: %s", ByteUtils.toBits(iVsEggAvailabilityBytes)[30]));
        sb.append(", ");
        sb.append(String.format("Ability: %s", ByteUtils.toBits(iVsEggAvailabilityBytes)[31]));
        sb.append(", ");
        sb.append(String.format("ribbonsObedience: 0x%s", toHexString(ribbonsObedienceBytes)));
        sb.append("]");
        return sb.toString();
    }

    public static class Origin{
        private final byte[] bytes;
        private final BitSet bits;
        private final Gender gender;
        private final Ball ball;
        private final Game game;
        private final int level;

        public Origin(byte[] bytes) {
            this.bytes = bytes;
            this.bits = BitSet.valueOf(this.bytes);
            this.gender = bits.get(0) ? Gender.FEMALE : Gender.MALE;
            this.ball = Ball.fromBitSet(this.bits.get(11, 14));
            this.game = Game.fromBitSet(this.bits.get(7,10));
            this.level = (int) bits.get(0, 6).toByteArray()[0];
        }

        public byte[] getBytes() {
            return bytes;
        }

        @Override
        public String toString() {
            return String.format("Origin [%s, %s, %s, %d]", this.gender.name(), this.ball.name(), this.game.name(), this.level);
        }
    }

    @Override
    public char getType() {
        return 'M';
    }

    //TODO add setters
}
