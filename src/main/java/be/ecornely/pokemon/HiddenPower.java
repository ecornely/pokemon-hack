package be.ecornely.pokemon;

import java.util.stream.Stream;

public enum HiddenPower {

	 Fighting(0),
	 Flying(1),
	 Poison(2),
	 Ground(3),
	 Rock(4),
	 Bug(5),
	 Ghost(6),
	 Steel(7),
	 Fire(8),
	 Water(9),
 	 Grass(10),
 	 Electric(11),
 	 Psychic(12),
 	 Ice(13),
 	 Dragon(14),
 	 Dark(15);

     private int value;

    HiddenPower(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static HiddenPower ofValue(int v){
        return Stream.of(HiddenPower.values()).filter(h->h.getValue()==v).findFirst().orElse(null);
    }
}
