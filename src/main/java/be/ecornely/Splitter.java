package be.ecornely;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Splitter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Splitter.class);

    private final File sourceFile;
    private final Path outFolder;

    public Splitter(File sourceFile) throws IOException{
        this(sourceFile, null);
    }
    public Splitter(File sourceFile, Path outFolder) throws IOException{
        this.outFolder = outFolder;
        if(this.outFolder ==null){
            Path tempFolder = Files.createTempDirectory("split");

        }
        LOGGER.info("All sections will be stored in {}", outFolder);
        this.sourceFile = sourceFile;
    }

    public Path split() throws IOException {
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(sourceFile));
        for (int i = 0; i < 14; i++) {
            int start = i*4096, end = (i*4096)+4096;
            final byte[] section = Arrays.copyOfRange(bytes, start, end);
            LOGGER.info("Split bytes from {} to {} which read {} bytes", start, end, section.length);
            byte[] saveIndex = Arrays.copyOfRange(section, 4092, 4096);
            byte[] signature = Arrays.copyOfRange(section, 4088, 4092);
            byte[] checksum = Arrays.copyOfRange(section, 4086, 4088);
            byte[] sectionId = Arrays.copyOfRange(section, 4084, 4086);
            int sectionIdInt = ByteBuffer.wrap(sectionId).order(ByteOrder.LITTLE_ENDIAN).getShort();
            byte[] data = Arrays.copyOfRange(section, 0, 4084);

            String sectionIdString = new String(Hex.encodeHex(sectionId));

            File sectionFile = outFolder.resolve(String.format("section-idx_%d-id_%d.bin", i, sectionIdInt)).toFile();
            try(FileOutputStream fos = new FileOutputStream(sectionFile)) {
                IOUtils.write(section, fos);
                fos.flush();
            }

            LOGGER.debug(String.format("--- Section position %d : %d [%04x,%04x] ---%n", i, sectionIdInt, start, end));
            LOGGER.trace("First bytes are {}", new String(Hex.encodeHex(Arrays.copyOfRange(section, 0, 12))));
            LOGGER.debug("There are {} bytes of data", data.length);
            LOGGER.debug("sectionId is {}", sectionIdString);
            LOGGER.debug("checksum is {}", new String(Hex.encodeHex(checksum)));
            LOGGER.debug("signature is {}", new String(Hex.encodeHex(signature)));
            LOGGER.debug("saveIndex is {}", new String(Hex.encodeHex(saveIndex)));
            LOGGER.trace("Last bytes are {}", new String(Hex.encodeHex(Arrays.copyOfRange(section, 4080, 4095))));
        }
        File endFile = outFolder.resolve("end.bin").toFile();
        try(FileOutputStream fos = new FileOutputStream(endFile)){
            IOUtils.write(Arrays.copyOfRange(bytes, 14 * 4096, bytes.length), fos);
            fos.flush();
        }
        return outFolder;
    }
}
