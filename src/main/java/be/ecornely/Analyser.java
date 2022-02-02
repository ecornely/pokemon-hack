package be.ecornely;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Analyser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Analyser.class);

    private final Path splittedFolder;
    private final ArrayList<Section> sections;
    private final byte[] endBytes;
    private MaskConverter maskConverter;

    public Analyser(Path splittedFolder) throws IOException {
        this.splittedFolder = splittedFolder;
        this.sections = new ArrayList<Section>();
        try(Stream<Path> files = Files.list(this.splittedFolder)){
            Pattern pattern = Pattern.compile("section-idx_(\\d+)-id_(\\d+).bin");
            files
                    .filter(f -> pattern.matcher(f.getFileName().toString()).matches())
                    .forEach(f -> {
                        Matcher matcher = pattern.matcher(f.getFileName().toString());
                        if(matcher.matches()) {
                            int index = Integer.parseInt(matcher.group(1));
                            int sectionId = Integer.parseInt(matcher.group(2));
                            SectionType type = SectionType.ofSectionId(sectionId);
                            try {
                                byte[] bytes = IOUtils.toByteArray(new FileInputStream(f.toFile()));
                                LOGGER.trace("Read {} bytes from file: {}", bytes.length, f);
                                this.sections.add(new Section(bytes, type, index, f));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        this.sections.sort(Comparator.comparingInt(Section::getIndex));
        this.endBytes = IOUtils.toByteArray(new FileInputStream(splittedFolder.resolve("end.bin").toFile()));
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public Path getSplittedFolder() {
        return splittedFolder;
    }

    public Section getSection(SectionType type){
        return this.sections.stream().filter(s->s.getSectionType()==type).findFirst().orElse(null);
    }

    public byte[] join(){
        ByteBuffer bb = ByteBuffer.allocate(128*1024);
        this.sections.forEach(s -> {
            LOGGER.debug("Wrote byte of section: {}({}) with index {}", s.getSectionType().name(), s.getSectionType().sectionId(), s.getIndex());
            bb.put(s.getBytes());
        });
        bb.put(endBytes);
        return bb.array();
    }
    
    public MaskConverter getMaskConverter(){
        if(this.maskConverter == null){
            this.maskConverter = new MaskConverter(getMask());
        }
        return this.maskConverter;
    }

    private short getMask(){
        byte[] maskBytes = Arrays.copyOfRange(this.getSection(SectionType.TrainerInfo).getBytes(), 0x00ac, 0x00ae);
        System.out.printf("Read mask bytes are %s%n", new String(Hex.encodeHex(maskBytes)));
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).put(maskBytes).rewind().getShort();
    }

    public void updatePokemon(int pokemonIndex, byte[] pokemonBytes) {
        Section section = this.getSection(SectionType.TeamItems);
        byte[] teamItemsSectionBytes = section.getBytes();
        int pokemonStart = 568+((pokemonIndex)*100);
        for (int i = 0; i < 100; i++) {
            teamItemsSectionBytes[pokemonStart+i] = pokemonBytes[i];
        }
        section.setBytes(teamItemsSectionBytes);
    }
}
