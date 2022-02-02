package be.ecornely.pokemon.data;

import java.util.BitSet;

public enum Game {
    SAPPHIRE,
    RUBY,
    EMERALD,
    FIRE_RED,
    LEAF_GREEN,
    COLOSSEUM_OR_XD;

    public static Game fromBitSet(BitSet bits){
        switch (bits.toByteArray()[0]){
            case 1:
                return SAPPHIRE;
            case 2:
                return RUBY;
            case 3:
                return EMERALD;
            case 4:
                return FIRE_RED;
            case 5:
                return LEAF_GREEN;
            case 15:
                return COLOSSEUM_OR_XD;
        }
        return null;
    }
}
