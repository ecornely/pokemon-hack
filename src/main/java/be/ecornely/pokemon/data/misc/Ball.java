package be.ecornely.pokemon.data.misc;

import java.util.Arrays;
import java.util.stream.Stream;

public enum Ball {
    MASTER_BALL(new boolean[] {false, false, false, true}, (byte)1),
    ULTRA_BALL(new boolean[] {false, false, true, false}, (byte)2),
    GREAT_BALL(new boolean[] {false, false, true, true}, (byte)3),
    POKE_BALL(new boolean[] {false, true, false, false}, (byte)4),
    SAFARI_BALL(new boolean[] {false, true, false, true}, (byte)5),
    NET_BALL(new boolean[] {false, true, true, false}, (byte)6),
    DIVE_BALL(new boolean[] {false, true, true, true}, (byte)7),
    NEST_BALL(new boolean[] {true, false, false, false}, (byte)8),
    REPEAT_BALL(new boolean[] {true, false, false, true}, (byte)9),
    TIMER_BALL(new boolean[] {true, false, true, false}, (byte)10),
    LUXURY_BALL(new boolean[] {true, false, true, true}, (byte)11),
    PREMIER_BALL(new boolean[] {true, true, false, false}, (byte)12);

    private final boolean[] bits;
    private final byte value;

    Ball(boolean[] bits, byte value) {
        this.bits = bits;
        this.value = value;
    }

    public boolean[] getBits() {
        return bits;
    }

    public byte getValue() {
        return value;
    }

    public static Ball fromBits(boolean[] bits){
        return Stream.of(Ball.values()).filter(b -> Arrays.equals(b.bits, bits)).findFirst().orElse(null);
    }
}
