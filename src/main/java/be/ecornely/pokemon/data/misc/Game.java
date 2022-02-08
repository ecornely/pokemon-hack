package be.ecornely.pokemon.data.misc;

import be.ecornely.ByteUtils;

import java.util.Arrays;
import java.util.stream.Stream;

public enum Game {
    SAPPHIRE(1),
    RUBY(2),
    EMERALD(3),
    FIRE_RED(4),
    LEAF_GREEN(5),
    COLOSSEUM_OR_XD(15);

    private final byte value;
    private final boolean[] bits;

    Game(int value) {
        this.value = (byte) value;
        this.bits = Arrays.copyOfRange(ByteUtils.toBits(new byte[]{this.value}), 4, 8);
    }

    public byte getValue() {
        return value;
    }

    public boolean[] getBits() {
        return bits;
    }

    public static Game fromBits(boolean[] bits){
        return Stream.of(Game.values()).filter(g->Arrays.equals(g.bits, bits)).findFirst().orElse(null);
    }
}
