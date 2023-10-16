public class HarvestableItems extends Items {

    private final Terrain terrain;

    public HarvestableItems(int pID, String pName, int pRequiredLevel, String pDescription, Terrain pTerrain) {
        super(pID, pName, pRequiredLevel, pDescription);

        terrain = pTerrain;

    }


    @Override
    public int[][] howToObtain() {
        return new int[][]{{-1, this.terrain.obtainableResourceID}};
    }

}
