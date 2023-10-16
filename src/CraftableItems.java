public class CraftableItems extends Items {

    private final int[][] recipe;


    public CraftableItems(int pID, String pName, int pRequiredLevel, String pDescription, int[][] pRecipe) {
        super(pID, pName, pRequiredLevel, pDescription);

        recipe = pRecipe.clone();

    }


    @Override
    public int[][] howToObtain() {
        return this.recipe;
    }

}
