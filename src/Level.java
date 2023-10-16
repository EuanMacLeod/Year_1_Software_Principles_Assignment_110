import java.io.Serializable;

public class Level implements Serializable {

    private static final long serialVersionUID = -272252237736251693L;

    private final String levelName;
    private final int itemToProgress;
    private final Terrain[] availableLocations;
    private final String backstory;

    public Level(String pLevelName, int pItemToProgress, Terrain[] pAvailableLocations, String pBackstory) {

        levelName = pLevelName;
        itemToProgress = pItemToProgress;
        availableLocations = pAvailableLocations;
        backstory = pBackstory;

    }

    public void viewAvailableLocations() { //displays all the locations available in the level
        for (Terrain t : availableLocations) {
            System.out.println(t);
        }
    }

    //getters
    public String getLevelName() {
        return levelName;
    }

    public int getItemToProgress() {
        return itemToProgress;
    }

    public String getBackstory() {
        return backstory;
    }


}


