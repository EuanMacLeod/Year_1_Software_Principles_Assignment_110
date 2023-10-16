import java.io.*;

public class Game implements Serializable {

    @Serial
    private static final long serialVersionUID = -2024237781327955964L;
    private static final int NUMBER_OF_LEVELS = 3;

    Player player;
    Tutorial tutorial;

    private int currentLevel;
    private final Level[] levels;
    private boolean gameCompleted;

    public Game() {
        player = new Player();
        levels = new Level[NUMBER_OF_LEVELS];
        tutorial = new Tutorial();
    }

    public static Game deserialize(String fName) { //loads the game
        Game u = null;
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fName));
            u = (Game) is.readObject();
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Game Successfully Loaded");
        return u;
    }

    public void createLevels() { //used to create each level

        levels[0] = new Level("Tutorial Island",
                ItemIDs.BOAT.itemID,
                new Terrain[]{Terrain.FOREST, Terrain.BEACH, Terrain.PLAINS},
                """
                        Welcome To The Tutorial Level:
                                        
                        You wake up and find yourself marooned on a desert island!
                        After looking around for a couple of hours it doesn't seem like there's any way off the island.
                        Looks like you need need to craft your own way out of here then!
                                        
                            tip) If you get stuck then you can view the recipe for the item you need and work backwards from there.""");


        levels[1] = new Level("Abandoned Aerodrome",
                ItemIDs.BIPLANE.itemID,
                new Terrain[]{Terrain.FOREST, Terrain.BEACH, Terrain.PLAINS, Terrain.MINES, Terrain.OLD_DERRICK},
                """
                        After Sailing For Hours You Eventually Find Land!
                        There Doesn't Seem To Be Much Here Except For Some Old Mining And Drilling Operations As Well As The Wreck Of An Aerodrome.
                        There's Still A Lot Of Flat Open Space, Maybe Enough For A Plane To Take Off?""");


        levels[2] = new Level("Old Space Centre",
                ItemIDs.ROCKET.itemID,
                new Terrain[]{Terrain.FOREST, Terrain.BEACH, Terrain.PLAINS, Terrain.MINES, Terrain.OLD_DERRICK, Terrain.DESERT, Terrain.SOLAR_ARRAY},
                """
                        By Flying Over Land You Travel much Faster And Can See Much Further.
                        In The Distance You Spot A Large Tower Surrounded By A Field Of Solar Panels And You Decide To Land Nearby.
                        Turns Out It's An Old Space Centre Used For launching Rockets Into Space.
                        Well You've Come This Far, Why Not Make Your Own Rocket And Keep Going?""");

    }

    public boolean completeLevel() { //checks if the player meets the conditions to progress to the next level and moves to the next level if so

        if (player.getAmountOfItem(levels[currentLevel].getItemToProgress()) > 0) {

            player.removeItems(new int[][]{ //removes one of the quest items from the players inventory
                    {levels[currentLevel].getItemToProgress(), 1}
            });

            System.out.println("Congratulations!\nYou've Completed Level " + currentLevel + "\nNow Time To Progress Onto The Next Challenge!\n\n");
            currentLevel++;

            if (currentLevel == NUMBER_OF_LEVELS) { //checks if the player just beat the final level
                gameCompleted = true;
                System.out.println("WOW!\nYou've Completed Every Single Level!\nGreat Work!");
            }

            return true;
        } else {
            System.out.println("You Do Not Have The Required Item, To Progress You Need To Create A " + player.getItemName(levels[currentLevel].getItemToProgress()));
            return false;
        }
    }

    public void serialize(String fileName) { //saves the games progress
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
            os.writeObject(this);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Game Saved Successfully!");
    }

    //getters
    public int getCurrentLevel() {
        return currentLevel;
    }

    public String getCurrentLevelName() {
        return levels[currentLevel].getLevelName();
    }

    public String getCurrentLevelBackstory() {
        return levels[currentLevel].getBackstory();
    }

    public String getCurrentItemNameToProgress() {
        return player.getItemName(levels[currentLevel].getItemToProgress());
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }


    public void viewCurrentAvailableLocations() {
        levels[currentLevel].viewAvailableLocations();
    }

    public void displayHowToObtainItem(int itemID) {
        player.displayHowToObtainItem(itemID, currentLevel);
    }


}
