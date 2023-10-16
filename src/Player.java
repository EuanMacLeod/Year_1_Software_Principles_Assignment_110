import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    @Serial
    private static final long serialVersionUID = -8880232614394673427L;

    private final int MAX_INVENTORY_SIZE = 100;
    private Terrain location;
    private int numberOfItemsInInventory;
    private final ArrayList<Items> inventory;

    /*
    I decided to remove the XP system of the game and instead the player now unlocks new items with each game level they travel to as I feel that this suits the style of the game better.
    This means the player knows they are always able to complete a given level with the tools provided to them and do not need to grind for XP.
    The new item unlock method suits the style of a puzzle game far better and this will help challenge the players creative thinking without bloating them with items that may be unnecessary.
     */

    public Player() {
        location = Terrain.FOREST;
        inventory = new ArrayList<>();
    }


    public void changeLocation(Terrain moveTo) { //changes the players location
        location = moveTo;
        System.out.println("You have moved to: " + moveTo);
    }

    public void viewInventory() { //display all items the player has at least 1 of

        System.out.println("You Have,");
        for (Items items : inventory) {
            if (items.getAmount() > 0) {
                System.out.println("ID: " + items.getItemID() + "\tAmount: " + items.getAmount() + " " + items.getName());
            }
        }
        System.out.println("\nSpace Remaining: " + (MAX_INVENTORY_SIZE - numberOfItemsInInventory));
    }

    public void viewAllObtainableItems(int currentLevel) { //displays all the items that the player can possibly obtain in the current level
        System.out.println("\nHere's All The Items You Can Possibly Obtain: ");
        for (Items items : inventory) {
            if (items.getRequiredLevel() <= currentLevel) {
                System.out.println("ID:" + items.getItemID() + " Item: " + items.getName());
            }
        }
        System.out.println("\n");
    }

    public void viewCraftableItems(int currentLevel) { //displays all the items the player can craft with the items in their inventory
        for (Items items : inventory) {
            if ((items instanceof CraftableItems) && (isCraftable(items.getItemID(), currentLevel))) {

                System.out.println("ID:" + items.getItemID() + " Item: " + items.getName());
            }
        }
    }

    private void gainItem(int itemIdToGain) { //Attempts To Add An Items To The Players Inventory
        if (numberOfItemsInInventory < MAX_INVENTORY_SIZE) {
            inventory.get(itemIdToGain).changeAmountBy(1);
            numberOfItemsInInventory++;
            System.out.println("One " + inventory.get(itemIdToGain).getName() + " Has Been Added To Your inventory.\n");

            if (!inventory.get(itemIdToGain).isObtainedBefore()) {
                System.out.println(inventory.get(itemIdToGain).getDescription());
                inventory.get(itemIdToGain).setObtainedBefore(true);
            }

        } else {
            System.out.println("Cannot Add Item, Inventory Is Full.\n");
        }
    }


    public void harvestResource() { //harvests a resource based on the players location

        int itemID = location.obtainableResourceID;
        gainItem(itemID);

    }

    public void craftItem(int itemID, int currentLevel) { //attempts to craft a given item

        if (isCraftable(itemID, currentLevel)) {

            removeItems(inventory.get(itemID).howToObtain());
            gainItem(itemID);

        } else {
            System.out.println("That Item Is Not Craftable.\n");
        }
    }

    private boolean isCraftable(int itemID, int currentLevel) { //checks if the player can craft a given item
        try {
            //if item isn't of craftable type or item is too high a level then the return false
            if (!(inventory.get(itemID) instanceof CraftableItems) || inventory.get(itemID).getRequiredLevel() > currentLevel) {
                return false;
            }

            int[][] itemsNeeded = inventory.get(itemID).howToObtain();
            boolean craftable = false;
            for (int[] ints : itemsNeeded) { //checks if player has enough of each required item in their inventory
                if (inventory.get(ints[0]).getAmount() >= ints[1]) {
                    craftable = true;
                } else {
                    craftable = false;
                    break;
                }
            }
            return craftable;
        } catch (Exception e) {
            return false;
        }
    }


    public void removeItems(int[][] ItemsToRemove) { //accepts an item recipe and removes the given amount of each item
        for (int[] ints : ItemsToRemove) {
            inventory.get(ints[0]).changeAmountBy(-(ints[1])); //-= ItemsToRemove[i][1];
            numberOfItemsInInventory -= ints[1];
        }
    }

    public void displayHowToObtainItem(int itemID, int currentLevel) { //Displays how to obtain a given item
        try {
            if (inventory.get(itemID).requiredLevel > currentLevel) {
                throw new RuntimeException();
            }

            if (inventory.get(itemID) instanceof HarvestableItems) {
                System.out.println("You Can Get This Resource By Exploring In " + Terrain.find(itemID));
            } else {
                int[][] recipe = inventory.get(itemID).howToObtain();
                System.out.println("This Item Can Be Acquired From Crafting And Requires The Following: ");

                for (int[] ints : recipe) {
                    System.out.println(ints[1] + " " + inventory.get(ints[0]).getName());
                }
                System.out.println("\n");
            }

        } catch (Exception e) {
            System.out.println("Invalid Item Selected.");
        }

    }

    //getters
    public int getAmountOfItem(int itemID) {
        return inventory.get(itemID).getAmount();
    }

    public String getItemName(int itemID) {
        return inventory.get(itemID).getName();
    }

    public Terrain getLocation() {
        return location;
    }




    public void createItems() { //creates all the items in the game

        /*
        Level One Items
         */
        inventory.add(new HarvestableItems(ItemIDs.WOOD.itemID, "Wood", 0,
                "Wood Is A Natural Material Acquired From Tree's, It Is Easy To Shape And Make Things With.\nIf Made Correctly The Final Product Can Be Very Strong.\nStrong Enough To Survive Rough Seas Maybe?"
                , Terrain.FOREST));
        inventory.add(new HarvestableItems(ItemIDs.SEAWEED.itemID, "Seaweed", 0,
                "Seaweed Is A Fibrous Plant That Lives Underwater.\nIt's Not Much Good Right Now But Maybe You Can Find A use For It"
                , Terrain.BEACH));
        inventory.add(new HarvestableItems(ItemIDs.COTTON.itemID, "Cotton", 0,
                "Cotton Is A Fluffy Plant That Grows In Warmer Climates, Since Ancient Times It Has Been Used For Making A Variety Of Textiles.\nPerhaps You Can Put It To Good Use As Well."
                , Terrain.PLAINS));

        int[][] fabricRecipe = {
                {ItemIDs.COTTON.itemID, 3}
        };
        int[][] ropeRecipe = {
                {ItemIDs.SEAWEED.itemID, 3}
        };
        int[][] boatSailRecipe = {
                {ItemIDs.FABRIC.itemID, 2}
        };
        int[][] boatHullRecipe = {
                {ItemIDs.WOOD.itemID, 10}
        };
        int[][] boatMastRecipe = {
                {ItemIDs.WOOD.itemID, 2}
        };
        int[][] boatRudderRecipe = {
                {ItemIDs.WOOD.itemID, 2}
        };
        int[][] boatRecipe = {
                {ItemIDs.HULL.itemID, 1},
                {ItemIDs.RUDDER.itemID, 1},
                {ItemIDs.MAST.itemID, 2},
                {ItemIDs.SAIL.itemID, 2},
                {ItemIDs.ROPE.itemID, 5}
        };

        inventory.add(new CraftableItems(ItemIDs.FABRIC.itemID, "Fabric", 0,
                "After Removing The Seeds From A Boll Of Cotton The Remaining Plant Material Can Be Spun To Create A Soft Yet Durable Fabric.\nThese Fabrics Can Have Many Uses, Lets Hope Some Of Them Are Helpful For You"
                , fabricRecipe));
        inventory.add(new CraftableItems(ItemIDs.ROPE.itemID, "Rope", 0,
                "By Drying Out The Seaweed, Splitting it into individual Strands And Twisting Those Strands Together A Kind Of Rope Can Be Created.\nDue To Their Construction Ropes Are Only Strong When Places Under Tension, This Can Limit Their Usefulness But When Used Correctly They Are Strong And Flexible.\nThis Makes Them Excellent For Tying Things Together And Pulling Objects Towards You."
                , ropeRecipe));
        inventory.add(new CraftableItems(ItemIDs.SAIL.itemID, "Sail", 0,
                "Designed To Capture The Energy Of The Wind And Get The Boat Moving.\nAlthough Without A Way To Control Where You're Going That Wont Be Of Much Use."
                , boatSailRecipe));
        inventory.add(new CraftableItems(ItemIDs.MAST.itemID, "Boat Mast", 0,
                "Strong Wooden Beams Used To Hold Up The Ships Sails.\nWhen Combined With Rope They Can Be Used To Angle The Sails Into The Same Direction As The Wind To Achieve A Greater Speed"
                , boatMastRecipe));
        inventory.add(new CraftableItems(ItemIDs.RUDDER.itemID, "Rudder", 0,
                "A Simple Shaped Plank Of Wood, It Is Used To Control The Direction That The Boat Travels In By Forcing The Water To Change Direction.\nBy Attaching Some Rope To The Rudder It Can Be Manipulated Remotely."
                , boatRudderRecipe));
        inventory.add(new CraftableItems(ItemIDs.HULL.itemID, "Boat Hull", 0,
                "The Main Body Of A Boat.\nIt Needs To Be Strong Enough To Survive The Harshes Of The Open Sea And Yet Still Be Light Enough To Float."
                , boatHullRecipe));
        inventory.add(new CraftableItems(ItemIDs.BOAT.itemID, "Sailing Boat", 0,
                "The Finished Boat, Meticulously Crafted Over Many Hours.\nTime To Find Out If Its Seaworthy."
                , boatRecipe));

        /*
        level Two Items
         */
        inventory.add(new HarvestableItems(ItemIDs.IRON.itemID, "Iron", 1,
                "Iron Is A Relatively Abundant Metal Found In The Earth Crust.\nOn It's Own It Is Fairly Soft And Not That Useful.\nMaybe You Can Refine It And Find Some Good Uses For It."
                , Terrain.MINES));
        inventory.add(new HarvestableItems(ItemIDs.OIL.itemID, "Oil", 1,
                "Oil Is A Viscous Liquid Made Mostly From Dead Plants And Animals That Have Been Compressed By The Earth Over Millions Of Years.\nBy Itself It Is A Very Slippery Liquid But It Does Not Burn All That Well.\nLet's See What Uses You Can Find For It."
                , Terrain.OLD_DERRICK));

        int[][] steelRecipe = {
                {ItemIDs.IRON.itemID, 2}
        };
        int[][] fuelRecipe = {
                {ItemIDs.OIL.itemID, 2}
        };
        int[][] propellerRecipe = {
                {ItemIDs.WOOD.itemID, 3}
        };
        int[][] engineRecipe = {
                {ItemIDs.STEEL.itemID, 10},
                {ItemIDs.OIL.itemID, 5}
        };
        int[][] wheelRecipe = {
                {ItemIDs.WOOD.itemID, 3}
        };
        int[][] wingRecipe = {
                {ItemIDs.WOOD.itemID, 4},
                {ItemIDs.ROPE.itemID, 4},
                {ItemIDs.SAIL.itemID, 1}
        };
        int[][] fuselageRecipe = {
                {ItemIDs.WOOD.itemID, 10},
                {ItemIDs.FABRIC.itemID, 5}
        };
        int[][] seatRecipe = {
                {ItemIDs.WOOD.itemID, 2}
        };
        int[][] biplaneRecipe = {
                {ItemIDs.FUEL.itemID, 10},//10 fuel
                {ItemIDs.PROPELLER.itemID, 1}, //1 propeller
                {ItemIDs.ENGINE.itemID, 1}, //1 engine
                {ItemIDs.WHEELS.itemID, 2}, //2 wheels
                {ItemIDs.WINGS.itemID, 4}, //4 wings
                {ItemIDs.FUSELAGE.itemID, 1}, //1 fuselage
                {ItemIDs.SEAT.itemID, 1}  //1 seat
        };

        inventory.add(new CraftableItems(ItemIDs.STEEL.itemID, "Steel", 1,
                "By Removing The Impurities From Iron And Adding Small Amounts Of Other Elements Like Carbon And Chromium An Alloy Can Be Created Than Is Far Stronger And Resists Rust Far Better."
                , steelRecipe));
        inventory.add(new CraftableItems(ItemIDs.FUEL.itemID, "Fuel", 1,
                "Oil Can Be Separated Into Individual Products Known As Fractions, Each Fraction Has Different Properties And Is Better At Different Things.\nThe Fraction You Have Here Burns Very Well With Even The Slightest Spark."
                , fuelRecipe));
        inventory.add(new CraftableItems(ItemIDs.PROPELLER.itemID, "Propeller", 1,
                "Works In A Similar Way To A Sail But Instead Of Catching The Natural Wind It Spins And Forces The Air Through It To Generate Thrust And Push Itself Along."
                , propellerRecipe));
        inventory.add(new CraftableItems(ItemIDs.ENGINE.itemID, "Engine", 1,
                "By Burning Small Amounts Of Fuel Inside A Combustion Chamber The Explosion Can Be Harnessed To Push A Piston And Turn A Crank To Generate Rotating Motion.\nThe Violent Nature of The Combustion Requires That The Engine Be Made Of Strong Materials And The Fast Motion Of The Pistons Require Oil To Lubricate Them Otherwise The Friction Would Melt The Engine."
                , engineRecipe));
        inventory.add(new CraftableItems(ItemIDs.WHEELS.itemID, "Wheel", 1,
                "A Simple Wooden Wheel, Wheels Allow For Heavier Objects To Be Moved Easier By Rolling Instead Of Sliding."
                , wheelRecipe));
        inventory.add(new CraftableItems(ItemIDs.WINGS.itemID, "Wing", 1,
                "Made From Flexible Wooden Skeleton And Covered In A Canvas.\nWings Force The Air To Flow Downwards And Hence Generate Lift Allowing For The Plane To Fly.\nUsing Rope The Pilot Can Flex The Wings To Change The Direction Of Airflow And Allow The Plane To Roll."
                , wingRecipe));
        inventory.add(new CraftableItems(ItemIDs.FUSELAGE.itemID, "Fuselage", 1,
                "The Main Body Of The Plane And To Which Everything Attaches.\nBy Covering The Fuselage In Fabric The Air Can Flow Around The Planes Body Smoothly, This Allows The Plane To Fly Faster Or Fly At The Same Speed But Using Less Power."
                , fuselageRecipe));
        inventory.add(new CraftableItems(ItemIDs.SEAT.itemID, "Seat", 1,
                "A Simple Seat To Sit On Allowing FOr Access To All Of The Controls Whilst Remaining In A Comfortable Position"
                , seatRecipe));
        inventory.add(new CraftableItems(ItemIDs.BIPLANE.itemID, "Biplane", 1,
                "The Earliest Form Of Powered Flight, Using Two Wings Allows For Each Wing To Be Shorter Reducing Stress On The Airframe For The Same Amount Of Lift.\nBy Changing The Positions Of Certain Wing Elements Like The Rudder Or Elevators The Direction That The Plane Moves In Can Be Controlled"
                , biplaneRecipe));

        /*
        Level Three Items
         */
        inventory.add(new HarvestableItems(ItemIDs.SAND.itemID, "Sand", 2,
                "Sand Is A Course And Rough Substance. It's Made Up Of Grains That Get Everywhere."
                , Terrain.DESERT));
        inventory.add(new HarvestableItems(ItemIDs.ELECTRICITY.itemID, "Electricity", 2,
                "Electricity Is Caused Movement Of Tiny Particles Called Electrons, By Harnessing This Power Many Useful Things Can Be Done."
                , Terrain.SOLAR_ARRAY));

        int[][] siliconRecipe = {
                {ItemIDs.SAND.itemID, 2}
        };
        int[][] oxygenRecipe = {
                {ItemIDs.ELECTRICITY.itemID, 2}
        };
        int[][] computerRecipe = {
                {ItemIDs.ELECTRICITY.itemID, 2},
                {ItemIDs.SILICON.itemID, 1}
        };
        int[][] heatResistantPanelRecipe = {
                {ItemIDs.SILICON.itemID, 1},
                {ItemIDs.STEEL.itemID, 2}
        };
        int[][] noseConeRecipe = {
                {ItemIDs.HEAT_RESISTANT_PANEL.itemID, 3},
                {ItemIDs.STEEL.itemID, 2}
        };
        int[][] parachuteRecipe = {
                {ItemIDs.FABRIC.itemID, 2},
                {ItemIDs.ROPE.itemID, 2}
        };
        int[][] cockpitRecipe = {
                {ItemIDs.SEAT.itemID, 1},
                {ItemIDs.STEEL.itemID, 2},
                {ItemIDs.COMPUTER_CHIP.itemID, 2},
                {ItemIDs.OXYGEN.itemID, 3}
        };
        int[][] guidanceRingRecipe = {
                {ItemIDs.COMPUTER_CHIP.itemID, 5},
                {ItemIDs.STEEL.itemID, 5}
        };
        int[][] fuelTankRecipe = {
                {ItemIDs.STEEL.itemID, 5}
        };
        int[][] rocketBodyRecipe = {
                {ItemIDs.HEAT_RESISTANT_PANEL.itemID, 1},
                {ItemIDs.STEEL.itemID, 2}
        };
        int[][] rocketNozzleRecipe = {
                {ItemIDs.HEAT_RESISTANT_PANEL.itemID, 3}
        };
        int[][] finRecipe = {
                {ItemIDs.HEAT_RESISTANT_PANEL.itemID, 1},
                {ItemIDs.RUDDER.itemID, 1}
        };
        int[][] fuelIgnitorRecipe = {
                {ItemIDs.STEEL.itemID, 1},
                {ItemIDs.ELECTRICITY.itemID, 2}
        };
        int[][] rocketRecipe = {
                {ItemIDs.NOSE_CONE.itemID, 1},
                {ItemIDs.PARACHUTE.itemID, 3},
                {ItemIDs.COCKPIT.itemID, 1},
                {ItemIDs.GUIDANCE_RING.itemID, 1},
                {ItemIDs.FUEL_TANK.itemID, 2},
                {ItemIDs.ROCKET_BODY.itemID, 4},
                {ItemIDs.ENGINE.itemID, 2},
                {ItemIDs.ROCKET_NOZZLE.itemID, 1},
                {ItemIDs.FINS.itemID, 4},
                {ItemIDs.FUEL_IGNITOR.itemID, 1},
                {ItemIDs.OXYGEN.itemID, 10},
                {ItemIDs.FUEL.itemID, 10}
        };

        inventory.add(new CraftableItems(ItemIDs.SILICON.itemID, "Silicon", 2,
                "Silicon Is A Semiconductor, This Allows It To Be Used To Create Transistors And As Such It Is The Basis For The Majority Of Modern Electronics."
                , siliconRecipe));
        inventory.add(new CraftableItems(ItemIDs.OXYGEN.itemID, "Oxygen", 2,
                "By Running An Electrical Current Though Water It Can Be Broken Down Into Hydrogen And Oxygen.\nOxygen Is A Natural Element Existing Within The Earth's Atmosphere, It Allows Most Living Creatures To Breath And Without It Things Would Not Be Able To Burn."
                , oxygenRecipe));
        inventory.add(new CraftableItems(ItemIDs.COMPUTER_CHIP.itemID, "Computer Chips", 2,
                "A Basic Computer Chip, It Can Be Used For A Wide Variety Of Tasks And Calculations."
                , computerRecipe));
        inventory.add(new CraftableItems(ItemIDs.HEAT_RESISTANT_PANEL.itemID, "Heat Resistant Panel", 2,
                "The Extreme Speeds That Rockets Travel At Means The Body Of The Rocket gets Heated To Extreme Temperatures, Unique Materials Are Needed To Resist These Temperatures."
                , heatResistantPanelRecipe));
        inventory.add(new CraftableItems(ItemIDs.NOSE_CONE.itemID, "Nose Cone", 2,
                "The Very Top Of The Rocket.\nIt Needs To Be Shaped Aerodynamically In Order To Fly Though The Air With As Little Resistance As Possible, It Also Needs To Be Able To Withstand Extreme Temperatures And Pressures Of A Rocket Launch."
                , noseConeRecipe));
        inventory.add(new CraftableItems(ItemIDs.PARACHUTE.itemID, "Parachute", 2,
                "Parachutes Slow The Decent Of Whatever They're Attached To, Allowing For A Safe Landing."
                , parachuteRecipe));
        inventory.add(new CraftableItems(ItemIDs.COCKPIT.itemID, "Cockpit", 2,
                "The Area From Which The Rocket Is Operated, It Holds Some Computers Allowing For The Pilot To Interact With Other Rocket Parts Remotely."
                , cockpitRecipe));
        inventory.add(new CraftableItems(ItemIDs.GUIDANCE_RING.itemID, "Guidance Ring", 2,
                "The Main Computer Hub Of The Rocket, It Carries Out Many Guidance Calculations To Ensure The Rocket Gets To Where It Is Supposed To."
                , guidanceRingRecipe));
        inventory.add(new CraftableItems(ItemIDs.FUEL.itemID, "Fuel Tank", 2,
                "These large Tanks Take Up The Majority Of The Rockets Space, One Tank Is Used To Hold The Fuel And Another Tank Is Used To Hold The Oxidiser Which Allows The Fuel To Burn.\nSometimes These Tanks Are Held At Extremely Low Temperatures To Allow The Use Of Cryogenic Propellants."
                , fuelTankRecipe));
        inventory.add(new CraftableItems(ItemIDs.ROCKET_BODY.itemID, "Rocket Body", 2,
                "The Main Body Of The Rocket, For Larger Rockets The Body Is Split Into Sections Known As Stages.\nStages Are Detached After They Have Served Their Purpose, This Saves On Weight And Allows The Rocket To Travel Further."
                , rocketBodyRecipe));
        inventory.add(new CraftableItems(ItemIDs.ROCKET_NOZZLE.itemID, "Rocket Nozzle", 2,
                "The Point At Which The Fuel And Oxidiser Are Mixed, This Is The Hottest Part Of The Rocket And Quite Often Requires Actively Cooling To Avoid Melting."
                , rocketNozzleRecipe));
        inventory.add(new CraftableItems(ItemIDs.FINS.itemID, "Stabilising Fins", 2,
                "These Fins Allow For Control Of The Rocket. Working In The Exact Same Way As The Rudder On A Boat Or On A Plane, The Only Difference Being That These Fins Are Larger And Need To Be Heat Resistant."
                , finRecipe));
        inventory.add(new CraftableItems(ItemIDs.FUEL_IGNITOR.itemID, "Fuel Ignitor", 2,
                "Used To Initially Ignite The Propellant Mixture Whilst The Rocket Is About To Take Off.\nOnce The Propellants Are Burning They Will Continue To Burn Until One Of The Runs Out, This Means The Ignitor Is No Longer Needed And As Such It Is Left On The Launch Pad."
                , fuelIgnitorRecipe));
        inventory.add(new CraftableItems(ItemIDs.ROCKET.itemID, "Rocket", 2,
                "The Completed Rocket.\nTime To See If All Your Hard Work Has Paid Off.\nGet To The Launch Pad And Begin The Countdown."
                , rocketRecipe));


    }

}

