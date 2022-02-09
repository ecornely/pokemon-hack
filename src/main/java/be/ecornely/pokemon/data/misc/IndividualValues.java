package be.ecornely.pokemon.data.misc;

import be.ecornely.ByteUtils;

import java.util.Arrays;

public class IndividualValues {
    private boolean[] bits;

    private boolean[] hitPoint;
    private boolean[] attack;
    private boolean[] defense;
    private boolean[] speed;
    private boolean[] specialAttack;
    private boolean[] specialDefense;

    private final Misc misc;

    public IndividualValues(boolean[] bits, Misc misc) {
        this.misc = misc;
        this.bits = bits;
        this.hitPoint = Arrays.copyOfRange(bits, 32-5,32);
        this.attack = Arrays.copyOfRange(bits, 32-10,32-5);
        this.defense = Arrays.copyOfRange(bits, 32-15,32-10);
        this.speed = Arrays.copyOfRange(bits, 32-20,32-15);
        this.specialAttack = Arrays.copyOfRange(bits, 32-25,32-20);
        this.specialDefense = Arrays.copyOfRange(bits, 32-30,32-25);
    }

    public boolean[] getBits(){
        return this.bits;
    }

    public String prettyPrint(boolean[] set){
        String bitSetToString = ByteUtils.toBinaryString(set);
        return String.format("%s(%d)", bitSetToString, Integer.parseInt(bitSetToString, 2));
    }

    public int getHitPoint() {
        return Integer.parseInt(ByteUtils.toBinaryString(hitPoint), 2);
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
        return String.format("IVs %s [hp=%s" +
                ", attack=%s" +
                ", defense=%s" +
                ", speed=%s" +
                ", specialAttack=%s" +
                ", specialDefense=%s" +
                "]",
                ByteUtils.toBinaryString(this.getBits()),
                this.prettyPrint(hitPoint),
                this.prettyPrint(attack),
                this.prettyPrint(defense),
                this.prettyPrint(speed),
                this.prettyPrint(specialAttack),
                this.prettyPrint(specialDefense)
        ) ;
    }

    public void setHitPoint(int hp) {
        if(hp>31) throw new IllegalArgumentException("IV max is 31");
        boolean[] bits = ByteUtils.fromBinaryString(String.format("%8s",Integer.toBinaryString((byte)hp)).replaceAll(" ", "0").substring(3,8));
        this.hitPoint = bits;
        for (int i = 0; i < bits.length; i++) {
            this.bits[32-5+i] = bits[i];
        }
        this.misc.updateIndividualValues(this);
    }

    public void setAttack(int attack) {
        if(attack>31) throw new IllegalArgumentException("IV max is 31");
        boolean[] bits = ByteUtils.fromBinaryString(String.format("%8s",Integer.toBinaryString((byte)attack)).replaceAll(" ", "0").substring(3,8));
        this.attack = bits;
        for (int i = 0; i < bits.length; i++) {
            this.bits[32-10+i] = bits[i];
        }
        this.misc.updateIndividualValues(this);
    }

    public void setDefense(int defense) {
        if(defense>31) throw new IllegalArgumentException("IV max is 31");
        boolean[] bits = ByteUtils.fromBinaryString(String.format("%8s",Integer.toBinaryString((byte)defense)).replaceAll(" ", "0").substring(3,8));
        this.defense = bits;
        for (int i = 0; i < bits.length; i++) {
            this.bits[32-15+i] = bits[i];
        }
        this.misc.updateIndividualValues(this);
    }

    public void setSpeed(int speed) {
        if(speed>31) throw new IllegalArgumentException("IV max is 31");
        boolean[] bits = ByteUtils.fromBinaryString(String.format("%8s",Integer.toBinaryString((byte)speed)).replaceAll(" ", "0").substring(3,8));
        this.speed = bits;
        for (int i = 0; i < bits.length; i++) {
            this.bits[32-20+i] = bits[i];
        }
        this.misc.updateIndividualValues(this);
    }

    public void setSpecialAttack(int specialAttack) {
        if(specialAttack>31) throw new IllegalArgumentException("IV max is 31");
        boolean[] bits = ByteUtils.fromBinaryString(String.format("%8s",Integer.toBinaryString((byte)specialAttack)).replaceAll(" ", "0").substring(3,8));
        this.specialAttack = bits;
        for (int i = 0; i < bits.length; i++) {
            this.bits[32-25+i] = bits[i];
        }
        this.misc.updateIndividualValues(this);
    }

    public void setSpecialDefense(int specialDefense) {
        if(specialDefense>31) throw new IllegalArgumentException("IV max is 31");
        boolean[] bits = ByteUtils.fromBinaryString(String.format("%8s",Integer.toBinaryString((byte)specialDefense)).replaceAll(" ", "0").substring(3,8));
        this.specialDefense = bits;
        for (int i = 0; i < bits.length; i++) {
            this.bits[32-30+i] = bits[i];
        }
        this.misc.updateIndividualValues(this);
    }

}
