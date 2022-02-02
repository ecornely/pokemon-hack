package be.ecornely.pokemon.data;

import be.ecornely.ByteUtils;

import java.util.Arrays;
import java.util.BitSet;

public class IndividualValues {
    private final boolean[] bitSet;

    private final boolean[] healthPoint;
    private final boolean[] attack;
    private final boolean[] defense;
    private final boolean[] speed;
    private final boolean[] specialAttack;
    private final boolean[] specialDefense;

    public IndividualValues(boolean[] bitSet) {
        this.bitSet = bitSet;
        this.healthPoint = Arrays.copyOfRange(bitSet, 0,5);
        this.attack = Arrays.copyOfRange(bitSet,5,10);
        this.defense = Arrays.copyOfRange(bitSet, 10,15);
        this.speed = Arrays.copyOfRange(bitSet, 15,20);
        this.specialAttack = Arrays.copyOfRange(bitSet, 20,25);
        this.specialDefense = Arrays.copyOfRange(bitSet, 25,30);
    }

    public String prettyPrint(boolean[] set){
        String bitSetToString = ByteUtils.toBinaryString(set);
        return String.format("%s(%d)", bitSetToString, Integer.parseInt(bitSetToString, 2));
    }

    public int getHealthPoint() {
        return Integer.parseInt(ByteUtils.toBinaryString(healthPoint), 2);
    }

    public int getAttack() {
        return Integer.parseInt(ByteUtils.toBinaryString(attack), 2);
    }

    public int getDefense() {
        return Integer.parseInt(ByteUtils.toBinaryString(defense), 2);
    }

    public int getSpeed() {
        return Integer.parseInt(ByteUtils.toBinaryString(speed), 2);
    }

    public int getSpecialAttack() {
        return Integer.parseInt(ByteUtils.toBinaryString(specialAttack), 2);
    }

    public int getSpecialDefense() {
        return Integer.parseInt(ByteUtils.toBinaryString(specialDefense), 2);
    }

    @Override
    public String toString() {
        return String.format("IVs [hp=%s" +
                ", attack=%s" +
                ", defense=%s" +
                ", speed=%s" +
                ", specialAttack=%s" +
                ", specialDefense=%s" +
                "]",
                this.prettyPrint(healthPoint),
                this.prettyPrint(attack),
                this.prettyPrint(defense),
                this.prettyPrint(speed),
                this.prettyPrint(specialAttack),
                this.prettyPrint(specialDefense)
        ) ;
    }
}
