package be.ecornely;

import java.util.stream.Stream;

public enum SectionType {
    TrainerInfo(0, 3884),
    TeamItems(1, 3968),
    GameState(2, 3968),
    MiscData(3, 3968),
    RivalInfo(4, 3848),
    PCBufferA(5, 3968),
    PCBufferB(6, 3968),
    PCBufferC(7, 3968),
    PCBufferD(8, 3968),
    PCBufferE(9, 3968),
    PCBufferF(10, 3968),
    PCBufferG(11, 3968),
    PCBufferH(12, 3968),
    PCBufferI(13, 2000);

    private final int sectionId;
    private final int dataSize;

    private SectionType(int id, int dataSize) {
        this.sectionId = id;
        this.dataSize = dataSize;
    }

    public static SectionType ofSectionId(int sectionId) {
        return Stream.of(values()).filter(e->e.sectionId==sectionId).findFirst().orElse(null);
    }

    public int sectionId(){return this.sectionId;}
    public int dataSize(){return this.dataSize;}
}