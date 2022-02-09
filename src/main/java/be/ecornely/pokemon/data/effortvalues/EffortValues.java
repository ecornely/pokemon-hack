package be.ecornely.pokemon.data.effortvalues;

import be.ecornely.pokemon.PokemonAnalyser;
import be.ecornely.pokemon.data.DataPart;

public class EffortValues implements DataPart {
    private final byte[] bytes;
    private final PokemonAnalyser pokemonAnalyser;
    private int hp;
    private int attack;
    private int defense;
    private int speed;
    private int specialAttack;
    private int specialDefense;
    private int coolness;
    private int beauty;
    private int cuteness;
    private int smartness;
    private int toughness;
    private int feel;

    public EffortValues(byte[] bytes, PokemonAnalyser pokemonAnalyser) {
        this.pokemonAnalyser = pokemonAnalyser;
        this.bytes = bytes;
        this.hp = Byte.toUnsignedInt(bytes[0]);
        this.attack = Byte.toUnsignedInt(bytes[1]);
        this.defense = Byte.toUnsignedInt(bytes[2]);
        this.speed = Byte.toUnsignedInt(bytes[3]);
        this.specialAttack = Byte.toUnsignedInt(bytes[4]);
        this.specialDefense = Byte.toUnsignedInt(bytes[5]);
        this.coolness = Byte.toUnsignedInt(bytes[6]);
        this.beauty = Byte.toUnsignedInt(bytes[7]);
        this.cuteness = Byte.toUnsignedInt(bytes[8]);
        this.smartness = Byte.toUnsignedInt(bytes[9]);
        this.toughness = Byte.toUnsignedInt(bytes[10]);
        this.feel = Byte.toUnsignedInt(bytes[11]);

        //TODO compare with http://www.ppnstudio.com/maker/ it should all be 0 and I have a lot pf values there
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        return "EffortValues{" +
                "hp=" + hp +
                ", attack=" + attack +
                ", defense=" + defense +
                ", speed=" + speed +
                ", specialAttack=" + specialAttack +
                ", specialDefense=" + specialDefense +
                ", coolness=" + coolness +
                ", beauty=" + beauty +
                ", cuteness=" + cuteness +
                ", smartness=" + smartness +
                ", toughness=" + toughness +
                ", feel=" + feel +
                '}';
    }

    @Override
    public char getType() {
        return 'E';
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
        this.bytes[0] = (byte) this.hp;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
        this.bytes[1] = (byte) this.attack;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
        this.bytes[2] = (byte) this.defense;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        this.bytes[3] = (byte) this.speed;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(int specialAttack) {
        this.specialAttack = specialAttack;
        this.bytes[4] = (byte) this.specialAttack;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    public void setSpecialDefense(int specialDefense) {
        this.specialDefense = specialDefense;
        this.bytes[5] = (byte) this.specialDefense;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public int getCoolness() {
        return coolness;
    }

    public void setCoolness(int coolness) {
        this.coolness = coolness;
        this.bytes[6] = (byte) this.coolness;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public int getBeauty() {
        return beauty;
    }

    public void setBeauty(int beauty) {
        this.beauty = beauty;
        this.bytes[7] = (byte) this.beauty;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public int getCuteness() {
        return cuteness;
    }

    public void setCuteness(int cuteness) {
        this.cuteness = cuteness;
        this.bytes[8] = (byte) this.cuteness;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public int getSmartness() {
        return smartness;
    }

    public void setSmartness(int smartness) {
        this.smartness = smartness;
        this.bytes[9] = (byte) this.smartness;
        this.pokemonAnalyser.updateDatapart(this);
    }

    public int getToughness() {
        return toughness;
    }

    public void setToughness(int toughness) {
        this.toughness = toughness;
        this.bytes[10] = (byte) this.toughness;
        this.pokemonAnalyser.updateDatapart(this);

    }

    public int getFeel() {
        return feel;
    }

    public void setFeel(int feel) {
        this.feel = feel;
        this.bytes[11] = (byte) this.feel;
        this.pokemonAnalyser.updateDatapart(this);
    }
}
