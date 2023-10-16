import utils.KeyboardReader;

import java.io.File;

public class GameMenu {

    /*
    This class handles the interactions with the player
     */


    private final String filename = "gameSave.dat";
    private KeyboardReader kbr;
    private Game game;

    public void NewGame() { //runs scripts to generate the game assets and asks the user if they would like a tutorial

        game = new Game();

        if ('y' == kbr.getChar("Would You like To Enable The Tutorial?\n\tIf This Is Your First Time Playing Then This Is recommended.\nEnter y/n:", "yn")) {
            game.tutorial.tutorialEnabled = true;
        }

        game.player.createItems();
        game.createLevels();

        //uncomment to effectively skip the tutorial level
 /*
        for (int i = 0; i < 16; i++) {
            game.player.harvestResource();
        }

        game.player.changeLocation(Terrain.PLAINS);
        for (int i = 0; i < 12; i++) {
            game.player.harvestResource();
        }

        game.player.changeLocation(Terrain.BEACH);
        for (int i = 0; i < 15; i++) {
            game.player.harvestResource();
        }

        for (int i = 0; i < 5; i++) {
            game.player.craftItem(4,game.getCurrentLevel());
        }

        for (int i = 0; i < 4; i++) {
            game.player.craftItem(3,game.getCurrentLevel());
        }

        for (int i = 0; i < 2; i++) {
            game.player.craftItem(5,game.getCurrentLevel());
        }

        for (int i = 0; i < 2; i++) {
            game.player.craftItem(6,game.getCurrentLevel());
        }
        game.player.craftItem(7,game.getCurrentLevel());
        game.player.craftItem(8,game.getCurrentLevel());
        game.player.craftItem(9,game.getCurrentLevel());
  */

        mainMenu();
    }

    public void loadGame() { //this is equivalent to the load game section of the 'start menu flowchart' from 010 and also implements the logical checks from the 'loading a file' trace table
        if (new File(filename).exists()) {
            game = Game.deserialize(filename);

            mainMenu();
        } else {
            System.out.println("No Save File Exists.");
        }


    }


    public void mainMenu() {
        /*
        equivalent to most of the 'game flowchart' from 010 combined with the 'pause menu flowchart'
         */

        if (game.tutorial.tutorialEnabled && !game.tutorial.mainMenuTutorialShown) {
            System.out.println(game.tutorial.mainMenuTutorial);
            game.tutorial.mainMenuTutorialShown = true;
            kbr.pause();
        }


        char menuSelection;
        do {
            if (game.isGameCompleted()) {
                System.out.println("You've Completed The Game!");
                break;
            }

            System.out.println(game.getCurrentLevelBackstory());
            System.out.println("Current Level: " + game.getCurrentLevelName());
            System.out.println("Current Location: " + game.player.getLocation());

            menuSelection = kbr.getChar("""
                            \nWhat Would You Like To Do?
                            \ta) View Inventory.
                            \tb) Gather Resources.
                            \tc) Move To A Different Location.
                            \td) Save Game.
                            \tx) Exit Game."""
                    , "abcdx");

            switch (menuSelection) {
                case 'a' -> InventoryMenu();
                case 'b' -> harvestResource();
                case 'c' -> moveMenu();
                case 'd' -> saveGame();
                case 'x' -> menuSelection = confirmExit();
            }


        } while (menuSelection != 'x');
    }

    public void InventoryMenu() { //equivalent to the open inventory branch of the main game flowchart from 010


        if (game.tutorial.tutorialEnabled && !game.tutorial.inventoryMenuTutorialShown) {
            System.out.println(game.tutorial.inventoryMenuTutorial);
            game.tutorial.inventoryMenuTutorialShown = true;
            kbr.pause();
        }

        char menuSelection;
        do {
            System.out.println("Here Is Your Inventory:\n");
            game.player.viewInventory();

            menuSelection = kbr.getChar("""
                            What Would You Like To Do Now?
                            \ta) View Crafting Options.
                            \tb) See How To Obtain A Specific Item.
                            \tc) Throw Away Items.
                            \td) View Or Hand In Quest Item.
                            \tx) Go Back."""
                    , "abcdx");

            switch (menuSelection) {
                case 'a' -> CraftingMenu();
                case 'b' -> ItemInformationMenu();
                case 'c' -> itemRemovalMenu();
                case 'd' -> menuSelection = viewQuestItem();
                case 'x' -> System.out.println("Returning To Main Menu...\n");
            }
        } while (menuSelection != 'x');
    }

    public void CraftingMenu() { //prompts the user for an item to craft, check if said item is craftable and then crafts it if so

        if (game.tutorial.tutorialEnabled && !game.tutorial.craftingMenuTutorialShown) {
            System.out.println(game.tutorial.craftingMenuTutorial);
            game.tutorial.craftingMenuTutorialShown = true;
            kbr.pause();
        }

        System.out.println("Here Is What You Are Able To Craft With The Items You Have:\n");
        game.player.viewCraftableItems(game.getCurrentLevel());

        if (kbr.getChar("Would You Like To Craft An Item?\n\ty/n: ", "yn") == 'y') {
            System.out.flush();
            game.player.craftItem(kbr.getInt("Enter The ID Of An Item You Want To Craft"), game.getCurrentLevel());
        }
        kbr.pause();
    }

