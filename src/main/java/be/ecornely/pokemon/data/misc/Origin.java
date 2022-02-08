package be.ecornely.pokemon.data.misc;

import be.ecornely.ByteUtils;

import java.util.Arrays;

public class Origin {
    private byte[] bytes;

    private Gender originalTrainerGender;
    private Ball ball;
    private Game gameMet;
    private int levelMet;

    private final Misc misc;

    public Origin(byte[] bytes, Misc misc) {
        this.misc = misc;
        this.bytes = bytes;

        boolean[] bits = ByteUtils.toBits(this.swappedBytes(this.bytes));
        this.originalTrainerGender = bits[0] ? Gender.FEMALE : Gender.MALE;
        this.ball = Ball.fromBits(Arrays.copyOfRange(bits, 1, 5));
        this.gameMet = Game.fromBits(Arrays.copyOfRange(bits, 5, 9));
        this.levelMet = ByteUtils.fromBits(Arrays.copyOfRange(bits, 9, 16))[0];
    }

    private byte[] swappedBytes(byte[] bytes) {
        return new byte[]{bytes[1], bytes[0]};
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        return String.format("Origin %s [%s, %s, %s, %d]", ByteUtils.toHexString(this.bytes), this.originalTrainerGender.name(), this.ball.name(), this.gameMet.name(), this.levelMet);
    }

    public Gender getOriginalTrainerGender() {
        return originalTrainerGender;
    }

    public void setOriginalTrainerGender(Gender originalTrainerGender) {
        this.originalTrainerGender = originalTrainerGender;
        boolean[] bits = ByteUtils.toBits(this.swappedBytes(this.bytes));
        bits[0] = this.originalTrainerGender == Gender.FEMALE;
        this.bytes = this.swappedBytes(ByteUtils.fromBits(bits));
        this.misc.updateOriginBytes(this);
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
        boolean[] bits = ByteUtils.toBits(this.swappedBytes(this.bytes));
        for (int i = 0; i < 4; i++) {
            bits[1 + i] = this.ball.getBits()[i];
        }
        this.bytes = this.swappedBytes(ByteUtils.fromBits(bits));
        this.misc.updateOriginBytes(this);
    }

    public Game getGameMet() {
        return gameMet;
    }

    public void setGameMet(Game gameMet) {
        this.gameMet = gameMet;
        boolean[] bits = ByteUtils.toBits(this.swappedBytes(this.bytes));
        for (int i = 0; i < 4; i++) {
            bits[5 + i] = this.gameMet.getBits()[i];
        }
        this.bytes = this.swappedBytes(ByteUtils.fromBits(bits));
        this.misc.updateOriginBytes(this);
    }

    public int getLevelMet() {
        return levelMet;
    }

    public void setLevelMet(int levelMet) {
        this.levelMet = levelMet;
        boolean[] bits = ByteUtils.toBits(this.swappedBytes(this.bytes));
        boolean[] lvlBits = Arrays.copyOfRange(ByteUtils.toBits(new byte[]{(byte) levelMet}), 1, 8);
        for (int i = 0; i < 7; i++) {
            bits[9 + i] = lvlBits[i];
        }
        this.bytes = this.swappedBytes(ByteUtils.fromBits(bits));
        this.misc.updateOriginBytes(this);
    }
}
