package be.ecornely.pokemon;

import java.util.stream.Stream;

public enum Order {
    GAEM(0),
    GAME(1),
    GEAM(2),
    GEMA(3),
    GMAE(4),
    GMEA(5),
    AGEM(6),
    AGME(7),
    AEGM(8),
    AEMG(9),
    AMGE(10),
    AMEG(11),
    EGAM(12),
    EGMA(13),
    EAGM(14),
    EAMG(15),
    EMGA(16),
    EMAG(17),
    MGAE(18),
    MGEA(19),
    MAGE(20),
    MAEG(21),
    MEGA(22),
    MEAG(23);

    private int modulo;

    Order(int modulo) {
        this.modulo = modulo;
    }

    public char[] partOrder() {
        return this.name().toCharArray();
    }

    public char getIndexedType(int index) {
        return partOrder()[index];
    }

    public int getTypeIndex(char c) {
        char[] subs = this.name().toCharArray();
        int i = 0;
        while (i <= 3) {
            if (subs[i] == c) return i;
            i++;
        }
        return -1;
    }

    public static Order byModulo(int m) {
        return Stream.of(Order.values()).filter(o -> o.modulo == m).findFirst().orElse(null);
    }
}
