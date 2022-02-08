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
        this.bits = bits; //TODO ensure byte order is OK

        this.hitPoint = Arrays.copyOfRange(bits, 3,8);
        this.attack = new boolean[] { this.bits[14], this.bits[15], this.bits[0], this.bits[1], this.bits[2] };
        this.defense = Arrays.copyOfRange(bits, 9,14);
        this.speed = new boolean[] { this.bits[20], this.bits[21], this.bits[22], this.bits[23], this.bits[8] };
        this.specialAttack = new boolean[] { this.bits[31], this.bits[16], this.bits[17], this.bits[18], this.bits[19] };
        this.specialDefense = Arrays.copyOfRange(bits, 25,30);
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
        //TODO
        this.misc.updateIndividualValues(this);
    }

    public void setAttack(int attack) {
        //TODO
        this.misc.updateIndividualValues(this);
    }

    public void setDefense(int defense) {
        //TODO
        this.misc.updateIndividualValues(this);
    }

    public void setSpeed(int speed) {
        //TODO
        this.misc.updateIndividualValues(this);
    }

    public void setSpecialAttack(int specialAttack) {
        //TODO
        this.misc.updateIndividualValues(this);
    }

    public void setSpecialDefense(int specialDefense) {
        //TODO
        this.misc.updateIndividualValues(this);
    }

    public static byte[] swap4Bytes(byte[] bytes) {
        byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = bytes[bytes.length-1-i];
        }
        return newBytes;
    }
}
