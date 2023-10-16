public enum Terrain {

    FOREST(0),
    BEACH(1),
    PLAINS(2),
    MINES(10),
    OLD_DERRICK(11),
    DESERT(21),
    SOLAR_ARRAY(22);


    final int obtainableResourceID;

    Terrain(int resourceID) {
        this.obtainableResourceID = resourceID;
    }


    public static Terrain find(int value) throws RuntimeException {

        for (Terrain t : Terrain.values()) {
            if (t.obtainableResourceID == value) {
                return t;
            }
        }
        throw new RuntimeException();
    }

}
