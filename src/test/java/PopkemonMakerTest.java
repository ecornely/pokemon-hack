import be.ecornely.ByteUtils;
import be.ecornely.pokemon.PokemonAnalyser;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.lang.module.Configuration;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PopkemonMakerTest {

    @Test
    public void test() throws Exception{
        Path outFolder = Paths.get("/home/corne/Games/pokemon/split");
        PokemonAnalyser pkAnalyser = new PokemonAnalyser(outFolder.resolve("team_pokemon_5.bin"));
        pkAnalyser.getAttack().setAttack1(284, 5);
        pkAnalyser.getAttack().setAttack2(76, 10);
        pkAnalyser.getAttack().setAttack3(249, 15);
        pkAnalyser.getAttack().setAttack4(70, 15);
        System.out.printf("CHAMALLOT: %s%n", ByteUtils.toHexString(pkAnalyser.getBytes()));
//        byte[] dvMax = ByteUtils.fromHexString("4FF54381C880BA18BDC2BBC7BBC6C6C9CEFF0302BFCCC3BDFFFFFF0052D700009B74B5997E75BF99827FF696D4747199A878F999873DF999876E7690788A06A68775F9998775F9998775F9998775F999");
        byte[] dvMax = ByteUtils.fromHexString("4FF54381C880BA18BDC2BBC7BBC6C6C9CEFF0302BFCCC3BDFFFFFF009FC400009B74B5997E75BF99827FF696D4747199A878F999873DF999876E76909409C1288775F9998775F9998775F9998775F999");
        byte[] zeroed = new byte[100];
        for (int i = 0; i < dvMax.length; i++) {
            zeroed[i] = dvMax[i];
        }

        pkAnalyser = new PokemonAnalyser(zeroed);
        System.out.printf("%s%n", pkAnalyser.toString());
        System.out.printf("CHAMALLOT: %s%n", ByteUtils.toHexString(pkAnalyser.getBytes()));
    }


    @Test
    public void bitsTest(){
        /*
        * ivEggAbilityBytes          = 137c38b1
        * ivEggAbilityBits          = 00010011011111000011100010110001
        * [hp=10011(19), attack=00000(0), defense=11111(31), speed=10000(16), specialAttack=10011(19), specialDefense=11000(24)]
        * 100110000011111100001001101100??
        * */
        byte[] bytes =  new byte[] {
                (byte)0x13, //00010011
                (byte)0x7c, //01111100
                (byte)0x38, //00111000
                (byte)0xb1};//10110001

        String binaryString = ByteUtils.toBinaryString(bytes);
        System.out.printf("USUAL binaryString=%s%n", binaryString);
        String expected = "10011 00000 11111 10000 10011 11000 0 1";
        System.out.printf("EXPECTED    string=%s%n", expected);
        byte[] swappedBytes = this.swapBytes(bytes);
        System.out.printf("REORDERED   string=%s%n",  this.split(ByteUtils.toBinaryString(swappedBytes)));
        // 1 0 11000 10011 10000 11111 00000 10011

    }

    private String split(String binaryString) {
        ArrayList<String> parts = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int from = 32 - (i * 5) - 5;
            int to = 32 - (i * 5);
            parts.add(binaryString.substring(from, to));
        }
        parts.add(binaryString.substring(1, 2));
        parts.add(binaryString.substring(0, 1));
        return parts.stream().collect(Collectors.joining(" "));
    }

    private byte[] swap4Bytes(byte[] bytes) {
        byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = bytes[bytes.length-1-i];
        }
        return newBytes;
    }
}
