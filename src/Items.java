import java.io.Serial;
import java.io.Serializable;

public abstract class Items implements Serializable {

    @Serial
    private static final long serialVersionUID = 6163575913649787664L;

    protected int itemID;
    protected String name;
    protected int amount;
    protected int requiredLevel;
    protected boolean obtainedBefore;
    protected String description;

    public Items(int pID, String pName, int pRequiredLevel, String pDescription) {
        itemID = pID;
        name = pName;
        requiredLevel = pRequiredLevel;
        description = pDescription;
    }


    public abstract int[][] howToObtain();

    public int getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public boolean isObtainedBefore() {
        return obtainedBefore;
    }

    public void setObtainedBefore(boolean obtainedBefore) {
        this.obtainedBefore = obtainedBefore;
    }

    public String getDescription() {
        return description;
    }

    public void changeAmountBy(int changeBy) {
        amount += changeBy;
    }


}


