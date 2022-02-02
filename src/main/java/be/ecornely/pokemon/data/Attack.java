package be.ecornely.pokemon.data;

import be.ecornely.ByteUtils;
import be.ecornely.pokemon.PokemonAnalyser;

import java.util.Arrays;
import java.util.Map;

import static be.ecornely.ByteUtils.toHexString;
import static be.ecornely.ByteUtils.toInt;

public class Attack implements DataPart {
    private final byte[] bytes;
    private byte[] attack1;
    private byte pp1;
    private byte[] attack2;
    private byte pp2;
    private byte[] attack3;
    private byte pp3;
    private byte[] attack4;
    private byte pp4;
    private final PokemonAnalyser pokemonAnalyser;

    public Attack(byte[] bytes, PokemonAnalyser pokemonAnalyser) {
        this.pokemonAnalyser = pokemonAnalyser;
        this.bytes = bytes;
        this.attack1 = getAttackBytes(0);
        this.pp1 = this.bytes[8];
        this.attack2 = getAttackBytes(1);
        this.pp2 = this.bytes[9];
        this.attack3 = getAttackBytes(2);
        this.pp3 = this.bytes[10];
        this.attack4 = getAttackBytes(3);
        this.pp4 = this.bytes[11];
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    public byte[] getAttack1() {
        return attack1;
    }

    public byte getPp1() {
        return pp1;
    }

    public byte[] getAttack2() {
        return attack2;
    }

    public byte getPp2() {
        return pp2;
    }

    public byte[] getAttack3() {
        return attack3;
    }

    public byte getPp3() {
        return pp3;
    }

    public byte[] getAttack4() {
        return attack4;
    }

    public byte getPp4() {
        return pp4;
    }

    public String toString() {
        Map<Integer, AttackDetail> allAttacks = AttackDetail.getAllAttacks();
        StringBuilder sb = new StringBuilder();
        sb.append("Attack: [");
        sb.append(String.format("%s - id: 0x%s(%d) - pp:0x%02x(%d)", allAttacks.get(toInt(attack1)).getName(), toHexString(attack1), toInt(attack1), pp1, Byte.toUnsignedInt(pp1)));
        sb.append("], [");
        sb.append(String.format("%s - id: 0x%s(%d) - pp:0x%02x(%d)", allAttacks.get(toInt(attack2)).getName(), toHexString(attack2), toInt(attack2), pp2, Byte.toUnsignedInt(pp2)));
        sb.append("], [");
        sb.append(String.format("%s - id: 0x%s(%d) - pp:0x%02x(%d)", allAttacks.get(toInt(attack3)).getName(), toHexString(attack3), toInt(attack3), pp3, Byte.toUnsignedInt(pp3)));
        sb.append("], [");
        sb.append(String.format("%s - id 0x%s(%d) - pp:0x%02x(%d)", allAttacks.get(toInt(attack4)).getName(), toHexString(attack4), toInt(attack4), pp4, Byte.toUnsignedInt(pp4)));
        sb.append("]");
        return sb.toString();
    }

    private byte[] getAttackBytes(int i) {
        return Arrays.copyOfRange(this.bytes, i * 2, (i * 2) + 2);
    }

    @Override
    public char getType() {
        return 'A';
    }

    public void setAttack1(int index, int pp){
        this.attack1 = ByteUtils.fromInt(index, 2);
        this.pp1 = (byte) pp;
        this.setAttack(0, this.attack1, pp);
    }
    public void setAttack2(int index, int pp){
        this.attack2 = ByteUtils.fromInt(index, 2);
        this.pp2 = (byte) pp;
        this.setAttack(1, this.attack2, pp);
    }
    public void setAttack3(int index, int pp){
        this.attack3 = ByteUtils.fromInt(index, 2);
        this.pp3 = (byte) pp;
        this.setAttack(2, this.attack3, pp);
    }
    public void setAttack4(int index, int pp){
        this.attack4 = ByteUtils.fromInt(index, 2);
        this.pp4 = (byte) pp;
        this.setAttack(3, this.attack4, pp);
    }

    private void setAttack(int attack, byte[] attackBytes, int pp){
        this.bytes[attack*2] = attackBytes[0];
        this.bytes[attack*2+1] = attackBytes[1];
        this.bytes[8+attack] = (byte) pp;
        this.pokemonAnalyser.updateDatapart(this);
    }
}
