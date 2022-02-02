package be.ecornely;

import be.ecornely.pokemon.PokemonAnalyser;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException, DecoderException {
        Path outFolder = Paths.get("/home/corne/Games/pokemon/split");
        Path sourceFile = Paths.get("/home/corne/Games/pokemon/emeraude.sav.ok");

        split(sourceFile, outFolder);
        Analyser analyser = new Analyser(outFolder);
        PokemonAnalyser pkAnalyser = new PokemonAnalyser(outFolder.resolve("team_pokemon_1.bin"));
        System.out.printf("Team section checksum: %s%n", ByteUtils.toHexString(analyser.getSection(SectionType.TeamItems).getChecksumBytes()));
        System.out.printf("Team section checksum: %s%n", ByteUtils.toHexString(analyser.getSection(SectionType.TeamItems).calculateChecksumBytes()));
        pkAnalyser.getAttack().setAttack1(58, 10);
//        pkAnalyser.getAttack().setAttack2(323, 5);
//        pkAnalyser.getAttack().setAttack3(91, 10);
//        pkAnalyser.getAttack().setAttack3(188, 10);
        outFolder.resolve("team_pokemon_1.bin").toFile().renameTo(outFolder.resolve("team_pokemon_1.bin.bak").toFile());
        try(FileOutputStream fos = new FileOutputStream(outFolder.resolve("team_pokemon_1.bin").toFile())) {
            IOUtils.write(pkAnalyser.getBytes(), fos);
        }
        analyser.updatePokemon(1, pkAnalyser.getBytes());

        joinTeam(outFolder, analyser);

//        combine(outFolder);
//        Analyser analyser = new Analyser(outFolder);
//        System.out.printf("99 should be written: %s%n",analyser.getMaskConverter().toMaskedHexString(99));
//        System.out.printf("96 should be written: %s%n",analyser.getMaskConverter().toMaskedHexString(96));
//        System.out.printf("15244 should be written: %s%n",analyser.getMaskConverter().toMaskedHexString(15244));
//        System.out.printf("5 should be written: %s%n",analyser.getMaskConverter().toMaskedHexString(5));
//
//        System.out.printf("f40d is %d%n",analyser.getMaskConverter().toDecimal((short)0xf40d));
//        System.out.printf("324b is %d%n",analyser.getMaskConverter().toDecimal((short)0x324b));
    }

    private static void joinTeam(Path splittedFolder, Analyser analyser) throws IOException {
        ByteBuffer teamBytes = ByteBuffer.allocate(600);
        for (int i = 0; i < 6; i++) {
            try(FileInputStream fis = new FileInputStream(splittedFolder.resolve(String.format("team_pokemon_%d.bin", i)).toFile())) {
                byte[] pokemon = IOUtils.toByteArray(fis);
                teamBytes.put(pokemon);
            }
        }
        splittedFolder.resolve("team.bin").toFile().renameTo(splittedFolder.resolve("team.bin.bak").toFile());
        try(FileOutputStream fos = new FileOutputStream(splittedFolder.resolve("team.bin").toFile())){
            fos.write(teamBytes.array());
            fos.flush();
        }
        Section section = analyser.getSection(SectionType.TeamItems);
        byte[] teamItemsSectionBytes = section.getBytes();
        System.out.printf("Team section was:%s%n", ByteUtils.toHexString(teamItemsSectionBytes));
        for (int i = 0; i < 600; i++) {
            teamItemsSectionBytes[568+i] = teamBytes.get(i);
        }
        System.out.printf("Team section is :%s%n", ByteUtils.toHexString(teamItemsSectionBytes));
        section.setBytes(teamItemsSectionBytes);
        try(FileOutputStream fos = new FileOutputStream(analyser.getSplittedFolder().resolve("emeraude.sav").toFile())) {
            IOUtils.write(analyser.join(), fos);
        }
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

    @Deprecated
    private static void combine(Path splittedFolder) throws IOException {
        Analyser analyser = new Analyser(splittedFolder);
        analyser.getSection(SectionType.TeamItems).updateChecksumBytes();
        IOUtils.write(analyser.join(), new FileOutputStream(analyser.getSplittedFolder().resolve("emeraude.sav").toFile()));
    }
}
