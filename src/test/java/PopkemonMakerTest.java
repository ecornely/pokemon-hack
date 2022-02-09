import be.ecornely.ByteUtils;
import be.ecornely.pokemon.PokemonAnalyser;
import org.junit.jupiter.api.Test;

public class PopkemonMakerTest {

    @Test
    public void test() throws Exception{
//        byte[] dvMax = ByteUtils.fromHexString("4FF54381C880BA18BDC2BBC7BBC6C6C9CEFF0302BFCCC3BDFFFFFF009FC400009B74B5997E75BF99827FF696D4747199A878F999873DF999876E76909409C1288775F9998775F9998775F9998775F999");
        byte[] dvMax = ByteUtils.fromHexString("48B8A38DC880BA18BDC2BBC7BBC6C6C9CEFF0302BFCCC3BDFFFFFF00525700009C39559579385F958532169A803819958038199580381995D3399195AF351995807019958023969C7FC7E62A80381995");
        byte[] zeroed = new byte[100];
        for (int i = 0; i < dvMax.length; i++) {
            zeroed[i] = dvMax[i];
        }
        PokemonAnalyser pkAnalyser = new PokemonAnalyser(zeroed);
        System.out.printf("%s%n", pkAnalyser.toString());
        System.out.printf("HiddenPower: %s (%d)%n", pkAnalyser.calculateHiddenPowerType(), pkAnalyser.calculateHiddenPowerValue());

    }

    //TODO find where happiness is and what value it can have? on http://www.ppnstudio.com/maker/


}
