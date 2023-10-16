public enum ItemIDs {

    /*
    An ENUM to assign each itemID to the name of the item, this makes creating the item recipes far easier
     */

    WOOD(0),
    SEAWEED(1),
    COTTON(2),
    FABRIC(3),
    ROPE(4),
    SAIL(5),
    MAST(6),
    RUDDER(7),
    HULL(8),
    BOAT(9),
    IRON(10),
    OIL(11),
    STEEL(12),
    FUEL(13),
    PROPELLER(14),
    ENGINE(15),
    WHEELS(16),
    WINGS(17),
    FUSELAGE(18),
    SEAT(19),
    BIPLANE(20),
    SAND(21),
    ELECTRICITY(22),
    SILICON(23),
    OXYGEN(24),
    COMPUTER_CHIP(25),
    HEAT_RESISTANT_PANEL(26),
    NOSE_CONE(27),
    PARACHUTE(28),
    COCKPIT(29),
    GUIDANCE_RING(30),
    FUEL_TANK(31),
    ROCKET_BODY(32),
    ROCKET_NOZZLE(33),
    FINS(34),
    FUEL_IGNITOR(35),
    ROCKET(36);

    final int itemID;

    ItemIDs(int resourceID) {
        this.itemID = resourceID;
    }


}
