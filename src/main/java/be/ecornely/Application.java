package be.ecornely;

import be.ecornely.pokemon.PokemonAnalyser;
import be.ecornely.pokemon.data.misc.Ball;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException, DecoderException {
        Path outFolder = Paths.get("/home/corne/Games/pokemon/split");
        Path sourceFile = Paths.get("/home/corne/Games/pokemon/emeraude.sav.ok");

        split(sourceFile, outFolder);
        Analyser analyser = new Analyser(outFolder);
        PokemonAnalyser pkAnalyser;
        pkAnalyser = new PokemonAnalyser(outFolder.resolve("team_pokemon_0.bin"));
        pkAnalyser.getAttack().setAttack1(323, 5);
        pkAnalyser.getAttack().setAttack2(58, 16);
        pkAnalyser.getAttack().setAttack3(341, 15);
        pkAnalyser.getAttack().setAttack4(188, 10);
        System.out.printf("Pokemon 0 is %s%n", pkAnalyser.toString());
        analyser.updatePokemon(0, pkAnalyser.getBytes());

        pkAnalyser = new PokemonAnalyser(outFolder.resolve("team_pokemon_1.bin"));
        pkAnalyser.getAttack().setAttack1(354, 8);
        pkAnalyser.getAttack().setAttack2(247, 15);
        pkAnalyser.getAttack().setAttack3(94, 10);
        pkAnalyser.getAttack().setAttack4(100, 20);
        System.out.printf("Pokemon 1 is %s%n", pkAnalyser.toString());
        analyser.updatePokemon(1, pkAnalyser.getBytes());

        pkAnalyser = new PokemonAnalyser(outFolder.resolve("team_pokemon_2.bin"));
        pkAnalyser.getAttack().setAttack1(157, 10); // Rock Slide
        pkAnalyser.getAttack().setAttack2(231, 15); // Iron Tail
        pkAnalyser.getAttack().setAttack3(87, 15); // Thunder
        pkAnalyser.getAttack().setAttack4(238, 5); // Cross Chop
        System.out.printf("Pokemon 2 is %s%n", pkAnalyser.toString());
        analyser.updatePokemon(2, pkAnalyser.getBytes());

        pkAnalyser = new PokemonAnalyser(outFolder.resolve("team_pokemon_4.bin"));
        pkAnalyser.getAttack().setAttack1(332, 20);
        pkAnalyser.getAttack().setAttack2(19, 15);
        pkAnalyser.getAttack().setAttack3(148, 20);
        pkAnalyser.getAttack().setAttack4(15, 30);
        System.out.printf("Pokemon 4 is %s%n", pkAnalyser.toString());
        analyser.updatePokemon(4, pkAnalyser.getBytes());

        pkAnalyser = new PokemonAnalyser(outFolder.resolve("team_pokemon_5.bin"));
        pkAnalyser.getAttack().setAttack1(284, 5);
        pkAnalyser.getAttack().setAttack2(76, 10);
        pkAnalyser.getAttack().setAttack3(249, 15);
        pkAnalyser.getAttack().setAttack4(70, 15);

        System.out.printf("CHAMALLOT: %s%n", ByteUtils.toHexString(pkAnalyser.getBytes()));

//        pkAnalyser.getMisc().getIndividualValues().trySomething("000000000000000000000000000000");
//        pkAnalyser.getMisc().setEgg(false);
//        pkAnalyser.getMisc().setAbility(false);

        System.out.printf("Pokemon 5 is %s%n", pkAnalyser.toString());
        analyser.updatePokemon(5, pkAnalyser.getBytes());

//
        updateBag(analyser);

        IOUtils.write(analyser.join(), new FileOutputStream(analyser.getSplittedFolder().resolve("emeraude.sav").toFile()));

        analyser.getSplittedFolder().resolve("emeraude.sav").toFile().renameTo(analyser.getSplittedFolder().resolve("../emeraude.sav").toFile());

    }

    private static void updateBag(Analyser analyser) {
        Section section = analyser.getSection(SectionType.TeamItems);
        byte[] sectionBytes = section.getBytes();
        MaskConverter maskConverter = analyser.getMaskConverter();

        TreeMap<Integer, Integer> forced_items = getForcedItems();
        AtomicInteger i = new AtomicInteger();
        i.set(0);
        forced_items.entrySet().forEach(e -> {
            int itemStart = 0x0560+(i.getAndIncrement()*4);
            byte[] item = ByteUtils.fromInt(e.getKey(), 2);
            byte[] quantity = maskConverter.toMaskedBytes(e.getValue());
            sectionBytes[itemStart] = item[0];
            sectionBytes[itemStart+1] = item[1];
            sectionBytes[itemStart+2] = quantity[0];
            sectionBytes[itemStart+3] = quantity[1];
        });

        TreeMap<Integer, Integer> forced_pokeballs = getForcedPokeBalls();
        i.set(0);
        forced_pokeballs.entrySet().forEach(e -> {
            int itemStart = 0x0650+(i.getAndIncrement()*4);
            byte[] item = ByteUtils.fromInt(e.getKey(), 2);
            byte[] quantity = maskConverter.toMaskedBytes(e.getValue());
            sectionBytes[itemStart] = item[0];
            sectionBytes[itemStart+1] = item[1];
            sectionBytes[itemStart+2] = quantity[0];
            sectionBytes[itemStart+3] = quantity[1];
        });

        //TODO Add berry and put a lot of lum berries 0x008D
        //TODO Add a super rod in right pocket 0x0108

        section.setBytes(sectionBytes);
    }

    private static TreeMap<Integer, Integer> getForcedItems() {
        TreeMap<Integer, Integer> forced_items = new TreeMap<>();
        forced_items.put(0x13, 99); // Full restore (heal)
        forced_items.put(0x19, 99); // Max revive (restore fainted)
        forced_items.put(0x23, 99); // Max ether (restore PP of a single move)
        forced_items.put(0x17, 99); // Full heal (remove statuses)
        forced_items.put(0x25, 99); // Max elixir (restore PP of all moves)
        forced_items.put(0x54, 99); // Max repel (repel pokemons)
        forced_items.put(0x47, 99); // PP Max (increase max PP of a move)
        forced_items.put(0x3f, 99); // HP Up (increase HP)
        forced_items.put(0x44, 99); // Rare candy (increase lvl)
        forced_items.put(0x40, 99); // Protein
        forced_items.put(0x41, 99); // Iron
        forced_items.put(0x42, 99); // Carbos
        forced_items.put(0x45, 99); // Calcium
        forced_items.put(0x46, 99); // Zinc
        forced_items.put(0x49, 99); // Gard spec.
        forced_items.put(0x4a, 99); // Dire hit
        forced_items.put(0x4b, 99); // X attack
        forced_items.put(0x4c, 99); // X Defend
        forced_items.put(0x4d, 99); // X Speed
        forced_items.put(0x4e, 99); // X accuracy
        forced_items.put(0x4f, 99); // X Special
        return forced_items;
    }

    private static TreeMap<Integer, Integer> getForcedPokeBalls() {
        TreeMap<Integer, Integer> forced_pokeballs = new TreeMap<>();
        forced_pokeballs.put(0x01, 99); // Master Ball
        forced_pokeballs.put(0x04, 99); // Poke Ball
        forced_pokeballs.put(0x0b, 99); // Luxury
        return forced_pokeballs;
    }

    private static void extractTeam(Path splittedFolder) throws IOException {
        Analyser analyser = new Analyser(splittedFolder);
        byte[] teamBytes = Arrays.copyOfRange(analyser.getSection(SectionType.TeamItems).getBytes(), 568, 568 + 600);
        try(FileOutputStream fos = new FileOutputStream(splittedFolder.resolve("team.bin").toFile())){
            fos.write(teamBytes);
            fos.flush();
        }
        for (int i = 0; i < 6; i++) {
            try(FileOutputStream fos = new FileOutputStream(splittedFolder.resolve(String.format("team_pokemon_%d.bin", i)).toFile())){
                fos.write(Arrays.copyOfRange(teamBytes, i*100, (i*100)+100));
                fos.flush();
            }
        }
    }

    private static void split(Path sourceFile, Path outFolder) throws IOException {
        if (!outFolder.toFile().exists()) {
            outFolder.toFile().mkdirs();
        }
        if(outFolder.toFile().isDirectory() && outFolder.toFile().list().length > 0){
            System.out.printf("The directory %s is not empty... please do the cleanup manually%n", outFolder.toString());
        }else{
            new Splitter(sourceFile.toFile(), outFolder).split();
            extractTeam(outFolder);
        }
    }

}
