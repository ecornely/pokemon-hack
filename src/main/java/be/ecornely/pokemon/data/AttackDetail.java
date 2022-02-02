package be.ecornely.pokemon.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AttackDetail {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttackDetail.class);

    private int index;
    private String name;
    private Type type;
    private Category category;
    private String contest;
    private int pp;
    private int power;
    private String accuracy;
    private String generation;

    public AttackDetail() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getContest() {
        return contest;
    }

    public void setContest(String contest) {
        this.contest = contest;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public static enum Type{
        Bug,
        Dark,
        Dragon,
        Electric,
        Fairy,
        Fighting,
        Fire,
        Flying,
        Ghost,
        Grass,
        Ground,
        Ice,
        Normal,
        Poison,
        Psychic,
        Rock,
        Steel,
        Water;
    }

    public static enum Category{
        Unknown,
        Physical,
        Special,
        Status;
    }

    private static List<AttackDetail> ATTACKS = null;
    private static Map<Integer, AttackDetail> ATTACK_DETAIL_MAP;

    public static Map<Integer, AttackDetail> getAllAttacks() {
        try {
            if(ATTACKS ==null){
               ObjectMapper om = new ObjectMapper();
               ATTACKS = om.readValue(AttackDetail.class.getResourceAsStream("/attacks.json"), om.getTypeFactory().constructCollectionType(ArrayList.class, AttackDetail.class));
               ATTACK_DETAIL_MAP = ATTACKS.stream().collect(Collectors.toMap(AttackDetail::getIndex, a -> a));
            }
        } catch (IOException e) {
            LOGGER.warn("Impossible to load attack details from attacks.json", e);
            ATTACK_DETAIL_MAP = Collections.EMPTY_MAP;
        }
        return ATTACK_DETAIL_MAP;
    }
}
