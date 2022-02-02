package be.ecornely.pokemon.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PokemonDetail {

    private static final Logger LOGGER = LoggerFactory.getLogger(PokemonDetail.class);

    private String hexIndex;
    private int decIndex;
    private String name;
    private String type;

    public PokemonDetail() {
    }

    public String getHexIndex() {
        return hexIndex;
    }

    public void setHexIndex(String hexIndex) {
        this.hexIndex = hexIndex;
    }

    public int getDecIndex() {
        return decIndex;
    }

    public void setDecIndex(int decIndex) {
        this.decIndex = decIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private static List<PokemonDetail> POKEMONS = null;

    private static Map<Integer, PokemonDetail> POKEMON_MAP = null;

    public static Map<Integer, PokemonDetail> getPokemonMap(){
        try {
            if(POKEMONS ==null){
                ObjectMapper om = new ObjectMapper();
                POKEMONS = om.readValue(PokemonDetail.class.getResourceAsStream("/pokemons.json"), om.getTypeFactory().constructCollectionType(ArrayList.class, PokemonDetail.class));
                POKEMON_MAP = POKEMONS.stream().collect(Collectors.toMap(PokemonDetail::getDecIndex, p -> p));
            }
        } catch (IOException e) {
            LOGGER.warn("Impossible to load attack details from attacks.json", e);
            POKEMON_MAP = Collections.EMPTY_MAP;
        }
        return POKEMON_MAP;
    }

}