    public void ItemInformationMenu() { //prompts the user for an item ID and gives them information on said item

        if (game.tutorial.tutorialEnabled && !game.tutorial.itemInformationMenuTutorialShown) {
            System.out.println(game.tutorial.itemInformationMenuTutorial);
            game.tutorial.itemInformationMenuTutorialShown = true;
            kbr.pause();
        }


        game.player.viewAllObtainableItems(game.getCurrentLevel());
        System.out.flush();
        int itemID = kbr.getInt("Enter The ID Of The Item You Wish To Learn About: ");
        game.displayHowToObtainItem(itemID);
        kbr.pause();
    }

    public void itemRemovalMenu() {

        if (game.tutorial.tutorialEnabled && !game.tutorial.itemRemovalMenuTutorialShown) {
            System.out.println(game.tutorial.itemRemovalMenuTutorial);
            game.tutorial.itemRemovalMenuTutorialShown = true;
            kbr.pause();
        }

        try {
            int itemID = kbr.getInt("Enter The ID Of The Item Would You Like To Remove:");
            int amount = kbr.getInt("How Many Would You Like To Remove?");

            if (amount == 0) {
                System.out.println("You Threw Away Nothing, Good Job.");
            } else if (game.player.getAmountOfItem(itemID) >= amount) {

                game.player.removeItems(new int[][]{
                        {itemID, amount}
                });

                System.out.println("Items Successfully Removed.");
            } else {
                System.out.println("You Do Not Have Enough Of That Item.");
            }

        } catch (Exception e) {
            System.out.println("Invalid Item Entered.");
        }
        kbr.pause();
    }

    public char viewQuestItem() {

        if (game.tutorial.tutorialEnabled && !game.tutorial.viewQuestItemTutorialShown) {
            System.out.println(game.tutorial.viewQuestItemTutorial);
            game.tutorial.viewQuestItemTutorialShown = true;
            kbr.pause();
        }

        System.out.println("To Complete This Level You Need To Craft A " + game.getCurrentItemNameToProgress());

        if ('y' == kbr.getChar("Would You Like To Try And Hand In The Quest Item?\nEnter y/n", "yn")) {
            if (game.completeLevel()) {
                kbr.pause();
                return 'x';
            }
        }
        kbr.pause();
        return '#';

    }


    public void harvestResource() { //this implements the logic from the 'gathering a resource' trace table from 010

        if (game.tutorial.tutorialEnabled && !game.tutorial.harvestResourceTutorialShown) {
            System.out.println(game.tutorial.harvestResourceTutorial);
            game.tutorial.harvestResourceTutorialShown = true;
            kbr.pause();
        }

        game.player.harvestResource();
        kbr.pause();
    }


    public void moveMenu() { //this implements the change location segment of the 'game flowchart' from 010
        //prompts the player and moves them to the given location

        if (game.tutorial.tutorialEnabled && !game.tutorial.moveMenuTutorialShown) {
            System.out.println(game.tutorial.moveMenuTutorial);
            game.tutorial.moveMenuTutorialShown = true;
            kbr.pause();
        }

        boolean valid = false;
        do {
            game.viewCurrentAvailableLocations();

            String moveTo = kbr.getString("Enter The Location That You Would Like To Move To.\n");
            try {
                game.player.changeLocation(Terrain.valueOf(moveTo.toUpperCase()));
                valid = true;
            } catch (Exception e) {
                System.out.println("Invalid Location Entered, Please Try Again.");
            }
        } while (!valid);
        kbr.pause();
    }


    public void saveGame() { //this is equivalent to the save game section of the 'pause menu flowchart' from 010 and also implements the logical checks from the 'saving a file' trace table
        if (game.tutorial.tutorialEnabled && !game.tutorial.saveGameTutorialShown) {
            System.out.println(game.tutorial.saveGameTutorial);
            game.tutorial.saveGameTutorialShown = true;
            kbr.pause();
        }

        if (new File(filename).exists()) { //if save file exists prompt user if they wish to overwrite the saved data
            if ('y' == kbr.getChar("Existing Save File Found, Would You Like To Overwrite The Data?\nEnter y/n", "yn")) {
                game.serialize(filename);
            }
        } else {
            game.serialize(filename);
        }
        kbr.pause();
    }


    public char confirmExit() { //reminds the user to save before exiting
        return (kbr.getChar("Warning: Exiting Will Cause Any Unsaved Progress To Be Lost.\nWould You Still Like To Exit? y/n", "yn")
                == 'y' ? 'x' : '#');
    }


    public void startMenu() { //equivalent to start menu flowchart from 010

        kbr = new KeyboardReader();
        char menuSelection;
        do {
            menuSelection = kbr.getChar("""
                            Welcome To IntelligentCraft Crafting Game\s
                            What Would You Like To Do Today:
                            \ta) Begin A New Game.
                            \tb) Load An Existing Game.
                            \tx) Exit.
                            """
                    , "abx");

            switch (menuSelection) {
                case 'a' -> NewGame();
                case 'b' -> loadGame();
                case 'x' -> System.out.println("Bye Bye!");
            }
        } while (menuSelection != 'x');
    }
}
