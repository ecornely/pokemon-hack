package be.ecornely.pokemon.data;

import java.util.BitSet;

public enum Ball {
    MASTER_BALL,
    ULTRA_BALL,
    GREAT_BALL,
    POKE_BALL,
    SAFARI_BALL,
    NET_BALL,
    DIVE_BALL,
    NEST_BALL,
    REPEAT_BALL,
    TIMER_BALL,
    LUXURY_BALL,
    PREMIER_BALL;

    public static Ball fromBitSet(BitSet bits){
        switch(bits.toByteArray()[0]){
            case 1:
                return MASTER_BALL;
            case 2:
                return ULTRA_BALL;
            case 3:
                return GREAT_BALL;
            case 4:
                return POKE_BALL;
            case 5:
                return SAFARI_BALL;
            case 6:
                return NET_BALL;
            case 7:
                return DIVE_BALL;
            case 8:
                return NEST_BALL;
            case 9:
                return REPEAT_BALL;
            case 10:
                return TIMER_BALL;
            case 11:
                return LUXURY_BALL;
            case 12:
                return PREMIER_BALL;
        }

        return null;
    }

    public static void main(String[] args) {
        BitSet bitSet = BitSet.valueOf(new byte[]{12});
        fromBitSet(bitSet);
    }
}
